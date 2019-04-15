package vn.tungnt.research.studyspringretry.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.tungnt.research.studyspringretry.dto.GatewayStatusDTO;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayStatus;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayStatusRepository;
import vn.tungnt.research.studyspringretry.service.GatewayStatusImporter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

/**
 * @author java dev be team on 2019-04-14
 * @project study-spring-retry
 */
@Slf4j
@Service
class GatewayStatusImporterImpl implements GatewayStatusImporter {

    private final RestTemplate restClient;

    private final GatewayStatusRepository repository;

    private LinkedList<GatewayStatus> gwStatuses;

    public GatewayStatusImporterImpl(final RestTemplate restClient, final GatewayStatusRepository repository) {
        this.restClient = restClient;
        this.repository = repository;
        this.gwStatuses = new LinkedList<>();
    }

    @Async("gwImporterThreadPool")
    @Override
    public CompletableFuture<GatewayStatusDTO> importStatus(String gatewayId) {
        log.info("::: Collect percentage of gateway {} :::", gatewayId);
        String url = String.format("http://localhost:8181/gateway/%s/status", gatewayId);
        ResponseEntity<GatewayStatusDTO> entity = this.restClient.getForEntity(url, GatewayStatusDTO.class);
        GatewayStatusDTO body = entity.getBody();
        log.info("::: Status of gateway {} is {} :::", gatewayId, body.getStatus());
        this.gwStatuses.add(transformFrom(body, gatewayId));
        return CompletableFuture.completedFuture(body);
    }

    @Override
    public Collection<GatewayStatus> getStatusCache() {
        return this.gwStatuses;
    }

    public GatewayStatus transformFrom(GatewayStatusDTO dto, String gatewayId) {
        GatewayStatus entity = new GatewayStatus();
        entity.setGatewayId(gatewayId);
        entity.setImportedTime(new Timestamp(System.currentTimeMillis()));
        entity.setStatus(dto.getStatus());
        return entity;
    }

}

    