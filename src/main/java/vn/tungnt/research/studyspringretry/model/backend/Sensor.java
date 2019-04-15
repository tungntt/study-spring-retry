package vn.tungnt.research.studyspringretry.model.backend;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author java dev be team on 2019-04-13
 * @project study-spring-retry
 */
@Data
@Entity
@Table(name = "sensor")
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_id")
    private String sensorId;
}

    