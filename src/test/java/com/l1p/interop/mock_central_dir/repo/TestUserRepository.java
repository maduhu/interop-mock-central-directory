package com.l1p.interop.mock_central_dir.repo;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestUserRepository extends TestCase{

    private static final Logger log = LoggerFactory.getLogger(TestUserRepository.class);

    @Test
    public void testAddUser() throws IOException {
        UserRepository userRepository = new UserRepository();
        String response = userRepository.addUser("{\"identifier\":\"tel:123456789\"}","Basic ZGZzcDE6ZGZzcDE=");
        assertNotNull(response);
        log.info("AddUser response: {}",response);
    }

    @Test
    public void testGetUser() throws IOException {
        UserRepository userRepository = new UserRepository();
        String response = userRepository.getUser("tel:123456789");
        assertNotNull(response);
        log.info("GetUser response: {}",response);
    }
}
