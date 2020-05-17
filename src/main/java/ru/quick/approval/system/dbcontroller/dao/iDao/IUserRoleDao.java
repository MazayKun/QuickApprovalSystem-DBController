package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.UserRoleRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для взаимодействия с таблицей user_role
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IUserRoleDao {

    List<UserRoleRecord> getAllUserRoles();

    UserRoleRecord getUserRoleById(int id);

    boolean updateUserRoleById(int id, UserRoleRecord newUserRole);

    int addUserRole(UserRoleRecord newUserRole);

}
