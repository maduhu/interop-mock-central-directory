package com.l1p.interop.mock_central_dir.repo;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestDFSPDirectory extends TestCase{

    private static final Logger log = LoggerFactory.getLogger(TestDFSPDirectory.class);

    @Test
    public void testAddDFSP() {
        DFSPRepository dfspRepository = new DFSPRepository();
        String response = dfspRepository.addDFSP("{\"name\": \"dfsp1\",\"shortName\": \"dfsp1\",\"providerUrl\": \"http://10.0.15.11:8088/scheme/adapter/v1\"}");
        assertNotNull(response);
        log.info("Response: "+response);
    }

    public void testGetDFSP() throws IOException {
        DFSPRepository dfspRepository = new DFSPRepository();
        String response = dfspRepository.getDFSP("dfsp1");
        assertNotNull(response);
        log.info("Response: "+response);
    }
}
