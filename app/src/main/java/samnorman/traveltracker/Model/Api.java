package samnorman.traveltracker.Model;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    //Post to TTracker, used from RetrofitClient
    @POST("/location")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json;charset=utf-8",
            "Cache-Control: max-age=640000"
    })
    Call<JSONPojo> sendcoordinates(@Body JSONPojo jsonpojo);
}
