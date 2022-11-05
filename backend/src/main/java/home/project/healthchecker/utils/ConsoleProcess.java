package home.project.healthchecker.utils;

import home.project.healthchecker.exceptions.ConsoleCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ConsoleProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger("ConsoleProcess");

    public int run(String dir, String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = getProcessBuilder(dir, command);

        LOGGER.info("run command: {}", command);
        Process process = processBuilder.start();
        process.getInputStream();

        BufferedReader processError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String errorBuffer = null;
        while ((errorBuffer = processError.readLine()) != null) {
            LOGGER.info(errorBuffer);
        }

        BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String outputBuffer = null;
        while ((outputBuffer = processOutput.readLine()) != null) {
            LOGGER.error(outputBuffer);
        }

        return process.waitFor();
    }

    public String getOutputCommand(String dir, String command) throws IOException, InterruptedException, ConsoleCommandException {
        ProcessBuilder processBuilder = getProcessBuilder(dir, command);

        LOGGER.info("run command: {}", command);
        Process process = processBuilder.start();
        process.getInputStream();

        BufferedReader processError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String errorBuffer = null;
        while ((errorBuffer = processError.readLine()) != null) {
            throw new ConsoleCommandException(errorBuffer);
        }

        BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder outputBuilder = new StringBuilder();
        String outputBuffer;
        while ((outputBuffer = processOutput.readLine()) != null) {
            outputBuilder.append(outputBuffer);
        }

        process.waitFor();
        return outputBuilder.toString();
    }

    private ProcessBuilder getProcessBuilder(String dir, String command){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);
        processBuilder.directory(new File(dir));

        return processBuilder;
    }
}
