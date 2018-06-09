package com.juez.service;

import com.juez.domain.Problem;
import com.juez.repository.ProblemRepository;
import com.juez.service.dto.ProblemDTO;
import com.juez.service.mapper.ProblemMapper;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.UrlResource;

/**
 * Service Implementation for managing Problem.
 */
@Service
@Transactional
public class ProblemService {

    private final Logger log = LoggerFactory.getLogger(ProblemService.class);

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;
    private final UserService userService;
    public ProblemService(
        ProblemRepository problemRepository, 
        ProblemMapper problemMapper,
        UserService userService) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
        this.userService = userService;
    }

    /**
     * Save a problem.
     *
     * @param problemDTO the entity to save
     * @return the persisted entity
     */
    public ProblemDTO save(ProblemDTO problemDTO) {
        log.debug("Request to save Problem : {}", problemDTO);
        Problem problem = problemMapper.toEntity(problemDTO);
        problem.setCreator( userService.getUserWithAuthorities());
        problem = problemRepository.save(problem);
        return problemMapper.toDto(problem);
    }

    /**
     * Get all the problems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Problems");
        return problemRepository.findAll(pageable)
            .map(problemMapper::toDto);
    }
   /**
     * Get all the problems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProblemDTO> findAllByCreator(Pageable pageable) {
        log.debug("Request to get all Problems");
        return problemRepository.findByCreatorIsCurrentUser(pageable)
            .map(problemMapper::toDto);
    }
    /**
     * Get one problem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProblemDTO findOne(Long id) {
        log.debug("Request to get Problem : {}", id);
        Problem problem = problemRepository.findOne(id);
        return problemMapper.toDto(problem);
    }

    /**
     * Delete the problem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Problem : {}", id);
        problemRepository.delete(id);
    }

    public Resource loadFileAsResource (String name) {
        Path filePath = Paths.get(getCurrentDir()+"/utils/problems/" + name + ".pdf");
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                //byte[] encoded = Base64.encodeBase64(resource.getFile());
                return resource;
            } else {
                System.out.println("ERROR doesnt exist");
            } 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
    }
    public Resource loadSampleAsResource () {
        Path filePath = Paths.get(getCurrentDir()+"/utils/test.doc");
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                System.out.println("-----------------------------------------------------works");
                return resource;
            } else {
                System.out.println("ERROR doesnt exist");
            } 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
    }
    public String storeFile (MultipartFile file, String problemId) throws IOException {
        System.out.println(problemId + " ARRRRRRY");
        Path location = Paths.get(getCurrentDir()  + "/utils/problems/"+ problemId+".pdf");
        System.out.println("AFS :"+ location);
        Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
		return file.getOriginalFilename();
    }
    
    public String getCurrentDir() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }    
}
