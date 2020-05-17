package ru.quick.approval.system.dbcontroller.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.demo.db.tables.records.TaskRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.dbcontroller.dao.iDao.ITaskDao;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.demo.db.tables.Task.TASK;


/**
 * DAO-класс для взаимодействия с таблицей task
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class TaskDao implements ITaskDao {

    /** Нужно для соединения и работы с бд*/
    private final DSLContext dslContext;

    @Autowired
    public TaskDao(DSLContext dslContext){
        this.dslContext = dslContext;
    }

    /**
     * Возвращает список всех задач
     * @return List<TaskRecord>
     */
    @Override
    public List<TaskRecord> getAllTasks() {
        List<TaskRecord> answer = new ArrayList<>();
        Result<TaskRecord> result = dslContext.selectFrom(TASK).fetch();
        for (TaskRecord record : result){
            answer.add(record);
        }
        return answer;
    }

    /**
     * Возвращает задачу с заданным id
     * @param id
     * @return TaskRecord
     */
    @Override
    public TaskRecord getTaskById(int id) {
        return dslContext.selectFrom(TASK).where(TASK.ID_TASK.eq(id)).fetchAny();
    }

    /**
     * Возвращает все задачи, привязанные к процессу
     * @param process_id - id процесса
     * @return - List<TaskRecord>
     */
    @Override
    public List<TaskRecord> getAllTasksByProcessId(int process_id) {
        return dslContext.selectFrom(TASK).where(TASK.PROCESS_ID.eq(process_id)).fetch();
    }

    /**
     * Обновляет данные задачи с заданным id
     * @param id
     * @param newTask объект, хранящий новые данные
     * @return true, если все прошло успешно, иначе false
     */
    @Override
    public boolean updateTaskById(int id, TaskRecord newTask) {
        int response = dslContext.update(TASK).
                set(TASK.PROCESS_ID, newTask.getProcessId()).
                set(TASK.USER_PERFORMER_ID, newTask.getUserPerformerId()).
                set(TASK.ROLE_PERFORMER_ID, newTask.getRolePerformerId()).
                set(TASK.DATE_START, newTask.getDateStart()).
                set(TASK.DATE_END_PLANNING, newTask.getDateEndPlanning()).
                set(TASK.DATE_END_FACT, newTask.getDateEndFact()).
                set(TASK.STATUS_ID, newTask.getStatusId()).
                where(TASK.ID_TASK.eq(id)).execute();
        return response != 0;
    }

    /**
     * Добавляет новую задачу
     * @param newTask
     * @return id новой задачи
     */
    @Override
    public int addTask(TaskRecord newTask) {
        Record record = dslContext.insertInto(
                TASK, TASK.PROCESS_ID, TASK.USER_PERFORMER_ID, TASK.ROLE_PERFORMER_ID,
                TASK.DATE_START, TASK.DATE_END_PLANNING, TASK.DATE_END_FACT, TASK.STATUS_ID)
                .values(newTask.getProcessId(), newTask.getUserPerformerId(), newTask.getRolePerformerId(),
                        newTask.getDateStart(), newTask.getDateEndPlanning(),
                        newTask.getDateEndFact(), newTask.getStatusId())
                .returning(TASK.ID_TASK).fetchOne();
        return record.getValue(TASK.ID_TASK);
    }
}
