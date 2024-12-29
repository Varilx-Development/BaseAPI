package de.varilx.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MathUtils {

    Random random = new Random();
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

    public String formatNumber(long number) {
        return numberFormat.format(number);
    }

    public String formatNumber(double number) {
        return numberFormat.format(number);
    }

    public String shortenDecimal(Long value) {
        if (value >= 1_000 && value < 1_000_000) {
            return (value / 1_000) + "k";
        } else if (value >= 1_000_000 && value < 1_000_000_000) {
            return (value / 1_000_000) + "M";
        } else if (value >= 1_000_000_000)
            return (value / 1_000_000_000) + "b";
        return String.valueOf(value);
    }

    public boolean isDouble(String doubleString) {
        try {
            double value = Double.parseDouble(doubleString);
            return value >= 0;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isInteger(String integerString, boolean withZero) {
        if (!withZero && integerString.startsWith("0")) {
            return false;
        }
        try {
            int value = Integer.parseInt(integerString);
            return value >= (withZero ? 0 : 1);
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isLong(String longString, boolean withZero) {
        if (!withZero && longString.startsWith("0")) {
            return false;
        }
        try {
            long value = Long.parseLong(longString);
            return value >= (withZero ? 0 : 1);
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public TimeUnit getTimeUnitByName(String timeUnitString) {
        for (TimeUnit timeUnit : TimeUnit.values()) {
            if (timeUnit.name().equalsIgnoreCase(timeUnitString)) {
                return timeUnit;
            }
        }
        return null;
    }

}
