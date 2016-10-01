package es.alvaroweb.serialcommunication.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
public class BufferFramesTest {
    // NOTE: bytes values in java are from -126 to 127, they are not from 0 to 255
    private final static byte FF = Byte.MAX_VALUE;
    private final static byte[] INITIAL_SEQUENCE = {FF,FF,FF};


    @Test
    public void addChunk() throws Exception {
        final byte[] arr0 = {FF,FF,FF,100,FF,FF,45,34,93,31,42,42,FF,FF,FF,-33,2,5,24,52};
        BufferFrames bufferFrames = new BufferFrames();
        bufferFrames.addChunk(new Chunk(arr0));
        assertEquals(1,bufferFrames.getmFramesCount());
        // answer should be (100,FF,FF,45,34,93,31,42,42)
    }

    @Test
    public void add2Chunks() throws Exception {
        final byte[] arr0 = {FF,FF,FF,100,FF,FF,45,34,93,31,42,42,FF,FF,FF,-33,2,5,24,52};
        final byte[] arr1 = {FF,FF,FF,100,FF,FF,45,34,93,31,42,42,FF,FF,FF,-33,2,5,24,52};
        BufferFrames bufferFrames = new BufferFrames();
        bufferFrames.addChunk(new Chunk(arr0));
        bufferFrames.addChunk(new Chunk(arr1));
        assertEquals(3,bufferFrames.getmFramesCount());
        // answer should be 100,FF,FF,45,34,93,31,42,42 | -33,2,5,24,52 | 100,FF,FF,45,34,93,31,42,42,
    }

}