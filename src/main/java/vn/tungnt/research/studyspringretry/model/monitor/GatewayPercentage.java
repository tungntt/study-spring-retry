package vn.tungnt.research.studyspringretry.model.monitor;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author java dev be team on 2019-04-13
 * @project study-spring-retry
 */
@Data
@Entity
@Table(name = "gateway_percentage", indexes = {@Index(name = "index_gateway_id", columnList = "gateway_id", unique = true)})
public class GatewayPercentage implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gateway_id")
    private String gatewayId;

    @Column(name = "created_time")
    private Timestamp createdTime;

}

    