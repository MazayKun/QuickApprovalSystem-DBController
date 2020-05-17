package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.RoleQasRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.Role;

/**
 * Переводит роли из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class RoleTranslator implements ITranslator<RoleQasRecord, Role>{

    @Override
    public Role translate(RoleQasRecord source) {
        Role role = new Role();
        role.setIdRole(source.getIdRole());
        role.setName(source.getName());
        return role;
    }

    @Override
    public RoleQasRecord reverseTranslate(Role source) {
        RoleQasRecord roleQasRecord = new RoleQasRecord(
                source.getIdRole(),
                source.getName()
        );
        return roleQasRecord;
    }
}
