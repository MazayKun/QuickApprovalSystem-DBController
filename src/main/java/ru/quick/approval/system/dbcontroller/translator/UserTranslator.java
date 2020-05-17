package ru.quick.approval.system.dbcontroller.translator;

import org.jooq.demo.db.tables.records.UserQasRecord;
import org.springframework.stereotype.Component;
import ru.quick.approval.system.api.model.User;

/**
 * Переводит пользователей из записей, сгенерированных Jooq'ом, в поджо, сгенерированные OpenAPI, и обратно
 * @author Kirill Mikheev
 * @version 1.0
 */

@Component
public class UserTranslator implements ITranslator<UserQasRecord, User> {

    @Override
    public User translate(UserQasRecord source) {
        User user = new User();
        user.setIdUser(source.getIdUser());
        user.setFio(source.getFio());
        user.setLogin(source.getLogin());
        user.setPassword(source.getPassword());
        user.setEmail(source.getEmail());
        user.setTelegramChatId(source.getTelegramChatId());
        return user;
    }

    @Override
    public UserQasRecord reverseTranslate(User source) {
        UserQasRecord userQasRecord = new UserQasRecord(
                source.getIdUser(),
                source.getFio(),
                source.getLogin(),
                source.getPassword(),
                source.getEmail(),
                source.getTelegramChatId()
        );
        return userQasRecord;
    }
}
