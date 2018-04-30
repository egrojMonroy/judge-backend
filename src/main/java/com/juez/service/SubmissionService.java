package com.juez.service;

import com.juez.domain.Code;
import com.juez.domain.TestCase;
import com.juez.domain.User;
import com.juez.repository.SubmissionRepository;

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
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Locale;
import com.juez.service.TestCaseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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
    public SubmissionService (
        SubmissionRepository submissionRepository, 
        FileService fileService,
        CodeRepository codeRepository,
        TestCaseService testCaseService
        ) {
        this.submissionRepository = submissionRepository;
        this.fileService = fileService;
        this.codeRepository = codeRepository;
        this.testCaseService = testCaseService;
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
                veredict = fileService.runScript(bashPath,fileName+" "+compilationName+" "+inputName+" "+outputName +" "+solutionName+" "+codeFolder);
                System.out.println("$$$$$$$$$$*********VEREDICT "+veredict);
                if(veredict.toString().contains("Accepted")){
                    ac++;
                } else {
                    veredict = "Wrong Answer";
                    wa++;
                    break;
                }
            }
            System.out.println(veredict);
            System.out.println("**********************************************************************************");
            System.out.println("****************************w*****w***a*******************************************");
            System.out.println("*****************************w***w***a*a******************************************");
            System.out.println("******************************w*w***a***a*****************************************");
            System.out.println("*******************************w***a*****a****************************************");
            System.out.println("**********************************************************************************");
            System.out.println(ac+"  -  "+wa);
            if( wa>0 ) {
                veredict = "Wrong answer";
            } 
            System.out.println(veredict);
            System.out.println("************************************");
            
    }
    public Code getCode(Long id){
        Code code = codeRepository.getOne(id);
        return code;
    }
    public String getDirSolution() {
        return "/home/jorge/Desktop/Tests/solutionCostCutting";
    }

    public String getDirInput() {
        return "/home/jorge/Desktop/Tests/Main";
    }
}
