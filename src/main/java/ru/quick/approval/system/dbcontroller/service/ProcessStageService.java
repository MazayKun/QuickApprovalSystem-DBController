package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessStageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.ProcessStage;
import ru.quick.approval.system.dbcontroller.dao.ProcessStageDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessStageDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessStageService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с контроллером ProcessStageController
 * @author Игорь М
 */

@Service
public class ProcessStageService implements IProcessStageService {

    private final IProcessStageDao processStageDao;

    private final ITranslator<ProcessStageRecord, ProcessStage> processStageTranslator;

    @Autowired
    public ProcessStageService(IProcessStageDao processStageDao, ITranslator<ProcessStageRecord, ProcessStage> processStageTranslator) {
        this.processStageTranslator = processStageTranslator;
        this.processStageDao = processStageDao;
    }

    /**
     * Возвращает все Stage
     * @return - List<ProcessStage>
     */
    @Override
    public List<ProcessStage> getAllStages() {
        List<ProcessStage> processStageList = new ArrayList<>();
        List<ProcessStageRecord> allProcessStages = processStageDao.getAllProcessStages();
        for (ProcessStageRecord processStage: allProcessStages) {
            processStageList.add(processStageTranslator.translate(processStage));
        }
        return processStageList;
    }

    /**
     * Возвращает PrcessStage по id
     * @param id - id ProcessStage
     * @return - ProcessStage
     */
    @Override
    public ProcessStage getStageById(int id) {
        return processStageTranslator.translate(processStageDao.getProcessStageById(id));
    }

    /**
     * Добавляет новый ProcessStage с заданным process_type_id
     * @param process_type_id - id ProcessType
     * @param processStage - объект ProcessStage
     * @return - true/false
     */
    @Override
    public boolean addStageByProcessType(int process_type_id, ProcessStage processStage) {
        processStage.setProcessTypeId(process_type_id);
        return processStageDao.addProcessStage(processStageTranslator.reverseTranslate(processStage)) != 0;
    }
}
