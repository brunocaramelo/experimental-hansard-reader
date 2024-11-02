package com.bgasparotto.hansardreader.service;

import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiReaderService {
    private final RestTemplate restTemplate;

    @Value("${external-api.hansard.uri}")
    private String uri;

    @Value("${external-api.hansard.feed.path}")
    private String feedPath;

    @Value("${external-api.hansard.feed.dataset}")
    private int feedDataset;

    @Value("${external-api.hansard.feed.take}")
    private String feedTake;

    public HansardFeed queryFeed() {
        log.info("Querying the Hansard API for the feed resources list...");
        URI hansardUri = UriComponentsBuilder.fromUriString(uri)
                .path(feedPath)
                .queryParam("dataset", feedDataset)
                .queryParam("take", feedTake)
                .build()
                .toUri();

        return restTemplate.getForObject(hansardUri, HansardFeed.class);
    }
}
