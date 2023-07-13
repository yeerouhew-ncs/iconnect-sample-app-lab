package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.*;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalTemplateDataService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalTemplateDataMapper;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalTemplateTestData;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApprovalTemplateDataResource REST controller.
 *
 * @see ApprovalTemplateDataRTResource
 */
@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalTemplateDataRTResourceIT {

    private static final String DEFAULT_APPROVER_ID = "AAAAAAAAAA";
    private static final String UPDATED_APPROVER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVER_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_APPROVER_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_APPROVER_SEQ = 1;
    private static final Integer UPDATED_APPROVER_SEQ = 2;

    private static final String DEFAULT_APPROVER_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_APPROVER_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_REJECT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_REJECT_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ROLL_BACK_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ROLL_BACK_ACTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_ROLL_BACK_ACTION = false;
    private static final Boolean UPDATED_ENABLE_ROLL_BACK_ACTION = true;
    @Autowired
    ApprovalTemplateRepository approvalTemplateRepository;
    @Autowired
    private ApprovalTemplateDataRepository approvalTemplateDataRepository;
    @Autowired
    private ApprovalTemplateDataMapper approvalTemplateDataMapper;
    @Autowired
    private ApprovalTemplateDataService approvalTemplateDataService;
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Autowired
    private ExceptionTranslator exceptionTranslator;
    @Autowired
    private EntityManager em;

    private MockMvc restApprovalTemplateDataMockMvc;

    private ApprovalTemplateData approvalTemplateData;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalTemplateData createEntity(EntityManager em) {
        return new ApprovalTemplateData()
            .approverId(DEFAULT_APPROVER_ID)
            .approverTitle(DEFAULT_APPROVER_TITLE)
            .approverSeq(DEFAULT_APPROVER_SEQ);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApprovalTemplateDataRTResource approvalTemplateDataResource = new ApprovalTemplateDataRTResource(approvalTemplateDataService);
        this.restApprovalTemplateDataMockMvc = MockMvcBuilders.standaloneSetup(approvalTemplateDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @BeforeEach
    public void initTest() {
        approvalTemplateData = createEntity(em);
    }

    @Test
    @Transactional
    public void getApprovalTemplateDataByTemplateKey() throws Exception {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approver1 = approvalTemplateDataRepository.save(approver1);
        approver2 = approvalTemplateDataRepository.save(approver2);

        // Get the approvalTemplateData
        restApprovalTemplateDataMockMvc.perform(get("/api/approval/approval-template-datas:by-template-id/{templateId}", savedApprovalTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(approver1.getId()))
            .andExpect(jsonPath("$.[0].approverId").value(approver1.getApproverId()))
            .andExpect(jsonPath("$.[0].approverTitle").value(approver1.getApproverTitle()))
            .andExpect(jsonPath("$.[0].approverSeq").value(approver1.getApproverSeq()))
            .andExpect(jsonPath("$.[1].id").value(approver2.getId()))
            .andExpect(jsonPath("$.[1].approverId").value(approver2.getApproverId()))
            .andExpect(jsonPath("$.[1].approverTitle").value(approver2.getApproverTitle()))
            .andExpect(jsonPath("$.[1].approverSeq").value(approver2.getApproverSeq()));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        ApprovalTemplateData approvalTemplateData1 = new ApprovalTemplateData();
        approvalTemplateData1.setId("1");
        ApprovalTemplateData approvalTemplateData2 = new ApprovalTemplateData();
        approvalTemplateData2.setId(approvalTemplateData1.getId());
        assertThat(approvalTemplateData1).isEqualTo(approvalTemplateData2);
        approvalTemplateData2.setId("2");
        assertThat(approvalTemplateData1).isNotEqualTo(approvalTemplateData2);
        approvalTemplateData1.setId(null);
        assertThat(approvalTemplateData1).isNotEqualTo(approvalTemplateData2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        ApprovalTemplateDataDTO approvalTemplateDataDTO1 = new ApprovalTemplateDataDTO();
        approvalTemplateDataDTO1.setId("1");
        ApprovalTemplateDataDTO approvalTemplateDataDTO2 = new ApprovalTemplateDataDTO();
        assertThat(approvalTemplateDataDTO1).isNotEqualTo(approvalTemplateDataDTO2);
        approvalTemplateDataDTO2.setId(approvalTemplateDataDTO1.getId());
        assertThat(approvalTemplateDataDTO1).isEqualTo(approvalTemplateDataDTO2);
        approvalTemplateDataDTO2.setId("2");
        assertThat(approvalTemplateDataDTO1).isNotEqualTo(approvalTemplateDataDTO2);
        approvalTemplateDataDTO1.setId(null);
        assertThat(approvalTemplateDataDTO1).isNotEqualTo(approvalTemplateDataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(approvalTemplateDataMapper.fromId("42").getId()).isEqualTo("42");
        assertThat(approvalTemplateDataMapper.fromId(null)).isNull();
    }

    @Test
    public void testApprovalHistoryItem() {
        ApprovalHistoryItem approvalHistoryItem = new ApprovalHistoryItem();
        approvalHistoryItem.setComment("test");
        approvalHistoryItem.compareTo(approvalHistoryItem);
        approvalHistoryItem.setId(1L);
        approvalHistoryItem.compareTo(approvalHistoryItem);
        approvalHistoryItem.getRequest();
        approvalHistoryItem.compareTo(null);
    }

    @Test
    public void testApprovalRequestAttachment() {
        ApprovalRequestAttachment approvalRequestAttachment = new ApprovalRequestAttachment();
        approvalRequestAttachment.setId(1L);
        approvalRequestAttachment.setApprovalRequestFieldKey("testKey");
        approvalRequestAttachment.getUploadedDate();
    }

    @Test
    public void testApprovalRequestEntity() {
        ApprovalRequestEntity approvalRequestEntity = new ApprovalRequestEntity();
        approvalRequestEntity.getUpdatedBy();
        approvalRequestEntity.getCompletedDate();
        approvalRequestEntity.toString();
    }

    @Test
    public void testApprovalTemplate() {
        ApprovalTemplate approvalTemplate = new ApprovalTemplate();
        ApprovalTemplateData ApprovalTemplateData = new ApprovalTemplateData();
        approvalTemplate.addApprovalTemplateData(ApprovalTemplateData);
        approvalTemplate.removeApprovalTemplateData(ApprovalTemplateData);

        Set<ApprovalTemplateData> approvalTemplateDataSet = new HashSet<>();
        approvalTemplateDataSet.add(ApprovalTemplateData);

        approvalTemplate.approvalTemplateData(approvalTemplateDataSet);
        approvalTemplate.setApprovalTemplateData(approvalTemplateDataSet);

        ApprovalTemplateData.approverDisplayName("testName");
        ApprovalTemplateData.setApproverDisplayName("testName");
        ApprovalTemplateData.approvalTemplate(approvalTemplate);
    }

    @Test
    public void testApprover() {
        Approver approver = new Approver();
        approver.setRequest(null);
        approver.compareTo(null);
        approver.setId(1L);
        approver.compareTo(approver);
    }
}
