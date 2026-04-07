package de.vayd.sebastianbrunnert.email;

import lombok.Value;

import java.io.InputStream;

@Value
public class EmailAttachment {

    private String name;

    private InputStream content;

}
