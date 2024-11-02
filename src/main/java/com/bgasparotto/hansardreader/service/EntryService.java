package com.bgasparotto.hansardreader.service;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import com.bgasparotto.hansardreader.messaging.producer.DownloadableFileProducer;
import com.bgasparotto.hansardreader.model.dto.HansardEntry;
import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import com.bgasparotto.hansardreader.service.validator.EntryValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryValidation entryValidation;
    private final DownloadableFileProducer downloadableFileProducer;

    public void publishDownloadableEntries(HansardFeed hansardFeed) {
        log.info("Breaking-down Hansard feed entries into downloadable files...");
        List<HansardEntry> entries = hansardFeed.getEntries();
        if (Objects.isNull(entries) || entries.isEmpty()) {
            log.warn("No Hansard feed entries found.");
            return;
        }

        entries.stream()
                .map(entryValidation::filterOutInvalid)
                .flatMap(Optional::stream)
                .forEach(this::publishDownloadableEntry);
    }

    private void publishDownloadableEntry(HansardEntry hansardEntry) {
        DownloadableFile downloadableFile = DownloadableFile.newBuilder()
                .setId(hansardEntry.getId())
                .setTitle(hansardEntry.getTitle())
                .setUri(hansardEntry.getLink().getUri())
                .build();

        downloadableFileProducer.produce(downloadableFile);
    }
}
