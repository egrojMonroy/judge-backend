package com.juez.web.rest;

import com.juez.JuezApp;

import com.juez.domain.Code;
import com.juez.repository.CodeRepository;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.CodeMapper;
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

/**
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JuezApp.class)
public class CodeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATEUPLOAD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEUPLOAD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodeMockMvc;

    private Code code;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodeResource codeResource = new CodeResource(codeRepository, codeMapper);
        this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
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
    public static Code createEntity(EntityManager em) {
        Code code = new Code()
            .name(DEFAULT_NAME)
            .dateupload(DEFAULT_DATEUPLOAD);
        return code;
    }

    @Before
    public void initTest() {
        code = createEntity(em);
    }

    @Test
    @Transactional
    public void createCode() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate + 1);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCode.getDateupload()).isEqualTo(DEFAULT_DATEUPLOAD);
    }

    @Test
    @Transactional
    public void createCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codeRepository.findAll().size();

        // Create the Code with an existing ID
        code.setId(1L);
        CodeDTO codeDTO = codeMapper.toDto(code);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeMockMvc.perform(post("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCodes() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get all the codeList
        restCodeMockMvc.perform(get("/api/codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(code.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateupload").value(hasItem(sameInstant(DEFAULT_DATEUPLOAD))));
    }

    @Test
    @Transactional
    public void getCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);

        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", code.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(code.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dateupload").value(sameInstant(DEFAULT_DATEUPLOAD)));
    }

    @Test
    @Transactional
    public void getNonExistingCode() throws Exception {
        // Get the code
        restCodeMockMvc.perform(get("/api/codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Update the code
        Code updatedCode = codeRepository.findOne(code.getId());
        // Disconnect from session so that the updates on updatedCode are not directly saved in db
        em.detach(updatedCode);
        updatedCode
            .name(UPDATED_NAME)
            .dateupload(UPDATED_DATEUPLOAD);
        CodeDTO codeDTO = codeMapper.toDto(updatedCode);

        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isOk());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate);
        Code testCode = codeList.get(codeList.size() - 1);
        assertThat(testCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCode.getDateupload()).isEqualTo(UPDATED_DATEUPLOAD);
    }

    @Test
    @Transactional
    public void updateNonExistingCode() throws Exception {
        int databaseSizeBeforeUpdate = codeRepository.findAll().size();

        // Create the Code
        CodeDTO codeDTO = codeMapper.toDto(code);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodeMockMvc.perform(put("/api/codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codeDTO)))
            .andExpect(status().isCreated());

        // Validate the Code in the database
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCode() throws Exception {
        // Initialize the database
        codeRepository.saveAndFlush(code);
        int databaseSizeBeforeDelete = codeRepository.findAll().size();

        // Get the code
        restCodeMockMvc.perform(delete("/api/codes/{id}", code.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Code> codeList = codeRepository.findAll();
        assertThat(codeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Code.class);
        Code code1 = new Code();
        code1.setId(1L);
        Code code2 = new Code();
        code2.setId(code1.getId());
        assertThat(code1).isEqualTo(code2);
        code2.setId(2L);
        assertThat(code1).isNotEqualTo(code2);
        code1.setId(null);
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDTO.class);
        CodeDTO codeDTO1 = new CodeDTO();
        codeDTO1.setId(1L);
        CodeDTO codeDTO2 = new CodeDTO();
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO2.setId(codeDTO1.getId());
        assertThat(codeDTO1).isEqualTo(codeDTO2);
        codeDTO2.setId(2L);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO1.setId(null);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(codeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(codeMapper.fromId(null)).isNull();
    }
}
