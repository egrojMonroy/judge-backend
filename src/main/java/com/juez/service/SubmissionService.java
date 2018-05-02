package com.juez.service;

import com.juez.domain.Code;
import com.juez.domain.Problem;
import com.juez.domain.Submission;
import com.juez.domain.TestCase;
import com.juez.domain.User;
import com.juez.domain.enumeration.Language;
import com.juez.domain.enumeration.Veredict;
import com.juez.repository.SubmissionRepository;
import com.juez.repository.UserRepository;

import io.github.jhipster.config.JHipsterProperties;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Proxy;

import com.juez.repository.CodeRepository;
import com.juez.repository.ProblemRepository;

import javax.mail.internet.MimeMessage;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.juez.service.TestCaseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.juez.repository.ProblemRepository;
import com.juez.repository.UserRepository;
/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
@Proxy(lazy=false)
public class SubmissionService {

    private final Logger log = LoggerFactory.getLogger(SubmissionService.class);
    private final SubmissionRepository submissionRepository;
    private final FileService fileService;
    private final CodeRepository codeRepository;
    private final TestCaseService testCaseService; 
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    public SubmissionService (
        SubmissionRepository submissionRepository, 
        FileService fileService,
        CodeRepository codeRepository,
        TestCaseService testCaseService,
        ProblemRepository problemRepository,
        UserRepository userRepository
        ) {
        this.submissionRepository = submissionRepository;
        this.fileService = fileService;
        this.codeRepository = codeRepository;
        this.testCaseService = testCaseService;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }
    private String currentDir; 
    public String getCurrentDir() {
        return currentDir;
    }
    @Async
    public void runCode(Long problemId, String userName, String language, Code code, String dir) {
            System.out.println("333333 "+ userName+ " " + language);
            currentDir = dir;
            Code actual = code;
            Problem actualProblem = getProblem(problemId);
            System.out.println(actual.toString());
            //directory of current code | where the compilation and output will be
            // $6 
            String codeFolder = getCurrentDir()+actual.getPath();
            Integer index = codeFolder.indexOf("/");
            System.out.println(":: CODE FOLDER ::" + codeFolder);
            codeFolder = codeFolder.replace("/"+actual.getName(), "");
            System.out.println(":: CODE FOLDER ::" + codeFolder);
            //file name  without Extention!!!
            // $1
            String fileName = actual.getName();
            fileName = language.equalsIgnoreCase("java")? fileName.replace(".java", ""): fileName.replace(".cpp","");
            System.out.println(" 1 @@@@@@@@@@@@@@@@@@ FILE NAME : "+fileName);
            //class name 
            // $2
            String compilationName = fileName;
            System.out.println(" 2 @@@@@@@@@@@@@@@@@@ COMP NAME :" + compilationName );
            //output name for code without ext 
            // $4
            String outputName = fileName;
            //input file name without ext
            System.out.println(" 4 @@@@@@@@@@@@@@@@@@ OUT NAME: "+outputName); 
            String veredict = "In Queue";
            String bashPath = getCurrentDir();
            System.out.println("Bash Path " + bashPath);
            Integer ac = 0;
            Integer wa = 0;
            List<TestCase> listTC = testCaseService.getTestCasesNameByProblem(problemId);
            String testPath = getCurrentDir()+"/test_cases/"+problemId+"/";
            System.out.println("TEST PATH "+testPath);
            if( language.equalsIgnoreCase("java")) { 
                bashPath += "/utils/compile_java.sh ";
            } else {
                bashPath += "/utils/compile_c.sh ";
            }
            for( TestCase tc: listTC ) {
                String inputName = testPath+tc.getInputfl();
                String solutionName = testPath+tc.getOutputfl();
                System.out.println("INPUT 3 "+inputName+ " 4: "+ outputName + " 5:" +solutionName+ " 6:" + codeFolder );
                veredict = fileService.runScript(bashPath,fileName+" "+compilationName+" "+inputName+" "+outputName +" "+solutionName+" "+codeFolder+" "+actualProblem.getTimelimit());
                System.out.println("$$$$$$$$$$*********VEREDICT "+veredict);
                if(veredict.toString().contains("Compilation Error")) {
                    veredict = "Compilation Error";
                    wa = 0;
                    break;
                }
                else if(veredict.toString().contains("Accepted")){
                    ac++;
                } else if( veredict.toString().contains("timeout")) {
                    veredict = "timeout";
                    wa = 0;
                    break;
                } else {
                    veredict = "Wrong Answer";
                    wa++;
                    break;
                }
            }
            System.out.println(veredict);
            System.out.println("**********************************************************************************");
            System.out.println("************A*******C*******w*****w***a*******************************************");
            System.out.println("*************N*****F*O*******w***w***a*a******************************************");
            System.out.println("**************S***O***D*******w*w***a***a*****************************************");
            System.out.println("***************W*R*****E*******w***a*****a****************************************");
            System.out.println("****************E*****************************************************************");
            System.out.println(ac+"  -  "+wa);
            if( wa>0 && !veredict.toString().contains("timeout")) {
                veredict = "Wrong answer";
            } 
            System.out.println(veredict);
            System.out.println("************************************");
            Submission submission = new Submission();
            Language lang = null;
            if(language.equalsIgnoreCase("java")){
                lang = lang.JAVA;
            } else {
                lang = lang.C;
            }
            Veredict ver = null; 
            if(veredict.toString().contains("Compilation Error")) { 
                ver = ver.RUN_TIME_ERROR;
            } else if( veredict.toString().contains("Accepted")){
                ver = ver.ACCEPTED;
            } else if (veredict.toString().contains("timeout") ) {
                ver = ver.TIME_LIMIT;
            } else { 
                ver = ver.WRONG_ANSWER;
            }
            System.out.println(lang + " --- "+problemId + " --- "+ ver.toString());
            submission.setLanguage(lang);
            submission.setProblem(actualProblem);
            submission.setStatus(ver);
            Optional<User> user = userRepository.findOneByLogin(userName);
            if(user.isPresent()) {
                submission.setSubmitter(user.get());
            }
            submission.setDateupload(ZonedDateTime.now());
            submissionRepository.save(submission);
            code.setSubmission(submission);
            codeRepository.save(code);
            
    }
    public Code getCode(Long id){
        Code code = codeRepository.findById(id);
        return code;
    }
    public Problem getProblem(Long id ) {
        Problem problem = problemRepository.findById(id);
        return problem;
    }
    public String getDirSolution() {
        return "/home/jorge/Desktop/Tests/solutionCostCutting";
    }

    public String getDirInput() {
        return "/home/jorge/Desktop/Tests/Main";
    }
}
