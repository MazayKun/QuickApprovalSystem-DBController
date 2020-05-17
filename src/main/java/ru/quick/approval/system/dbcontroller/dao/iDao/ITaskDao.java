package ru.quick.approval.system.dbcontroller.dao.iDao;

import org.jooq.demo.db.tables.records.TaskRecord;

import java.util.List;

/**
 * Интерфейс DAO-класса для работы с таблицей task
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface ITaskDao {

    List<TaskRecord> getAllTasks();

    TaskRecord getTaskById(int id);

    List<TaskRecord> getAllTasksByProcessId(int process_id);

    boolean updateTaskById(int id, TaskRecord newTask);

    int addTask(TaskRecord newTask);
}
