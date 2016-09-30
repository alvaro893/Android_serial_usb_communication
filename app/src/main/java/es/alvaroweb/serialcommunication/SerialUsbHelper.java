/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * TODO: Create JavaDoc
 */
public class SerialUsbHelper {
    private static final String TAG = SerialUsbHelper.class.getSimpleName();
    private static final int BAUD_RATE = 115200;
    private final ApiConnection mApiConnection;
    private String deviceName;
    private UsbSerialDriver driver;
    private final Activity context;
    private UsbDeviceConnection connection;
    private SerialInputOutputManager mSerialIoManager;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private UsbSerialPort mPort;

    public final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) context).updateReceivedData(data);
                            mApiConnection.postData(data);
                        }
                    });
                }
            };



    public SerialUsbHelper(Activity context) {
        this.context = context;
        this.mApiConnection = new ApiConnection(context);
    }


    public String findUsb(){
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return "not found";
        }

        // Open a connection to the first available driver.
        driver = availableDrivers.get(0);
        connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            return "not found";
        }

        deviceName = driver.getDevice().getDeviceName();
        return deviceName;
    }

    public void read(){

        // Read some data! Most have just one port (port 0).
        Log.d(TAG, "ports: " + driver.getPorts());
        mPort = driver.getPorts().get(0);
        try {
            mPort.open(connection);
            mPort.setParameters(BAUD_RATE, UsbSerialPort.DATABITS_8,
                    UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(mPort, mListener);
            mExecutor.submit(mSerialIoManager);

        } catch (IOException e) {
            Log.e(TAG,"ioException: " + e.getMessage());
        }
    }

    public void stop() {
        mSerialIoManager.stop();
        if (mPort != null) {
            try {
                mPort.close();
            } catch (IOException e) {
                // Ignore.
            }
            mPort = null;
        }
    }
}
