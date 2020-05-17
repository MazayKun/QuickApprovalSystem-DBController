package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.UserQas;
import org.jooq.demo.db.tables.records.UserQasRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.UserQas.USER_QAS;


/**
 * DAO-класс для работы с таблицей user_qas
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class UserDao implements IUserDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    public UserDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех пользователей в таблице
     * @return List<UserQasRecord>
     */
    @Override
    public List<UserQasRecord> getAllUsers() {
        List<UserQasRecord> answer = dslContext.selectFrom(USER_QAS).fetch();
        return answer;
    }

    /**
     * Возвращает пользователя с заданным id
     * @param id
     * @return UserQasRecord
     */
    @Override
    public UserQasRecord getUserById(int id) {
        return dslContext.selectFrom(USER_QAS).where(USER_QAS.ID_USER.eq(id)).fetchAny();
    }

    /**
     * Возвращает пользователя с заданным login'ом
     * @param login
     * @return UserQasRecord
     */
    @Override
    public UserQasRecord getUserByLogin(String login) {
        return dslContext.selectFrom(USER_QAS).where(USER_QAS.LOGIN.eq(login)).fetchAny();
    }

    /**
     * Обновляет данные для пользователя с заданным id
     * @param id
     * @param newUser объект записи пользователя с новыми данными
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean updateUserById(int id, UserQasRecord newUser) {
        int response = dslContext.update(USER_QAS).
                set(USER_QAS.FIO, newUser.getFio()).
                set(USER_QAS.LOGIN, newUser.getFio()).
                set(USER_QAS.PASSWORD, newUser.getPassword()).
                set(USER_QAS.EMAIL, newUser.getEmail()).
                set(USER_QAS.TELEGRAM_CHAT_ID, newUser.getTelegramChatId()).
                where(USER_QAS.ID_USER.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет в аблицу нового пользователя
     * @param newUser
     * @return id нового пользователя
     */
    @Override
    public int addUser(UserQasRecord newUser) {
        Record record = dslContext.insertInto(USER_QAS, USER_QAS.FIO, USER_QAS.LOGIN, USER_QAS.PASSWORD, USER_QAS.EMAIL, USER_QAS.TELEGRAM_CHAT_ID)
                .values(newUser.getFio(), newUser.getLogin(), newUser.getPassword(), newUser.getEmail(), newUser.getTelegramChatId())
                .returning(USER_QAS.ID_USER).fetchOne();
        return record.getValue(USER_QAS.ID_USER);
    }
}
