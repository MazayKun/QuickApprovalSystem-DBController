package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.quick.approval.system.api.controller.ProcessApi;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.api.model.StatusType;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.service.ProcessService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер ProcessController
 * @author Игорь М
 */

@Slf4j
@RestController
public class ProcessController implements ProcessApi {

    private static final HttpStatus OK = HttpStatus.OK;
    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private final ProcessService processService;

    @Autowired
    public ProcessController(ProcessService processService) {
        this.processService = processService;
        log.info("ProcessController was created");
    }

    @Override
    public ResponseEntity<Void> createProcessByProcessType(Integer id, @Valid Process process) {
        log.info("Request for creating process by process type (process type id = " + id + ")");
        if (processService.createNewProcessByProcessType(id, process))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> createProcessTaskByRoleId(Integer id, Integer roleId, @Valid Task task) {
        log.info("Request for creating process task by role id (process id = "+ id +  ", role id = " + roleId + ")");
        if (processService.createNewTaskByRoleId(id, roleId, task))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> createProcessTaskByUserId(Integer id, Integer userId, @Valid Task task) {
        log.info("Request for creating process task by user id (process id = "+ id +  ", user id = " + userId + ")");
        if (processService.createNewTaskByUserId(id, userId, task))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<List<Process>> getAllActiveProcess() {
        log.info("Request for all active processes");
        return new ResponseEntity<>(processService.getProcessStatusActive(), OK);
    }

    @Override
    public ResponseEntity<List<Process>> getAllCompleteProcess() {
        log.info("Request for all complete processes");
        return new ResponseEntity<>(processService.getProcessStatusComplete(), OK);

    }

    @Override
    public ResponseEntity<List<Process>> getAllProcess() {
        log.info("Request for all processes");
        return new ResponseEntity<>(processService.getAllProcesses(), OK);

    }

    @Override
    public ResponseEntity<List<Task>> getAllTasksByThisId(Integer id) {
        log.info("Request for all tasks of process by id (process id = " + id + ")");
        return new ResponseEntity<>(processService.getAllTaskByProcessId(id), OK);
    }

    @Override
    public ResponseEntity<Process> getProcessById(Integer id) {
        log.info("Request for process by id (process id = " + id + ")");
        return new ResponseEntity<>(processService.getProcessById(id), OK);
    }

    @Override
    public ResponseEntity<StatusType> getProcessStatus(Integer id) {
        log.info("Request for process status by id (process status id = " + id + ")");
        return new ResponseEntity<>(processService.getProcessStatusById(id), OK);
    }

    @Override
    public ResponseEntity<Void> updateProcessById(Integer id, @Valid Process process) {
        log.info("Request for updating process by id (process id = " + id + ")");
        if (processService.updateProcessById(id, process))
            return new ResponseEntity<>(OK);
        return new ResponseEntity<>(ERROR);
    }
}
