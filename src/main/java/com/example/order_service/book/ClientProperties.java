package com.example.order_service.book;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "polar")
public class ClientProperties {
    @NotNull
    URI catalogServiceUrl;

    public URI catalogServiceUrl() {
        return catalogServiceUrl;
    }

    public void setCatalogServiceUrl(URI catalogServiceUrl) {
        this.catalogServiceUrl = catalogServiceUrl;
    }
}
