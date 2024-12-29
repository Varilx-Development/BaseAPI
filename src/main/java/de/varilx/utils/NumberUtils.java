package de.varilx.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {

    public boolean isMultipleOf(int number, int divisor) {
        return number % divisor == 0;
    }

    public boolean isMultipleOf(long number, int divisor) {
        return number % divisor == 0;
    }

    public boolean isNumeric(String input) {
        return input.matches("-?\\d+");
    }

    public boolean isDouble(String input) {
        return input.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Checks whether a given number is a whole integer or has a decimal part of 0.5.
     *
     * @param number the number to be checked.
     *
     * @return true if the decimal part of the number is 0.0 or 0.5, false otherwise.
     */
    public static boolean checkDouble(double number) {
        double decimalPart = number - (int) number;
        return decimalPart == 0.0 || decimalPart == 0.5;
    }

    public String formatDouble(double value) {
        String[] suffix = new String[]{"", "k", "m", "b", "t"};
        int size = (value != 0) ? (int) Math.log10(Math.abs(value)) : 0;
        return String.format("%.2f", value / Math.pow(10, (size / 3) * 3)) + suffix[size / 3];
    }

    public String formatLong(long balance) {
        String[] suffix = new String[]{"", "k", "m", "b", "t"};
        int size = (balance != 0) ? (int) Math.log10(Math.abs(balance)) : 0;
        return String.format("%.2f", balance / Math.pow(10, (size / 3) * 3)) + suffix[size / 3];
    }

    public int parseNumberFromString(String s) {
        int factor = 1;
        if (s.endsWith("m")) {
            factor = 1000000;
        } else if (s.endsWith("k")) {
            factor = 1000;
        }
        String numberPart = s.replaceAll("[mk]", "");
        return Integer.parseInt(numberPart)*factor;
    }

    public boolean isShortened(String s) {
        return s.endsWith("k") || s.endsWith("m");
    }

}
