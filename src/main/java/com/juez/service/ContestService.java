package com.juez.service;

import org.apache.commons.lang3.text.translate.CodePointTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.juez.repository.ProblemRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.juez.service.FileService;
import com.juez.domain.Code;
import com.juez.domain.Contest;
import com.juez.domain.Problem;
import com.juez.domain.Submission;
import com.juez.repository.ContestRepository;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.ContestMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.juez.service.SubmissionService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Service
@EnableAsync
public class ContestService {

    private final Logger log = LoggerFactory.getLogger(ContestService.class);;
    private final ContestRepository contestRepository;
    private final ContestMapper contestMapper;
    
    private final FileService fileService;
    private final SubmissionService submissionService;
    private final ProblemRepository problemRepository;
    public ContestService (
        ContestRepository contestRepository, 
        ContestMapper contestMapper, 
        FileService fileService, 
        SubmissionService submissionService,
        ProblemRepository problemRepository
        ) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.fileService = fileService;
        this.submissionService = submissionService;
        this.problemRepository = problemRepository;
    }
    
    public Contest addProblems (Long contestId, Long problemId) {
        System.out.println("ADD PROBLEMS TO CONTEST "+contestId+":"+problemId);
        Problem problem = problemRepository.findById(problemId);
        Contest contest = contestRepository.findById(contestId);
        problem.addContest(contest);
        contest.addProblem(problem);
        return contest;
    }
}
