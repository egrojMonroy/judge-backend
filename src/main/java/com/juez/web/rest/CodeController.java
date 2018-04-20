package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.juez.domain.Code;

import com.juez.repository.CodeRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.service.dto.CodeDTO;
import com.juez.service.mapper.CodeMapper;
import com.juez.service.FileService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.spring.web.advice.general.ProblemAdviceTrait;

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
    public CodeController(CodeRepository codeRepository, CodeMapper codeMapper, FileService fileService) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.fileService = fileService;
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
    public String createCode(@RequestPart MultipartFile reportFile , @RequestParam String language) throws IOException {
        String code = "";
        try { 
            code = new String(reportFile.getBytes(), "UTF-8");
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
         System.out.println(code);
        String userName = "admin";
        String problemName = "COST";
        //create dir and return actual dir
        String actualDir = createDir(userName, problemName, language);
        System.out.println("The actual Dir is " + actualDir +" 1111111 ");
        Path path = Paths.get(actualDir);

        String veredict = "In Queue";
		File f = new File(createFile(code, language, path));
        if(f.exists() && !f.isDirectory()) { 
            veredict = runCode(actualDir,language);
        }
        // Code code = codeMapper.toEntity(codeDTO);
        // code = codeRepository.save(code);
        // CodeDTO result = codeMapper.toDto(code);

        return veredict;
    }
    
    public String createDir(String userName, String problemName, String language) {
        String currentDir = getCurrentDir();
        String dirToCreate = currentDir+"/"+userName+"/"+problemName+"/";
        if(language.equalsIgnoreCase("java")){
            dirToCreate += "java";
        } else {
            dirToCreate += "cpp";
        }
        System.out.println("DIR TO CREATE "+dirToCreate);
        fileService.runScript("mkdir -p "+dirToCreate, "");
        return  dirToCreate;
    }
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
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }

    // @code is the actual code of the file
    // @language is the language in witch the code is 
    // @path is where to create the file 
    // Returns (String) the complite filepath of the new code created 
    public String createFile(String code, String language, Path path){
        System.out.println("222222");
        String newFileName = "Main";
        String filePath = "";
        try {
            FileWriter fw;
            
            if(language.equalsIgnoreCase("java")) {
                filePath = path+"/"+newFileName+".java";
                fw = new FileWriter(filePath);
                log.debug("JAVA",filePath);
            } else {
                filePath = path+"/"+newFileName+".cpp";
                fw = new FileWriter(filePath);
                log.debug("CPP",filePath);
            }
            fw.write(code);
            fw.close();
          } catch (FileNotFoundException e) {
            // File not found
            log.debug("ERROR");
            e.printStackTrace();
          } catch (IOException e) {
            log.debug("EXCEPTOE");
            // Error when writing to the file
            e.printStackTrace();
          }
        return filePath;
    }


}