package vn.tungnt.research.studyspringretry.model.monitor;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author java dev be team on 2019-04-15
 * @project study-spring-retry
 */
@Data
@Entity
@Table(name = "gateway_status")
public class GatewayStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gateway_id")
    private String gatewayId;

    private String status;

    @Column(name = "imported_time")
    private Timestamp importedTime;
}

    