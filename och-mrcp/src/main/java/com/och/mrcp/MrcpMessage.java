package com.och.mrcp;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class MrcpMessage {

    public static final String CRLF = "\r\n";

    protected String version; // MRCPv2 version
    protected String requestId; // request id
    protected Map<String, String> headers = new HashMap<>();
    protected String body; // message body

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}