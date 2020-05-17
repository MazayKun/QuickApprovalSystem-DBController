package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.ProcessStageRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для взаимодействия с таблицей process_stage
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IProcessStageDao {

    List<ProcessStageRecord> getAllProcessStages();

    ProcessStageRecord getProcessStageById(int id);

    boolean updateProcessStageById(int id, ProcessStageRecord newProcessStage);

    int addProcessStage(ProcessStageRecord newProcessStage);
}
