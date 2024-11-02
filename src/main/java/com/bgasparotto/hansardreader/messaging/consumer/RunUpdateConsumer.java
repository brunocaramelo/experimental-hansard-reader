package com.bgasparotto.hansardreader.messaging.consumer;

import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import com.bgasparotto.hansardreader.service.ApiReaderService;
import com.bgasparotto.hansardreader.service.EntryService;
import com.bgasparotto.scheduler.message.RunUpdate;
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
