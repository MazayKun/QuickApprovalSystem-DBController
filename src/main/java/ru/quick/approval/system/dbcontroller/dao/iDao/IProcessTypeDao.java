package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.ProcessTypeRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для взаимодействия с таблицей process_type
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IProcessTypeDao {

    List<ProcessTypeRecord> getAllProcessTypes();

    ProcessTypeRecord getProcessTypeById(int id);

    boolean updateProcessTypeById(int id, ProcessTypeRecord newProcessType);

    int addProcessType(ProcessTypeRecord newProcessType);

}
