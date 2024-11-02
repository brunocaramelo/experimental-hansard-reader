package com.bgasparotto.hansardreader.model.dto;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TestDtoFactory {

    public HansardFeed newHansardFeed(int numberOfHansardEntries) {
        List<HansardEntry> hansardEntries = newHansardEntries(numberOfHansardEntries);

        return HansardFeed.builder()
                .id("ID Test Hansard Feed")
                .title("Title Test Hansard Feed")
                .updated(Instant.now())
                .entries(hansardEntries)
                .build();
    }

    public List<HansardEntry> newHansardEntries(int numberOfHansardEntries) {
        return IntStream.range(0, numberOfHansardEntries)
                .mapToObj(i -> newHansardEntry())
                .collect(Collectors.toList());
    }

    public HansardEntry newHansardEntry() {
        HansardResourceLink link = newHansardResourceLink();

        return HansardEntry.builder()
                .id(link.getUri())
                .title(link.getUri())
                .updated(Instant.now())
                .link(link)
                .build();
    }

    public HansardResourceLink newHansardResourceLink() {
        String randomDummyUri = randomDummyUri();

        return HansardResourceLink.builder()
                .uri(randomDummyUri)
                .build();
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public String randomDummyUri() {
        return new StringBuilder()
                .append(UUID.randomUUID().toString())
                .append(".domain")
                .append("/")
                .append(UUID.randomUUID().toString())
                .append(".zip")
                .toString();
    }
}
