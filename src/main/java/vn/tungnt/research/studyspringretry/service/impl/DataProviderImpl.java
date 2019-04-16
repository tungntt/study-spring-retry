package vn.tungnt.research.studyspringretry.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.tungnt.research.studyspringretry.dto.GatewayStatusDTO;
import vn.tungnt.research.studyspringretry.service.DataProvider;

import java.util.concurrent.CompletableFuture;

/**
 * @author java dev be team on 2019-04-16
 * @project study-spring-retry
 */
@Slf4j
@Service
public class DataProviderImpl implements DataProvider {

    private final RestTemplate restClient;

    public DataProviderImpl(RestTemplate restClient) {
        this.restClient = restClient;
    }

    @Async("gwImporterThreadPool")
    @Override
    public CompletableFuture<GatewayStatusDTO> checkGwStatus(String gatewayId) {
        log.info("::: Collect percentage of gateway {} :::", gatewayId);
        String url = String.format("http://localhost:8181/gateway/%s/status", gatewayId);
        ResponseEntity<GatewayStatusDTO> entity = this.restClient.getForEntity(url, GatewayStatusDTO.class);
        return CompletableFuture.completedFuture(entity.getBody());
    }
}

    