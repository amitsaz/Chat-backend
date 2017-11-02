package com.chat.controller;

import com.chat.config.Challenge;
import com.chat.exceptions.ErrorCodes;
import com.chat.model.Message;
import com.chat.model.MessageListWrapper;
import com.chat.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;


/**
 * Starts an instance of Jersey and reads from Test yaml files(can be configured for local env)
 * By default inserts two users so that the tests for message getting can work properly.
 * Had test methods for :
 * 1. Insert valid message
 * 2. Check bad message is not inserted.
 * 3. Get messages without pagination
 * 4. Get messages with pagination
 * 5. Test no messages returned if pagination params are not set properly.
 */
public class MessageResourceTest {

    @ClassRule
    public static final DropwizardAppRule APP_RULE =
            new DropwizardAppRule<>(Challenge.class, ResourceHelpers.resourceFilePath("test-app.yaml"));

    public static Client client = null ;

    public static User user1 = new User("user111", "1q2w3e4r");

    public static User user2 = new User("user222", "1q2w3e4r");

    public Message goodMessage= new Message("user111:user222","user111","user222",null,"text"
                                            ,"Message from user 111 to user 222",null);

    public Message badMessage= new Message("user111:user222","userBlahhh","user222",null,"text"
            ,"Should not insert since 'sender' is not in system",null);

    @BeforeClass
    public static void insertUsers() {
        final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
        configuration.setTimeout(Duration.minutes(5L));
         client = new JerseyClientBuilder(APP_RULE.getEnvironment()).using(configuration).build(
                "test");
        
         client.target(String.format("http://localhost:%d/users", APP_RULE.getLocalPort()))
                .request().post(Entity.entity(user1, MediaType.APPLICATION_JSON_TYPE));

         client.target(String.format("http://localhost:%d/users", APP_RULE.getLocalPort()))
                .request().post(Entity.entity(user2, MediaType.APPLICATION_JSON_TYPE));


    }

    @Test
    public void insertGoodMessage()  {

        Response eventResponse=client.target(String.format("http://localhost:%d/message", APP_RULE.getLocalPort()))
                .request().post(Entity.entity(goodMessage, MediaType.APPLICATION_JSON_TYPE));

        assertTrue(eventResponse.getStatus() == HttpStatus.SC_OK );



    }


    @Test
    public void insertBadMessage() throws IOException {

        Response eventResponse=client.target(String.format("http://localhost:%d/message", APP_RULE.getLocalPort()))
                .request().post(Entity.entity(badMessage, MediaType.APPLICATION_JSON_TYPE));

        String responsetext= eventResponse.readEntity(String.class);

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, String> map = reader.readValue(responsetext);


        assertTrue(map.get("message").equals(ErrorCodes.USER_NOT_FOUND.getMessage()) );
        assertTrue (eventResponse.getStatus()==HttpStatus.SC_BAD_REQUEST);




    }

    @Test
    public void getMessage()  {

        Response eventResponse=client.target(String.format("http://localhost:%d/message?user1=user111&user2=user222",
                APP_RULE.getLocalPort()))
                .request().get();

        MessageListWrapper response= eventResponse.readEntity(MessageListWrapper.class);

        assertTrue(response.getTotalMessages()>0);

        assertTrue (eventResponse.getStatus()==HttpStatus.SC_OK);


    }


    @Test
    public void getMessagesWithPagination(){

        Response eventResponse=client.target(String.format("http://localhost:%d/message?user1=user111&user2=user222&pagenum=1&pagesize=3",
                APP_RULE.getLocalPort()))
                .request().get();

        MessageListWrapper response= eventResponse.readEntity(MessageListWrapper.class);

        assertTrue(response.getTotalMessages()>0);

        assertTrue (eventResponse.getStatus()==HttpStatus.SC_OK);


    }

    @Test
    public void getMessagesWithMissingParams(){

        Response eventResponse=client.target(String.format("http://localhost:%d/message?user1=user111&user2=user222&pagesize=3",
                APP_RULE.getLocalPort()))
                .request().get();

        MessageListWrapper response= eventResponse.readEntity(MessageListWrapper.class);

        assertTrue(response.getTotalMessages()==0);

        assertTrue (eventResponse.getStatus()==HttpStatus.SC_OK);


    }
}