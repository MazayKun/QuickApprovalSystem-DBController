package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.StatusRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для взаимодействия с таблицей status
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IStatusDao {

    List<StatusRecord> getAllStatuses();

    StatusRecord getStatusById(int id);

    StatusRecord getStatusByName(String name);

    boolean updateStatusById(int id, StatusRecord newStatus);

    int addStatus(StatusRecord newStatus);
}
