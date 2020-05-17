package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessRecord;
import org.jooq.demo.db.tables.records.StatusRecord;
import org.jooq.demo.db.tables.records.TaskRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.Process;
import ru.quick.approval.system.api.model.StatusType;
import ru.quick.approval.system.api.model.Task;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.IStatusDao;
import ru.quick.approval.system.dbcontroller.dao.iDao.ITaskDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessCurator;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessService;
import ru.quick.approval.system.dbcontroller.translator.ProcessTranslator;
import ru.quick.approval.system.dbcontroller.translator.TaskTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Класс для тестирования большей части методов класса ProcessService
 * @author Kirill Mikheev
 * @version 1.0
 */

public class ProcessServiceTest {

    private IProcessService processService;

    @Mock
    private IProcessDao processDao;
    @Mock
    private IStatusDao statusDao;
    @Mock
    private ITaskDao taskDao;

    private List<ProcessRecord> processMockList;
    private List<TaskRecord> taskMockList;
    @Mock
    private StatusRecord status;

    @BeforeAll
    public static void setUp() {

    }

    @BeforeEach
    public void init() {
        initMocks(this);
        //TODO: Костыль, убери потом
        IProcessCurator processCurator = mock(ProcessCurator.class);
        processService = new ProcessService(processCurator, processDao, taskDao, statusDao, new TaskTranslator(), new ProcessTranslator());
        processMockList = new ArrayList<>();
        taskMockList = new ArrayList<>();
        ProcessRecord process;
        for (int i = 1; i <= 2; i++) {
            process = mock(ProcessRecord.class);
            when(process.getStatusId()).thenReturn(i);
            when(process.getName()).thenReturn("name" + i);
            when(process.getIdProcess()).thenReturn(i);
            processMockList.add(process);
        }
        TaskRecord task;
        task = mock(TaskRecord.class);
        when(task.getIdTask()).thenReturn(1);
        when(task.getProcessId()).thenReturn(1);
        taskMockList.add(task);
        when(status.getName()).thenReturn("active");
    }

    @Test
    public void getAllProcessesTest() {
        doReturn(processMockList).when(processDao).getAllProcesses();
        List<Process> answ = processService.getAllProcesses();
        assertEquals(answ.size(), processMockList.size());
        for(int i = 0; i < answ.size(); i++){
            assertEquals(answ.get(i).getIdProcess(), processMockList.get(i).getIdProcess());
        }
    }

    @Test
    public void getProcessByIdTest() {
        doReturn(processMockList.get(0)).when(processDao).getProcessById(1);
        Process answ = processService.getProcessById(1);
        assertEquals(answ.getName(), processMockList.get(0).getName());
    }

    @Test
    public void getAllTaskByProcessIdTest() {
        doReturn(taskMockList).when(taskDao).getAllTasksByProcessId(1);
        List<Task> answ = processService.getAllTaskByProcessId(1);
        assertEquals(answ.size(), taskMockList.size());
        assertEquals(answ.get(0).getIdTask(), taskMockList.get(0).getIdTask());
    }

    @Test
    public void getProcessStatusByIdTest() {
        doReturn(status).when(statusDao).getStatusById(1);
        doReturn(processMockList.get(0)).when(processDao).getProcessById(1);
        StatusType answ = processService.getProcessStatusById(1);
        assertEquals(answ.toString(), status.getName());
    }

    @Test
    public void getProcessStatusActiveTest() {
        List<ProcessRecord> test = new ArrayList<ProcessRecord>();
        test.add(processMockList.get(0));
        doReturn(test).when(processDao).getProcessByStatusId(1);
        doReturn(status).when(statusDao).getStatusByName("Active");
        doReturn(1).when(status).getIdStatus();
        List<Process> answ = processService.getProcessStatusActive();
        assertEquals(answ.size(), 1);
        assertEquals(answ.get(0).getName(), processMockList.get(0).getName());
    }

    @Test
    public void getProcessStatusCompleteTest() {
        List<ProcessRecord> test1 = new ArrayList<>();
        List<ProcessRecord> test2 = new ArrayList<>();
        test1.add(processMockList.get(0));
        test2.add(processMockList.get(1));
        doReturn(test1).when(processDao).getProcessByStatusId(1);
        doReturn(test2).when(processDao).getProcessByStatusId(2);
        doReturn(status).when(statusDao).getStatusByName("denied");
        doReturn(status).when(statusDao).getStatusByName("agreed");
        doReturn(1, 2).when(status).getIdStatus();
        List<Process> answ = processService.getProcessStatusComplete();
        assertEquals(answ.size(), 2);
        assertEquals(answ.get(0).getName(), processMockList.get(0).getName());
        assertEquals(answ.get(1).getName(), processMockList.get(1).getName());
    }
}
