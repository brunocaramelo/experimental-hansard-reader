package com.bgasparotto.hansardreader.service.validator;

import com.bgasparotto.hansardreader.model.dto.HansardEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntryValidation {
    private final Validator validator;

    public Optional<HansardEntry> filterOutInvalid(HansardEntry hansardEntry) {
        Set<ConstraintViolation<HansardEntry>> constraintViolations = validator.validate(hansardEntry);
        if (!constraintViolations.isEmpty()) {
            log.warn("Skipping entry due to constraint violations: ");
            constraintViolations.stream().map(ConstraintViolation::getMessage).forEach(log::warn);
            return Optional.empty();
        }

        return Optional.of(hansardEntry);
    }
}
