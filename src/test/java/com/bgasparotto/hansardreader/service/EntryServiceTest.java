package com.bgasparotto.hansardreader.service;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import com.bgasparotto.hansardreader.messaging.producer.DownloadableFileProducer;
import com.bgasparotto.hansardreader.model.dto.HansardEntry;
import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import com.bgasparotto.hansardreader.model.dto.HansardResourceLink;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EntryServiceTest {

    @MockBean
    private DownloadableFileProducer downloadableFileProducer;

    @Autowired
    private EntryService entryService;

    @Test
    public void shouldInvokeProducerForAllEntries() {
        HansardResourceLink linkOne = new HansardResourceLink();
        linkOne.setUri("protocol://some.uri.1");
        HansardEntry one = new HansardEntry();
        one.setId("1");
        one.setTitle("One");
        one.setLink(linkOne);

        HansardResourceLink linkTwo = new HansardResourceLink();
        linkTwo.setUri("protocol://some.uri.2");
        HansardEntry two = new HansardEntry();
        two.setId("2");
        two.setTitle("Two");
        two.setLink(linkTwo);

        HansardFeed feed = new HansardFeed();
        feed.setEntries(List.of(one, two));

        entryService.publishDownloadableEntries(feed);
        verify(downloadableFileProducer, times(2)).produce(any(DownloadableFile.class));
    }

    @Test
    public void shouldInvokeProducerForValidEntryOnly() {
        HansardResourceLink link = new HansardResourceLink();
        link.setUri("protocol://some.uri.valid");
        HansardEntry validEntry = new HansardEntry();
        validEntry.setId("V");
        validEntry.setTitle("Valid");
        validEntry.setLink(link);

        HansardEntry invalidEntry = new HansardEntry();
        invalidEntry.setId("I");
        invalidEntry.setTitle("Invalid");

        HansardFeed feed = new HansardFeed();
        feed.setEntries(List.of(validEntry, invalidEntry));

        entryService.publishDownloadableEntries(feed);
        verify(downloadableFileProducer, times(1)).produce(any(DownloadableFile.class));
    }
}