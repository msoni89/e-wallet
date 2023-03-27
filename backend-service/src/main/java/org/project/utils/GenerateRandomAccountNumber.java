package org.project.utils;

import java.util.Random;
import java.util.StringJoiner;

public class GenerateRandomAccountNumber {
    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    private static final String BANK_CODE = "BNK-K&N-";

    public static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(BANK_CODE);
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            sb.append(String.valueOf(digit));
        }
        return sb.toString();
    }
}
