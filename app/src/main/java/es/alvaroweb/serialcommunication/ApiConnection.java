/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.serialcommunication;

import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 * TODO: Create JavaDoc
 */
public class ApiConnection implements Callback<ResponseObject> {
    private static final String URL_BASE = "http://users.metropolia.fi/~alvarob/";
    private static final String DEBUG_TAG = ApiConnection.class.getSimpleName();
    private final StreamService mService;
    private Context mContext;

    public ApiConnection(Context mContext) {
        this.mContext = mContext;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .build();
        mService = retrofit.create(StreamService.class);
    }

    void postData(byte[] data){
        mService.postData(data);
    }


    @Override
    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
        Log.d(DEBUG_TAG, String.valueOf(response.body()));
    }

    @Override
    public void onFailure(Call<ResponseObject> call, Throwable t) {
        Log.e(DEBUG_TAG, call.request().body().toString());
    }
}
