package knemognition.heartauth.orchestrator.tenants.app.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeyHasherTest {

    @Test
    void shouldGenerateStableSha256Hash() {
        KeyHasher hasher = new KeyHasher();

        String hash = hasher.handle("plain-text");

        assertThat(hash).hasSize(64);
        assertThat(hash).isEqualTo(hasher.handle("plain-text"));
    }
}
