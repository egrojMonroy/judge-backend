package com.juez.service;

import java.util.List;

import com.juez.domain.TestCase;
import com.juez.repository.TestCaseRepository;
import com.juez.service.dto.TestCaseDTO;
import com.juez.service.mapper.TestCaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TestCase.
 */
@Service
@Transactional
public class TestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    private final FileService fileService;

    public TestCaseService (TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper, FileService fileService) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
        this.fileService = fileService;
    }
    

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save
     * @return the persisted entity
     */
    public TestCaseDTO save(TestCaseDTO testCaseDTO) {
        log.debug("Request to save TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toDto(testCase);
    }

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAll(pageable)
            .map(testCaseMapper::toDto);
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TestCaseDTO findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        TestCase testCase = testCaseRepository.findOne(id);
        return testCaseMapper.toDto(testCase);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        testCaseRepository.delete(id);
    }
    List<TestCase> getTestCasesNameByProblem( Long id){
        log.debug("REST request to get TestCase of problem  : {}", id);
        List<TestCase> result = testCaseRepository.findAllByProblemId(id);
        return result;
    }
}
