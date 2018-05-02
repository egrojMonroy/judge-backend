package com.juez.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.net.HttpHeaders;
import com.juez.domain.Submission;

import com.juez.repository.SubmissionRepository;
import com.juez.web.rest.util.HeaderUtil;
import com.juez.web.rest.util.PaginationUtil;
import com.juez.service.dto.SubmissionDTO;
import com.juez.service.mapper.SubmissionMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import javax.print.attribute.standard.Media;

/**
 * REST controller for managing Submission.
 * @param <LabPatientInfo>
 */
@RestController
@RequestMapping("/api")
public class SubmissionController<LabPatientInfo> {

    private final SubmissionRepository submissionRepository;

    public SubmissionController(SubmissionRepository submissionRepository){
        this.submissionRepository = submissionRepository;
    }
	/**
     * POST  /submissions : Create a new submission.
     *
     */
    @PostMapping("/upload")
    @Timed
    public MultipartFile createSubmission(@RequestPart MultipartFile myFile) throws IOException {
        System.out.println("REST request to save Submission : {}"   );
        //System.out.println(myFile.getName());
        System.out.println("ASDLASJDF------------------"+myFile.getOriginalFilename());
        return myFile;
    }
    /**
     * GET  /submissions : get all the submissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of submissions in body
     */
    @GetMapping("/submissions/all")
    @Timed
    public ResponseEntity< Page<Submission> > getAllSubmissions(Pageable pageable) {
        Page<Submission> page = submissionRepository.findAll(pageable);
        return new ResponseEntity<>(page , HttpStatus.OK);
    }


    @PostMapping("/post")
    @ResponseBody
    public String saveReport(@RequestPart MultipartFile reportFile) throws IOException {
        String content = "";
        try { 
            System.out.println("REPOT" +reportFile.getContentType());
            System.out.println("NAME "+reportFile.getName());
            System.out.println("OE"+ reportFile.getOriginalFilename());  
            content = new String(reportFile.getBytes(), "UTF-8");
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
         System.out.println("------------------------" + content);    
        // System.out.println(reportFile.getOriginalFilename());
        //     try {
        //         File newFile = new File("/home/jorge/Desktop/create/" + reportFile.getOriginalFilename());  
        //         if (!newFile.exists()) {  
        //             newFile.createNewFile();  
        //         } else { 
        //             String q = reportFile.getOriginalFilename()+'1';
        //             newFile = new File("/home/jorge/Desktop/create/" + reportFile.getOriginalFilename()+"1");
        //             newFile.createNewFile();    
        //         }
        //         System.out.println("----------------------");
        //         reportFile.transferTo(newFile); 
        //      }catch(FileAlreadyExistsException e) {
        //         System.out.println("ALREADY EXISTS");
        //         throw e;
        //      }
        //      catch (IOException  e) {
        //         System.out.println("Failed to do something, and cannot continue" + e.getMessage());                
        //         e.printStackTrace();
        //         throw e;
        //      }

			return "A-----.....----f";
           
        }




}
