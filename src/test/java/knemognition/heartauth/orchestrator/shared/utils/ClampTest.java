package knemognition.heartauth.orchestrator.shared.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClampTest {

    @Test
    void shouldClampIntegers() {
        Integer value = 5;
        assertThat(Clamp.clampOrDefault(value, 1, 10, 7)).isEqualTo(5);
        assertThat(Clamp.clampOrDefault(Integer.valueOf(-5), 1, 10, 7)).isEqualTo(1);
        assertThat(Clamp.clampOrDefault(Integer.valueOf(20), 1, 10, 7)).isEqualTo(10);
        assertThat(Clamp.clampOrDefault((Integer) null, 1, 10, 7)).isEqualTo(7);
    }

    @Test
    void shouldClampLongs() {
        Long value = 5L;
        assertThat(Clamp.clampOrDefault(value, 0L, 3L, 2L)).isEqualTo(3L);
        assertThat(Clamp.clampOrDefault(Long.valueOf(-1), 0L, 3L, 2L)).isEqualTo(0L);
        assertThat(Clamp.clampOrDefault((Long) null, 0L, 3L, 2L)).isEqualTo(2L);
    }

    @Test
    void shouldClampComparables() {
        assertThat(Clamp.clampOrDefault("m", "a", "z", "d")).isEqualTo("m");
        assertThat(Clamp.clampOrDefault("A", "a", "z", "d")).isEqualTo("a");
        assertThat(Clamp.clampOrDefault("zz", "a", "z", "d")).isEqualTo("z");
        assertThat(Clamp.clampOrDefault(null, "a", "z", "d")).isEqualTo("d");
    }
}
