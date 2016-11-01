/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/*
 * TODO: Create JavaDoc
 */
public class UdpSocket {

    private String LOCAL_HOST = "192.168.20.100";
    private int SENDING_PORT = 4445;
    DatagramSocket socket;

    public UdpSocket(String host, int port) throws SocketException {
        this.socket = new DatagramSocket();
        if(host != null){
            LOCAL_HOST = host;
            SENDING_PORT = port;
        }
    }

    public void send(byte[] data) throws IOException {
        if(socket.isClosed()){
            return;
        }
        InetAddress address = InetAddress.getByName(LOCAL_HOST);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, SENDING_PORT);
        socket.send(packet);
    }

    public void stop(){
        socket.close();
    }
}
