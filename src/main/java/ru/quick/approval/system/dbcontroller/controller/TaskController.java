package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.quick.approval.system.api.controller.TaskApi;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.service.TaskService;

import javax.validation.Valid;
import java.util.List;

/**
 * Рестовый контроллер для Task
 * @author Игорь М
 */

@Slf4j
@RestController
public class TaskController implements TaskApi {

    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus OK = HttpStatus.OK;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        log.info("TaskController was created ");
    }

    @Override
    public ResponseEntity<List<Task>> getAllTask() {
        log.info("Request for all tasks");
        return new ResponseEntity<>(taskService.getAllTask(), OK);
    }

    @Override
    public ResponseEntity<Task> getTaskById(Integer id) {
        log.info("Request for task by id (task id = " + id + ")");
        return new ResponseEntity<>(taskService.getTaskById(id), OK);
    }

    @Override
    public ResponseEntity<Void> updateTaskById(Integer id, @Valid Task task) {
        log.info("Request for updating task by id (task id = " + id + ")");
        if (taskService.updateTaskById(id,task)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> updateTaskByIdActive(Integer id) {
        log.info("Request for updating task status to active by task id (task id = "+ id + ")");
        if (taskService.updateTaskByIdActive(id)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> updateTaskByIdAgreed(Integer id) {
        log.info("Request for updating task status to agreed by task id (task id = " + id + ")");
        if (taskService.updateTaskByIdAgreed(id)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> updateTaskByIdCanceled(Integer id) {
        log.info("Request for updating task status to canceled by task id (task id = " + id + ")");
        if (taskService.updateTaskByIdCanceled(id)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> updateTaskByIdDenied(Integer id) {
        log.info("Request for updating task status to denied by task id (task id = " + id + ")");
        if (taskService.updateTaskByIdDenied(id)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<Void> updateTaskByIdSended(Integer id) {
        log.info("Request for updating task status to sended by task id (task id = " + id + ")");
        if (taskService.updateTaskByIdSended(id)) {
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }
}
