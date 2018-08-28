package com.juez.service;

import com.juez.domain.Submission;
import com.juez.repository.SubmissionRepository;
import com.juez.service.dto.SubmissionDTO;
import com.juez.service.mapper.ProblemMapper;
import com.juez.service.mapper.SubmissionMapper;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juez.domain.Code;
import com.juez.domain.Coder;
import com.juez.domain.Contest;
import com.juez.domain.Problem;
import com.juez.domain.Submission;
import com.juez.domain.TestCase;
import com.juez.domain.User;
import com.juez.domain.enumeration.Language;
import com.juez.domain.enumeration.Veredict;
import com.juez.repository.SubmissionRepository;
import com.juez.repository.UserRepository;
import com.juez.repository.ContestRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.juez.service.TestCaseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.juez.repository.CodeRepository;
import com.juez.repository.ProblemRepository;
import com.juez.repository.UserRepository;
/**
 * Service Implementation for managing Submission.
 */
@Service
@Transactional
public class SubmissionService {

    private final Logger log = LoggerFactory.getLogger(SubmissionService.class);
    private final SubmissionRepository submissionRepository;
    private final FileService fileService;
    private final CodeRepository codeRepository;
    private final TestCaseService testCaseService; 
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final SubmissionMapper submissionMapper;
    private final ContestRepository contestRepository;
    public SubmissionService (
        SubmissionRepository submissionRepository, 
        FileService fileService,
        CodeRepository codeRepository,
        TestCaseService testCaseService,
        ProblemRepository problemRepository,
        UserRepository userRepository, 
        SubmissionMapper submissionMapper,
        ContestRepository contestRepository
        ) {
        this.submissionRepository = submissionRepository;
        this.fileService = fileService;
        this.codeRepository = codeRepository;
        this.testCaseService = testCaseService;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.submissionMapper = submissionMapper;
        this.contestRepository = contestRepository;
    }
    /**
     * Save a submission.
     *
     * @param submissionDTO the entity to save
     * @return the persisted entity
     */
    public SubmissionDTO save(SubmissionDTO submissionDTO) {
        log.debug("Request to save Submission : {}", submissionDTO);
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);
        return submissionMapper.toDto(submission);
    }

    /**
     * Get all the submissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Submissions");
        return submissionRepository.findAll(pageable)
            .map(submissionMapper::toDto);
    }

    /**
     * Get one submission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SubmissionDTO findOne(Long id) {
        log.debug("Request to get Submission : {}", id);
        Submission submission = submissionRepository.findOne(id);
        return submissionMapper.toDto(submission);
    }

    /**
     * Delete the submission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Submission : {}", id);
        submissionRepository.delete(id);
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
            
            int timelimit = 10;
            if( language.equalsIgnoreCase("java")) { 
                bashPath += "/utils/compile_java.sh ";
                timelimit = actualProblem.getTimelimitjava();
            } else {
                bashPath += "/utils/compile_c.sh ";
                timelimit = actualProblem.getTimelimit();
            }

            for( TestCase tc: listTC ) {
                String inputName = testPath+tc.getInputfl();
                String solutionName = testPath+tc.getOutputfl();
                System.out.println("INPUT 3 "+inputName+ " 4: "+ outputName + " 5:" +solutionName+ " 6:" + codeFolder );
                veredict = fileService.runScript(bashPath,fileName+" "+compilationName+" "+inputName+" "+outputName +" "+solutionName+" "+codeFolder+" "+timelimit);
                System.out.println("$$$$$$$$$$*********VEREDICT "+veredict+ " inputName:  "+ inputName);

                if(veredict.toString().contains("Compilation Error")) {
                    veredict = "Compilation Error";
                    wa = 0;
                    break;
                } else if(veredict.toString().contains("Accepted")){
                    ac++;
                } else if( veredict.toString().contains("timeout")) {
                    veredict = "timeout";
                    wa = 0;
                    break;
                }else if (veredict.toString().contains("runtime") ) {
                    veredict = "runtime";
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
            // if( wa>0 && !veredict.toString().contains("timeout")) {
            //     veredict = "Wrong answer";
            // } 
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
                ver = ver.COMPILATION_ERROR;
            } else if( veredict.toString().contains("Accepted")){
                ver = ver.ACCEPTED;
            } else if (veredict.toString().contains("timeout") ) {
                ver = ver.TIME_LIMIT;
            } else if (veredict.toString().contains("runtime") ) {
                ver = ver.RUN_TIME_ERROR;
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
    public void deleteFiles( String codeDir, String language) {
        String bashPath = getCurrentDir();
        if( language.equalsIgnoreCase("java")) { 
            bashPath += "/utils/deleteFilesJava.sh ";
        } else {
            bashPath += "/utils/deleteFilesCpp.sh ";
        }
        fileService.runScript(bashPath,codeDir);
    }

    public String runCodeSyncNoSave(String dir_code, String dir_input, String codeName, String compName, String dirFolder, String currentdir, String language) {
        
        
        String compilationName = compName;
        //input file name without ext
        String veredict = "In Queue";
        String bashPath = currentdir;
        Integer ac = 0;
        Integer wa = 0;
        String testPath = dir_input;
        System.out.println("**///////////////**********************"+language+"***************\\\\\\\\\\\\\\\\\\\\**");
        if( language.equalsIgnoreCase("java")) { 
            bashPath += "/utils/Bash-Time.sh  ";
        } else {
            bashPath += "/utils/Bash-Time-c.sh ";
        }
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(bashPath + " " + dir_code+" "+dir_input+" "+codeName+" "+compName+ " "+ dirFolder);
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            veredict = fileService.runScriptIEO(bashPath, dir_code+" "+dir_input+" "+codeName+" "+compName+ " "+ dirFolder +"");
            System.out.println("$$$$$$$$$$*********VEREDICT "+veredict);
            

        System.out.println(veredict);
        int x = veredict.indexOf("real");
        String time = "";
        if(x != -1)
            time = veredict.substring(x + 5, x+ 9);
        else 
            time = veredict;


       return time;
        
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

    public List<SubmissionDTO> getSubmissionsOfContest(Long id) {
        Contest contest = this.contestRepository.findOne(id);
        if(contest != null) {
            ZonedDateTime start = contest.getStartdate();
            ZonedDateTime end = contest.getEnddate();
            System.out.println("/////////////////////////////CONTEST start "+start+" end "+end);
            List<SubmissionDTO> list = submissionMapper.toDto(submissionRepository.findAllByDateuploadBetween(start, end));
            return list; 
        }
        return null;
    }
    public List<SubmissionDTO> getSubmissionsUsersContest(Long id) {
        Contest contest = this.contestRepository.findOne(id);
        if(contest != null) {
            Set<User> submitters = getSubmitters(contest);
            Set<Problem> problems = getProblems(contest);
            ZonedDateTime start = contest.getStartdate();
            ZonedDateTime end = contest.getEnddate();
            System.out.println("/////////////////////////////CONTEST start "+start+" end "+end);
            List<Submission> submissions = submissionRepository.findAllByDateuploadBetweenAndSubmitterInAndProblemIn(start,end,submitters, problems);
            group(submissions);
            List<SubmissionDTO> list = submissionMapper.toDto(submissions);
            return list; 
        }
        return null;

    }
    public JSONArray getJSON (Long id) throws JSONException{
        Contest contest = this.contestRepository.findOne(id);
        if(contest != null) {
            Set<User> submitters = getSubmitters(contest);
            Set<Problem> problems = getProblems(contest);
            ZonedDateTime start = contest.getStartdate();
            ZonedDateTime end = contest.getEnddate();
            System.out.println("/////////////////////////////CONTEST start "+start+" end "+end);
            Iterator<User> iterator = submitters.iterator();
            
            JSONArray jarray = new JSONArray();
            
            while(iterator.hasNext()){
                JSONObject json = new JSONObject();    
                User s = iterator.next();
                List<Submission> submissions = submissionRepository.findAllByDateuploadBetweenAndSubmitterAndProblemIn(start,end,s, problems);
                //json.put("userName",s.getLogin());
                
                
                json = groupSubmissions(submissions,start);
                json.put("username", s.getLogin());
                json.put("firstName", s.getFirstName());
                json.put("lastName", s.getLastName());
                json.put("email", s.getEmail());

                jarray.put(json);
            }
            
            return jarray;
        }
        return null;

    }
    
    
    public JSONObject groupSubmissions(List<Submission> subs, ZonedDateTime start) throws JSONException{
        
        Veredict ver = Veredict.ACCEPTED; 
        JSONObject status = new JSONObject(); 
        JSONArray jarray = new JSONArray();
        JSONObject data = new JSONObject();
        int accepteds = 0;
        long total = 0;
        Map<String, List<Submission> > listGrouped = 
        subs.stream().collect(Collectors.groupingBy(s -> s.getProblem().getName()));
        for(String key: listGrouped.keySet() ){
            System.out.println("----------"+key+"-----"+accepteds);
            JSONObject info = new JSONObject();
            
            List<Submission> list = listGrouped.get(key);
            Integer count = list.size();
            
            ZonedDateTime date = null;
            Veredict finalV = null;
            for(Submission q: list) {

                if(date == null) {
                    date = q.getDateupload();
                    finalV = q.getStatus();
                    System.out.println("111111111111111111111111111111  => "+finalV);
                    if(finalV == ver) break;
                } else {
                    if(q.getStatus() == ver) {
                        date = q.getDateupload();
                        finalV = q.getStatus();
                        System.out.println("22222222222222222222222 ACCEPTED BREAK  => "+finalV);
                        break;
                    } else {
                        if(q.getDateupload().isAfter(date)) {
                            date = q.getDateupload();
                            finalV = q.getStatus();
                            System.out.println("3333333333333333333333333 CONTINUE  => "+finalV);
                        }
                    }
                }
            }
        
            // info.put("problemName",key);
            info.put("cout",count);
            Duration x = Duration.between(start,date);
            
            info.put("date",x.getSeconds()); 
            info.put("veredict", finalV);
            if(finalV == ver) {
                accepteds++;
                total+=x.getSeconds();
            }
            data.put(key,info);
        }
        status.put("data",data);
        status.put("totalTime", total);
        status.put("accepteds",accepteds);
        return status;
    }
    
    public JSONObject group(List<Submission> subs) {
        Map<Object, List<Submission>> studlistGrouped =
        subs.stream().collect(Collectors.groupingBy(s -> s.getSubmitter().getLogin()));
        System.out.println(" HEEEEEEEEEEE GROUPED BY SUBMITTER");
        System.out.println(studlistGrouped.toString());
        
        
        System.out.println("--//////////////////////////////-- "+new JSONObject(studlistGrouped));
        return new JSONObject(studlistGrouped);
    }
    public Set<Problem> getProblems (Contest contest) {
        Set<Problem> problems = contest.getProblems();
        return problems;
    }
    public Set<User> getSubmitters(Contest contest){
        Set<Coder> coders = contest.getCoders();
        Set<User> users = new HashSet<User>();
        coders.forEach(
            (c) -> {
                users.add(c.getUser() );
            }
        );
        return users;
    } 
    public List<SubmissionDTO> getStatusContest( Long id ) {
        List<SubmissionDTO> subs = getSubmissionsOfContest(id);
        Set<Long> problems = getIdProblems(id);
        Iterator<SubmissionDTO> iterator = subs.iterator();
        while(iterator.hasNext()) {
            SubmissionDTO s = iterator.next();
            if(!problems.contains(s.getProblemId())){
                iterator.remove();
            }
        }
         
        return subs;
    }
    public Set<Long> getIdProblems (Long id) {
        Contest contest = contestRepository.findById(id);
        Set<Problem> problems = contest.getProblems();
        Set<Long> problemsId = new HashSet<Long>();
        problems.forEach((p) -> {
            Long x = p.getId();
            problemsId.add(x);
        });
        return problemsId;
    }

    public Page<Submission> getAllSubmission(Pageable pageable){
        Page<Submission> page = submissionRepository.findAll(pageable);
        //Page<SubmissionDTO> pageDTO = page.map(submissionMapper::toDto);
        return page;
    }
}
