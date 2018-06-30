package com.appirio.api.member.tests;

import org.testng.annotations.BeforeClass;

import com.appirio.api.member.model.User;
import com.appirio.automation.api.config.AuthenticationConfiguration;
import com.appirio.automation.api.config.EnvironmentConfiguration;
import com.appirio.automation.api.config.UserConfiguration;
import com.appirio.automation.api.model.AuthenticationInfo;
import com.appirio.automation.api.service.AuthenticationService;
import com.appirio.automation.api.service.UserService;


public class UserLogin {
    static AuthenticationService service = null;
    static UserService userService = null;
    static AuthenticationInfo authInfo;

   static {
        try {
        	System.out.println("******* UserLogin setup ");
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the test environment.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        initializeConfiguration();
        initializeService();
    }

    /**
     * Initialize Configuration so that configuration properties are accessible by test cases
     */
    private static void initializeConfiguration() {
        EnvironmentConfiguration.initialize();
        AuthenticationConfiguration.initialize();
        UserConfiguration.initialize();
    }

    /**
     * Initialize shared automation library services
     */
    private static void initializeService() {
        service = new AuthenticationService();
        userService = new UserService();
    }


    public static String login(User u) throws Exception {
        return service.getV3JWTToken(u.getUsername(), u.getPassword());
    }
}