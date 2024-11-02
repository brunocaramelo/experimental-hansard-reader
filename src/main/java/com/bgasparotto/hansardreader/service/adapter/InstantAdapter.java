package com.bgasparotto.hansardreader.service.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.util.Date;

public class InstantAdapter extends XmlAdapter<Date, Instant> {

    @Override
    public Instant unmarshal(Date value) {
        return value.toInstant();
    }

    @Override
    public Date marshal(Instant value) {
        return Date.from(value);
    }
}
