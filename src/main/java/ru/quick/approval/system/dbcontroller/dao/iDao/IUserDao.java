package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.UserQasRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для работы с таблицей user_qas
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IUserDao {

    List<UserQasRecord> getAllUsers();

    UserQasRecord getUserById(int id);

    UserQasRecord getUserByLogin(String login);

    boolean updateUserById(int id, UserQasRecord newUser);

    int addUser(UserQasRecord newUser);
}
