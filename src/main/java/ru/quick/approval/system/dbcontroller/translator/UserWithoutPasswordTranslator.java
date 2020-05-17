package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.UserQasRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.UserWithoutPassword;

/**
 * Переводит пользователей из записей, сгенерированных Jooq'ом, в поджо UserWithoutPassword, сгенерированные OpenAPI
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class UserWithoutPasswordTranslator implements ITranslator<UserQasRecord, UserWithoutPassword> {
    @Override
    public UserWithoutPassword translate(UserQasRecord source) {
        UserWithoutPassword user = new UserWithoutPassword();
        user.setIdUser(source.getIdUser());
        user.setFio(source.getFio());
        user.setLogin(source.getLogin());
        user.setEmail(source.getEmail());
        user.setTelegramChatId(source.getTelegramChatId());
        return user;
    }

    /**
     * В этом методе нет смысла
     */
    @Override
    public UserQasRecord reverseTranslate(UserWithoutPassword source) {
        return null;
    }
}
