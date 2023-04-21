package net.kodehawa.migrator;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static String decodeURL(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}