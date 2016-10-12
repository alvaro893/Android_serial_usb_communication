/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.networking;

import java.io.IOException;
import java.net.Socket;

/*
 * TODO: Create JavaDoc
 */
public class SocketConnection extends Socket {
    public SocketConnection(String host, int port) throws IOException {
        super(host, port);
    }

    public void reciveFromSource(byte[] buff){
        try {
            getOutputStream().write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToSource() {

    }
}
