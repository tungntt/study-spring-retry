package vn.tungnt.research.studyspringretry.repository.monitor;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayStatus;

/**
 * @author java dev be team on 2019-04-15
 * @project study-spring-retry
 */
public interface GatewayStatusRepository extends JpaRepository<GatewayStatus, Long> {
}

    