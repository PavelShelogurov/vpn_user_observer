package home.project.healthchecker.service.users.operation.creation.impl;

import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.WgKeys;
import home.project.healthchecker.service.users.operation.creation.WgManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class WgManagerMock implements WgManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WgManagerMock.class);
    @Override
    public WgKeys generateKeys() throws CreateUserException {
        LOGGER.info("Generating wg private and public key");
        return new WgKeys("fakePrivateKey", "facePublicKey");
    }

    @Override
    public void restartWgDemon() {
        LOGGER.info("Restarting wg demon");
    }
}
