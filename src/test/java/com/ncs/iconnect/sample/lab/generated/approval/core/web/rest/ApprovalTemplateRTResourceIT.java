package com.ncs.iconnect.sample.lab.generated.approval.core.web.rest;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalTemplateService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalTemplateMapper;
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
import static org.hamcrest.Matchers.hasItem;
import javax.persistence.EntityManager;
import java.util.UUID;
import java.util.List;
import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import org.springframework.validation.Validator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.sql.Timestamp;

/**
 * Test class for the ApprovalTemplateResource REST controller.
 *
 * @see ApprovalTemplateRTResource
 */
@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalTemplateRTResourceIT {

    
    private static final Timestamp UPDATED_UPDATED_DATE = new Timestamp(System.currentTimeMillis());
    private static long  UPDATE_DATE = new Timestamp(System.currentTimeMillis()).getDate();
    private static final String DEFAULT_PROCESS_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_OWNER = "BBBBBBBBBB";

    private static final ApprovalBehavior DEFAULT_APPROVAL_BEHAVIOR = ApprovalBehavior.UNANIMOUS_APPROVAL;
    private static final ApprovalBehavior UPDATED_APPROVAL_BEHAVIOR = ApprovalBehavior.UNANIMOUS_APPROVAL;

    private static final String DEFAULT_REQUEST_TYPE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_KEY = "BBBBBBBBBB";

    private static final ApproverSelection DEFAULT_APPROVER_SELECTION = ApproverSelection.FIXED_STEP;
    private static final ApproverSelection UPDATED_APPROVER_SELECTION = ApproverSelection.FIXED;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Timestamp DEFAULT_CREATED_DATE = new Timestamp(System.currentTimeMillis());
    private static final Timestamp UPDATED_CREATED_DATE = new Timestamp(System.currentTimeMillis());
    private static final long CREATE_DATE = new Timestamp(System.currentTimeMillis()).getDate();
    private static final String DEFAULT_UPDATEDBY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATEDBY = "BBBBBBBBBB";

    @Autowired
    private ApprovalTemplateRepository approvalTemplateRepository;

    @Autowired
    private ApprovalTemplateMapper approvalTemplateMapper;

    @Autowired
    private ApprovalTemplateService approvalTemplateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApprovalTemplateMockMvc;

    private ApprovalTemplate approvalTemplate;

    @Autowired
    private Validator validator;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalTemplate createEntity(EntityManager em) {
        ApprovalTemplate approvalTemplate = new ApprovalTemplate()
            .processOwner(DEFAULT_PROCESS_OWNER)
            .approvalBehavior(DEFAULT_APPROVAL_BEHAVIOR)
            .multiInstanceType(MultiInstanceType.SEQUENTIAL)
            .enableRejectAll(true)
            .enableRejectStep(false)
            .enableRejectToApplicant(true)
            .requestTypeKey(DEFAULT_REQUEST_TYPE_KEY)
            .templateKey(DEFAULT_TEMPLATE_KEY)
            .approverSelection(DEFAULT_APPROVER_SELECTION);
        approvalTemplate.setId(UUID.randomUUID().toString());
        approvalTemplate.setUpdatedDt(UPDATED_UPDATED_DATE);
        approvalTemplate.setCreatedBy(DEFAULT_CREATED_BY);
        approvalTemplate.setCreatedDt(DEFAULT_CREATED_DATE);
        approvalTemplate.setUpdatedBy(DEFAULT_UPDATEDBY);
        return approvalTemplate;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApprovalTemplateRTResource approvalTemplateResource = new ApprovalTemplateRTResource(approvalTemplateService);
        this.restApprovalTemplateMockMvc = MockMvcBuilders.standaloneSetup(approvalTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        approvalTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void getApprovalTemplate() throws Exception {
        // Initialize the database
        approvalTemplateRepository.saveAndFlush(approvalTemplate);

        // Get the approvalTemplate
        restApprovalTemplateMockMvc.perform(get("/api/approval/approval-templates:by-selector/{requestTypeKey}/{templateKey}", approvalTemplate.getRequestTypeKey(), approvalTemplate.getTemplateKey()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(approvalTemplate.getId()))
            .andExpect(jsonPath("$.processOwner").value(DEFAULT_PROCESS_OWNER))
            .andExpect(jsonPath("$.requestTypeKey").value(DEFAULT_REQUEST_TYPE_KEY))
            .andExpect(jsonPath("$.templateKey").value(DEFAULT_TEMPLATE_KEY))
            .andExpect(jsonPath("$.approverSelection").value(DEFAULT_APPROVER_SELECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApprovalTemplate() throws Exception {
        // Get the approvalTemplate
        restApprovalTemplateMockMvc.perform(get("/api/approval/approval-templates:by-selector/{requestTypeKey}/{templateKey}", "dummykey", "dummyselector"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        ApprovalTemplate approvalTemplate1 = new ApprovalTemplate();
        approvalTemplate1.setId("1");
        ApprovalTemplate approvalTemplate2 = new ApprovalTemplate();
        approvalTemplate2.setId(approvalTemplate1.getId());
        assertThat(approvalTemplate1).isEqualTo(approvalTemplate2);
        approvalTemplate2.setId("2");
        assertThat(approvalTemplate1).isNotEqualTo(approvalTemplate2);
        approvalTemplate1.setId(null);
        assertThat(approvalTemplate1).isNotEqualTo(approvalTemplate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        ApprovalTemplateRTDTO approvalTemplateNoAuditDTO1 = new ApprovalTemplateRTDTO();
        approvalTemplateNoAuditDTO1.setId("1");
        ApprovalTemplateRTDTO approvalTemplateNoAuditDTO2 = new ApprovalTemplateRTDTO();
        assertThat(approvalTemplateNoAuditDTO1).isNotEqualTo(approvalTemplateNoAuditDTO2);
        approvalTemplateNoAuditDTO2.setId(approvalTemplateNoAuditDTO1.getId());
        assertThat(approvalTemplateNoAuditDTO1).isEqualTo(approvalTemplateNoAuditDTO2);
        approvalTemplateNoAuditDTO2.setId("2");
        assertThat(approvalTemplateNoAuditDTO1).isNotEqualTo(approvalTemplateNoAuditDTO2);
        approvalTemplateNoAuditDTO1.setId(null);
        assertThat(approvalTemplateNoAuditDTO1).isNotEqualTo(approvalTemplateNoAuditDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(approvalTemplateMapper.fromId("42").getId()).isEqualTo("42");
        assertThat(approvalTemplateMapper.fromId(null)).isNull();
    }

    @Test
    @Transactional
    public void testGetApprovalTemplate() throws Exception {
        approvalTemplateRepository.saveAndFlush(approvalTemplate);

        restApprovalTemplateMockMvc.perform(get("/api/approvalTemplates", approvalTemplate.getId(),approvalTemplate.getRequestTypeKey()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // .andExpect(jsonPath("$.[*].id").value(hasItem(approvalTemplate.getId())))
            // .andExpect(jsonPath("$.[*].processOwner").value(hasItem(DEFAULT_PROCESS_OWNER)))
            // .andExpect(jsonPath("$.[*].requestTypeKey").value(hasItem(DEFAULT_REQUEST_TYPE_KEY)))
            // .andExpect(jsonPath("$.[*].templateKey").value(hasItem(DEFAULT_TEMPLATE_KEY)))
            // .andExpect(jsonPath("$.[*].approverSelection").value(hasItem(DEFAULT_APPROVER_SELECTION.toString())));
    }

    @Test
    @Transactional
    public void testGetApprovalTemplateById() throws Exception {
        approvalTemplateRepository.saveAndFlush(approvalTemplate);

        restApprovalTemplateMockMvc.perform(get("/api/approvalTemplates/{id}", approvalTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(approvalTemplate.getId()))
            .andExpect(jsonPath("$.processOwner").value(DEFAULT_PROCESS_OWNER))
            .andExpect(jsonPath("$.requestTypeKey").value(DEFAULT_REQUEST_TYPE_KEY))
            .andExpect(jsonPath("$.templateKey").value(DEFAULT_TEMPLATE_KEY))
            .andExpect(jsonPath("$.approverSelection").value(DEFAULT_APPROVER_SELECTION.toString()));
    }

    @Test
    @Transactional
    public void deleteApprovalTemplate() throws Exception {
        // Initialize the database
        approvalTemplateRepository.saveAndFlush(approvalTemplate);

        int databaseSizeBeforeDelete = approvalTemplateRepository.findAll().size();

        // Delete the order
        restApprovalTemplateMockMvc.perform(delete("/api/approvalTemplates/{id}", approvalTemplate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database contains one less item
        List<ApprovalTemplate> approvalTemplateList = approvalTemplateRepository.findAll();
        assertThat(approvalTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void createApprovalTemplate() throws Exception {
        int databaseSizeBeforeCreate = approvalTemplateRepository.findAll().size();

        approvalTemplate.setId(null);
        restApprovalTemplateMockMvc.perform(post("/api/approvalTemplates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalTemplate)))
            .andExpect(status().isCreated());

        // Validate the ApprovalTemplate in the database
        List<ApprovalTemplate> approvalTemplateList = approvalTemplateRepository.findAll();
        assertThat(approvalTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalTemplate testApprovalTemplate = approvalTemplateList.get(approvalTemplateList.size() - 1);
        assertThat(testApprovalTemplate.getApprovalBehavior()).isEqualTo(DEFAULT_APPROVAL_BEHAVIOR);
        assertThat(testApprovalTemplate.getUpdatedDt().getDate()).isEqualTo(UPDATE_DATE);
        assertThat(testApprovalTemplate.getMultiInstanceType()).isEqualTo(MultiInstanceType.SEQUENTIAL);
        assertThat(testApprovalTemplate.getTemplateKey()).isEqualTo(DEFAULT_TEMPLATE_KEY);
    }

    @Test
    @Transactional
    public void createApprovalTemplateWithExistID() throws Exception {
        restApprovalTemplateMockMvc.perform(post("/api/approvalTemplates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalTemplate)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateApprovalTemplate() throws Exception {
        // Initialize the database
        approvalTemplateRepository.saveAndFlush(approvalTemplate);

        int databaseSizeBeforeUpdate = approvalTemplateRepository.findAll().size();

        // Update the ApprovalTemplate
        ApprovalTemplate updatedApprovalTemplate = approvalTemplateRepository.findById(approvalTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalTemplate are not directly saved in db
        em.detach(updatedApprovalTemplate);
        updatedApprovalTemplate
            .requestTypeKey(UPDATED_REQUEST_TYPE_KEY)
            .templateKey(UPDATED_TEMPLATE_KEY)
            .approverSelection(UPDATED_APPROVER_SELECTION)
            .approvalBehavior(UPDATED_APPROVAL_BEHAVIOR);
        updatedApprovalTemplate.setUpdatedDt(UPDATED_UPDATED_DATE);
        updatedApprovalTemplate.setCreatedDt(UPDATED_CREATED_DATE);
        restApprovalTemplateMockMvc.perform(put("/api/approvalTemplates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApprovalTemplate)))
            .andExpect(status().isOk());

        // Validate the ApprovalTemplate in the database
        List<ApprovalTemplate> approvalTemplateList = approvalTemplateRepository.findAll();
//        assertThat(approvalTemplateList).hasSize(databaseSizeBeforeUpdate);
        ApprovalTemplate testApprovalTemplate = approvalTemplateList.get(approvalTemplateList.size() - 1);
        assertThat(testApprovalTemplate.getUpdatedDt().getDate()).isEqualTo(UPDATE_DATE);
        assertThat(testApprovalTemplate.getRequestTypeKey()).isEqualTo(UPDATED_REQUEST_TYPE_KEY);
        assertThat(testApprovalTemplate.getTemplateKey()).isEqualTo(UPDATED_TEMPLATE_KEY);
        assertThat(testApprovalTemplate.getApproverSelection()).isEqualTo(UPDATED_APPROVER_SELECTION);
        assertThat(testApprovalTemplate.getCreatedDt().getDate()).isEqualTo(CREATE_DATE);
        assertThat(testApprovalTemplate.getApprovalBehavior()).isEqualTo(UPDATED_APPROVAL_BEHAVIOR);
    }
}
