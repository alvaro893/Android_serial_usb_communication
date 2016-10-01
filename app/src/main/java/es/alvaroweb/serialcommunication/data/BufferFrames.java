/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.data;

import java.util.Arrays;

/*
 * TODO: Create JavaDoc
 */
public class BufferFrames {
    private final static byte FF = Byte.MAX_VALUE;
    private final static byte[] INITIAL_SEQUENCE = {FF, FF, FF};
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
        boolean current = mItIsFirstTime;
        if (current) {
            mCurrentFrame = new Frame();
        }
        mItIsFirstTime = false;

        return current;
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

}
