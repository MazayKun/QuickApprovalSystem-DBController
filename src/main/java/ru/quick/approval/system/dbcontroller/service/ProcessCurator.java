package ru.quick.approval.system.dbcontroller.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.demo.db.tables.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.quick.approval.system.api.model.*;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.dbcontroller.dao.iDao.*;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessCurator;
import ru.quick.approval.system.dbcontroller.service.iservice.IRoleService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Класс управляет жизненным циклом процессов и задач
 * @author Kirill Mikheev
 * @version 1.0
 */

@Slf4j
@Service
public class ProcessCurator implements IProcessCurator {

    /** URL для отправки сообщений в сервиса оповещений */
    private final String TASK_INFO_MESSAGE_URL = "localhost:8081/message/user";

    private final IProcessTypeDao processTypeDao;
    private final IProcessStageDao processStageDao;
    private final IProcessDao processDao;
    private final IRoleDao roleDao;
    private final IUserDao userDao;
    private final IUserRoleDao userRoleDao;
    private final IRoleService roleService;
    private final IStatusDao statusDao;
    private final ITaskDao taskDao;

    private final ITranslator<ProcessTypeRecord, ProcessType> processTypeTranslator;
    private final ITranslator<ProcessStageRecord, ProcessStage> processStageTranslator;
    private final ITranslator<ProcessRecord, Process> processTranslator;
    private final ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator;
    private final ITranslator<TaskRecord, Task> taskTranslator;

