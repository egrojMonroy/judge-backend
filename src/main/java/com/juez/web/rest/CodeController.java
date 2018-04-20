package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
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
         
        String userName = "admin";
        String currentDir = getCurrentDir();
        String dir = currentDir + "/" + userName;
        Path path = Paths.get(dir);
        log.debug(path.toString());
        log.debug(code);
        if(!Files.exists(path))
            createDir(dir);
        
		File f = new File(createFile(code, language, path));
        if(f.exists() && !f.isDirectory()) { 
            runCode(currentDir,language,path.toString());
        }
        // Code code = codeMapper.toEntity(codeDTO);
        // code = codeRepository.save(code);
        // CodeDTO result = codeMapper.toDto(code);

        return "YES";
    }
    public void createDir(String dir) {
        fileService.runScript("mkdir -p "+dir, "");
    }
    public void runCode(String dir, String language, String path) {
        if( language.equalsIgnoreCase("java")){
            //Path where the file is created
            String pathFile = path;
            //file name  without ext
            String fileName = "Main";
            //class name 
            String name = "Main";
            //input file name without ext
            String input = "Main1";
            //output file name without ext 
            String output = "Main1";
            String veredict = fileService.runScript(dir + "/utils/compile_java.sh ",fileName+" "+name+" "+input+" "+output +" "+path);
            System.out.println("the veredict is "+veredict);

        } 
        
    }
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }
    public String createFile(String code, String language, Path path){
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