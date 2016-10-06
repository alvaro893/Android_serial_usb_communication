package es.alvaroweb.serialcommunication.networking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import es.alvaroweb.serialcommunication.SerialUsbHelper;
import es.alvaroweb.serialcommunication.data.BufferFrames;
import es.alvaroweb.serialcommunication.data.Frame;

public class MyService extends Service {
    private static final String INFO_TAG =  "ServiceUsb";
    private SerialUsbHelper serialUsbHelper;
    private ServerConnection server;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * <p>
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     * <p>
     * <p>If you need your application to run on platform versions prior to API
     * level 5, you can use the following model to handle the older {@link #onStart}
     * callback in that case.  The <code>handleCommand</code> method is implemented by
     * you as appropriate:
     * <p>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
     * start_compatibility}
     * <p>
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(INFO_TAG, "started service:" + startId);
        server = new ServerConnection();
        serialUsbHelper = new SerialUsbHelper(getApplicationContext(), new SerialUsbHelper.OnBufferFull() {
            @Override
            public void getBuffer(BufferFrames bf) {
                sendData(bf);
            }
        });

        serialUsbHelper.startReading();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        serialUsbHelper.stop();
        Log.d(INFO_TAG, "service destroyed");
    }

    public void sendData(BufferFrames bufferFrames) {
        for(Frame f : bufferFrames.getmBuffer()){
            if(f == null){
                Log.d(INFO_TAG, "frame dropped!!!");
                continue;
            }
            new NetworkTask().execute(f);
        }
        //server.closeConnection();
    }

    private class NetworkTask extends AsyncTask<Frame, Void, Void>{

        @Override
        protected Void doInBackground(Frame... params) {
            server.postFrame(params[0]);
            return null;
        }
    }
}
