/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication.networking;

import android.net.Uri;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.alvaroweb.serialcommunication.data.Frame;

import static android.content.ContentValues.TAG;

/*
 * TODO: Create JavaDoc
 */
public class ServerConnection {
    private static final String SCHEME_HTTP = "http";
    private static final String AUTHORITY = "users.metropolia.fi";
    private static final String MAIN_PATH = "~alvarob/";
    private final String RELATIVE_URL = "saveData.php";
    private final Uri.Builder mUri;
    private final URL mPostFrameUrl;
    private boolean mIsConnected = false;
    private HttpURLConnection mConnection;

    public ServerConnection() {
        this.mUri = setUri();
        this.mPostFrameUrl = setPostFrameUrl();
    }



    private Uri.Builder setUri(){
        Uri.Builder uri = new Uri.Builder()
                .scheme(SCHEME_HTTP)
                .authority(AUTHORITY)
                .path(MAIN_PATH);
        return uri;
    }

    private URL setPostFrameUrl() {
        try {
            return new URL(mUri.appendEncodedPath(RELATIVE_URL).toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void postFrame(Frame frame){
        final String FIELD = "data=";
        try {
            mConnection = (HttpURLConnection) mPostFrameUrl.openConnection();
            mConnection.setDoOutput(true);
            mConnection.setRequestMethod("POST");
            mConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            mConnection.setUseCaches(false);
            mConnection.setRequestProperty("Connection", "Keep-Alive");

            OutputStream out = mConnection.getOutputStream();
            out.write(FIELD.getBytes());
            out.write(frame.getFrameAsByteArray());

            int code = mConnection.getResponseCode();
            String response = mConnection.getResponseMessage();
            Log.d(TAG, "response: " + code + ", " + response + " bytes sent: " +
                    FIELD.getBytes().length + frame.getFrameAsByteArray().length);

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: Handle response?
    }

//    public void closeConnection(){
//        try {
//            mConnection.getOutputStream().close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
