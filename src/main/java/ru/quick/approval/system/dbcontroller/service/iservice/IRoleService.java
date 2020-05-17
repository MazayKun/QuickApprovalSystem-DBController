package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.Role;
import ru.quick.approval.system.api.model.UserWithoutPassword;

import java.util.List;

/**
 * Интерфейс сервиса для обработки запросов RoleController
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IRoleService {

    /**
     * Добавляет новую роль в таблицу
     * @param role
     * @return true, если все прошло успешно, иначе false
     */
    boolean addRole(Role role);

    /**
     * Выводит список всех ролей
     * @return
     */
    List<Role> allRoles();

    /**
     * Возвращает пользователей данной роли
     * @param id
     * @return List<UserWithoutPassword>
     */
    List<UserWithoutPassword> getUsersByRoleId(int id);

    /**
     * Возвращает роль с заданным id
     * @param id
     * @return Role
     */
    Role getRoleById(int id);
}