    @Autowired
    public ProcessCurator(IProcessTypeDao processTypeDao, IProcessStageDao processStageDao,
                          IProcessDao processDao, IRoleDao roleDao, IUserDao userDao, ITaskDao taskDao,
                          IUserRoleDao userRoleDao, IRoleService roleService, IStatusDao statusDao,
                          ITranslator<ProcessTypeRecord, ProcessType> processTypeTranslator,
                          ITranslator<ProcessStageRecord, ProcessStage> processStageTranslator,
                          ITranslator<ProcessRecord, Process> processTranslator,
                          ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator,
                          ITranslator<TaskRecord, Task> taskTranslator) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.taskDao = taskDao;
        this.statusDao = statusDao;
        this.processDao = processDao;
        this.userRoleDao = userRoleDao;
        this.roleService = roleService;
        this.processTypeDao = processTypeDao;
        this.processStageDao = processStageDao;
        this.taskTranslator = taskTranslator;
        this.processTranslator = processTranslator;
        this.processTypeTranslator = processTypeTranslator;
        this.processStageTranslator = processStageTranslator;
        this.userWithoutPasswordTranslator = userWithoutPasswordTranslator;
    }

    /**
     * Создание процесса, создание и рассылка задач первого этапа на подтверждение
     * @param processTypeId тип процесса
     * @param process объект с информацией о процессе
     */
    @Override
    public boolean createProcess(Integer processTypeId, Process process) {
        process.setProcessTypeId(processTypeId);
        process.statusId(statusDao.getStatusByName("active").getIdStatus());
        process.setDateStart(Timestamp.valueOf(LocalDateTime.now()));
        int idProcess = processDao.addProcess(processTranslator.reverseTranslate(process));
        log.info("Процесс с id = " + idProcess + " создан");
        process.setIdProcess(idProcess);
        List<ProcessStage> stages = getAllStagesOfProcessType(processTypeId);
        // Поиск минимального уровня, с которого нужно начать
        int minStage = stages.get(0).getStage();
        for(ProcessStage stage : stages) {
            if(stage.getStage().compareTo(minStage) < 0 ) {
                minStage = stage.getStage();
            }
        }
        // Создание задач по всем этапам минимального уровня
        for(ProcessStage stage : stages) {
            if(stage.getStage().compareTo(minStage) == 0) {
                createTasksByStage(stage, process);
            }
        }
        return idProcess != 0;
    }

    /**
     * Здесь происходит логика, которая должна быть выполнена после очередного подтверждения задачи
     * Проверка, выполнен ли очередной этап. выполнен ли процесс и прочее
     * @param taskId айди задачи
     */
    @Override
    public void agreeTask(Integer taskId) {
        Task task = taskTranslator.translate(taskDao.getTaskById(taskId));
        log.info("Задача с id = " + taskId + " одобрена");
        // Проверка на удачну завершенность процесса
        if(isAllStagesComplete(task.getProcessId())){
            processComplete(task.getProcessId());
            return;
        }
        // Здесь будут храниться отсортированные все уровни процесса
        TreeSet<Integer> stageNumbers = new TreeSet<>();
        // Полученеи всех уровней процесса
        List<ProcessStage> processStages = getAllStagesOfProcessType(
                processTranslator.translate(processDao.getProcessById(task.getProcessId())).getProcessTypeId()
        );
        for(ProcessStage tmp : processStages) {
            stageNumbers.add(tmp.getStage());
        }
        // Проверка выполнения всех этапов, начиная с первого
        Integer agreedStatus = statusDao.getStatusByName("agreed").getIdStatus();
        Process process = processTranslator.translate(processDao.getProcessById(task.getProcessId()));
        for(Integer tmp : stageNumbers) {
            List<Task> tasks = getAllTasksOfProcessStage(process, tmp);
            if(tasks.size() == 0) {
                for(ProcessStage stage : processStages) {
                    if(stage.getStage().compareTo(tmp) == 0) {
                        createTasksByStage(stage, process);
                    }
                }
                return;
            }
            for(Task tmpTask : tasks) {
                if(tmpTask.getStatusId().compareTo(agreedStatus) != 0) {
                    return;
                }
            }
        }
        processComplete(task.getProcessId());
    }

    /**
     * Здесь происходит логика, которая должна быть выполнена после отклонения задачи одним из пользователей
     * @param processId айди процесса, к которому относилаь задача
     */
    @Override
    public void deniedTask(Integer processId) {
        log.info("Процесс с id = " + processId + " отклонен");
        Integer deniedId = statusDao.getStatusByName("Denied").getIdStatus();
        ProcessRecord process = processDao.getProcessById(processId);
        process.setStatusId(deniedId);
        process.setDateEndFact(Timestamp.valueOf(LocalDateTime.now()));
        processDao.updateProcessById(processId, process);
        List<TaskRecord> tasks = taskDao.getAllTasks();
        for(TaskRecord tmp : tasks) {
            if(tmp.getProcessId().compareTo(processId) == 0) {
                if(tmp.getStatusId().compareTo(deniedId) != 0) {
                    tmp.setDateEndFact(Timestamp.valueOf(LocalDateTime.now()));
                }
                tmp.setStatusId(deniedId);
                taskDao.updateTaskById(tmp.getIdTask(), tmp);
            }
        }
    }

    /**
     * Выдает список всех этапов заданного типа процесса
     * @param processTypeId
     * @return List<ProcessStage>
     */
    private List<ProcessStage> getAllStagesOfProcessType(Integer processTypeId)  {
        ProcessType processType = processTypeTranslator.translate(processTypeDao.getProcessTypeById(processTypeId));
        List<ProcessStageRecord> allStages = processStageDao.getAllProcessStages();
        List<ProcessStage> stages = new ArrayList<>();
        for(ProcessStageRecord tmp : allStages) {
            if (tmp.getProcessTypeId().compareTo(processType.getIdProcessType()) == 0) {
                stages.add(processStageTranslator.translate(tmp));
            }
        }
        return stages;
    }

    /**
     * Создает набор задач для заданного уровня определенного процесса, сохраняет их в бд и рассылает запросы пользователям
     * @param processStage уровень процеса, который нужно заполнить заполнить
     * @param process процесс, к которому относятя задачи
     */
    private void createTasksByStage(ProcessStage processStage, Process process) {

        // Получение списка всех пользователей данной роли для оповещения
        int roleId = processStage.getRoleId();
        List<UserWithoutPassword> users = roleService.getUsersByRoleId(roleId);

        for(UserWithoutPassword tmp : users) {

            // Заполнение данных по задаче
            Task task = new Task();
            task.setProcessId(process.getIdProcess());
            task.setUserPerformerId(tmp.getIdUser());
            task.setRolePerformerId(roleId);
            task.setDateStart(process.getDateStart());
            task.setDateEndPlanning(process.getDateEndPlanning());
            task.setStatusId(statusDao.getStatusByName("sended").getIdStatus());

            // Добавление задачи в бд
            int taskId = taskDao.addTask(taskTranslator.reverseTranslate(task));
            log.info("Задача с id = " + taskId + " добавлена к процессу с id = " + process.getIdProcess());
            // Заполнение объекта для отправки в TaskInfo
            TaskTransferObject taskTransferObject = new TaskTransferObject(
                    tmp.getTelegramChatId(),
                    process.getDescription(),
                    taskId
            );

            // Заполнение запроса на оповещение пользователя
            RestTemplate template = new RestTemplate();
            ResponseEntity<Void> responseEntity = template.postForEntity(TASK_INFO_MESSAGE_URL, taskTransferObject, Void.class);
        }
    }

    /**
     * Возвращает список всех задач данного процесса c заданным уровнем
     * @param process процесс, задачи которого нужно получить
     * @param stage номер уровня, задачи которого нужно получить
     * @return List<Task>
     */
    private List<Task> getAllTasksOfProcessStage(Process process, Integer stage) {

        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        List<Task> processTasks = new ArrayList<>();

        // Получение списка объектов этапов данного уровня
        List<ProcessStage> processStages = new ArrayList<>();
        for (ProcessStage tmp : getAllStagesOfProcessType(process.getProcessTypeId())) {
            if(tmp.getStage().compareTo(stage) == 0) {
                processStages.add(tmp);
            }
        }
        // Поиск задач
        search:
        for(TaskRecord tmp : taskRecords) {
            for(ProcessStage processStage : processStages) {
                if(processStage.getRoleId().compareTo(tmp.getRolePerformerId()) == 0 && tmp.getProcessId().compareTo(process.getIdProcess()) == 0) {
                    processTasks.add(taskTranslator.translate(tmp));
                    continue search;
                }
            }
        }

        return processTasks;
    }

    /**
     * Проверка выполнения всех этапов
     * @param processId айди процесса
     * @return true, если все этапы выполнены успешно, иначе false
     */
    private boolean isAllStagesComplete(Integer processId) {
        Process process = processTranslator.translate(processDao.getProcessById(processId));
        List<ProcessStage> stages = getAllStagesOfProcessType(process.getProcessTypeId());

        // поиск номера последнего этапа подтверждения
        int maxStage = stages.get(0).getStage();
        for(ProcessStage tmp : stages) {
            if(tmp.getStage().compareTo(maxStage) > 0) {
                maxStage = tmp.getStage();
            }
        }
        // Получение всех задач последнего этапа, если список пуст, то значит мы еще не перешли к этому этапу
        List<Task> lastStageTasks = getAllTasksOfProcessStage(process, maxStage);
        if(lastStageTasks.size() == 0) return false;

        int agreedStatus = statusDao.getStatusByName("agreed").getIdStatus();
        for(Task tmp : lastStageTasks) {
            if(tmp.getStatusId().compareTo(agreedStatus) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Выполнение всех завершающих этапов при удачном окончании процесса
     * @param processId айди процесса
     */
    private void processComplete(Integer processId) {
        log.info("Процесс с id = " + processId + " завершен");
        ProcessRecord proess = processDao.getProcessById(processId);
        proess.setDateEndFact(Timestamp.valueOf(LocalDateTime.now()));
        proess.setStatusId(statusDao.getStatusByName("agreed").getIdStatus());
        processDao.updateProcessById(processId, proess);
    }
}

/**
 * Объект для передачи данных в TaskInfo
 */
@AllArgsConstructor
@Setter
@Getter
class TaskTransferObject {
    private Integer telegramId;
    private String text;
    private Integer taskId;
}
