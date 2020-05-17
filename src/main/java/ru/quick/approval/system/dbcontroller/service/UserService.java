package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.*;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.dbcontroller.dao.iDao.*;
import ru.quick.approval.system.dbcontroller.service.iservice.IUserService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;
import ru.quick.approval.system.dbcontroller.translator.ProcessTranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для обработки запросов UserController
 * @author Kirill Mikheev
 * @version 1.0
 */

@Service
public class UserService implements IUserService {

    private final IUserDao userDao;
    private final IRoleDao roleDao;
    private final ITaskDao taskDao;
    private final IStatusDao statusDao;
    private final IProcessDao processDao;
    private final IUserRoleDao userRoleDao;

    private final ITranslator<TaskRecord, Task> taskTranslator;
    private final ITranslator<RoleQasRecord, Role> roleTranslator;
    private final ITranslator<UserQasRecord, User> userTranslator;
    private final ITranslator<ProcessRecord, Process> processTranslator;
    private final ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator;

    @Autowired
    public UserService(IUserDao userDao, IRoleDao roleDao, ITaskDao taskDao, IStatusDao statusDao, IUserRoleDao userRoleDao, IProcessDao processDao,
                       ITranslator<UserQasRecord, User> userTranslator,
                       ITranslator<RoleQasRecord, Role> roleTranslator,
                       ITranslator<TaskRecord, Task> taskTranslator,
                       ProcessTranslator processTranslator,
                       ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator){
        this.taskTranslator = taskTranslator;
        this.userWithoutPasswordTranslator = userWithoutPasswordTranslator;
        this.userTranslator = userTranslator;
        this.roleTranslator = roleTranslator;
        this.processTranslator = processTranslator;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.taskDao = taskDao;
        this.statusDao = statusDao;
        this.processDao = processDao;
        this.userRoleDao = userRoleDao;
    }

    /**
     * Возвращает список всех пользователей
     * @return List<UserWithoutPassword>
     */
    @Override
    public List<UserWithoutPassword> allUsers() {
        List<UserQasRecord> records = userDao.getAllUsers();
        List<UserWithoutPassword> answer = new ArrayList<>();
        for (UserQasRecord record : records) {
            answer.add(userWithoutPasswordTranslator.translate(record));
        }
        return answer;
    }

    /**
     * Добавляет нового пользователя
     * @param newUser
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean addUser(User newUser) {
        return userDao.addUser(userTranslator.reverseTranslate(newUser)) != 0;
    }

    /**
     * Проверяет была ли такая роль в тблице, если нет, то добавляет ее
     * Далее добавляет новую пару в таблицу user_role
     * @param id пользователя, к которому нужно добавить новую роль
     * @param role объект, содержащий данные о роли, которую нужно добавить
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean addRoleToUserById(int id, Role role) {
        RoleQasRecord roleQasRecord = roleDao.getRoleByName(role.getName());
        if(roleQasRecord == null){
            roleDao.addRole(new RoleQasRecord(0, role.getName()));
            roleQasRecord = roleDao.getRoleByName(role.getName());
        }
        return userRoleDao.addUserRole(new UserRoleRecord(0, id, roleQasRecord.getIdRole())) != 0;
    }

    /**
     * Возвращает пользователя с заданным id
     * @param id
     * @return UserWithoutPassword
     */
    @Override
    public UserWithoutPassword getUserById(int id) {
        UserQasRecord record = userDao.getUserById(id);
        if(record == null){
            return null;
        }

        return userWithoutPasswordTranslator.translate(record);
    }

