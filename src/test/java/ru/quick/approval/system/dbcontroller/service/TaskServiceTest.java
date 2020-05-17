package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.TaskRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.dao.iDao.IStatusDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.ITaskDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessCurator;
import ru.quick.approval.system.dbcontroller.service.iservice.ITaskService;
import ru.quick.approval.system.dbcontroller.translator.TaskTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Класс для тестирования большей части методов класса TaskService
 * @author Kirill Mikheev
 * @version 1.0
 */

public class TaskServiceTest {

    private ITaskService taskService;

    @Mock
    private ITaskDao taskDao;
    @Mock
    private IStatusDao statusDao;

    private List<TaskRecord> taskMockList;

    @BeforeEach
    public void init() {
        initMocks(this);
        //TODO: Костыль, убери потом
        IProcessCurator processCurator = mock(ProcessCurator.class);
        taskService = new TaskService(processCurator, taskDao, statusDao, new TaskTranslator());
        taskMockList = new ArrayList<>();
        TaskRecord task;
        for (int i = 1; i <= 2; i++) {
            task = mock(TaskRecord.class);
            when(task.getIdTask()).thenReturn(i);
            taskMockList.add(task);
        }
    }

    @Test
    public void getAllTaskTest() {
        doReturn(taskMockList).when(taskDao).getAllTasks();
        List<Task> answ = taskService.getAllTask();
        assertEquals(answ.size(), taskMockList.size());
        for(int i = 0; i < answ.size(); i++) {
            assertEquals(answ.get(i).getIdTask(), taskMockList.get(i).getIdTask());
        }
    }

    @Test
    public void getTaskByIdTest() {
        doReturn(taskMockList.get(0)).when(taskDao).getTaskById(1);
        Task answ = taskService.getTaskById(1);
        assertEquals(taskMockList.get(0).getIdTask(), answ.getIdTask());
    }

}
