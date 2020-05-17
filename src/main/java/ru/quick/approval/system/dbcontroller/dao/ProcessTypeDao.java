package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.ProcessTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessTypeDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.ProcessType.PROCESS_TYPE;


/**
 * DAO-класс для взаимодейтсвия с таблицей process_type
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class ProcessTypeDao implements IProcessTypeDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public ProcessTypeDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех типов процессов
     * @return List<ProcessTypeRecord>
     */
    @Override
    public List<ProcessTypeRecord> getAllProcessTypes() {
        List<ProcessTypeRecord> answer = new ArrayList<>();
        Result<ProcessTypeRecord> result = dslContext.selectFrom(PROCESS_TYPE).fetch();
        for (ProcessTypeRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает тип процесса с заданным id
     * @param id
     * @return ProcessTypeRecord
     */
    @Override
    public ProcessTypeRecord getProcessTypeById(int id) {
        return dslContext.selectFrom(PROCESS_TYPE).where(PROCESS_TYPE.ID_PROCESS_TYPE.eq(id)).fetchAny();
    }

    /**
     * Обновляет тип процесса с заданным id
     * @param id
     * @param newProcessType объект, хранящий новые данные
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean updateProcessTypeById(int id, ProcessTypeRecord newProcessType) {
        int response = dslContext.update(PROCESS_TYPE).
                set(PROCESS_TYPE.NAME, newProcessType.getName()).
                set(PROCESS_TYPE.DESCRIPTION, newProcessType.getDescription()).
                set(PROCESS_TYPE.TIME_TO_DO, newProcessType.getTimeToDo()).
                where(PROCESS_TYPE.ID_PROCESS_TYPE.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новый тип процесса
     * @param newProcessType
     * @return id нового этипа процесса
     */
    @Override
    public int addProcessType(ProcessTypeRecord newProcessType) {
        Record record = dslContext.insertInto(
                PROCESS_TYPE, PROCESS_TYPE.NAME, PROCESS_TYPE.DESCRIPTION, PROCESS_TYPE.TIME_TO_DO)
                .values(newProcessType.getName(), newProcessType.getDescription(), newProcessType.getTimeToDo()).
                returning(PROCESS_TYPE.ID_PROCESS_TYPE).fetchOne();
        return record.getValue(PROCESS_TYPE.ID_PROCESS_TYPE);
    }
}
