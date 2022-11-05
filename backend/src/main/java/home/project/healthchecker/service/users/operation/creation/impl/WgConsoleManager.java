package home.project.healthchecker.service.users.operation.creation.impl;

import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.WgKeys;
import home.project.healthchecker.service.users.operation.creation.WgManager;
import home.project.healthchecker.utils.ConsoleProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class WgConsoleManager implements WgManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WgConsoleManager.class);
    @Value("${wg.config.path}")
    private String configPath;

    @Override
    public WgKeys generateKeys() throws CreateUserException{
        try {
            ConsoleProcess process = new ConsoleProcess();
            String wg_privateKey = process.getOutputCommand(configPath, "wg genkey");
            String wg_publicKey = process.getOutputCommand(configPath, String.format("echo %s | wg pubkey"));
            return new WgKeys(wg_privateKey, wg_publicKey);
        } catch (Exception e) {
            String errorMessage = "Generate keyPair exception";
            LOGGER.error(errorMessage, e);
            throw new CreateUserException(errorMessage, e);
        }
    }

    @Override
    public void restartWgDemon() {
        ConsoleProcess consoleProcess = new ConsoleProcess();
        try {
            consoleProcess.run("/", "systemctl restart wg-quick@wg0");
        } catch (Exception e) {
            LOGGER.error("Restart wg demon ERROR", e);
        }
    }
}
