package com.bgasparotto.hansardreader.messaging.consumer;

import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import com.bgasparotto.hansardreader.model.dto.TestDtoFactory;
import com.bgasparotto.hansardreader.service.ApiReaderService;
import com.bgasparotto.hansardreader.service.EntryService;
import com.bgasparotto.scheduler.message.RunUpdate;
import com.bgasparotto.spring.kafka.avro.test.EmbeddedKafkaAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@ActiveProfiles("test")
@EmbeddedKafka
@DirtiesContext
public class RunUpdateConsumerTest {

    public static final long FIVE_SECONDS = 5 * 1000;

    @Autowired
    private EmbeddedKafkaAvro embeddedKafkaAvro;

    @Autowired
    private TestDtoFactory dtoFactory;

    @Value("${topics.input.run-hansard-update}")
    private String topic;

    @MockBean
    private ApiReaderService mockApiReaderService;

    @MockBean
    private EntryService mockEntryService;

    @SpyBean
    private RunUpdateConsumer runUpdateConsumer;

    @Captor
    private ArgumentCaptor<ConsumerRecord<String, RunUpdate>> runUpdateMessageCaptor;

    @Captor
    private ArgumentCaptor<HansardFeed> hansardFeedCaptor;

    private HansardFeed hansardFeedToPublish;

    @BeforeEach
    public void setUp() {
        hansardFeedToPublish = dtoFactory.newHansardFeed(3);
        when(mockApiReaderService.queryFeed()).thenReturn(hansardFeedToPublish);
    }

    @Test
    public void shouldQueryTheApiAndPublishTheFeedWhenAnInputMessageIsConsumed() {
        String inputMessageKey = "some-message-key";
        RunUpdate inputMessageValue = buildTestMessage();
        embeddedKafkaAvro.produce(topic, inputMessageKey, inputMessageValue);

        assertMessageIsConsumed(inputMessageKey, inputMessageValue);
        assertHansardFeedIsPublished();
    }

    private RunUpdate buildTestMessage() {
        return RunUpdate.newBuilder()
                .setDescription("Test message")
                .build();
    }

    private void assertMessageIsConsumed(String inputMessageKey, RunUpdate inputMessageValue) {
        verify(runUpdateConsumer, timeout(FIVE_SECONDS).times(1))
                .consume(runUpdateMessageCaptor.capture());

        ConsumerRecord<String, RunUpdate> consumedRecord = runUpdateMessageCaptor.getValue();
        assertThat(consumedRecord.key()).isEqualTo(inputMessageKey);
        assertThat(consumedRecord.value()).isEqualTo(inputMessageValue);
    }

    private void assertHansardFeedIsPublished() {
        verify(mockEntryService, timeout(FIVE_SECONDS).times(1))
                .publishDownloadableEntries(hansardFeedCaptor.capture());

        HansardFeed publishedHansardFeed = hansardFeedCaptor.getValue();
        assertThat(publishedHansardFeed).isEqualTo(hansardFeedToPublish);
    }
}
