package com.juez.service;

import org.apache.commons.lang3.text.translate.CodePointTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.juez.service.FileService;
import com.juez.domain.Code;
import com.juez.domain.Submission;
import com.juez.repository.CodeRepository;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.CodeMapper;
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

@Service
@EnableAsync
public class CodeService {

    private final Logger log = LoggerFactory.getLogger(CodeService.class);;
    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper;
    
    private final FileService fileService;
    private final SubmissionService submissionService;
    public CodeService (
        CodeRepository codeRepository, 
        CodeMapper codeMapper, 
        FileService fileService, 
        SubmissionService submissionService
        ) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.fileService = fileService;
        this.submissionService = submissionService;
    }
    /**
     * Save file on disk
     * 
     */
    public Code create( MultipartFile reportFile , String language, String problemId){
        String code = "";
        System.out.println(reportFile.getOriginalFilename());
        try { 
            code = new String(reportFile.getBytes(), "UTF-8");
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName(); //get logged in username
        System.out.println(code);
        String problemID = problemId;
        //create dir and return actual dir
        String actualDir = createDir(userName, problemID, language);
        System.out.println("The actual Dir is " + actualDir +" *********** ");
        Path path = Paths.get(actualDir);
        String pathFile = createFile(reportFile,path,reportFile.getOriginalFilename());
		File f = new File(pathFile);
        String pathSave = pathFile.replace(getCurrentDir(), "");
        CodeDTO result = new CodeDTO();
        String name = reportFile.getOriginalFilename();
        if( language.equalsIgnoreCase("java")) { 
            name = name.replace(".java", "");
        } else {
            name = name.replace(".cpp", "");
        }
        result.setName(reportFile.getOriginalFilename());
        result.setPath(pathSave);
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("Time created: "+now);
        result.setDateupload(now);
        Code codeFinal = codeMapper.toEntity(result);
        codeFinal = codeRepository.save(codeFinal);
        submissionService.runCode(Long.parseLong(problemId), userName, language, codeFinal, getCurrentDir());
		return codeFinal;
        
    }
    
    public Code getCode(Long id){
        Code code = codeRepository.getOne(id);
        return code;
    }
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }

    public String createDir(String userName, String problemName, String language) {
        String currentDir = getCurrentDir();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();        
        String dirToCreate = currentDir+"/code/"+userName+"/"+problemName+"/";
        if(language.equalsIgnoreCase("java")){
            dirToCreate += "java/";
        } else {
            dirToCreate += "cpp/";
        }
        dirToCreate += sdf.format(date)+"/";
        System.out.println("DIR TO CREATE "+dirToCreate);
        fileService.runScript("mkdir -p "+dirToCreate, "");
        return  dirToCreate;
    }
    // @code is the actual code of the file
    // @language is the language in witch the code is 
    // @path is where to create the file 
    // Returns (String) the complite filepath of the new code created 
    public String createFile(MultipartFile code, Path path, String fileName){
        System.out.println("222222");
        String filePath = path.toString()+"/"+fileName;
               
        File codeDest = new File(filePath);
        try {
            code.transferTo(codeDest);
            System.out.println("Code created in path: "+ filePath);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return filePath;
    }

    public void setSubmission( Long id, Long subId ) {
        Code code = codeRepository.findOne(id);
        // Submission submission = submissionRepository.findOne(id); 
        // code.setSubmission(submission);
        // codeRepository.save(code);
        System.out.println(code.getName());
    }   

    @Async
    public String runCode(String dir, String language) {
        System.out.println("333333");
            //file name  without ext
            String fileName = "Main";
            //class name 
            String compilationName = "Main";
            //input file name without ext
            String inputName = getDirInput();
            //output file name without ext 
            String outputName = "Name"; 
            String solutionName = getDirSolution();
            String veredict = "In Queue";
            String bashPath = getCurrentDir();
            System.out.println("Bash Path " + bashPath);
            if( language.equalsIgnoreCase("java")) { 
                bashPath += "/utils/compile_java.sh ";
                veredict = fileService.runScript(bashPath,fileName+" "+compilationName+" "+inputName+" "+outputName +" "+solutionName+" "+dir);
            } else {
                bashPath += "/utils/compile_c.sh ";
                veredict = fileService.runScript(bashPath,fileName+" "+fileName+" "+inputName+" "+outputName+" "+solutionName+" "+dir );
            }
            return veredict;
    }

    public String getDirSolution() {
        return "/home/jorge/Desktop/Tests/solutionCostCutting";
    }

    public String getDirInput() {
        return "/home/jorge/Desktop/Tests/Main";
    }
}
