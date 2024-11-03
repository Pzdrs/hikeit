package cz.pycrs.hikeit.ui.goals;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.pycrs.hikeit.R;
import cz.pycrs.hikeit.api.HikeItApiService;
import cz.pycrs.hikeit.goal.DistanceGoal;
import cz.pycrs.hikeit.goal.Goal;
import cz.pycrs.hikeit.goal.StepsGoal;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class GoalsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Goal>> goals;
    public LiveData<List<Goal>> getGoals() {
        if (goals == null) {
            goals = new MutableLiveData<>(new ArrayList<>());
            loadGoals();
        }
        return goals;
    }

    private final HikeItApiService api;

    public GoalsViewModel(Application application) {
        super(application);

        try {
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(
                            new OkHttpClient.Builder().addInterceptor(chain ->
                                            chain.proceed(chain.request().newBuilder().addHeader("Authorization", String.format("Bearer %s", appInfo.metaData.getString("cz.pycrs.hikeit.api.token"))).build()))
                                    .build())
                    .baseUrl("https://hikeit-api.pycrs.cz/api/v1/")
                    .build();
            this.api = retrofit.create(HikeItApiService.class);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadGoals() {
        api.listGoals().enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    List<Goal> goals = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        goals.add(Goal.of(jsonArray.getJSONObject(i)));
                    }
                    new Handler(Looper.getMainLooper()).post(() -> GoalsViewModel.this.goals.setValue(goals));
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createGoal(String label, LocalDateTime deadline, String type, float value) throws JSONException {
        JSONObject body = new JSONObject()
                .put("label", label)
                .put("deadline", deadline.toEpochSecond(ZoneOffset.UTC))
                .put("type", Goal.getType(type))
                .put("value", value);
        api.createGoal(RequestBody.create(
                MediaType.get("application/json"), body.toString()
        )).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() != 201) {
                    throw new RuntimeException("Failed to create goal - " + response.code());
                }
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    Goal goal = Goal.of(json);

                    List<Goal> goals = new ArrayList<>(GoalsViewModel.this.goals.getValue());
                    goals.add(goal);
                    new Handler(Looper.getMainLooper()).post(() -> GoalsViewModel.this.goals.setValue(goals));
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}