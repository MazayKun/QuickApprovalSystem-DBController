package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.StatusRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IStatusDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.Status.STATUS;


/**
 * DAO-класс для работы с таблицей status
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class StatusDao implements IStatusDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public StatusDao(DSLContext dslContext){
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех статусов
     * @return List<StatusRecord>
     */
    @Override
    public List<StatusRecord> getAllStatuses() {
        List<StatusRecord> answer = new ArrayList<>();
        Result<StatusRecord> result = dslContext.selectFrom(STATUS).fetch();
        for (StatusRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает статус с заданным id
     * @param id
     * @return StatusRecord
     */
    @Override
    public StatusRecord getStatusById(int id) {
        return dslContext.selectFrom(STATUS).where(STATUS.ID_STATUS.eq(id)).fetchAny();
    }

    /**
     * Возвращает статус по названию
     * @param name - название статуса
     * @return - StatusRecord
     */
    @Override
    public StatusRecord getStatusByName(String name) {
        return dslContext.selectFrom(STATUS).where(STATUS.NAME.equalIgnoreCase(name)).fetchAny();
    }

    /**
     * Обновляет данные статуса по заданному id
     * @param id
     * @param newStatus объект, соержащий новые данные
     * @return true, если все прошло успешно, иначе false
     */


    @Override
    public boolean updateStatusById(int id, StatusRecord newStatus) {
        int response = dslContext.update(STATUS).set(STATUS.NAME ,newStatus.getName()).where(STATUS.ID_STATUS.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новый статус
     * @param newStatus
     * @return id нового статуаа
     */
    @Override
    public int addStatus(StatusRecord newStatus) {
        Record record = dslContext.insertInto(STATUS, STATUS.NAME).values(newStatus.getName()).returning(STATUS.ID_STATUS).fetchOne();
        return record.getValue(STATUS.ID_STATUS);
    }
}
