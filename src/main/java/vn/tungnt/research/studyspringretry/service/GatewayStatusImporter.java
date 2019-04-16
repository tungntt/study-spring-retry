package vn.tungnt.research.studyspringretry.service;

import vn.tungnt.research.studyspringretry.model.monitor.GatewayStatus;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author java dev be team on 2019-04-14
 * @project study-spring-retry
 */
public interface GatewayStatusImporter {

    CompletableFuture<Void> importStatus(String gatewayId);

    Collection<GatewayStatus> getStatusCache();
}

    