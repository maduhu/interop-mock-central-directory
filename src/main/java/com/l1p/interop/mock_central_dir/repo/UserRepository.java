package com.l1p.interop.mock_central_dir.repo;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.mule.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.apache.commons.lang.CharEncoding.UTF_8;

public class UserRepository {

    private String fileLocation;

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository() {
        this.fileLocation = System.getProperty("user.home") + "/users_db.csv";
    }

    /**
     *
     * @param data
     * @param auth
     * @return
     * @throws IOException
     */
    public String addUser(String data, String auth) throws IOException {

        log.info("Data: {} Auth: {}",data,auth);
        String base64Credentials = auth.substring("Basic".length()).trim();
        String credentials = new String(Base64.decode(base64Credentials), Charset.forName(Base64.PREFERRED_ENCODING));

        JsonObject dataObject = Json.createReader(new StringReader(data)).readObject();
        String identifier = dataObject.getString("identifier");
        boolean primary = dataObject.containsKey("primary");

        DFSPRepository dfspRepository = new DFSPRepository();
        String dfspData = dfspRepository.getDFSP(credentials.split(":")[0]);
        JsonObject dfspObject = Json.createReader(new StringReader(dfspData)).readObject();

        String result = Json.createObjectBuilder()
                .add("name", dfspObject.getString("name"))
                .add("shortName", dfspObject.getString("shortName"))
                .add("providerUrl", dfspObject.getString("providerUrl"))
                .add("primary", String.valueOf(primary))
                .add("registered", String.valueOf(primary))
                .build()
                .toString();

        try {
            Files.write(Paths.get(fileLocation), (identifier + "=" + result + System.lineSeparator()).getBytes(UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            log.error("Error adding user", "stack trace", ExceptionUtils.getFullStackTrace(e));
            return null;
        }

        return result;

    }

    /**
     *
     * @param identifier
     * @return
     * @throws IOException
     */
    public String getUser(String identifier) throws IOException {
        JsonArrayBuilder responseBuilder = Json.createArrayBuilder();
        StringBuffer sb = new StringBuffer();
        Files.lines(new File(fileLocation).toPath())
                .map(line -> Arrays.asList(line.split("=")))
                .filter(list -> list.get(0).equalsIgnoreCase(identifier))
                .map(list -> list.get(1))
                .collect(Collectors.toList())
                //.forEach(data -> responseBuilder.add(data));
                .forEach(data -> sb.append(data).append(","));

        return "["+sb.toString().substring(0,sb.length()-1)+"]";

    }
}
