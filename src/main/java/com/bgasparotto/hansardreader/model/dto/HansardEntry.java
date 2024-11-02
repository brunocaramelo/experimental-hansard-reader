package com.bgasparotto.hansardreader.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class HansardEntry {

    @NotBlank(message = "Entry id can't be blank")
    private String id;

    @NotBlank(message = "Entry title can't be blank")
    private String title;
    private Instant updated;

    @Valid
    @NotNull
    private HansardResourceLink link;
}
