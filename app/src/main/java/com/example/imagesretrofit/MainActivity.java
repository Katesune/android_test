package com.example.imagesretrofit;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    LinearLayout linlayout;
    TextView results_count;

    String API_URL = "https://pixabay.com/";
    String q = "bad dog";
    String key = "20630466-7835c214d11cd52f12b73f9dc";
    String image_type = "photo";

    private Gson gson = new GsonBuilder().create();

    // TODO: реализовать скачивание и отображение картинок, найденных по запросу

    interface PixabayAPI {
        @GET("/api") // метод запроса (POST/GET) и путь к API
            // пример содержимого веб-формы q=dogs+and+people&key=MYKEY&image_type=photo
        Call<Response> search(@Query("q") String q, @Query("key") String key, @Query("image_type") String image_type);
        // Тип ответа, действие, содержание запроса

        @GET()
        Call<ResponseBody> getImage (@Url String pictureURL);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        linlayout = (LinearLayout) findViewById(R.id.main);
        results_count = (TextView) findViewById(R.id.results_count);
    }

    public void startSearch() {
        // вызывается, когда пользователь вводит текст и нажимает кнопку поиска

        // создаём экземпляр службы для обращения к API
        // можно использовать экземпляр для нескольких API сразу
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL) // адрес API сервера
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // создаём обработчик, определённый интерфейсом PixabayAPI выше
        PixabayAPI api = retrofit.create(PixabayAPI.class);

        Call<Response> getImages = api.search(q, key, image_type);

        Callback <Response> imagescallback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                displayResults(r.hits);
                results_count.setText(Integer.toString(r.hits.length)+" results found");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("mytag", "fail:" + t.getLocalizedMessage());
            }
        };
        getImages.enqueue(imagescallback);
    }

    public void displayResults(Hit[] hits) {

        for (Hit h: hits) {

            new getImagesTask().execute(h.previewURL);

//            Call<ResponseBody> getImage = api.getImage(h.previewURL);
//
//            Callback<ResponseBody> imagecallback = new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
//
//
//                    ImageView image = new ImageView(getApplicationContext());
//
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
//                            (ConstraintLayout.LayoutParams.MATCH_PARENT , ConstraintLayout.LayoutParams.MATCH_PARENT);
//                    image.setLayoutParams(layoutParams);
//
//                    image.setImageBitmap(bmp);
//
//                    linlayout.addView(image);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.d("mytag", "fail:" + t.getLocalizedMessage());
//                }
//            };
//            getImage.enqueue(imagecallback);

           // setContentView(linlayout);
        }
        // вызывается, когда появятся результаты поиска

    }

    public void onSearchClick(View v) {
        TextView text = (TextView)findViewById(R.id.text);
        q = text.getText().toString();

        startSearch();
    }

    public void cnangeType(View v){
        image_type = ((Button) v).getText().toString();
    }

    public class getImagesTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {
            return getImageTask(url[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView image = new ImageView(getApplicationContext());

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
                    (result.getWidth()+550, result.getHeight()+550);
            layoutParams.leftMargin=200;
            image.setLayoutParams(layoutParams);

            image.setImageBitmap(result);
            image.invalidate();

            linlayout.addView(image);
        }

        private Bitmap getImageTask(String url) {
            Bitmap bmp =null;

            try{
                URL url_url = new URL(url);
                HttpURLConnection con = (HttpURLConnection)url_url.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);

            } catch(Exception e){
                e.getLocalizedMessage();
            }
            return bmp;
        }
    }
}