package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.ProcessRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.Process;

/**
 * Переводит процессы из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class ProcessTranslator implements ITranslator<ProcessRecord, Process> {

    @Override
    public Process translate(ProcessRecord source) {
        Process process = new Process();
        process.setIdProcess(source.getIdProcess());
        process.setProcessTypeId(source.getProcessTypeId());
        process.setName(source.getName());
        process.setDescription(source.getDescription());
        process.setUserStartId(source.getUserStartId());
        process.setDateStart(source.getDateStart());
        process.setDateEndPlanning(source.getDateEndPlanning());
        process.setDateEndFact(source.getDateEndFact());
        process.setStatusId(source.getStatusId());
        return process;
    }

    @Override
    public ProcessRecord reverseTranslate(Process source) {
        ProcessRecord processRecord = new ProcessRecord(
                source.getIdProcess(),
                source.getProcessTypeId(),
                source.getName(),
                source.getDescription(),
                source.getUserStartId(),
                source.getDateStart(),
                source.getDateEndPlanning(),
                source.getDateEndFact(),
                source.getStatusId()
        );
        return processRecord;
    }
}
