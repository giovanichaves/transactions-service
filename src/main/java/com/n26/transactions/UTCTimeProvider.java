package com.n26.transactions;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;

@Component
public class UTCTimeProvider {

    public ZonedDateTime now() {
        return ZonedDateTime.now(Clock.systemUTC());
    }

}
