package home.project.healthchecker.service.users.operation.creation.impl;

import home.project.healthchecker.exceptions.CreateUserException;
import home.project.healthchecker.models.CreateUserRequest;
import home.project.healthchecker.models.CreateUserResult;
import home.project.healthchecker.models.UserDescription;
import home.project.healthchecker.models.WgKeys;
import home.project.healthchecker.service.users.operation.creation.CreateUser;
import home.project.healthchecker.service.users.operation.creation.IpGenerator;
import home.project.healthchecker.service.users.operation.creation.WgManager;
import home.project.healthchecker.utils.ini.IniParser;
import home.project.healthchecker.utils.ini.IniSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * WireGuard user creator. Create user in WireGuard network
 */
@Service
public class WgUserCreator implements CreateUser {

    private static final Logger LOGGER = LoggerFactory.getLogger(WgUserCreator.class);
    private static final String INTERFACE_IP_CONFIG_KEY = "Address";
    private static final String PEER_IP_CONFIG_KEY = "AllowedIPs";
    private static final String PUBLIC_KEY_CONFIG_KEY = "PublicKey";
    private static final String PRIVATE_KEY_CONFIG_KEY = "PrivateKey";
    private static final String ENDPOINT_CONFIG_KEY = "Endpoint";
    private static final String KEEP_ALIVE_CONFIG_KEY = "PersistentKeepalive";
    private static final String DNS_INTERFACE_KEY = "DNS";
    private static final String PEER_SECTION_KEY = "Peer";
    private static final String INTERFACE_SECTION_KEY = "Interface";


    @Value("${wg.config.path}")
    private String configPath;
    @Value("${wg.server.public.key}")
    private String serverPublicKey;
    @Value("${wg.server.ip}")
    private String serverIp;
    @Value("${wg.server.port}")
    private String serverPort;

    private IpGenerator ipGenerator;
    private WgManager wgManager;

    public WgUserCreator(IpGenerator ipGenerator, WgManager wgManager) {
        this.ipGenerator = ipGenerator;
        this.wgManager = wgManager;
    }

    @Override
    public CreateUserResult createUser(CreateUserRequest request) throws CreateUserException {


        WgKeys wgKeys = wgManager.generateKeys();

        String peerIp = editWgConfig(wgKeys);

        wgManager.restartWgDemon();

        //todo add description of new user to description storage (file or db - by interface)

        return generateUserResult(wgKeys.privateKey(), peerIp, request);
    }

    private String editWgConfig(WgKeys wgKeys) throws CreateUserException {
        String newPeerIp;
        IniParser iniParser = new IniParser();
        Map<String, List<IniSection>> configMap;
        File config = new File(configPath);
        try(Reader reader = new FileReader(config)) {
            configMap = iniParser.parseConfig(reader);

            newPeerIp = ipGenerator.getFreeIp(getUsedIps(configMap));

            IniSection peerSectionOfNewUser = new IniSection(PEER_SECTION_KEY);
            peerSectionOfNewUser.addParam(PUBLIC_KEY_CONFIG_KEY, wgKeys.publicKey());
            peerSectionOfNewUser.addParam(PEER_IP_CONFIG_KEY, newPeerIp);

            configMap.get(PEER_SECTION_KEY).add(peerSectionOfNewUser);

        } catch (Exception e){
            String errorMessage = "Error while read/modify wg config";
            LOGGER.error(errorMessage, e);
            throw new CreateUserException(errorMessage, e);
        }

        try(Writer writer = new FileWriter(config)) {
            iniParser.writeConfigToWriter(writer, configMap);
        } catch (IOException e) {
            String errorMessage = "Error while write config to file";
            LOGGER.error(errorMessage, e);
            throw new CreateUserException(errorMessage, e);
        }
        return newPeerIp;
    }

    private Set<String> getUsedIps(Map<String, List<IniSection>> config){
        Set<String> usedIps = new HashSet<>();
        for(Map.Entry<String, List<IniSection>> element : config.entrySet()){
            element.getValue().forEach(section -> {
                Map<String, String> configMap = section.getParamMap();
                if(configMap.containsKey(INTERFACE_IP_CONFIG_KEY)){
                    usedIps.add(configMap.get(INTERFACE_IP_CONFIG_KEY));
                }
                if(configMap.containsKey(PEER_IP_CONFIG_KEY)){
                    usedIps.add(configMap.get(PEER_IP_CONFIG_KEY));
                }
            });
        }

        return  usedIps;
    }

    private CreateUserResult generateUserResult(String privateKey, String peerIp, CreateUserRequest request) throws CreateUserException {

        UserDescription userDescription = new UserDescription(peerIp, request.name(), request.description());

        Map<String, List<IniSection>> configMap = new HashMap<>();

        IniSection interfaceSection = new IniSection(INTERFACE_SECTION_KEY);
        interfaceSection.addParam(PRIVATE_KEY_CONFIG_KEY, privateKey);
        interfaceSection.addParam(INTERFACE_IP_CONFIG_KEY, peerIp);
        interfaceSection.addParam(DNS_INTERFACE_KEY, "8.8.8.8");

        IniSection peerSection = new IniSection(PEER_SECTION_KEY);
        peerSection.addParam(PUBLIC_KEY_CONFIG_KEY, serverPublicKey);
        peerSection.addParam(ENDPOINT_CONFIG_KEY, String.format("%s:%s", serverIp, serverPort));
        peerSection.addParam(PEER_IP_CONFIG_KEY, "0.0.0.0/0");
        peerSection.addParam(KEEP_ALIVE_CONFIG_KEY, "20");

        configMap.put(INTERFACE_SECTION_KEY, List.of(interfaceSection));
        configMap.put(PEER_SECTION_KEY, List.of(peerSection));

        StringWriter result = new StringWriter();
        try {
            IniParser iniParser = new IniParser();
            iniParser.writeConfigToWriter(result, configMap);
        } catch (IOException e) {
            String errorMessage = "Write result error";
            LOGGER.error(errorMessage, e);
            throw new CreateUserException(errorMessage, e);
        }

        return new CreateUserResult(userDescription, result.toString());
    }


}
