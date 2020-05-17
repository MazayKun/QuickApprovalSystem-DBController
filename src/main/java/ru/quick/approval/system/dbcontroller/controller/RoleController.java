package ru.quick.approval.system.dbcontroller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.quick.approval.system.api.controller.RoleApi;
import ru.quick.approval.system.api.model.Role;
import ru.quick.approval.system.api.model.User;
import ru.quick.approval.system.api.model.UserWithoutPassword;
import ru.quick.approval.system.dbcontroller.service.RoleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Mikheev
 * @version 1.0
 */

@Slf4j
@RestController
public class RoleController implements RoleApi {

    private static final HttpStatus ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus OK = HttpStatus.OK;
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
        log.info("RoleController was created");
    }

    @Override
    public ResponseEntity<Void> addRole(@Valid Role role) {
        log.info("Request for adding role");
        if(roleService.addRole(role)){
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(ERROR);
    }

    @Override
    public ResponseEntity<List<Role>> allRoles() {
        log.info("Request for all roles");
        return new ResponseEntity<>(roleService.allRoles(), OK);
    }

    @Override
    public ResponseEntity<List<UserWithoutPassword>> getUsersByRoleId(Integer id) {
        log.info("Request for all users by role id (role id = " + id + ")");
        return new ResponseEntity<>(roleService.getUsersByRoleId(id), OK);
    }

    @Override
    public ResponseEntity<Role> getRoleById(Integer id) {
        log.info("Request for role by id (role od = " + id + ")");
        return new ResponseEntity<>(roleService.getRoleById(id), OK);
    }

}
