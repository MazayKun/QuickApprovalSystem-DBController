package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.ProcessType;

import java.util.List;

public interface IProcessTypeService {

    /**
     * Возвращает все ProcessTypes
     * @return List<ProcessTypes>
     */
    List<ProcessType> getAllProcessTypes();

    /**
     * Создаёт новый ProcessType
     * @param processType - объект типа processType
     * @return - true/false
     */
    boolean createNewProcessType(ProcessType processType);

    /**
     * Возвращает ProcessType по id
     * @param id - id ProcessType
     * @return - ProcessType
     */
    ProcessType getProcessTypeById(int id);
}
