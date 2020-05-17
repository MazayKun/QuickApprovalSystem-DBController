package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.ProcessTypeRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.ProcessType;

/**
 * Переводит типы процессов из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class ProcessTypeTranslator implements ITranslator<ProcessTypeRecord, ProcessType> {
    @Override
    public ProcessType translate(ProcessTypeRecord source) {
        ProcessType processType = new ProcessType();
        processType.setIdProcessType(source.getIdProcessType());
        processType.setName(source.getName());
        processType.setDescription(source.getDescription());
        processType.setTimeToDo(source.getTimeToDo());
        return processType;
    }

    @Override
    public ProcessTypeRecord reverseTranslate(ProcessType source) {
        ProcessTypeRecord processTypeRecord = new ProcessTypeRecord(
                source.getIdProcessType(),
                source.getName(),
                source.getDescription(),
                source.getTimeToDo()
        );
        return processTypeRecord;
    }
}
