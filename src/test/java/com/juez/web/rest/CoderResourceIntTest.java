package com.juez.web.rest;

import com.juez.JuezApp;

import com.juez.domain.Coder;
import com.juez.repository.CoderRepository;
import com.juez.service.CoderService;
import com.juez.service.dto.CoderDTO;
import com.juez.service.mapper.CoderMapper;
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

import static com.juez.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoderResource REST controller.
 *
 * @see CoderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JuezApp.class)
public class CoderResourceIntTest {

    private static final String DEFAULT_RANKING = "AAAAAAAAAA";
    private static final String UPDATED_RANKING = "BBBBBBBBBB";

    @Autowired
    private CoderRepository coderRepository;

    @Autowired
    private CoderMapper coderMapper;

    @Autowired
    private CoderService coderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoderMockMvc;

    private Coder coder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoderResource coderResource = new CoderResource(coderService);
        this.restCoderMockMvc = MockMvcBuilders.standaloneSetup(coderResource)
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
    public static Coder createEntity(EntityManager em) {
        Coder coder = new Coder()
            .ranking(DEFAULT_RANKING);
        return coder;
    }

    @Before
    public void initTest() {
        coder = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoder() throws Exception {
        int databaseSizeBeforeCreate = coderRepository.findAll().size();

        // Create the Coder
        CoderDTO coderDTO = coderMapper.toDto(coder);
        restCoderMockMvc.perform(post("/api/coders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coderDTO)))
            .andExpect(status().isCreated());

        // Validate the Coder in the database
        List<Coder> coderList = coderRepository.findAll();
        assertThat(coderList).hasSize(databaseSizeBeforeCreate + 1);
        Coder testCoder = coderList.get(coderList.size() - 1);
        assertThat(testCoder.getRanking()).isEqualTo(DEFAULT_RANKING);
    }

    @Test
    @Transactional
    public void createCoderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coderRepository.findAll().size();

        // Create the Coder with an existing ID
        coder.setId(1L);
        CoderDTO coderDTO = coderMapper.toDto(coder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoderMockMvc.perform(post("/api/coders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coder in the database
        List<Coder> coderList = coderRepository.findAll();
        assertThat(coderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoders() throws Exception {
        // Initialize the database
        coderRepository.saveAndFlush(coder);

        // Get all the coderList
        restCoderMockMvc.perform(get("/api/coders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coder.getId().intValue())))
            .andExpect(jsonPath("$.[*].ranking").value(hasItem(DEFAULT_RANKING.toString())));
    }

    @Test
    @Transactional
    public void getCoder() throws Exception {
        // Initialize the database
        coderRepository.saveAndFlush(coder);

        // Get the coder
        restCoderMockMvc.perform(get("/api/coders/{id}", coder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coder.getId().intValue()))
            .andExpect(jsonPath("$.ranking").value(DEFAULT_RANKING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoder() throws Exception {
        // Get the coder
        restCoderMockMvc.perform(get("/api/coders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoder() throws Exception {
        // Initialize the database
        coderRepository.saveAndFlush(coder);
        int databaseSizeBeforeUpdate = coderRepository.findAll().size();

        // Update the coder
        Coder updatedCoder = coderRepository.findOne(coder.getId());
        // Disconnect from session so that the updates on updatedCoder are not directly saved in db
        em.detach(updatedCoder);
        updatedCoder
            .ranking(UPDATED_RANKING);
        CoderDTO coderDTO = coderMapper.toDto(updatedCoder);

        restCoderMockMvc.perform(put("/api/coders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coderDTO)))
            .andExpect(status().isOk());

        // Validate the Coder in the database
        List<Coder> coderList = coderRepository.findAll();
        assertThat(coderList).hasSize(databaseSizeBeforeUpdate);
        Coder testCoder = coderList.get(coderList.size() - 1);
        assertThat(testCoder.getRanking()).isEqualTo(UPDATED_RANKING);
    }

    @Test
    @Transactional
    public void updateNonExistingCoder() throws Exception {
        int databaseSizeBeforeUpdate = coderRepository.findAll().size();

        // Create the Coder
        CoderDTO coderDTO = coderMapper.toDto(coder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoderMockMvc.perform(put("/api/coders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coderDTO)))
            .andExpect(status().isCreated());

        // Validate the Coder in the database
        List<Coder> coderList = coderRepository.findAll();
        assertThat(coderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoder() throws Exception {
        // Initialize the database
        coderRepository.saveAndFlush(coder);
        int databaseSizeBeforeDelete = coderRepository.findAll().size();

        // Get the coder
        restCoderMockMvc.perform(delete("/api/coders/{id}", coder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coder> coderList = coderRepository.findAll();
        assertThat(coderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coder.class);
        Coder coder1 = new Coder();
        coder1.setId(1L);
        Coder coder2 = new Coder();
        coder2.setId(coder1.getId());
        assertThat(coder1).isEqualTo(coder2);
        coder2.setId(2L);
        assertThat(coder1).isNotEqualTo(coder2);
        coder1.setId(null);
        assertThat(coder1).isNotEqualTo(coder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoderDTO.class);
        CoderDTO coderDTO1 = new CoderDTO();
        coderDTO1.setId(1L);
        CoderDTO coderDTO2 = new CoderDTO();
        assertThat(coderDTO1).isNotEqualTo(coderDTO2);
        coderDTO2.setId(coderDTO1.getId());
        assertThat(coderDTO1).isEqualTo(coderDTO2);
        coderDTO2.setId(2L);
        assertThat(coderDTO1).isNotEqualTo(coderDTO2);
        coderDTO1.setId(null);
        assertThat(coderDTO1).isNotEqualTo(coderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(coderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(coderMapper.fromId(null)).isNull();
    }
}
