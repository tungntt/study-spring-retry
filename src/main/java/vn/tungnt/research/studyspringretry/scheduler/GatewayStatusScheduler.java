package vn.tungnt.research.studyspringretry.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayStatus;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayStatusRepository;
import vn.tungnt.research.studyspringretry.service.GatewayStatusImporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author java dev be team on 2019-04-14
 * @project study-spring-retry
 */
@Slf4j
@Component
@EnableScheduling
public class GatewayStatusScheduler {

    private GatewayStatusRepository repository;

    private GatewayStatusImporter importer;

    public GatewayStatusScheduler(GatewayStatusRepository repository, GatewayStatusImporter importer) {
        this.repository = repository;
        this.importer = importer;
    }

    @Scheduled(fixedDelay = 60000 * 5)
    public void scheduleImportGwStatus(){
        log.info("******** BEGIN TO IMPORT GW STATUS ********");
        List<String> fakeGwId = this.getFakeGwId(9);
        CompletableFuture<Void>[] completableTasks = new CompletableFuture[fakeGwId.size()];
        for(int i = 0; i < fakeGwId.size(); i++) {
            CompletableFuture<Void> future = this.importer.importStatus(fakeGwId.get(i));
            completableTasks[i] = future;
        }

        CompletableFuture.allOf(completableTasks).join();
        Collection<GatewayStatus> statusCache = this.importer.getStatusCache();
        this.repository.save(statusCache);
        log.info("******** IMPORT DONE ********");
    }

    private List<String> getFakeGwId(int numb) {
        List<String> gwIds = new ArrayList<>();
        for (int i = 0; i < numb; i++){
            gwIds.add(String.format("gw-%s", i));
        }
        return gwIds;
    }
}

    