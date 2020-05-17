package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.quick.approval.system.api.controller.TypeApi;
import ru.quick.approval.system.api.model.ProcessType;
import ru.quick.approval.system.dbcontroller.service.ProcessTypeService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер ProcessType
 * @author Игорь М
 */

@Slf4j
@RestController
public class ProcessTypeController implements TypeApi {

    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus OK = HttpStatus.OK;
    private final ProcessTypeService processTypeService;

    @Autowired
    public ProcessTypeController(ProcessTypeService processTypeService) {
        this.processTypeService = processTypeService;
        log.info("ProcessTypeController was created");
    }

    @Override
    public ResponseEntity<Void> addProcessType(@Valid ProcessType processType) {
        log.info("Request for adding process type");
        if (processTypeService.createNewProcessType(processType))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<List<ProcessType>> allProcessTypes() {
        log.info("Request for all process types");
        return new ResponseEntity<>(processTypeService.getAllProcessTypes(), OK);
    }

    @Override
    public ResponseEntity<ProcessType> getProcessTypeById(Integer id) {
        log.info("Request for process type by id (process type id = " + id + ")");
        return new ResponseEntity<>(processTypeService.getProcessTypeById(id), OK);
    }
}
