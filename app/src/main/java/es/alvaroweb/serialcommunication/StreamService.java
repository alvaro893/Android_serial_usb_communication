package es.alvaroweb.serialcommunication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public interface StreamService {
    @FormUrlEncoded
    @POST("saveData.php")
    Call<ResponseObject> postData(@Field("data") byte[] data);
}
