package com.bgasparotto.hansardreader.service.adapter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InstantAdapterTest {
    private static InstantAdapter instantAdapter;

    @BeforeAll
    public static void init() {
        instantAdapter = new InstantAdapter();
    }

    @Test
    public void shouldUnmarshalDateToInstant() {
        Date input = new Date();
        Instant result = instantAdapter.unmarshal(input);

        assertThat(result).isEqualTo(input.toInstant());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldThrowNpeWhenUnmarshallingNullDate() {
        assertThatThrownBy(() -> instantAdapter.unmarshal(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldMarshalInstantToDate() {
        Instant input = Instant.now();
        Date result = instantAdapter.marshal(input);

        assertThat(result).isEqualTo(Date.from(input));
    }

    @Test
    public void shouldThrowNpeWhenMarshallingNullInstant() {
        assertThatThrownBy(() -> instantAdapter.marshal(null))
                .isInstanceOf(NullPointerException.class);
    }
}
