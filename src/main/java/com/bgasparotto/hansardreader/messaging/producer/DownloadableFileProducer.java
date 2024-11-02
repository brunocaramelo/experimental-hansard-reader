package com.bgasparotto.hansardreader.messaging.producer;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadableFileProducer {
    private final KafkaTemplate<String, DownloadableFile> kafkaTemplate;

    @Value("${topics.output.downloadable-file}")
    private String topic;

    public void produce(DownloadableFile downloadableFile) {
        kafkaTemplate.send(topic, downloadableFile.getId(), downloadableFile);
        log.info("Produced message with downloadable file details: [{}]", downloadableFile);
    }
}
