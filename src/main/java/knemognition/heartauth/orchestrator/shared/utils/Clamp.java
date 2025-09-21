package knemognition.heartauth.orchestrator.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Clamp {

    private static <T extends Comparable<? super T>> void requireRange(T min, T max) {
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("min must be ≤ max");
    }

    public static <T extends Comparable<? super T>> T clampOrDefault(
            T value, T min, T max, T defaultValue) {
        requireRange(min, max);
        T v = (value == null) ? defaultValue : value;
        if (v.compareTo(min) < 0) return min;
        if (v.compareTo(max) > 0) return max;
        return v;
    }

    public static int clampOrDefault(Integer value, int min, int max, int defaultValue) {
        if (min > max) throw new IllegalArgumentException("min must be ≤ max");
        int v = (value == null) ? defaultValue : value;
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    public static long clampOrDefault(Long value, long min, long max, long defaultValue) {
        if (min > max) throw new IllegalArgumentException("min must be ≤ max");
        long v = (value == null) ? defaultValue : value;
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}
