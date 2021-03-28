package com.n26.statistics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @GetMapping("/statistics")
    public ResponseEntity getStatistics() {

        return ResponseEntity
                .ok()
                .build();
    }
}
