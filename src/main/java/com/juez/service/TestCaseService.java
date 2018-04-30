package com.juez.service;

import org.springframework.stereotype.Service;
import com.juez.domain.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.juez.repository.TestCaseRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.TestCaseDTO;
import com.juez.service.mapper.TestCaseMapper;
import com.juez.service.dto.ProblemDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class TestCaseService {

    private final Logger log = LoggerFactory.getLogger(CodeService.class);;
    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

    private final FileService fileService;

    public TestCaseService (TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper, FileService fileService) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
        this.fileService = fileService;
    }
    List<TestCase> getTestCasesNameByProblem( Long id){
        log.debug("REST request to get TestCase of problem  : {}", id);
        List<TestCase> result = testCaseRepository.findAllByProblemId(id);
        return result;
    }
}
