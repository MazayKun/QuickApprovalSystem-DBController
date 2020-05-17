package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.*;
import ru.quick.approval.system.api.model.Process;

import java.util.List;

/**
 * Интерфейс сервиса для обработки запросов UserController
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IUserService {

    /**
     * Возвращает список всех пользователей
     * @return List<UserWithoutPassword>
     */
    List<UserWithoutPassword> allUsers();

    /**
     * Добавляет нового пользователя
     * @param newUser
     * @return true, если все прошло успешно, иначе false
     */
    boolean addUser(User newUser);

    /**
     * Проверяет была ли такая роль в тблице, если нет, то добавляет ее
     * Далее добавляет новую пару в таблицу user_role
     * @param id пользователя, к которому нужно добавить новую роль
     * @param role объект, содержащий данные о роли, которую нужно добавить
     * @return true, если все прошло успешно, иначе false
     */
    boolean addRoleToUserById(int id, Role role);

    /**
     * Возвращает пользователя с заданным id
     * @param id
     * @return UserWithoutPassword
     */
    UserWithoutPassword getUserById(int id);

    /**
     * Обновление данных пользователя по его id
     * @param id
     * @param user объект с новыми данными
     * @return true, если все прошло успешно, иначе false
     */
    boolean updateUserById(int id, User user);

    /**
     * Возвращает список задач со статусом active у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    List<Task> getActiveTaskListOfUserById(int id);

    /**
     * Возвращает список задач со статусом complete у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    List<Task> getCompleteTaskListOfUserById(int id);

    /**
     * Возвращает список задач со статусом wait у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    List<Task> getWaitTaskListOfUserById(int id);

    /**
     * Возвращает список всех задач пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    List<Task> getTaskListOfUserById(Integer id);

    /**
     * Возвращает список всех ролей пользователя с заданны id
     * @param id
     * @return List<Role>
     */
    List<Role> getRoleListOfUserById(Integer id);

    /**
     * Сверяет пришедшие данные для авторизации с данными из базы
     * @param authData объект содержит логин пользователя и его пароль
     * @return true, если все прошло успешно, иначе false
     */
    boolean login(InlineObject authData);

    /**
     * Возвращает список всех активных задач пользователя с заданным телеграм айди
     * @param telegramId
     * @return List<Task>
     */
    List<Task> getActiveTasksByTelegramId(Integer telegramId);

    /**
     * Возвращает список всех процессов данного пользователя
     * @param id пользователя
     * @return List<Process>
     */
    List<Process> getProcessesByUserId(Integer id);
}
