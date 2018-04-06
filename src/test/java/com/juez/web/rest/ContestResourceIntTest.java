package com.juez.web.rest;

import com.juez.JuezApp;

import com.juez.domain.Contest;
import com.juez.repository.ContestRepository;
import com.juez.service.dto.ContestDTO;
import com.juez.service.mapper.ContestMapper;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContestResource REST controller.
 *
 * @see ContestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JuezApp.class)
public class ContestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_STARTDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ENDDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENDDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_STARTTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ENDTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENDTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContestMockMvc;

    private Contest contest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContestResource contestResource = new ContestResource(contestRepository, contestMapper);
        this.restContestMockMvc = MockMvcBuilders.standaloneSetup(contestResource)
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
    public static Contest createEntity(EntityManager em) {
        Contest contest = new Contest()
            .name(DEFAULT_NAME)
            .startdate(DEFAULT_STARTDATE)
            .enddate(DEFAULT_ENDDATE)
            .starttime(DEFAULT_STARTTIME)
            .endtime(DEFAULT_ENDTIME)
            .type(DEFAULT_TYPE);
        return contest;
    }

    @Before
    public void initTest() {
        contest = createEntity(em);
    }

    @Test
    @Transactional
    public void createContest() throws Exception {
        int databaseSizeBeforeCreate = contestRepository.findAll().size();

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);
        restContestMockMvc.perform(post("/api/contests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isCreated());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate + 1);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContest.getStartdate()).isEqualTo(DEFAULT_STARTDATE);
        assertThat(testContest.getEnddate()).isEqualTo(DEFAULT_ENDDATE);
        assertThat(testContest.getStarttime()).isEqualTo(DEFAULT_STARTTIME);
        assertThat(testContest.getEndtime()).isEqualTo(DEFAULT_ENDTIME);
        assertThat(testContest.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createContestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contestRepository.findAll().size();

        // Create the Contest with an existing ID
        contest.setId(1L);
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContestMockMvc.perform(post("/api/contests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContests() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get all the contestList
        restContestMockMvc.perform(get("/api/contests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(sameInstant(DEFAULT_STARTDATE))))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(sameInstant(DEFAULT_ENDDATE))))
            .andExpect(jsonPath("$.[*].starttime").value(hasItem(sameInstant(DEFAULT_STARTTIME))))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(sameInstant(DEFAULT_ENDTIME))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get the contest
        restContestMockMvc.perform(get("/api/contests/{id}", contest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startdate").value(sameInstant(DEFAULT_STARTDATE)))
            .andExpect(jsonPath("$.enddate").value(sameInstant(DEFAULT_ENDDATE)))
            .andExpect(jsonPath("$.starttime").value(sameInstant(DEFAULT_STARTTIME)))
            .andExpect(jsonPath("$.endtime").value(sameInstant(DEFAULT_ENDTIME)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContest() throws Exception {
        // Get the contest
        restContestMockMvc.perform(get("/api/contests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest
        Contest updatedContest = contestRepository.findOne(contest.getId());
        updatedContest
            .name(UPDATED_NAME)
            .startdate(UPDATED_STARTDATE)
            .enddate(UPDATED_ENDDATE)
            .starttime(UPDATED_STARTTIME)
            .endtime(UPDATED_ENDTIME)
            .type(UPDATED_TYPE);
        ContestDTO contestDTO = contestMapper.toDto(updatedContest);

        restContestMockMvc.perform(put("/api/contests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContest.getStartdate()).isEqualTo(UPDATED_STARTDATE);
        assertThat(testContest.getEnddate()).isEqualTo(UPDATED_ENDDATE);
        assertThat(testContest.getStarttime()).isEqualTo(UPDATED_STARTTIME);
        assertThat(testContest.getEndtime()).isEqualTo(UPDATED_ENDTIME);
        assertThat(testContest.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContestMockMvc.perform(put("/api/contests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isCreated());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);
        int databaseSizeBeforeDelete = contestRepository.findAll().size();

        // Get the contest
        restContestMockMvc.perform(delete("/api/contests/{id}", contest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contest.class);
        Contest contest1 = new Contest();
        contest1.setId(1L);
        Contest contest2 = new Contest();
        contest2.setId(contest1.getId());
        assertThat(contest1).isEqualTo(contest2);
        contest2.setId(2L);
        assertThat(contest1).isNotEqualTo(contest2);
        contest1.setId(null);
        assertThat(contest1).isNotEqualTo(contest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContestDTO.class);
        ContestDTO contestDTO1 = new ContestDTO();
        contestDTO1.setId(1L);
        ContestDTO contestDTO2 = new ContestDTO();
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
        contestDTO2.setId(contestDTO1.getId());
        assertThat(contestDTO1).isEqualTo(contestDTO2);
        contestDTO2.setId(2L);
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
        contestDTO1.setId(null);
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contestMapper.fromId(null)).isNull();
    }
}
