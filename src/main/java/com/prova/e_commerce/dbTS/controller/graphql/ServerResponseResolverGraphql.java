package com.prova.e_commerce.dbTS.controller.graphql;

import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.service.ServerResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class ServerResponseResolverGraphql {

    @Autowired
    private ServerResponseService serverResponseService;

    @QueryMapping
    public List<ServerResponse> findServerResponsesByTimeRange(@Argument String startTime, @Argument String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        return serverResponseService.findByTimeRange(start, end);
    }

    @QueryMapping
    public List<ServerResponse> findServerResponsesByServer(@Argument String server) {
        return serverResponseService.findByServer(server);
    }

    @QueryMapping
    public List<ServerResponse> findServerResponsesByEndpoint(@Argument String endpoint) {
        return serverResponseService.findByEndpoint(endpoint);
    }

    @QueryMapping
    public List<Map.Entry<String, Double>> getAverageResponseTimeByServer() {
        return serverResponseService.getAverageResponseTimeByServer().entrySet().stream().toList();
    }

    @QueryMapping
    public List<ServerResponse> findServerResponsesByServerEndpointAndTimeRange(
            @Argument String server,
            @Argument String endpoint,
            @Argument String startTime,
            @Argument String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        return serverResponseService.findByServerEndpointAndTimeRange(server, endpoint, start, end);
    }
}
