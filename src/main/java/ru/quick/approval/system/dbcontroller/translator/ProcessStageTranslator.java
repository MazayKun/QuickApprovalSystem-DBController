package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.ProcessStageRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.ProcessStage;

/**
 * Переводит этапы процессов из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class ProcessStageTranslator implements ITranslator<ProcessStageRecord, ProcessStage> {

    @Override
    public ProcessStage translate(ProcessStageRecord source) {
        ProcessStage processStage = new ProcessStage();
        processStage.setIdProcessStage(source.getIdProcessStage());
        processStage.setProcessTypeId(source.getProcessTypeId());
        processStage.setStage(source.getStage());
        processStage.setRoleId(source.getRoleId());
        return processStage;
    }

    @Override
    public ProcessStageRecord reverseTranslate(ProcessStage source) {
        ProcessStageRecord processStageRecord = new ProcessStageRecord(
                source.getIdProcessStage(),
                source.getProcessTypeId(),
                source.getStage(),
                source.getRoleId()
        );
        return processStageRecord;
    }
}
