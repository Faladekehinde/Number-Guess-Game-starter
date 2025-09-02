package com.studentapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberUtilsTest {

    @Test
    void validRange() {
        assertTrue(NumberUtils.isValidGuess(1));
        assertTrue(NumberUtils.isValidGuess(10));
        assertFalse(NumberUtils.isValidGuess(0));
        assertFalse(NumberUtils.isValidGuess(11));
    }

    @Test
    void compareLogic() {
        assertEquals("equal", NumberUtils.compare(5,5));
        assertEquals("low", NumberUtils.compare(3,5));
        assertEquals("high", NumberUtils.compare(7,5));
    }
}
