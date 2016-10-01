/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.data;

import java.util.Arrays;

/*
 * TODO: Create JavaDoc
 */
public class BufferFrames {
    // NOTE: bytes values in java are from -126 to 127, but -1 is 0x77 equivalent
    final static byte FF = -1;
    final static byte[] INITIAL_SEQUENCE = {FF, FF, FF};
    private static final int MAX_BUFFER = 20;
    private byte[] mSequence = new byte[3];
    private boolean mItIsFirstTime = true;
    private Frame[] mBuffer;
    private int mFramesCount = 0;
    private Frame mCurrentFrame = new Frame();

    public BufferFrames(int framesToBuffer) {
        this.mBuffer = new Frame[framesToBuffer];
    }

    public BufferFrames() {
        this.mBuffer = new Frame[MAX_BUFFER];
    }

    public Frame[] getmBuffer() {
        return mBuffer;
    }

    public byte[] getFrame(int i) {
        return mBuffer[i].getFrameAsByteArray();
    }

    public int getmFramesCount() {
        return mFramesCount;
    }

    public void addChunk(Chunk data) {
        for (Byte next : data) {
            if (detectInitialSequence() && !itIsFirstTime()) {
                addFrameToBuffer(mCurrentFrame);
                mCurrentFrame = new Frame();
            }
            moveSequence(next);
            mCurrentFrame.addByte(next);
        }
    }

    /**
     * this prevents to add the initial sequence FF FF FF as part of the frame
     */
    private boolean itIsFirstTime() {
        if (mItIsFirstTime) {
            mCurrentFrame = new Frame();
        }
        boolean itIsFirstTime = mItIsFirstTime;
        mItIsFirstTime = false;

        return itIsFirstTime;
    }


    private void addFrameToBuffer(Frame f) {
        mBuffer[mFramesCount] = f;
        mFramesCount++;
        System.out.println("added frame: " + f);
        if (mFramesCount > 20) {
            //stopBuffering()
        }
    }

    private void moveSequence(byte nextByte) {
        mSequence[2] = mSequence[1];
        mSequence[1] = mSequence[0];
        mSequence[0] = nextByte;
    }

    private boolean detectInitialSequence() {
        return Arrays.equals(mSequence, INITIAL_SEQUENCE);
    }

    public boolean isFull() {
        return mFramesCount >= mBuffer.length -1;
    }
}
