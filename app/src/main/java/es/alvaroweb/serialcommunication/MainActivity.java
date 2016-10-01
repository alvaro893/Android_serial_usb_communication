package es.alvaroweb.serialcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hoho.android.usbserial.util.HexDump;

import java.io.IOException;

import es.alvaroweb.serialcommunication.data.BufferFrames;
import es.alvaroweb.serialcommunication.data.Frame;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int BAUD_RATE = 115200;
    private TextView usbNameView;
    private TextView dataFoundView;
    SerialUsbHelper serialUsbHelper;
    private Button readButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serialUsbHelper = new SerialUsbHelper(this);
        usbNameView = (TextView) findViewById(R.id.textView);
        dataFoundView = (TextView) findViewById(R.id.textView2);
        readButton = (Button) findViewById(R.id.button2);
        stopButton = (Button) findViewById(R.id.button3);
    }

    public void findUsbDevices(View v) throws IOException {
        usbNameView.setText(serialUsbHelper.findUsb());
        readButton.setEnabled(true);
        stopButton.setEnabled(true);
    }


    public void readUsbDrive(View v) {
        dataFoundView.setText("start reading");
        serialUsbHelper.read();
    }

    public void stopReading(View v){
        serialUsbHelper.stop();
        dataFoundView.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        serialUsbHelper.stop();
        finish();
    }

    public void updateReceivedData(byte[] data){
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n";
        dataFoundView.append(message);
    }

    public void updateReceivedData(BufferFrames bufferFrames) {
        for(Frame f : bufferFrames.getmBuffer()){
            if(f == null){
                Log.d(TAG, "frame dropped!!!");
                continue;
            }
            updateReceivedData(f.getFrameAsByteArray());
        }
    }
}
