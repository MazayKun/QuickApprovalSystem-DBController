package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.ProcessStageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessStageDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.ProcessStage.PROCESS_STAGE;


/**
 * DAO-класс для взаимодействия с таблицей process_stage
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class ProcessStageDao implements IProcessStageDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public ProcessStageDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех этапов процесса
     * @return List<ProcessStageRecord>
     */
    @Override
    public List<ProcessStageRecord> getAllProcessStages() {
        List<ProcessStageRecord> answer = new ArrayList<>();
        Result<ProcessStageRecord> result = dslContext.selectFrom(PROCESS_STAGE).fetch();
        for (ProcessStageRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает этап процесса с заданным id
     * @param id
     * @return ProcessStageRecord
     */
    @Override
    public ProcessStageRecord getProcessStageById(int id) {
        return dslContext.selectFrom(PROCESS_STAGE).where(PROCESS_STAGE.ID_PROCESS_STAGE.eq(id)).fetchAny();
    }

    /**
     * Обновляет данные у этапа процесса с заданным id
     * @param id
     * @param newProcessStage объект, хранящий новые данные
     * @return true, если ивсе прошло успешно, иначе false
     */
    @Override
    public boolean updateProcessStageById(int id, ProcessStageRecord newProcessStage) {
        int response = dslContext.update(PROCESS_STAGE).
                set(PROCESS_STAGE.PROCESS_TYPE_ID, newProcessStage.getProcessTypeId()).
                set(PROCESS_STAGE.STAGE, newProcessStage.getStage()).
                set(PROCESS_STAGE.ROLE_ID, newProcessStage.getRoleId()).
                where(PROCESS_STAGE.ID_PROCESS_STAGE.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новый этап процесса
     * @param newProcessStage
     * @return id нового этапа
     */
    @Override
    public int addProcessStage(ProcessStageRecord newProcessStage) {
        Record record = dslContext.insertInto(
                PROCESS_STAGE, PROCESS_STAGE.PROCESS_TYPE_ID, PROCESS_STAGE.STAGE, PROCESS_STAGE.ROLE_ID)
                .values(newProcessStage.getProcessTypeId(), newProcessStage.getStage(), newProcessStage.getRoleId())
                .returning(PROCESS_STAGE.ID_PROCESS_STAGE).fetchOne();
        return record.getValue(PROCESS_STAGE.ID_PROCESS_STAGE);
    }
}
