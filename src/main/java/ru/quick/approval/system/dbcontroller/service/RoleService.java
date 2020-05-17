package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.RoleQasRecord;
import org.jooq.demo.db.tables.records.UserQasRecord;
import org.jooq.demo.db.tables.records.UserRoleRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.Role;
import ru.quick.approval.system.api.model.UserWithoutPassword;
import ru.quick.approval.system.dbcontroller.dao.RoleDao;
import ru.quick.approval.system.dbcontroller.dao.UserDao;
import ru.quick.approval.system.dbcontroller.dao.UserRoleDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IRoleDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserRoleDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IRoleService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для обработки запросов RoleController
 * @author Kirill Mikheev
 * @version 1.0
 */

@Service
public class RoleService implements IRoleService {

    private IRoleDao roleDao;
    private IUserDao userDao;
    private IUserRoleDao userRoleDao;
    private final ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator;
    private final ITranslator<RoleQasRecord, Role> roleTranslator;

    @Autowired
    public RoleService(IRoleDao roleDao, IUserDao userDao, IUserRoleDao userRoleDao, ITranslator<UserQasRecord, UserWithoutPassword> userWithoutPasswordTranslator, ITranslator<RoleQasRecord, Role> roleTranslator){
        this.userWithoutPasswordTranslator = userWithoutPasswordTranslator;
        this.roleTranslator = roleTranslator;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
    }

    /**
     * Добавляет новую роль в таблицу
     * @param role
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean addRole(Role role) {
        return roleDao.addRole(roleTranslator.reverseTranslate(role)) != 0;
    }

    /**
     * Выводит список всех ролей
     * @return
     */
    @Override
    public List<Role> allRoles() {
        List<RoleQasRecord> records = roleDao.getAllRoles();
        List<Role> roles = new ArrayList<>();
        for(RoleQasRecord tmp : records){
            roles.add(roleTranslator.translate(tmp));
        }
        return roles;
    }

    /**
     * Возвращает пользователей данной роли
     * @param id
     * @return List<UserWithoutPassword>
     */
    @Override
    public List<UserWithoutPassword> getUsersByRoleId(int id) {
        List<UserWithoutPassword> answer = new ArrayList<>();
        List<UserRoleRecord> records = userRoleDao.getAllUserRoles();
        for(UserRoleRecord tmp : records){
            if(tmp.getRoleId().compareTo(id) == 0){
                answer.add(userWithoutPasswordTranslator.translate(userDao.getUserById(tmp.getUserId())));
            }
        }
        return answer;
    }

    /**
     * Возвращает роль с заданным id
     * @param id
     * @return Role
     */
    @Override
    public Role getRoleById(int id) {
        return roleTranslator.translate(roleDao.getRoleById(id));
    }
}
