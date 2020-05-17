package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.ProcessStage;

import java.util.List;

public interface IProcessStageService {

    /**
     * Возвращает все Stage
     * @return - List<ProcessStage>
     */
    List<ProcessStage> getAllStages();

    /**
     * Возвращает PrcessStage по id
     * @param id - id ProcessStage
     * @return - ProcessStage
     */
    ProcessStage getStageById(int id);

    /**
     * Добавляет новый ProcessStage с заданным process_type_id
     * @param process_type_id - id ProcessType
     * @param processStage - объект ProcessStage
     * @return - true/false
     */
    boolean addStageByProcessType(int process_type_id, ProcessStage processStage);

}
