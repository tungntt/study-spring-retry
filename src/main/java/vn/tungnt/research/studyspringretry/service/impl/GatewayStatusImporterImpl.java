package vn.tungnt.research.studyspringretry.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tungnt.research.studyspringretry.dto.GatewayStatusDTO;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayStatus;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayStatusRepository;
import vn.tungnt.research.studyspringretry.service.DataProvider;
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

    private final DataProvider dataProvider;

    private final GatewayStatusRepository repository;

    private LinkedList<GatewayStatus> gwStatuses;

    public GatewayStatusImporterImpl(final DataProvider dataProvider, final GatewayStatusRepository repository) {
        this.dataProvider = dataProvider;
        this.repository = repository;
        this.gwStatuses = new LinkedList<>();
    }

    @Override
    public CompletableFuture<Void> importStatus(String gatewayId) {
       return this.dataProvider.checkGwStatus(gatewayId).thenAccept( (body) -> {
           log.info("::: Status of gateway {} is {} :::", gatewayId, body.getStatus());
           this.gwStatuses.add(transformFrom(body, gatewayId));
       });
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

    