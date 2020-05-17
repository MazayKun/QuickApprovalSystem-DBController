package ru.quick.approval.system.dbcontroller.service;

import org.jooq.demo.db.tables.records.ProcessStageRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.quick.approval.system.api.model.ProcessStage;
import ru.quick.approval.system.dbcontroller.dao.iDao.IProcessStageDao;
import ru.quick.approval.system.dbcontroller.service.iservice.IProcessStageService;
import ru.quick.approval.system.dbcontroller.translator.ProcessStageTranslator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Класс для тестирования большей части методов класса ProcessStageService
 * @author Kirill Mikheev
 * @version 1.0
 */

public class ProcessStageServiceTest {

    private IProcessStageService processStageService;

    @Mock
    private IProcessStageDao processStageDao;

    private List<ProcessStageRecord> processStageMockList;

    @BeforeEach
    public void init() {
        initMocks(this);
        processStageService = new ProcessStageService(processStageDao, new ProcessStageTranslator());
        ProcessStageRecord processStage;
        for(int i = 1; i < 3; i++){
            processStage = mock(ProcessStageRecord.class);
            when(processStage.getIdProcessStage()).thenReturn(i);
            processStageMockList.add(processStage);
        }

    }

    @Test
    public void getAllStagesTest() {
        doReturn(processStageMockList).when(processStageDao).getAllProcessStages();
        List<ProcessStage> answ = processStageService.getAllStages();
        assertEquals(answ.size(), processStageMockList.size());
        for(int i = 0; i < answ.size(); i++){
            assertEquals(answ.get(i).getIdProcessStage(), processStageMockList.get(i).getIdProcessStage());
        }
    }

    @Test
    public void getStageByIdTest() {
        doReturn(processStageMockList.get(0)).when(processStageDao).getProcessStageById(0);
        ProcessStage answ = processStageService.getStageById(0);
        assertEquals(answ.getIdProcessStage(), processStageMockList.get(0).getIdProcessStage());
    }

}
