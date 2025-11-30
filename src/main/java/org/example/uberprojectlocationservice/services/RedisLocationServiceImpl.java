package org.example.uberprojectlocationservice.services;

import org.example.uberprojectlocationservice.dtos.DriverLocationDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisLocationServiceImpl implements LocationService {

    private static final String DRIVER_GEO_OPS_KEY = "drivers";
    private static final Double SEARCH_RADIUS = 5.0;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisLocationServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {

        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();   // it basically returns geooperation object that store in key value pair as point and key

        geoOps.add(DRIVER_GEO_OPS_KEY, new RedisGeoCommands.GeoLocation<>(driverId, new Point(longitude, latitude)));     // here point(long, lat) as param

        return true;
    }

    @Override
    public List<DriverLocationDto> getNearbyDrivers(Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
        Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);

        Circle within = new Circle(new Point(longitude,latitude), radius);

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(DRIVER_GEO_OPS_KEY, within);
        List<DriverLocationDto> drivers = new ArrayList<>();

        for(GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){
            List<Point> point = geoOps.position(DRIVER_GEO_OPS_KEY, result.getContent().getName().describeConstable().get());    // i need to create this because positions are not directly accessible in building DriverLocationDto below
            DriverLocationDto driverLocationDto = DriverLocationDto.builder()
                                                .driverId(result.getContent().getName())
                                                .latitude(point.get(0).getY())
                                                .longitude(point.get(0).getY())
                                                .build();
            drivers.add(driverLocationDto);

        }
        return drivers;
    }
}
