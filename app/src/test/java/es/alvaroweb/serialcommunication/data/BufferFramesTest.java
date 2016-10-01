package es.alvaroweb.serialcommunication.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
public class BufferFramesTest {
    private final static byte FF = BufferFrames.FF;
    private final static byte[] INITIAL_SEQUENCE = BufferFrames.INITIAL_SEQUENCE;


    @Test
    public void addChunk() throws Exception {
        final byte[] arr0 = {FF,FF,FF,0,FF,FF,1,127,-128,-127,-126,42,FF,FF,
                -33,2,5,24,52,-33,2,5,24,52,-33,2,5,24,52,-33,2,5,24,52,
                52,-33,2,5,24,52,-33,2,5,24,52,52,-33,2,5,24,52,-33,2,5,
                2,5,24,52,-33,2,5,24,52,52,-33,2,5,24,52,FF,FF,FF,44,33,55};
        BufferFrames bufferFrames = new BufferFrames();
        bufferFrames.addChunk(new Chunk(arr0));
        assertEquals(1,bufferFrames.getmFramesCount());
        // answer should be an array between 2 initial sequences FF FF FF
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