    /**
     * Обновление данных пользователя по его id
     * @param id
     * @param user объект с новыми данными
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean updateUserById(int id, User user) {
        return userDao.updateUserById(id, userTranslator.reverseTranslate(user));
    }

    /**
     * Возвращает список задач со статусом active у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    @Override
    @SuppressWarnings("Duplicates")
    public List<Task> getActiveTaskListOfUserById(int id) {
        StatusRecord active = statusDao.getStatusByName("active");
        StatusRecord sended = statusDao.getStatusByName("sended");
        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        List<Task> tasks = new ArrayList<>();
        for(TaskRecord tmp : taskRecords){
            if((tmp.getStatusId().compareTo(active.getIdStatus()) == 0 || tmp.getStatusId().compareTo(sended.getIdStatus()) == 0) && tmp.getUserPerformerId().compareTo(id) == 0) {
                Task task = taskTranslator.translate(tmp);
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Возвращает список задач со статусом complete у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    @Override
    @SuppressWarnings("Duplicates")
    public List<Task> getCompleteTaskListOfUserById(int id) {
        StatusRecord agreed = statusDao.getStatusByName("agreed");
        StatusRecord denied = statusDao.getStatusByName("denied");
        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        List<Task> tasks = new ArrayList<>();
        for(TaskRecord tmp : taskRecords){
            if((tmp.getStatusId().compareTo(agreed.getIdStatus()) == 0 || tmp.getStatusId().compareTo(denied.getIdStatus()) == 0) && tmp.getUserPerformerId().compareTo(id) == 0) {
                Task task = taskTranslator.translate(tmp);
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Возвращает список задач со статусом wait у пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    @Override
    @SuppressWarnings("Duplicates")
    public List<Task> getWaitTaskListOfUserById(int id) {
        StatusRecord statusRecord = statusDao.getStatusByName("wait");
        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        List<Task> tasks = new ArrayList<>();
        for(TaskRecord tmp : taskRecords){
            if(tmp.getStatusId().compareTo(statusRecord.getIdStatus()) == 0 && tmp.getUserPerformerId().compareTo(id) == 0){
                Task task = taskTranslator.translate(tmp);
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Возвращает список всех задач пользователя с заданным id
     * @param id
     * @return List<Task>
     */
    @Override
    @SuppressWarnings("Duplicates")
    public List<Task> getTaskListOfUserById(Integer id) {
        List<TaskRecord> taskRecords = taskDao.getAllTasks();
        List<Task> tasks = new ArrayList<>();
        for(TaskRecord tmp : taskRecords){
            if(tmp.getUserPerformerId().compareTo(id) == 0) {
                Task task = taskTranslator.translate(tmp);
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Возвращает список всех ролей пользователя с заданны id
     * @param id
     * @return List<Role>
     */
    @Override
    public List<Role> getRoleListOfUserById(Integer id) {
        List<UserRoleRecord> userRoleRecords = userRoleDao.getAllUserRoles();
        List<Role> roles = new ArrayList<>();
        for(UserRoleRecord tmp : userRoleRecords) {
            if(tmp.getUserId().compareTo(id) == 0) {
                roles.add(roleTranslator.translate(roleDao.getRoleById(tmp.getRoleId())));
            }
        }
        return roles;
    }

    /**
     * Сверяет пришедшие данные для авторизации с данными из базы
     * @param authData объект содержит логин пользователя и его пароль
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean login(InlineObject authData) {
        UserQasRecord userQasRecord = userDao.getUserByLogin(authData.getLogin());
        return userQasRecord.getPassword().compareTo(authData.getPassword()) == 0;
    }

    /**
     * Возвращает список всех активных задач пользователя с заданным телеграм айди
     * @param telegramId
     * @return List<Task>
     *
     */
    @Override
    public List<Task> getActiveTasksByTelegramId(Integer telegramId) {
        List<UserQasRecord> users = userDao.getAllUsers();
        UserQasRecord user = users.stream().filter(tmp -> tmp.getTelegramChatId().compareTo(telegramId) == 0).findFirst().get();
        List<TaskRecord> tasks = taskDao.getAllTasks();
        List<Task> answer = new ArrayList<>();
        Integer activeStatusId = statusDao.getStatusByName("active").getIdStatus();
        for (TaskRecord tmp : tasks){
            if (tmp.getUserPerformerId().compareTo(user.getIdUser()) == 0 && tmp.getStatusId().compareTo(activeStatusId) == 0){
                answer.add(taskTranslator.translate(tmp));
            }
        }
        return answer;
    }

    /**
     * Возвращает список всех процессов данного пользователя
     * @param id пользователя
     * @return List<Process>
     */
    @Override
    public List<Process> getProcessesByUserId(Integer id) {
        List<ProcessRecord> processRecords = processDao.getAllProcesses();
        List<Process> answ = new ArrayList<>();
        for(ProcessRecord tmp : processRecords) {
            if(tmp.getUserStartId().compareTo(id) == 0) {
                answ.add(processTranslator.translate(tmp));
            }
        }
        return answ;
    }

}
