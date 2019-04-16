package vn.tungnt.research.studyspringretry.service;

import vn.tungnt.research.studyspringretry.dto.GatewayStatusDTO;

import java.util.concurrent.CompletableFuture;

/**
 * @author java dev be team on 2019-04-16
 * @project study-spring-retry
 */
public interface DataProvider {

    CompletableFuture<GatewayStatusDTO> checkGwStatus(String gatewayId);
}
