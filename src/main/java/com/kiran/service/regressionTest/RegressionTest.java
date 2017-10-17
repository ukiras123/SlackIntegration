package com.kiran.service.regressionTest;

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


    private static final Logger log = LoggerFactory.getLogger(RegressionTest.class);

    public String doRegression(String apiName, String email) throws IOException, InterruptedException {

        if (apiName.equals("price_model")) {
            String[] command = new String[]{"/bin/bash", "-c", "fathom test price_model/price_model_create.rb -c env:local id:1"};

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
                System.out.println("\n\nExit Value is " + exitValue);
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }
        return uploadAndSendEmail(email);
    }

    public String uploadAndSendEmail(String email) throws IOException, InterruptedException {
        String[] command = new String[] {"/bin/bash", "-c", "fathom publish report --upload qa --email --recipients kgautam@oceanx.com "+email};
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
            System.out.println("\n\nExit Value is " + exitValue);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        log.info(proc.toString());
        return out.substring(33,77);
    }

}
