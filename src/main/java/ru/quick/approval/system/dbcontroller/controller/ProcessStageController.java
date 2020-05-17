package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.quick.approval.system.api.controller.StageApi;
import ru.quick.approval.system.api.model.ProcessStage;
import ru.quick.approval.system.dbcontroller.service.ProcessStageService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер ProcessStage
 * @author Игорь М
 */

@Slf4j
@RestController
public class ProcessStageController implements StageApi {

    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus OK = HttpStatus.OK;
    private final ProcessStageService processStageService;

    @Autowired
    public ProcessStageController(ProcessStageService processStageService) {
        this.processStageService = processStageService;
        log.info("ProcessStageController was created");
    }

    @Override
    public ResponseEntity<Void> addProcessStageByTypeId(Integer id, @Valid ProcessStage processStage) {
        log.info("Request for adding process stage by process type id (process stage id = " + id + ")");
        if (processStageService.addStageByProcessType(id, processStage))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<List<ProcessStage>> allProcessStages() {
        log.info("Request for all process stages");
        return new ResponseEntity<>(processStageService.getAllStages(), OK);
    }

    @Override
    public ResponseEntity<ProcessStage> getProcessStageById(Integer id) {
        log.info("Request for process stage by id (process stage id = " + id + ")");
        return new ResponseEntity<>(processStageService.getStageById(id), OK);
    }
}
