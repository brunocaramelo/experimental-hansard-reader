package com.bgasparotto.hansardreader.service;

import com.bgasparotto.hansardreader.model.dto.HansardEntry;
import com.bgasparotto.hansardreader.model.dto.HansardFeed;
import com.bgasparotto.hansardreader.model.dto.HansardResourceLink;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(components = ApiReaderService.class)
@ActiveProfiles("test")
public class ApiReaderServiceTest {

    @Autowired
    private ApiReaderService service;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldQueryFeedAndReturnParsedObject() throws Exception {
        ClassPathResource resource = new ClassPathResource("api/sample/feed.xml");
        String body = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://api.data.parliament.uk/resources/files/feed?dataset=12&take=all")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .body(body)
                );

        HansardFeed feed = service.queryFeed();
        mockServer.verify();

        assertThat(feed.getId()).isEqualTo("http://api.data.parliament.uk/resources/files/feed");
        assertThat(feed.getTitle()).isEqualTo("Data.Parliament Resource File Feed");
        assertThat(feed.getUpdated()).isEqualTo(Instant.parse("2019-12-10T13:46:35Z"));
        assertThat(feed.getEntries()).hasSize(2);

        HansardEntry firstEntry = feed.getEntries().get(0);
        assertThat(firstEntry.getId()).isEqualTo("http://api.data.parliament.uk/resources/files/1167963.zip");
        assertThat(firstEntry.getTitle()).isEqualTo("Data.Parliament Resource File 1167963 - zip");
        assertThat(firstEntry.getUpdated()).isEqualTo(Instant.parse("2019-12-09T16:17:21Z"));

        HansardResourceLink firstLink = firstEntry.getLink();
        assertThat(firstLink.getUri()).isEqualTo("http://api.data.parliament.uk/resources/files/1167963.zip");

        HansardEntry secondEntry = feed.getEntries().get(1);
        assertThat(secondEntry.getId()).isEqualTo("http://api.data.parliament.uk/resources/files/1167958.zip");
        assertThat(secondEntry.getTitle()).isEqualTo("Data.Parliament Resource File 1167958 - zip");
        assertThat(secondEntry.getUpdated()).isEqualTo(Instant.parse("2019-11-06T01:24:34Z"));

        HansardResourceLink secondLink = secondEntry.getLink();
        assertThat(secondLink.getUri()).isEqualTo("http://api.data.parliament.uk/resources/files/1167958.zip");
    }
}