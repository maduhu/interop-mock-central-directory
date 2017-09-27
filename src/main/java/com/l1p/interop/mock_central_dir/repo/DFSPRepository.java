package com.l1p.interop.mock_central_dir.repo;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.apache.commons.lang.CharEncoding.UTF_8;

public class DFSPRepository {

    private String fileLocation;

    private static final Logger log = LoggerFactory.getLogger(DFSPRepository.class);

    public DFSPRepository() {
        this.fileLocation = System.getProperty("user.home")+"/dfsp_db.csv";
    }

    /**
     *
     * @param data
     * @return
     */
    public String addDFSP(String data) {
        log.info("AddDFSP Data: {}",data);
        JsonObject jsonObject = Json.createReader(new StringReader(data)).readObject();

        String result = Json.createObjectBuilder()
                            .add("name",jsonObject.getString("name"))
                            .add("shortName",jsonObject.getString("shortName"))
                            .add("providerUrl",jsonObject.getString("providerUrl"))
                            .add("key",jsonObject.getString("shortName"))
                            .add("secret",jsonObject.getString("shortName"))
                            .build()
                            .toString();

        try {
            Files.write(Paths.get(fileLocation), (jsonObject.getString("shortName")+"="+result + System.lineSeparator()).getBytes(UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception e){
            return getError("2345", "Error adding dfsp", "stack trace", ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    /**
     *
     * @param dfspId
     * @return
     * @throws IOException
     */
    public String getDFSP(String dfspId) throws IOException {
        return  Files.lines(new File(fileLocation).toPath())
                .map(line -> Arrays.asList(line.split("=")))
                .filter(list -> list.get(0).equalsIgnoreCase(dfspId))
                .collect(Collectors.toList())
                .get(0).get(1);
    }

    public String getError(String errorCode, String errorDescription, String key, String value) {
        JsonArrayBuilder keyValueBuilder = Json.createArrayBuilder().add(Json.createObjectBuilder().add("key", key).add("value", value));

        String errorResponse = Json.createObjectBuilder()
                .add("errorInformation", Json.createObjectBuilder().add("errorCode", errorCode)
                        .add("errorDescription", errorDescription).add("extensionList", keyValueBuilder))
                .build().toString();
        return errorResponse;
    }
}
