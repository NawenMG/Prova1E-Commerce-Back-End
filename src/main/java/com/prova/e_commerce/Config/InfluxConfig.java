package com.prova.e_commerce.Config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {

    @Value("${influxdb.token}")
    private String influxToken;  // Token che hai ricevuto

    @Value("${influxdb.url}")
    private String influxUrl;

    @Value("${influxdb.org}")
    private String influxOrg;

    @Value("${influxdb.bucket}")
    private String influxBucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        // Creazione del client InfluxDB
        return InfluxDBClientFactory.create(
                influxUrl,  // URL di InfluxDB
                influxToken.toCharArray(),  // Token
                influxOrg,  // Organization
                influxBucket  // Bucket
        );
    }
}
