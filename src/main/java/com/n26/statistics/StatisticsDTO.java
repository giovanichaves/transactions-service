package com.n26.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class StatisticsDTO {
    private String sum;
    private String avg;
    private String max;
    private String min;
    private long count;
}
