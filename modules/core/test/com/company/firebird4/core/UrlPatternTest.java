package com.company.firebird4.core;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class UrlPatternTest {

    Pattern pattern = Pattern.compile("jdbc:firebirdsql://([^:/\\s]+:?\\d*)/([^?]+)(\\?.*)?");

    @Test
    public void testHost() {
        Matcher m = pattern.matcher("jdbc:firebirdsql://localhost/C:/users/budarov/fbdb/sample.fdb?encoding=UTF8");
        assertTrue(m.matches());
        assertEquals("localhost", m.group(1));
        assertEquals("C:/users/budarov/fbdb/sample.fdb", m.group(2));
        assertEquals("?encoding=UTF8", m.group(3));
    }

    @Test
    public void testLinuxAndPort() {
        Matcher m = pattern.matcher("jdbc:firebirdsql://proddb:3090//home/budarov/fbdb/sample.fdb?encoding=UTF8&super=true");
        assertTrue(m.matches());
        assertEquals("proddb:3090", m.group(1));
        assertEquals("/home/budarov/fbdb/sample.fdb", m.group(2));
        assertEquals("?encoding=UTF8&super=true", m.group(3));
    }

    @Test
    public void testNoParams() {
        Matcher m = pattern.matcher("jdbc:firebirdsql://firebird/C:/user/sample.fdb");
        assertTrue(m.matches());
        assertEquals("firebird", m.group(1));
        assertEquals("C:/user/sample.fdb", m.group(2));
        assertNull(m.group(3));
    }

}
