package net.fredrikmeyer.logit.controllers;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HelpersTest {

    @Test
    void getLocalDateTime() {
        var res = Helpers.getLocalDateTime("2013-11-02");
        assertEquals(res, LocalDateTime.of(2013,11,2,0,0));
    }
}