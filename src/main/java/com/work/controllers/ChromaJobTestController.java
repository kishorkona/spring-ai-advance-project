package com.work.controllers;

import com.google.gson.Gson;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chromajob")
public class ChromaJobTestController {

    @Autowired
    private JobLauncher joblauncher;

    @Autowired
    private Job job;


    Gson gson = new Gson();

    @PostMapping("/importdocument")
    public ResponseEntity<String> importdocument( @RequestParam("documentName") String documentName) {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            final JobExecution jobExecution = joblauncher.run(job, jobParameters);
            final String status = jobExecution.getStatus().toString();
            return ResponseEntity.ok("Job Status: " + status);
        } catch (Exception e) {
            String error = "Error importing document: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
