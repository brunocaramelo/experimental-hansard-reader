package com.brunocaramelo.hansardreader.messaging.consumer;

import com.brunocaramelo.hansardreader.model.dto.HansardFeed;
import com.brunocaramelo.hansardreader.service.ApiReaderService;
import com.brunocaramelo.hansardreader.service.EntryService;
import com.brunocaramelo.scheduler.message.RunUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunUpdateConsumer {
    private final ApiReaderService apiReaderService;
    private final EntryService entryService;

    @KafkaListener(topics = "${topics.input.run-hansard-update}")
    public void consume(ConsumerRecord<String, RunUpdate> record) {
        log.info("Received command for updating Hansard data: [{}]", record.value());

        HansardFeed hansardFeed = apiReaderService.queryFeed();
        entryService.publishDownloadableEntries(hansardFeed);
    }
}
