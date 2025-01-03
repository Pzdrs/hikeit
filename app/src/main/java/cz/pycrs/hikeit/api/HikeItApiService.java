package cz.pycrs.hikeit.api;

import java.util.List;

import cz.pycrs.hikeit.goal.Goal;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HikeItApiService {
    @GET("goals")
    Call<ResponseBody> listGoals();

    @POST("goals")
    Call<ResponseBody> createGoal(@Body RequestBody body);

    @POST("goals/{id}/finish")
    Call<ResponseBody> finishGoal(@Path("id") int id);
}
