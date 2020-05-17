package ru.quick.approval.system.dbcontroller.service.iservice;

import ru.quick.approval.system.api.model.Process;

/**
 * Класс отвечает за работу с процессами и их жизненным циклом
 * @author Kirill Mikheev
 * @version 1.0
 */

public interface IProcessCurator {

    /**
     * Создание процесса, создание и рассылка задач первого этапа на подтверждение
     * @param processTypeId тип процесса
     * @param process объект с информацией о процессе
     */
    boolean createProcess(Integer processTypeId, Process process);

    /**
     * Здесь происходит логика, которая должна быть выполнена после очередного подтверждения задачи
     * Проверка, выполнен ли очередной этап. выполнен ли процесс и прочее
     * @param taskId айди задачи
     */
    void agreeTask(Integer taskId);

    /**
     * Здесь происходит логика, которая должна быть выполнена после отклонения задачи одним из пользователей
     * @param processId айди процесса, к которому относилаь задача
     */
    void deniedTask(Integer processId);
}
