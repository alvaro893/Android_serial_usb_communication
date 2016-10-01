/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.data;


import com.hoho.android.usbserial.util.HexDump;

/*
 * TODO: Create JavaDoc
 */
public class Frame {
    private static final int MAX_FRAME_SIZE = 72;

    public byte[] getFrameAsByteArray() {
        return mFrame;
    }

    private byte[] mFrame;
    private int mCount = 0;

    public Frame(byte[] frame) {
        this.mFrame = frame;
    }

    public Frame(){
        this.mFrame = new byte[MAX_FRAME_SIZE];
    }

    public void addByte(byte b){
        mFrame[mCount] = b;
        mCount++;
    }

    @Override
    public String toString() {
        return HexDump.dumpHexString(mFrame);
    }
}
