package com.mohamedabdelrazek.bloggerapp;

import java.util.Random;

/**
 * Created by Mohamed on 10/21/2017.
 */

public class Utils {
    public static String getRandomName() {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * ALPHABET.length());
            stringBuilder.append(ALPHABET.charAt(index));
        }
        String saltStr = stringBuilder.toString();
        return saltStr;

    }
}
