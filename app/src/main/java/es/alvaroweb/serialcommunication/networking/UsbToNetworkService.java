package es.alvaroweb.serialcommunication.networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;

import es.alvaroweb.serialcommunication.SerialUsbHelper;
import es.alvaroweb.serialcommunication.ui.MainActivity;

import static android.R.attr.port;


public class UsbToNetworkService extends Service implements SerialInputOutputManager.Listener {
    private static final String DEBUG_TAG = UsbToNetworkService.class.getSimpleName();
    private static final int PORT = 4444;
    private static final String HOST = "ircloud.ddns.net";
    private SerialUsbHelper serialUsbHelper;
    private UdpSocket udpSocket;
    private Thread t;
    private Intent mIntent;

    public UsbToNetworkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "started service:" + startId);
        mIntent = intent;
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
            Log.d(DEBUG_TAG, "camera n:" + ((int)data[3]));
            //logData(data);
        } catch (IOException e) {
            Log.e(DEBUG_TAG, e.getMessage());
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
        t = new Thread(new Runnable() {
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
            String host = mIntent.getStringExtra(MainActivity.HOST_KEY);
            int port = mIntent.getIntExtra(MainActivity.PORT_KEY, 0);
            udpSocket = new UdpSocket(host, port);
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "could not create socket: " + e.getMessage());
        }
    }
}
