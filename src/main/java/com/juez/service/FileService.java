package com.juez.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    public FileService() {

    }

    
    public String runScript( String command, String values) {
        try {
                String target = command; //new String("/home/jorge/ ");
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(target+values);
                proc.waitFor();
                StringBuffer output = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = "";                       
                while ((line = reader.readLine())!= null) {
                        output.append(line + "\n");
                }   
                System.out.println(output);
                return output.toString();
        } catch (Throwable t) {
            t.printStackTrace();
            return "print Stack Trace";
        }
		
    }

}