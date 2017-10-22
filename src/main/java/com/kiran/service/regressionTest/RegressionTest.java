package com.kiran.service.regressionTest;

import com.kiran.service.exception.InvalidMove;
import com.kiran.service.utilities.ApiDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author Kiran
 * @since 10/5/17
 */
@Component
public class RegressionTest {

    private static final String fathomFilePath = "/Users/kgautam/Drive/projects/slate/fathom-tests/tests";
    private static final Logger log = LoggerFactory.getLogger(RegressionTest.class);

    public String doRegression(String apiName, String branch, String email, String automationLevel) throws IOException, InterruptedException {

        ApiDetail apiDetail = ApiDetail.fromString(apiName);
        if (apiDetail == null) {
            throw new InvalidMove("Invalid API Name");
        }

        String success = changeBranch(apiDetail, branch);
        if (success == null || success.isEmpty()) {
            throw new InvalidMove("Invalid Branch");
        }
        getLatest(apiDetail);
        log.info("Getting latest change and starting server");
        stopPort(apiDetail);
        start(apiDetail);

        log.info("Starting testing");

        String fathomCommand = apiDetail.getSmokeTestFathomCommand();
        log.info("Automation Level: " + automationLevel);

        if (automationLevel != null) {
            if (automationLevel.equalsIgnoreCase("regression")) {
                fathomCommand = apiDetail.getRegressionFathomCommand();
            }
        }
        log.info("Fathom command: " + fathomCommand);

        String[] command = new String[]{"/bin/bash", "-c", fathomCommand};

        ProcessBuilder proc = new ProcessBuilder(command);
        proc.directory(new File(fathomFilePath));
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            log.info("Exit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        return uploadAndSendEmail(email);
    }

    public void stopPort(ApiDetail apiDetail) throws IOException, InterruptedException {
        log.info("Stopping port: " + apiDetail.getPort());

        String[] command = new String[]{"/bin/bash", "-c", "kill `lsof -t -i:" + apiDetail.getPort() + "`"};
        ProcessBuilder proc = new ProcessBuilder(command);
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        StringBuilder out = new StringBuilder();
        while ((line = br.readLine()) != null) {
            out.append(line);
        }
        log.info(out.toString());


        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            System.out.println("Exit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        log.info(proc.toString());
        log.info("Stopped the port");
    }

    public String changeBranch(ApiDetail apiDetail, String branch) throws IOException, InterruptedException {
        log.info("Changing Branch: " + branch);

        String[] command = new String[]{"/bin/bash", "-c", "git checkout " + branch};
        ProcessBuilder proc = new ProcessBuilder(command);
        String location = apiDetail.getRepoLocation();
        proc.directory(new File(location));
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        StringBuilder out = new StringBuilder();
        while ((line = br.readLine()) != null) {
            out.append(line);
        }
        log.info(out.toString());


        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            log.info("Exit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        log.info(proc.toString());
        log.info("Branch changed");
        log.info("----returning: " + out.toString());
        return out.toString();
    }


    public String getLatest(ApiDetail apiDetail) throws IOException, InterruptedException {
        log.info("Pulling latest change");
        String[] command = new String[]{"/bin/bash", "-c", "git pull"};
        ProcessBuilder proc = new ProcessBuilder(command);
        String location = apiDetail.getRepoLocation();
        log.info("Location found? " + location);
        proc.directory(new File(location));
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        StringBuilder out = new StringBuilder();
        while ((line = br.readLine()) != null) {
            out.append(line);
        }
        log.info(out.toString());


        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            log.info("Exit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        String outPUt = proc.toString();
        log.info(outPUt);
        log.info("Pulled the latest");

        log.info("----returning: " + out.toString());
        return out.toString();
    }

    public void start(ApiDetail apiDetail) throws IOException, InterruptedException {
        log.info("Starting the server");
        String[] command = new String[]{"/bin/bash", "-c", "mvn spring-boot:run -Dspring.profiles.active=local"};
        ProcessBuilder proc = new ProcessBuilder(command);
        proc.directory(new File(apiDetail.getRepoLocation()));
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder out = new StringBuilder();
        String line = br.readLine();

        log.info("Server in progress");

        Thread.sleep(20000l);
        log.info(line);

        String outPUt = proc.toString();

        log.info(outPUt);
        log.info("Server Started");
    }


    public String uploadAndSendEmail(String email) throws IOException, InterruptedException {
        log.info("Sending Report");

        String[] command = new String[]{"/bin/bash", "-c", "fathom publish report --upload qa --email --recipients kgautam@oceanx.com " + email};
        ProcessBuilder proc = new ProcessBuilder(command);
        proc.directory(new File("/Users/kgautam/Drive/projects/slate/fathom-tests/tests"));
        Process process = proc.start();

        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        StringBuilder out = new StringBuilder();
        while ((line = br.readLine()) != null) {
            out.append(line);
        }
        log.info(out.toString());


        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            log.info("Exit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        log.info(proc.toString());
        log.info(out.toString());
        log.info("Report Sent");
        if (out.length() > 78) {
            return out.substring(33, 77);
        } else {
            return "Report Generation Failed. Check log file.";
        }
    }

}
