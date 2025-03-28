package io.sanlam.bankaccountwithdrawal.configs;

/*
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientBuilder;

import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {

    @Value("${eventstore.host}")
    private String host;

    @Value("${eventstore.port}")
    private int port;

    @Value("${eventstore.username}")
    private String username;

    @Value("${eventstore.password}")
    private String password;

    @Value("${eventstore.secure}")
    private boolean secure;

    @Bean
    public EventStoreDBClient eventStoreDBClient() {

        EventStoreDBClientBuilder builder = EventStoreDBClient.builder();

        builder.singleNode(host, port);  // Connect to the EventStoreDB instance

        if (secure) {
            builder.secureConnection();
        }

        builder.credentials(username, password);  // Provide credentials for the EventStoreDB instance

        return builder.build();
    }
}
*/
