package com.chat.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Reads config from appl.yaml file for initializing data sources
 * and other setting.
 */
class ChallengeConfiguration extends Configuration {

    public DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;
}
