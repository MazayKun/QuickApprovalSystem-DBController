package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.UserRoleRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IUserRoleDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.UserRole.USER_ROLE;


/**
 * DAO-класс для взаимодействия с таблицей user_role
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class UserRoleDao implements IUserRoleDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public UserRoleDao(DSLContext dslContext){
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех значений user_role
     * @return List<UserRoleRecord>
     */
    @Override
    public List<UserRoleRecord> getAllUserRoles() {
        List<UserRoleRecord> answer = new ArrayList<>();
        Result<UserRoleRecord> result = dslContext.selectFrom(USER_ROLE).fetch();
        for (UserRoleRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает запись user_role с заданным id
     * @param id
     * @return UserRoleRecord
     */
    @Override
    public UserRoleRecord getUserRoleById(int id) {
        return dslContext.selectFrom(USER_ROLE).where(USER_ROLE.USER_ROLE_ID.eq(id)).fetchAny();
    }

    /**
     * Обновляет значение записи user_role
     * @param id
     * @param newUserRole объект, хранящий новые данные о записи user_role
     * @return true, если все прошло усппешно, иначе false
     */
    @Override
    public boolean updateUserRoleById(int id, UserRoleRecord newUserRole) {
        int response = dslContext.update(USER_ROLE).
                set(USER_ROLE.USER_ID, newUserRole.getUserId()).
                set(USER_ROLE.ROLE_ID, newUserRole.getRoleId()).
                where(USER_ROLE.USER_ROLE_ID.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новую запись user_role
     * @param newUserRole
     * @return id новой записи
     */
    @Override
    public int addUserRole(UserRoleRecord newUserRole) {
        Record record = dslContext.insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE_ID).
                values(newUserRole.getUserId(), newUserRole.getRoleId()).returning(USER_ROLE.USER_ROLE_ID).fetchOne();
        return record.getValue(USER_ROLE.USER_ROLE_ID);
    }
}
