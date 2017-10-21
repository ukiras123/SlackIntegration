package com.kiran.service.regressionTest;

import com.kiran.service.exception.InvalidMove;
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

    public String getFathomCommand(String api_name) {
        String command = null;
        if (api_name.equalsIgnoreCase("price_model")) {
            command = "fathom test price_model -c env:local id:1";
        } else if (api_name.equalsIgnoreCase("approved")) {
            command = "fathom test fixed_bundle/bundle_approved_databse.rb -c env:local brand:BQ";
        } else if (api_name.equalsIgnoreCase("product")) {
            command = "fathom test product_item -c env:local brand:BQ";
        }
        return command;
    }

    public String getRepoLocation(String api_name) {
        String location = null;
        if (api_name.equalsIgnoreCase("price_model")) {
            location = "/Users/kgautam/Drive/projects/slate/price-model-api";
        } else if (api_name.equalsIgnoreCase("approved") || api_name.equalsIgnoreCase("product")) {
            location = "/Users/kgautam/Drive/projects/slate/product-api";
        }
        return location;
    }

    public String getPortNumber(String api_name) {
        String port = null;
        if (api_name.equalsIgnoreCase("price_model")) {
            port = "8250";
        } else if (api_name.equalsIgnoreCase("approved") || api_name.equalsIgnoreCase("product")) {
            port = "8180";
        }
        return port;
    }

    public String doRegression(String apiName, String branch, String email) throws IOException, InterruptedException {
        String success = changeBranch(apiName, branch);
        if (success==null || success.isEmpty()) {
            throw new InvalidMove("Invalid Branch");
        }
        getLatest(apiName);
        log.info("Getting latest change and starting server");
        stopPort(apiName);
        start(apiName);
        log.info("Starting testing");


        String[] command = new String[]{"/bin/bash", "-c", getFathomCommand(apiName)};
        log.info("Fathom command: "+getFathomCommand(apiName));
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

    public void stopPort(String api_name) throws IOException, InterruptedException {
        log.info("Stopping port: " + getPortNumber(api_name));

        String[] command = new String[]{"/bin/bash", "-c", "kill `lsof -t -i:" + getPortNumber(api_name) + "`"};
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

    public String changeBranch(String apiName, String branch) throws IOException, InterruptedException {
        log.info("Changing Branch: " + branch);

        String[] command = new String[]{"/bin/bash", "-c", "git checkout " + branch};
        ProcessBuilder proc = new ProcessBuilder(command);
        String location = getRepoLocation(apiName);
        if (location == null || location.isEmpty()) {
            throw new InvalidMove("Invalid API Name");
        }
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
        log.info("----returning: "+out.toString());
        return out.toString();
    }


    public String getLatest(String apiName) throws IOException, InterruptedException {
        log.info("Pulling latest change");
        String[] command = new String[]{"/bin/bash", "-c", "git pull"};
        ProcessBuilder proc = new ProcessBuilder(command);
        String location = getRepoLocation(apiName);
        log.info("Location found? "+location);
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

        log.info("----returning: "+out.toString());
        return out.toString();
    }

    public void start(String apiName) throws IOException, InterruptedException {
        log.info("Starting the server");
        String[] command = new String[]{"/bin/bash", "-c", "mvn spring-boot:run -Dspring.profiles.active=local"};
        ProcessBuilder proc = new ProcessBuilder(command);
        proc.directory(new File(getRepoLocation(apiName)));
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
            return "Report Generation Failed. Check log file";
        }
    }

}
