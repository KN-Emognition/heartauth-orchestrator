package knemognition.heartauth.orchestrator.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Clamp {

    public static <T extends Comparable<T>> T withinOrDefault(
            T value, T min, T max, T defaultValue) {

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be ≤ max");
        }
        if (value == null) return defaultValue;
        return (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)
                ? value
                : defaultValue;
    }

    public static long withinOrDefault(Long value, long min, long max, long defaultValue) {
        if (min > max) throw new IllegalArgumentException("min must be ≤ max");
        if (value == null) return defaultValue;
        long v = value;
        return (v >= min && v <= max) ? v : defaultValue;
    }

    public static int withinOrDefault(Integer value, int min, int max, int defaultValue) {
        if (min > max) throw new IllegalArgumentException("min must be ≤ max");
        if (value == null) return defaultValue;
        int v = value;
        return (v >= min && v <= max) ? v : defaultValue;
    }
}
