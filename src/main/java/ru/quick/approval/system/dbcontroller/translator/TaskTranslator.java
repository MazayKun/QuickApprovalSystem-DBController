package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.TaskRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.Task;

/**
 * Переводит задачи из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class TaskTranslator implements ITranslator<TaskRecord, Task> {
    @Override
    public Task translate(TaskRecord source) {
        Task task = new Task();
        task.setIdTask(source.getIdTask());
        task.setProcessId(source.getProcessId());
        task.setUserPerformerId(source.getUserPerformerId());
        task.setRolePerformerId(source.getRolePerformerId());
        task.setDateStart(source.getDateStart());
        task.setDateEndPlanning(source.getDateEndPlanning());
        task.setDateEndFact(source.getDateEndFact());
        task.setStatusId(source.getStatusId());
        return task;
    }

    @Override
    public TaskRecord reverseTranslate(Task source) {
        TaskRecord taskRecord = new TaskRecord(
                source.getIdTask(),
                source.getProcessId(),
                source.getUserPerformerId(),
                source.getRolePerformerId(),
                source.getDateStart(),
                source.getDateEndPlanning(),
                source.getDateEndFact(),
                source.getStatusId()
        );
        return taskRecord;
    }
}
