package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.InlineObject;
import ru.quick.approval.system.api.model.Role;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.api.model.UserWithoutPassword;
import ru.quick.approval.system.dbcontroller.dao.iDao.*;
import ru.quick.approval.system.dbcontroller.service.iservice.IUserService;
import ru.quick.approval.system.dbcontroller.translator.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
 * Класс для тестирования большей части методов класса UserService
>>>>>>> Stashed changes
>>>>>>> Stashed changes
 * @author Kirill Mikheev
 * @version 1.0
 */

public class UsersServiceTest {

    private IUserService userService;

    @Mock
    private IUserDao userDao;
    @Mock
    private IRoleDao roleDao;
    @Mock
    private ITaskDao taskDao;
    @Mock
    private IStatusDao statusDao;
    @Mock
    private IUserRoleDao userRoleDao;
    @Mock
    private IProcessDao processDao;

    private List<UserRoleRecord> userRoleMockList;
    private List<UserQasRecord> userMockList;
    private List<RoleQasRecord> roleMockList;
    private List<TaskRecord> taskMockList;


    @BeforeEach
    public void init() {
        initMocks(this);
        userService = new UserService(userDao, roleDao, taskDao, statusDao, userRoleDao, processDao,
                new UserTranslator(), new RoleTranslator(), new TaskTranslator(), new ProcessTranslator(), new UserWithoutPasswordTranslator());
        userMockList = new ArrayList<>();
        roleMockList = new ArrayList<>();
        taskMockList = new ArrayList<>();
        userRoleMockList = new ArrayList<>();
        UserQasRecord user;
        for(int i = 1; i <= 2; i++) {
            user = mock(UserQasRecord.class);
            when(user.getIdUser()).thenReturn(i);
            when(user.getLogin()).thenReturn("name" + i);
            when(user.getTelegramChatId()).thenReturn(i);
            userMockList.add(user);
        }
        TaskRecord task;
        for(int i = 1; i <= 5; i++) {
            task = mock(TaskRecord.class);
            when(task.getUserPerformerId()).thenReturn(1);
            when(task.getIdTask()).thenReturn(i);
            when(task.getStatusId()).thenReturn(i);
            taskMockList.add(task);
        }
        UserRoleRecord userRole;
        RoleQasRecord role;
        for(int i = 1; i <= 2; i++){
            userRole = mock(UserRoleRecord.class);
            role = mock(RoleQasRecord.class);
            when(userRole.getRoleId()).thenReturn(i);
            when(userRole.getUserId()).thenReturn(1);
            when(role.getIdRole()).thenReturn(i);
            when(roleDao.getRoleById(i)).thenReturn(role);
            userRoleMockList.add(userRole);
            roleMockList.add(role);
        }
    }

    @Test
    public void allUsersTest() {
        doReturn(userMockList).when(userDao).getAllUsers();
        List<UserWithoutPassword> answ = userService.allUsers();
        assertEquals(answ.size(), userMockList.size());
        for (int i = 0; i < answ.size(); i++) {
            assertEquals(answ.get(i).getFio(), userMockList.get(i).getFio());
        }
    }

    @Test
    public void getUserByIdTest() {
        doReturn(userMockList.get(0)).when(userDao).getUserById(1);
        UserWithoutPassword answ = userService.getUserById(1);
        assertEquals(answ.getFio(), userMockList.get(0).getFio());
    }

    @Test
    public void getActiveTaskListOfUserByIdTest() {
        StatusRecord active = mock(StatusRecord.class);
        doReturn(1).when(active).getIdStatus();
        StatusRecord sended = mock(StatusRecord.class);
        doReturn(2).when(sended).getIdStatus();
        doReturn(active).when(statusDao).getStatusByName("active");
        doReturn(sended).when(statusDao).getStatusByName("sended");
        doReturn(taskMockList).when(taskDao).getAllTasks();
        List<Task> answ = userService.getActiveTaskListOfUserById(1);
        assertEquals(answ.size(), 2);
        for(int i = 0; i < 2; i++) {
            assertEquals(answ.get(i).getIdTask(), i + 1);
        }
    }

    @Test
    public void getCompleteTaskListOfUserByIdTest() {
        StatusRecord agreed = mock(StatusRecord.class);
        doReturn(3).when(agreed).getIdStatus();
        StatusRecord denied = mock(StatusRecord.class);
        doReturn(4).when(denied).getIdStatus();
        doReturn(agreed).when(statusDao).getStatusByName("agreed");
        doReturn(denied).when(statusDao).getStatusByName("denied");
        doReturn(taskMockList).when(taskDao).getAllTasks();
        List<Task> answ = userService.getCompleteTaskListOfUserById(1);
        assertEquals(answ.size(), 2);
        for(int i = 0; i < 2; i++) {
            assertEquals(answ.get(i).getIdTask(), i + 3);
        }
    }

    @Test
    public void getWaitTaskListOfUserByIdTest() {
        StatusRecord wait = mock(StatusRecord.class);
        doReturn(5).when(wait).getIdStatus();
        doReturn(wait).when(statusDao).getStatusByName("wait");
        doReturn(taskMockList).when(taskDao).getAllTasks();
        List<Task> answ = userService.getWaitTaskListOfUserById(1);
        assertEquals(answ.size(), 1);
        assertEquals(answ.get(0).getIdTask(), 5);
    }

    @Test
    public void getTaskListOfUserByIdTest() {
        doReturn(taskMockList).when(taskDao).getAllTasks();
        List<Task> answ = userService.getTaskListOfUserById(1);
        assertEquals(answ.size(), taskMockList.size());
        for (int i = 0; i < answ.size(); i++) {
            assertEquals(answ.get(i).getIdTask(), taskMockList.get(i).getIdTask());
        }
    }

    @Test
    public void getRoleListOfUserByIdTest() {
        doReturn(userRoleMockList).when(userRoleDao).getAllUserRoles();
        List<Role> answ = userService.getRoleListOfUserById(1);
        assertEquals(answ.size(), roleMockList.size());
        for(int i = 0; i < answ.size(); i++) {
            assertEquals(answ.get(i).getIdRole(), roleMockList.get(i).getIdRole());
        }
    }

    @Test
    public void loginTest() {
        InlineObject authData = new InlineObject();
        authData.setLogin("name1");
        authData.setPassword("123");
        doReturn(userMockList.get(0)).when(userDao).getUserByLogin("name1");
        doReturn("123").when(userMockList.get(0)).getPassword();
        boolean answ = userService.login(authData);
        assertTrue(answ);
    }

    @Test
    public void getTasksByTelegramIdTest() {
        doReturn(userMockList).when(userDao).getAllUsers();
        doReturn(taskMockList).when(taskDao).getAllTasks();
        StatusRecord statusRecord = mock(StatusRecord.class);
        doReturn(1).when(statusRecord).getIdStatus();
        doReturn(statusRecord).when(statusDao).getStatusByName("active");
        List<Task> answ = userService.getActiveTasksByTelegramId(1);
        assertEquals(1, answ.size());
        assertEquals(taskMockList.get(0).getIdTask(), answ.get(0).getIdTask());

    }
}
