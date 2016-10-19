package es.alvaroweb.serialcommunication.networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;

import es.alvaroweb.serialcommunication.SerialUsbHelper;


public class UsbToNetworkService extends Service implements SerialInputOutputManager.Listener {
    private static final String DEBUG_TAG = UsbToNetworkService.class.getSimpleName();
    private static final int PORT = 4444;
    private static final String HOST = "ircloud.ddns.net";
    private SerialUsbHelper serialUsbHelper;
    private UdpSocket udpSocket;


    public UsbToNetworkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "started service:" + startId);
        opentSocket();
        createThread();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serialUsbHelper.stop();
        Log.d(DEBUG_TAG, "service destroyed");
    }

    @Override
    public void onNewData(byte[] data) {
        try {
            udpSocket.send(data);
            logData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRunError(Exception e) {
        Log.d(DEBUG_TAG, "reading exception: " + e.getMessage());
    }

    private void logData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n";
        Log.d(DEBUG_TAG, message);
    }

    private void createThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                opentSocket();
                // the onNewData listener is implemented in the Service
                serialUsbHelper = new SerialUsbHelper(getApplicationContext(), UsbToNetworkService.this);
                serialUsbHelper.startReading();
            }
        });
        t.start();
    }

    private void opentSocket() {
        try {
            udpSocket = new UdpSocket();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "could not create socket: " + e.getMessage());
        }
    }
}
