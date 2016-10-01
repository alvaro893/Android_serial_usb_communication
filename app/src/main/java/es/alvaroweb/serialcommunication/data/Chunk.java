/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.data;

import java.util.Iterator;

/*
 * TODO: Create JavaDoc
 */
public class Chunk implements Iterable<Byte>{
    private byte[] mChunk;

    public Chunk(byte[] data) {
        this.mChunk = data;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int mCount = 0;

            @Override
            public boolean hasNext() {
                return mCount < mChunk.length;
            }

            @Override
            public Byte next() {
                byte b = mChunk[mCount];
                mCount++;
                return b;
            }
        };
    }
}
