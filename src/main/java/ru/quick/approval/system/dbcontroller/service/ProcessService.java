package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessRecord;
import org.jooq.demo.db.tables.records.StatusRecord;
import org.jooq.demo.db.tables.records.TaskRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.api.model.StatusType;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.dao.ProcessDao;
import ru.quick.approval.system.dbcontroller.dao.StatusDao;
import ru.quick.approval.system.dbcontroller.dao.TaskDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IStatusDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.ITaskDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessCurator;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с ProcessController
 * @author Игорь М
 */

@Service
public class ProcessService implements IProcessService {

    private final IProcessDao processDao;
    private final ITaskDao taskDao;
    private final IStatusDao statusDao;

    private final ITranslator<TaskRecord, Task> taskTranslator;
    private final ITranslator<ProcessRecord, Process> processTranslator;

    private final IProcessCurator processCurator;

    @Autowired
    public ProcessService(IProcessCurator processCurator, IProcessDao processDao, ITaskDao taskDao, IStatusDao statusDao, ITranslator<TaskRecord, Task> taskTranslator, ITranslator<ProcessRecord, Process> processTranslator) {
        this.processDao = processDao;
        this.taskDao = taskDao;
        this.statusDao = statusDao;
        this.processTranslator = processTranslator;
        this.taskTranslator = taskTranslator;
        this.processCurator = processCurator;
    }

    /**
     * Метод возвращает все процессы
     * @return - List<Process>
     */
    @Override
    public List<Process> getAllProcesses() {
        List<Process> processList = new ArrayList<>();
        List<ProcessRecord> allProcesses = processDao.getAllProcesses();
        for (ProcessRecord processRecord: allProcesses) {
            processList.add(processTranslator.translate(processRecord));
        }
        return processList;
    }

    /**
     * Метод возвращает процесс по id
     * @param id - id процесса
     * @return - Process
     */
    @Override
    public Process getProcessById(int id) {
        return processTranslator.translate(processDao.getProcessById(id));
    }

    /**
     * Метод модифицрует процесс по id
     * @param id - id процесса
     * @param process - новые данные
     * @return - true/false
     */
    @Override
    public boolean updateProcessById(int id, Process process) {
        return processDao.updateProcessById(id, processTranslator.reverseTranslate(process));
    }

    /**
     * Метод возвращает все Task привязанные к id процесса
     * @param id - id процесса
     * @return - List<Task>
     */
    @Override
    public List<Task> getAllTaskByProcessId(int id) {
        List<Task> taskList = new ArrayList<>();
        List<TaskRecord> allTasksByProcessId = taskDao.getAllTasksByProcessId(id);
        for (TaskRecord taskRecord: allTasksByProcessId) {
            taskList.add(taskTranslator.translate(taskRecord));
        }
        return taskList;
    }

    /**
     * Метод возвращает статус процесса по id
     * @param process_id - id процесса
     * @return StatusType
     */
    @Override
    public StatusType getProcessStatusById(int process_id) {
        Process process = processTranslator.translate(processDao.getProcessById(process_id));
        StatusRecord statusById = statusDao.getStatusById(process.getStatusId());
        return StatusType.fromValue(statusById.getName());
    }

    /**
     * Метод создает новую задачу Task привязывая её к пользователю
     * @param process_id - id процесса, к которому относится Task
     * @param user_id - id пользователя, который начал Task
     * @param task - Task
     * @return - true/false
     */
    @Override
    public boolean createNewTaskByUserId(int process_id, int user_id, Task task) {
        task.setProcessId(process_id);
        task.setUserPerformerId(user_id);
        return taskDao.addTask(taskTranslator.reverseTranslate(task)) != 0;
    }

    /**
     * Метод создаёт новую задачу Task привязывая её к роли
     * @param process_id -id процесса, к которому относится Task
     * @param role_id - id роли, к которой привязан Task
     * @param task - Task
     * @return - true/false
     */
    @Override
    public boolean createNewTaskByRoleId(int process_id, int role_id, Task task) {
        task.setProcessId(process_id);
        task.setRolePerformerId(role_id);
        return taskDao.addTask(taskTranslator.reverseTranslate(task)) != 0;
    }

    /**
     * Метод создаёт новый процесс типа Process_Type
     * @param process_type_id - id process_type
     * @param process - процесс
     * @return - true/false
     */
    @Override
    public boolean createNewProcessByProcessType(int process_type_id, Process process) {
        process.setProcessTypeId(process_type_id);
        boolean answ = processCurator.createProcess(process_type_id, process);
        return answ;
    }

    /**
     * Метод возвращает все активные процессы
     * @return - List<Process>
     */
    @Override
    public List<Process> getProcessStatusActive() {
        List<Process> processList = new ArrayList<>();
        List<ProcessRecord> processByStatusId = processDao.
                getProcessByStatusId(statusDao.getStatusByName("Active").getIdStatus());
        for (ProcessRecord processRecord: processByStatusId) {
            processList.add(processTranslator.translate(processRecord));
        }
        return processList;
    }

    /**
     * Метод возвращает все завершенные процессы
     * @return - List<Process>
     */
    @Override
    public List<Process> getProcessStatusComplete() {
        List<Process> processList = new ArrayList<>();
        List<ProcessRecord> processByStatusId = processDao.getProcessByStatusId
                (statusDao.getStatusByName("agreed").getIdStatus());
        processByStatusId.addAll(processDao.getProcessByStatusId
                (statusDao.getStatusByName("denied").getIdStatus()));
        for (ProcessRecord processRecord: processByStatusId) {
            processList.add(processTranslator.translate(processRecord));
        }
        return processList;
    }
}
