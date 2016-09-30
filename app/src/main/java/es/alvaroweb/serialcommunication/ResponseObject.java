/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/*
 * TODO: Create JavaDoc
 */
public class ResponseObject extends ResponseBody {
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        return null;
    }
}
