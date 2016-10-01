/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.data;


/*
 * TODO: Create JavaDoc
 */
public class Frame {
    private static final int MAX_FRAME_SIZE = 70;

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
        StringBuilder sb = new StringBuilder(mCount + "(");
        for(int i = 0; i < mCount; i++){
            sb.append(Integer.toString((mFrame[i]+128), 16) + ",")  ;
        }
        sb.append(")");
        return sb.toString();
    }
}
