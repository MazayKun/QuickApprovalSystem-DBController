package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.RoleQasRecord;
import org.jooq.demo.db.tables.records.UserQasRecord;
import org.jooq.demo.db.tables.records.UserRoleRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.Role;
import ru.quick.approval.system.api.model.User;
import ru.quick.approval.system.api.model.UserWithoutPassword;
import ru.quick.approval.system.dbcontroller.dao.iDao.IRoleDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserRoleDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IRoleService;
import ru.quick.approval.system.dbcontroller.translator.RoleTranslator;
import ru.quick.approval.system.dbcontroller.translator.UserWithoutPasswordTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Класс для тестирования большей части методов класса RoleService
 * @author Kirill Mikheev
 * @version 1.0
 */

public class RoleServiceTest {

    private IRoleService roleService;

    @Mock
    private IRoleDao roleDao;
    @Mock
    private IUserDao userDao;
    @Mock
    private IUserRoleDao userRoleDao;

    private List<RoleQasRecord> roleMockList;
    private List<UserQasRecord> userMockList;
    private List<UserRoleRecord> userRoleMockList;


    @BeforeEach
    public void init() {
        initMocks(this);
        roleService = new RoleService(roleDao, userDao, userRoleDao, new UserWithoutPasswordTranslator(), new RoleTranslator());
        roleMockList = new ArrayList<>();
        userMockList = new ArrayList<>();
        userRoleMockList = new ArrayList<>();
        RoleQasRecord roleQasRecord;
        UserQasRecord userQasRecord;
        UserRoleRecord userRoleRecord;
        for(int i = 1; i <= 3; i++){
            roleQasRecord = mock(RoleQasRecord.class);
            userQasRecord = mock(UserQasRecord.class);
            userRoleRecord = mock(UserRoleRecord.class);
            when(roleQasRecord.getIdRole()).thenReturn(i);
            when(userQasRecord.getIdUser()).thenReturn(i);
            when(userRoleRecord.getUserId()).thenReturn(i);
            when(userRoleRecord.getRoleId()).thenReturn(1);
            roleMockList.add(roleQasRecord);
            userMockList.add(userQasRecord);
            userRoleMockList.add(userRoleRecord);
        }
    }

    @Test
    public void allRolesTest() {
        doReturn(roleMockList).when(roleDao).getAllRoles();
        List<Role> answ = roleService.allRoles();
        assertEquals(answ.size(), roleMockList.size());
        for (int i = 0; i < answ.size(); i++){
            assertEquals(answ.get(i).getIdRole(), roleMockList.get(i).getIdRole());
        }
    }

    @Test
    public void getUsersByRoleIdTest() {
        doReturn(userRoleMockList).when(userRoleDao).getAllUserRoles();
        for (int i = 0; i < 3; i++) {
            doReturn(userMockList.get(i)).when(userDao).getUserById(i + 1);
        }
        List<UserWithoutPassword> answ = roleService.getUsersByRoleId(1);
        assertEquals(answ.size(), 3);
        for(int i = 0; i < 3; i++){
            assertEquals(userMockList.get(i).getIdUser(), answ.get(i).getIdUser());
        }
    }

    @Test
    public void getRoleByIdTest() {
        doReturn(roleMockList.get(0)).when(roleDao).getRoleById(1);
        Role role = roleService.getRoleById(1);
        assertEquals(role.getIdRole(), roleMockList.get(0).getIdRole());
    }
}
