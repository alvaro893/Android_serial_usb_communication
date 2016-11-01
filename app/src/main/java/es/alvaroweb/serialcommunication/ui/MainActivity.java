package es.alvaroweb.serialcommunication.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.alvaroweb.serialcommunication.R;
import es.alvaroweb.serialcommunication.networking.UsbToNetworkService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static final String HOST_KEY = "HOST";
    public static final String PORT_KEY = "PORT";
    private Button readButton;
    private Button stopButton;
    private EditText host;
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readButton = (Button) findViewById(R.id.button2);
        stopButton = (Button) findViewById(R.id.button3);
        host = (EditText) findViewById(R.id.host);
        port = (EditText) findViewById(R.id.port);

    }

    public void startReading(View v){
        Intent intent = new Intent(this, UsbToNetworkService.class);
        intent.putExtra(HOST_KEY, host.getText().toString());
        intent.putExtra(PORT_KEY, Integer.parseInt(port.getText().toString()));
        startService(intent);
        serviceState();
    }

    public void stopReading(View v){
        stopService(new Intent(this, UsbToNetworkService.class));
        serviceState();
    }

    private void serviceState(){
        if(isMyServiceRunning(UsbToNetworkService.class)){
            readButton.setBackgroundColor(Color.GREEN);
            stopButton.setBackgroundColor(Color.GREEN);
        }else{
            readButton.setBackgroundColor(Color.RED);
            stopButton.setBackgroundColor(Color.RED);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//    public void updateReceivedData(byte[] data){
//        final String message = "Read " + data.length + " bytes: \n"
//                + HexDump.dumpHexString(data) + "\n";
//        dataFoundView.append(message);
//    }

}
