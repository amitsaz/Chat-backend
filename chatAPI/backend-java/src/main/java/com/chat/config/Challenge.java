package com.chat.config;

import com.chat.controller.MessageResource;
import com.chat.controller.UserResource;
import com.chat.exceptions.UserAlreadyExistsExceptionMapper;
import com.chat.exceptions.UserNotFoundException;
import com.chat.exceptions.UserNotFoundExceptionMapper;
import com.chat.service.ChatService;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.skife.jdbi.v2.DBI;
import com.chat.controller.TestResource;

/**
 * Main config file for the drop wizard powered API.
 */
public class Challenge extends Application<ChallengeConfiguration> {
    @Override
    public void run(ChallengeConfiguration conf, Environment env) {
        DBIFactory dbiFactory = new DBIFactory();
        DBI dbi = dbiFactory.build(env, conf.database, "mysql");
        env.jersey().register(new TestResource(dbi));
        env.jersey().register(new UserResource(dbi.onDemand(ChatService.class)));
        env.jersey().register(new MessageResource(dbi.onDemand(ChatService.class)));
        env.jersey().register(new UserAlreadyExistsExceptionMapper());
        env.jersey().register(new UserNotFoundExceptionMapper());
    }

    public static void main(String[] args) throws Exception {
        new Challenge().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChallengeConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<ChallengeConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ChallengeConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }
}
