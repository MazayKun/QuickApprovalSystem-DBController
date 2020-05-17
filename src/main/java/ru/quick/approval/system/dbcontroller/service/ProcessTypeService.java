package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.api.model.ProcessType;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessTypeDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessTypeService;
import ru.quick.approval.system.dbcontroller.translator.ITranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с контроллером ProcessTypeController
 * @author Игорь М
 */

@Service
public class ProcessTypeService implements IProcessTypeService {

    private final IProcessTypeDao processTypeDao;
    private final ITranslator<ProcessTypeRecord, ProcessType> processTypeTranslator;

    @Autowired
    public ProcessTypeService(IProcessTypeDao processTypeDao, ITranslator<ProcessTypeRecord, ProcessType> processTypeTranslator) {
        this.processTypeTranslator = processTypeTranslator;
        this.processTypeDao = processTypeDao;
    }


    /**
     * Возвращает все ProcessTypes
     * @return List<ProcessTypes>
     */
    @Override
    public List<ProcessType> getAllProcessTypes() {
        List<ProcessType> processTypeList = new ArrayList<>();
        List<ProcessTypeRecord> allProcessTypes = processTypeDao.getAllProcessTypes();
        for (ProcessTypeRecord processTypeRecord: allProcessTypes) {
            processTypeList.add(processTypeTranslator.translate(processTypeRecord));
        }
        return processTypeList;
    }

    /**
     * Создаёт новый ProcessType
     * @param processType - объект типа processType
     * @return - true/false
     */
    @Override
    public boolean createNewProcessType(ProcessType processType) {
        return processTypeDao.addProcessType(processTypeTranslator.reverseTranslate(processType)) != 0;
    }

    /**
     * Возвращает ProcessType по id
     * @param id - id ProcessType
     * @return - ProcessType
     */
    @Override
    public ProcessType getProcessTypeById(int id) {
        return processTypeTranslator.translate(processTypeDao.getProcessTypeById(id));
    }
}
