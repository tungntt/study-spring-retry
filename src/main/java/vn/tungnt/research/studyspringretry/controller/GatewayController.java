package vn.tungnt.research.studyspringretry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vn.tungnt.research.studyspringretry.model.backend.Sensor;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayPercentage;
import vn.tungnt.research.studyspringretry.repository.backend.SensorRepository;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayPercentageRepository;

/**
 * @author java dev be team on 2019-04-13
 * @project study-spring-retry
 */
@RestController
public class GatewayController {

    @Autowired
    GatewayPercentageRepository repository;

    @Autowired
    SensorRepository sensorRepository;

    @GetMapping("/test/{mode}")
    public void test(@PathVariable("mode") String mode) {
        if (mode.equals("sensor")) {
            Sensor entity = new Sensor();
            entity.setId(new Long(1));
            entity.setSensorId("sensor0001");
            sensorRepository.save(entity);
        }
        GatewayPercentage entity = new GatewayPercentage();
        entity.setGatewayId("gw0001");

        repository.save(entity);
    }

}

    