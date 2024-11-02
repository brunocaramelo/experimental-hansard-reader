@XmlSchema(namespace = "http://www.w3.org/2005/Atom", elementFormDefault = XmlNsForm.QUALIFIED)
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value = InstantAdapter.class, type = Instant.class)
})
package com.bgasparotto.hansardreader.model.dto;

import com.bgasparotto.hansardreader.service.adapter.InstantAdapter;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.Instant;