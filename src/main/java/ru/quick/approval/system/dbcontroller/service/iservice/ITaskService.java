package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.Task;

import java.util.List;

public interface ITaskService {

    /**
     * Метод возвращает List всех объектов Task
     * @return - List<Task>
     */
    List<Task> getAllTask();

    /**
     * Возвращает объект Task по id
     * @param id - id таски
     * @return - объект типа Task
     */
    Task getTaskById(int id);

    /**
     * Метод апдейтит Task по id
     * @param id - id такси
     * @param task - объект типа Task
     * @return - true/false
     */
    boolean updateTaskById(int id, Task task);

    /**
     * Метод присваивает Task статус Sended
     * @param id - id таски
     * @return - true/false
     */
    boolean updateTaskByIdSended(int id);

    /**
     * Метод присваивает Task статус Agreed
     * @param id - id таски
     * @return - true/false
     */
    boolean updateTaskByIdAgreed(int id);

    /**
     * Метод присваивает Task статус Active
     * @param id - id таски
     * @return - true/false
     */
    boolean updateTaskByIdActive(int id);

    /**
     * Метод присваивает Task статус Denied
     * @param id - id таски
     * @return - true/false
     */
    boolean updateTaskByIdDenied(int id);

    /**
     * Метод присваивает Task статус Canceled
     * @param id - id таски
     * @return - true/false
     */
    boolean updateTaskByIdCanceled(int id);
}
