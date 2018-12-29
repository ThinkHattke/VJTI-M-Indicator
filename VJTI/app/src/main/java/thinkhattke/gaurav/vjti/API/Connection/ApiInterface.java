package thinkhattke.gaurav.vjti.API.Connection;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import thinkhattke.gaurav.vjti.API.Reponse.NewReportRes;
import thinkhattke.gaurav.vjti.API.Request.AddReportReq;

public interface ApiInterface {


    //Report Module
    @POST("api/problem")
    Call<NewReportRes> attemptReport(@Body AddReportReq params);


}
