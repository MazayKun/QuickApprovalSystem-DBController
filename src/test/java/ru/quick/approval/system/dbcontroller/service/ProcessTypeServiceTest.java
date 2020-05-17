package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessTypeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.ProcessType;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessTypeDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessTypeService;
import ru.quick.approval.system.dbcontroller.translator.ProcessTypeTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Класс для тестирования большей части методов класса ProcessTypeService
 * @author Kirill Mikheev
 * @version 1.0
 */

public class ProcessTypeServiceTest {

    @Mock
    private IProcessTypeDao processTypeDao;

    private List<ProcessTypeRecord> processTypeMockList;
    private IProcessTypeService processTypeService;

    @BeforeEach
    public void init() {
        initMocks(this);
        processTypeService = new ProcessTypeService(processTypeDao, new ProcessTypeTranslator());
        processTypeMockList = new ArrayList<>();
        ProcessTypeRecord processType;
        for (int i = 1; i <= 2; i++) {
            processType = mock(ProcessTypeRecord.class);
            when(processType.getIdProcessType()).thenReturn(i);
            processTypeMockList.add(processType);
        }
    }

    @Test
    public void getAllProcessTypesTest() {
        doReturn(processTypeMockList).when(processTypeDao).getAllProcessTypes();
        List<ProcessType> answ = processTypeService.getAllProcessTypes();
        assertEquals(answ.size(), processTypeMockList.size());
        for (int i = 0; i < answ.size(); i++){
            assertEquals(answ.get(i).getIdProcessType(), processTypeMockList.get(i).getIdProcessType());
        }
    }

    @Test
    public void getProcessTypeByIdTest() {
        doReturn(processTypeMockList.get(0)).when(processTypeDao).getProcessTypeById(1);
        ProcessType answ = processTypeService.getProcessTypeById(1);
        assertEquals(answ.getIdProcessType(), processTypeMockList.get(0).getIdProcessType());
    }
}
