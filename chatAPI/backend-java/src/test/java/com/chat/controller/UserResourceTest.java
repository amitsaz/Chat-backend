package com.chat.controller;

import com.chat.config.Challenge;
import com.chat.model.User;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.apache.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Starts an instance of Jersey and reads from Test yaml files(can be configured for local env)
 * Inserts a User into DB .
 * Insert either succeeds with Status 200 (for new user) or Status 400 (User already exists)
 */
public class UserResourceTest {

    @ClassRule
    public static final DropwizardAppRule APP_RULE =
            new DropwizardAppRule<>(Challenge.class, ResourceHelpers.resourceFilePath("test-app.yaml"));
    public User user = new User("user999", "1q2w3e4r");

    @Test
    public void insertUser() {
        final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
        configuration.setTimeToLive(Duration.seconds(5L));
        final Client client = new JerseyClientBuilder(APP_RULE.getEnvironment()).using(configuration).build(
                "test");
        final Response eventResponse = client.target(String.format("http://localhost:%d/users", APP_RULE.getLocalPort()))
                .request().post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));

        assertTrue(eventResponse.getStatus() == HttpStatus.SC_OK || eventResponse.getStatus() == HttpStatus.SC_BAD_REQUEST);

    }
}