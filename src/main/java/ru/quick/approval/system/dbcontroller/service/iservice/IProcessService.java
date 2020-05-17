package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.api.model.StatusType;
import ru.quick.approval.system.api.model.Task;

import java.util.List;

public interface IProcessService {

    /**
     * Метод возвращает все процессы
     * @return - List<Process>
     */
    List<Process> getAllProcesses();

    /**
     * Метод возвращает процесс по id
     * @param id - id процесса
     * @return - Process
     */
    Process getProcessById(int id);

    /**
     * Метод модифицрует процесс по id
     * @param id - id процесса
     * @param process - новые данные
     * @return - true/false
     */
    boolean updateProcessById(int id, Process process);

    /**
     * Метод возвращает все Task привязанные к id процесса
     * @param id - id процесса
     * @return - List<Task>
     */
    List<Task> getAllTaskByProcessId(int id);

    /**
     * Метод возвращает статус процесса по id
     * @param process_id - id процесса
     * @return StatusType
     */
    StatusType getProcessStatusById(int process_id);

    /**
     * Метод создает новую задачу Task привязывая её к пользователю
     * @param process_id - id процесса, к которому относится Task
     * @param user_id - id пользователя, который начал Task
     * @param task - Task
     * @return - true/false
     */
    boolean createNewTaskByUserId(int process_id, int user_id, Task task);

    /**
     * Метод создаёт новую задачу Task привязывая её к роли
     * @param process_id -id процесса, к которому относится Task
     * @param role_id - id роли, к которой привязан Task
     * @param task - Task
     * @return - true/false
     */
    boolean createNewTaskByRoleId(int process_id, int role_id, Task task);

    /**
     * Метод создаёт новый процесс типа Process_Type
     * @param process_type_id - id process_type
     * @param process - процесс
     * @return - true/false
     */
    boolean createNewProcessByProcessType(int process_type_id, Process process);

    /**
     * Метод возвращает все активные процессы
     * @return - List<Process>
     */
    List<Process> getProcessStatusActive();

    /**
     * Метод возвращает все завершенные процессы
     * @return - List<Process>
     */
    List<Process> getProcessStatusComplete();
}
