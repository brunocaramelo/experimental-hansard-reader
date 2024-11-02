package com.bgasparotto.hansardreader.service.validator;

import com.bgasparotto.hansardreader.model.dto.HansardEntry;
import com.bgasparotto.hansardreader.model.dto.HansardResourceLink;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EntryValidationTest {

    @Autowired
    private EntryValidation entryValidation;

    @Test
    public void shouldReturnOptionalOfEmptyWhenValidationFails() {
        HansardEntry invalidEntry = new HansardEntry();
        invalidEntry.setId(RandomStringUtils.randomAlphabetic(32));
        invalidEntry.setTitle(RandomStringUtils.randomAlphabetic(16));
        invalidEntry.setLink(new HansardResourceLink()); // Missing URI should fail the validation.

        Optional<HansardEntry> optionalEntry = entryValidation.filterOutInvalid(invalidEntry);
        assertThat(optionalEntry).isEmpty();
    }

    @Test
    public void shouldReturnOptionalOfEntryWhenValidationPasses() {
        HansardResourceLink link = new HansardResourceLink();
        link.setUri("protocol://some.uri");

        HansardEntry entry = new HansardEntry();
        entry.setId(RandomStringUtils.randomAlphabetic(32));
        entry.setTitle(RandomStringUtils.randomAlphabetic(16));
        entry.setLink(link);

        Optional<HansardEntry> optionalEntry = entryValidation.filterOutInvalid(entry);
        assertThat(optionalEntry).isPresent();
        assertThat(optionalEntry).contains(entry);
    }
}