package com.spring.task.gymcrm.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PasswordUtils {

    private PasswordUtils() {
    }

    private static final Random RANDOM = new Random();

    public static String generatePassword() {
        byte[] array = new byte[10];
        RANDOM.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
