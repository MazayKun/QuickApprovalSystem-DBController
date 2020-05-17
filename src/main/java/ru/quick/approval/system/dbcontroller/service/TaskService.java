package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.StatusRecord;
import org.jooq.demo.db.tables.records.TaskRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.dao.StatusDao;
import ru.quick.approval.system.dbcontroller.dao.TaskDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IStatusDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.ITaskDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessCurator;
import ru.quick.approval.system.dbcontroller.service.iservice.ITaskService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с TaskController
 * @author Игорь М
 */

@Service
public class TaskService implements ITaskService {

    private final ITaskDao taskDao;
    private final IStatusDao statusDao;

    private final ITranslator<TaskRecord, Task> taskTranslator;

    private final IProcessCurator processCurator;


    @Autowired
    public TaskService(IProcessCurator processCurator, ITaskDao taskDao, IStatusDao statusDao, ITranslator<TaskRecord, Task> taskTranslator) {
        this.taskTranslator = taskTranslator;
        this.taskDao = taskDao;
        this.statusDao = statusDao;
        this.processCurator = processCurator;
    }

    /**
     * Метод возвращает List всех объектов Task
     * @return - List<Task>
     */
    @Override
    public List<Task> getAllTask() {
        List<Task> taskList = new ArrayList<>();
        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        for (TaskRecord taskRecord: taskRecords) {
            taskList.add(taskTranslator.translate(taskRecord));
        }
        return taskList;
    }

    /**
     * Возвращает объект Task по id
     * @param id - id таски
     * @return - объект типа Task
     */
    @Override
    public Task getTaskById(int id) {
        return taskTranslator.translate(taskDao.getTaskById(id));
    }

    /**
     * Метод апдейтит Task по id
     * @param id - id такси
     * @param task - объект типа Task
     * @return - true/false
     */
    @Override
    public boolean updateTaskById(int id, Task task) {
        return taskDao.updateTaskById(id, taskTranslator.reverseTranslate(task));
    }

    /**
     * Метод присваивает Task статус Sended
     * @param id - id таски
     * @return - true/false
     */
    @Override
    public boolean updateTaskByIdSended(int id) {
        Task task = taskTranslator.translate(taskDao.getTaskById(id));
        Integer activeId = statusDao.getStatusByName("active").getIdStatus();
        Integer waitId = statusDao.getStatusByName("wait").getIdStatus();
        if(task.getStatusId().compareTo(activeId) == 0 || task.getStatusId().compareTo(waitId) == 0) {
            StatusRecord statusRecord = statusDao.getStatusByName("Sended");
            task.setStatusId(statusRecord.getIdStatus());
            return updateTaskById(id, task);
        }
        return false;
    }

    /**
     * Метод присваивает Task статус Agreed
     * @param id - id таски
     * @return - true/false
     */
    @Override
    public boolean updateTaskByIdAgreed(int id) {
        Task task = taskTranslator.translate(taskDao.getTaskById(id));
        Integer sendedId = statusDao.getStatusByName("sended").getIdStatus();
        if(task.getStatusId().compareTo(sendedId) == 0) {
            StatusRecord statusRecord = statusDao.getStatusByName("Agreed");
            task.setStatusId(statusRecord.getIdStatus());
            task.setDateEndFact(Timestamp.valueOf(LocalDateTime.now()));
            boolean answ = updateTaskById(id, task);
            if (answ) {
                processCurator.agreeTask(id);
            }
            return answ;
        }
        return false;
    }

    /**
     * Метод присваивает Task статус Active
     * @param id - id таски
     * @return - true/false
     */
    @Override
    public boolean updateTaskByIdActive(int id) {
        Task task = taskTranslator.translate(taskDao.getTaskById(id));
        Integer canceledId = statusDao.getStatusByName("canceled").getIdStatus();
        if(task.getStatusId().compareTo(canceledId) == 0) {
            StatusRecord statusRecord = statusDao.getStatusByName("Active");
            task.setStatusId(statusRecord.getIdStatus());
            return updateTaskById(id, task);
        }
        return false;
    }

    /**
     * Метод присваивает Task статус Denied
     * @param id - id таски
     * @return - true/false
     */
    @Override
    public boolean updateTaskByIdDenied(int id) {
        Task task = taskTranslator.translate(taskDao.getTaskById(id));
        Integer sendedId = statusDao.getStatusByName("sended").getIdStatus();
        if(task.getStatusId().compareTo(sendedId) == 0) {
            StatusRecord statusRecord = statusDao.getStatusByName("Denied");
            task.setStatusId(statusRecord.getIdStatus());
            task.setDateEndFact(Timestamp.valueOf(LocalDateTime.now()));
            processCurator.deniedTask(task.getProcessId());
            return updateTaskById(id, task);
        }
        return false;
    }

    /**
     * Метод присваивает Task статус Canceled
     * @param id - id таски
     * @return - true/false
     */
    @Override
    public boolean updateTaskByIdCanceled(int id) {
        Task task = taskTranslator.translate(taskDao.getTaskById(id));
        StatusRecord statusRecord = statusDao.getStatusByName("Canceled");
        task.setStatusId(statusRecord.getIdStatus());
        return updateTaskById(id, task);
    }
}
