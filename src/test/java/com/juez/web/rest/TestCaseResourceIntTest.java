package com.juez.web.rest;

import com.juez.JuezApp;

import com.juez.domain.TestCase;
import com.juez.repository.TestCaseRepository;
import com.juez.service.dto.TestCaseDTO;
import com.juez.service.mapper.TestCaseMapper;
import com.juez.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TestCaseResource REST controller.
 *
 * @see TestCaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JuezApp.class)
public class TestCaseResourceIntTest {

    private static final String DEFAULT_INPUTFL = "AAAAAAAAAA";
    private static final String UPDATED_INPUTFL = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUTFL = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUTFL = "BBBBBBBBBB";

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TestCaseResource testCaseResource = new TestCaseResource(testCaseRepository, testCaseMapper);
        this.restTestCaseMockMvc = MockMvcBuilders.standaloneSetup(testCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .inputfl(DEFAULT_INPUTFL)
            .outputfl(DEFAULT_OUTPUTFL);
        return testCase;
    }

    @Before
    public void initTest() {
        testCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInputfl()).isEqualTo(DEFAULT_INPUTFL);
        assertThat(testTestCase.getOutputfl()).isEqualTo(DEFAULT_OUTPUTFL);
    }

    @Test
    @Transactional
    public void createTestCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase with an existing ID
        testCase.setId(1L);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestCases() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc.perform(get("/api/test-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].inputfl").value(hasItem(DEFAULT_INPUTFL.toString())))
            .andExpect(jsonPath("$.[*].outputfl").value(hasItem(DEFAULT_OUTPUTFL.toString())));
    }

    @Test
    @Transactional
    public void getTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()))
            .andExpect(jsonPath("$.inputfl").value(DEFAULT_INPUTFL.toString()))
            .andExpect(jsonPath("$.outputfl").value(DEFAULT_OUTPUTFL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findOne(testCase.getId());
        updatedTestCase
            .inputfl(UPDATED_INPUTFL)
            .outputfl(UPDATED_OUTPUTFL);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(updatedTestCase);

        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getInputfl()).isEqualTo(UPDATED_INPUTFL);
        assertThat(testTestCase.getOutputfl()).isEqualTo(UPDATED_OUTPUTFL);
    }

    @Test
    @Transactional
    public void updateNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testCaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);
        int databaseSizeBeforeDelete = testCaseRepository.findAll().size();

        // Get the testCase
        restTestCaseMockMvc.perform(delete("/api/test-cases/{id}", testCase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCase.class);
        TestCase testCase1 = new TestCase();
        testCase1.setId(1L);
        TestCase testCase2 = new TestCase();
        testCase2.setId(testCase1.getId());
        assertThat(testCase1).isEqualTo(testCase2);
        testCase2.setId(2L);
        assertThat(testCase1).isNotEqualTo(testCase2);
        testCase1.setId(null);
        assertThat(testCase1).isNotEqualTo(testCase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseDTO.class);
        TestCaseDTO testCaseDTO1 = new TestCaseDTO();
        testCaseDTO1.setId(1L);
        TestCaseDTO testCaseDTO2 = new TestCaseDTO();
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
        testCaseDTO2.setId(testCaseDTO1.getId());
        assertThat(testCaseDTO1).isEqualTo(testCaseDTO2);
        testCaseDTO2.setId(2L);
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
        testCaseDTO1.setId(null);
        assertThat(testCaseDTO1).isNotEqualTo(testCaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(testCaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(testCaseMapper.fromId(null)).isNull();
    }
}
