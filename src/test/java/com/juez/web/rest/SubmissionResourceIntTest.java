package com.juez.web.rest;

import com.juez.JuezApp;

import com.juez.domain.Submission;
import com.juez.repository.SubmissionRepository;
import com.juez.service.dto.SubmissionDTO;
import com.juez.service.mapper.SubmissionMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.juez.web.rest.TestUtil.sameInstant;
import static com.juez.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juez.domain.enumeration.Veredict;
import com.juez.domain.enumeration.Language;
/**
 * Test class for the SubmissionResource REST controller.
 *
 * @see SubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JuezApp.class)
public class SubmissionResourceIntTest {

    private static final Veredict DEFAULT_STATUS = Veredict.ACCEPTED;
    private static final Veredict UPDATED_STATUS = Veredict.WRONG_ANSWER;

    private static final Integer DEFAULT_RUNTIME = 1;
    private static final Integer UPDATED_RUNTIME = 2;

    private static final Language DEFAULT_LANGUAGE = Language.JAVA;
    private static final Language UPDATED_LANGUAGE = Language.C;

    private static final ZonedDateTime DEFAULT_DATEUPLOAD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEUPLOAD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubmissionResource submissionResource = new SubmissionResource(submissionRepository, submissionMapper);
        this.restSubmissionMockMvc = MockMvcBuilders.standaloneSetup(submissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
            .status(DEFAULT_STATUS)
            .runtime(DEFAULT_RUNTIME)
            .language(DEFAULT_LANGUAGE)
            .dateupload(DEFAULT_DATEUPLOAD);
        return submission;
    }

    @Before
    public void initTest() {
        submission = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubmission() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        restSubmissionMockMvc.perform(post("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate + 1);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSubmission.getRuntime()).isEqualTo(DEFAULT_RUNTIME);
        assertThat(testSubmission.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testSubmission.getDateupload()).isEqualTo(DEFAULT_DATEUPLOAD);
    }

    @Test
    @Transactional
    public void createSubmissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // Create the Submission with an existing ID
        submission.setId(1L);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc.perform(post("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc.perform(get("/api/submissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].runtime").value(hasItem(DEFAULT_RUNTIME)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].dateupload").value(hasItem(sameInstant(DEFAULT_DATEUPLOAD))));
    }

    @Test
    @Transactional
    public void getSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc.perform(get("/api/submissions/{id}", submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.runtime").value(DEFAULT_RUNTIME))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.dateupload").value(sameInstant(DEFAULT_DATEUPLOAD)));
    }

    @Test
    @Transactional
    public void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get("/api/submissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findOne(submission.getId());
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
            .status(UPDATED_STATUS)
            .runtime(UPDATED_RUNTIME)
            .language(UPDATED_LANGUAGE)
            .dateupload(UPDATED_DATEUPLOAD);
        SubmissionDTO submissionDTO = submissionMapper.toDto(updatedSubmission);

        restSubmissionMockMvc.perform(put("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSubmission.getRuntime()).isEqualTo(UPDATED_RUNTIME);
        assertThat(testSubmission.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testSubmission.getDateupload()).isEqualTo(UPDATED_DATEUPLOAD);
    }

    @Test
    @Transactional
    public void updateNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubmissionMockMvc.perform(put("/api/submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        int databaseSizeBeforeDelete = submissionRepository.findAll().size();

        // Get the submission
        restSubmissionMockMvc.perform(delete("/api/submissions/{id}", submission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submission.class);
        Submission submission1 = new Submission();
        submission1.setId(1L);
        Submission submission2 = new Submission();
        submission2.setId(submission1.getId());
        assertThat(submission1).isEqualTo(submission2);
        submission2.setId(2L);
        assertThat(submission1).isNotEqualTo(submission2);
        submission1.setId(null);
        assertThat(submission1).isNotEqualTo(submission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmissionDTO.class);
        SubmissionDTO submissionDTO1 = new SubmissionDTO();
        submissionDTO1.setId(1L);
        SubmissionDTO submissionDTO2 = new SubmissionDTO();
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO2.setId(submissionDTO1.getId());
        assertThat(submissionDTO1).isEqualTo(submissionDTO2);
        submissionDTO2.setId(2L);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
        submissionDTO1.setId(null);
        assertThat(submissionDTO1).isNotEqualTo(submissionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(submissionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(submissionMapper.fromId(null)).isNull();
    }
}
