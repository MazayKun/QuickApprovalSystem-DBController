package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.jooq.demo.db.tables.records.UserQasRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.quick.approval.system.api.controller.UserApi;
import ru.quick.approval.system.api.model.*;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.dbcontroller.service.iservice.IUserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Рестовый контроллер для блока User
 * @author Kirill Mikheev
 * @version 1.0
 */

@Slf4j
@RestController
public class UserController implements UserApi {

    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus OK = HttpStatus.OK;
    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
        log.info("UserController was created");

    }

    @Override
    public ResponseEntity<Void> addRoleToUserById(Integer id, @Valid Role role) {
        log.info("Request for adding role to user by id (user id = " + id + ")");
        ResponseEntity<Void> responseEntity;
        if(userService.addRoleToUserById(id, role)){
            responseEntity = new ResponseEntity<>(OK);
        }else{
            responseEntity = new ResponseEntity<>(ERROR);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<Void> addUser(@Valid User user) {
        log.info("Request for adding user");
        ResponseEntity<Void> responseEntity;
        if(userService.addUser(user)){
            responseEntity = new ResponseEntity<>(OK);
        }else {
            responseEntity = new ResponseEntity<>(ERROR);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<List<UserWithoutPassword>> allUsers() {
        log.info("Request for all users");
        return new ResponseEntity<>(userService.allUsers(), OK);
    }

    @Override
    public ResponseEntity<List<Task>> getActiveTaskListOfUserById(Integer id) {
        log.info("Request for all active tasks of user by id (user id = " + id + ")");
        return new ResponseEntity<>(userService.getActiveTaskListOfUserById(id), OK);
    }

    @Override
    public ResponseEntity<List<Task>> getCompleteTaskListOfUserById(Integer id) {
        log.info("Request for all complete tasks of user by id (user id = " + id + ")");
        return new ResponseEntity<>(userService.getCompleteTaskListOfUserById(id), OK);
    }

    @Override
    public ResponseEntity<List<Task>> getWaitTaskListOfUserById(Integer id) {
        log.info("Request for all wait tasks of user by id (user id = " + id + ")");
        return new ResponseEntity<>(userService.getWaitTaskListOfUserById(id), OK);
    }

    @Override
    public ResponseEntity<List<Role>> getRoleListOfUserById(Integer id) {
        log.info("Request for all roles of user by id (user id = " + id + ")");
        return new ResponseEntity<>(userService.getRoleListOfUserById(id), OK);
    }

    @Override
    public ResponseEntity<List<Task>> getTaskListOfUserById(Integer id) {
        log.info("Request for all tasks of user by id (user id = "+ id + ")");
        return new ResponseEntity<>(userService.getTaskListOfUserById(id), OK);
    }

    @Override
    public ResponseEntity<UserWithoutPassword> getUserById(Integer id) {
        log.info("Request for user by id (user id = " + id + ")");
        ResponseEntity<UserWithoutPassword> responseEntity;
        UserWithoutPassword userWithoutPassword = userService.getUserById(id);
        if(userWithoutPassword != null){
            responseEntity = new ResponseEntity<>(userWithoutPassword, OK);
        }else {
            responseEntity = new ResponseEntity<>(null, ERROR);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<Boolean> login(@Valid InlineObject inlineObject) {
        log.info("Request for check auth info of user");
        return new ResponseEntity<>(userService.login(inlineObject), OK);
    }

    @Override
    public ResponseEntity<Void> updateUser(Integer id, @Valid User user) {
        log.info("Request for updating user by id (user id = "+ id + ")");
        ResponseEntity<Void> responseEntity;
        if(userService.updateUserById(id, user)){
            responseEntity = new ResponseEntity<>(OK);
        }else {
            responseEntity = new ResponseEntity<>(ERROR);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<List<Task>> getActiveTasksByTelegramId(Integer telegramId) {
        return new ResponseEntity<>(userService.getActiveTasksByTelegramId(telegramId), OK);
    }

    @Override
    public ResponseEntity<List<Process>> getProcessesByUserId(Integer id) {
        return new ResponseEntity<>(userService.getProcessesByUserId(id), OK);
    }
}
