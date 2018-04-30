package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.juez.domain.Code;

import com.juez.repository.CodeRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.CodeMapper;
import com.juez.service.FileService;
import com.juez.service.CodeService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xnio.channels.SuspendableAcceptChannel;
import org.zalando.problem.spring.web.advice.general.ProblemAdviceTrait;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.juez.service.SubmissionService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Code.
 */
@RestController
@RequestMapping("/api")
public class CodeController {

    private final Logger log = LoggerFactory.getLogger(CodeController.class);

    private static final String ENTITY_NAME = "code";

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;

    private final FileService fileService;

    private final CodeService codeService;

    public CodeController(CodeRepository codeRepository, CodeMapper codeMapper, FileService fileService, CodeService codeService) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.fileService = fileService;
        this.codeService = codeService;
    }

    /**
     * POST  /codes : Create a new code.
     *
     * @param codeDTO the codeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeDTO, or with status 400 (Bad Request) if the code has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/create/code")
    @ResponseBody
    public ResponseEntity<CodeDTO> createCode(@RequestPart MultipartFile reportFile , @RequestParam String language, @RequestParam String problemId) throws IOException, URISyntaxException {
        
        CodeDTO result;
        Code codeFinal = codeService.create(reportFile, language, problemId);
        result = codeMapper.toDto(codeFinal);
        return ResponseEntity.created(new URI("/api/codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
   
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }


}