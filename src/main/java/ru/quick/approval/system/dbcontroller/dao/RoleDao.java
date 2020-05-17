package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.RoleQasRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IRoleDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.RoleQas.ROLE_QAS;


/**
 * DAO-класс для работы с таблицей role_qas
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class RoleDao implements IRoleDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public RoleDao(DSLContext dslContext){
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех ролей
     * @return List<RoleQasRecord>
     */
    @Override
    public List<RoleQasRecord> getAllRoles() {
        List<RoleQasRecord> answer = new ArrayList<>();
        Result<RoleQasRecord> result = dslContext.selectFrom(ROLE_QAS).fetch();
        for (RoleQasRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает роль с заданным id
     * @param id
     * @return RoleQasRecord
     */
    @Override
    public RoleQasRecord getRoleById(int id) {
        return dslContext.selectFrom(ROLE_QAS).where(ROLE_QAS.ID_ROLE.eq(id)).fetchAny();
    }

    /**
     * Возвращает роль с заданным именем
     * @param name
     * @return RoleQasRecord
     */
    @Override
    public RoleQasRecord getRoleByName(String name) {
        return dslContext.selectFrom(ROLE_QAS).where(ROLE_QAS.NAME.eq(name)).fetchAny();
    }

    /**
     * Обновляет данные для роли по ее id
     * @param id
     * @param newRole объект записи пользователя с новыми данными
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean updateRoleById(int id, RoleQasRecord newRole) {
        int response = dslContext.update(ROLE_QAS).set(ROLE_QAS.NAME ,newRole.getName()).where(ROLE_QAS.ID_ROLE.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новую роль
     * @param newRole
     * @return id новой роли
     */
    @Override
    public int addRole(RoleQasRecord newRole) {
        Record record = dslContext.insertInto(ROLE_QAS, ROLE_QAS.NAME).values(newRole.getName()).returning(ROLE_QAS.ID_ROLE).fetchOne();
        return record.getValue(ROLE_QAS.ID_ROLE);
    }


}
