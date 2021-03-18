package com.example.azuremy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AzureTranslationApi {
    String API_URL = "https://api.cognitive.microsofttranslator.com";
    String key = "bf4ad45220754b0c8f00ce4c5accf366"; //
    String region = "westeurope";

    @GET("/languages?api-version=3.0&scope=translation")
    //Call<Translation> getLanguages(@Url String url);
    Call<Translation> getLanguages();

    @POST("/translate?api-version=3.0&scope=translation&from=en&")
    @Headers({
            "Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: "+key,
            "Ocp-Apim-Subscription-Region: "+region
            // TODO: указать ключ и регион
    })

        // TranslatedText - формат ответа от сервера
        // Тип ответа - TranslatedText, действие - translate, содержание запроса - пустое (нет полей формы)
        // TODO: с помощью аннотации @Body передать поля запроса к API (текст для перевода)
        // см. примеры https://square.github.io/retrofit/

    Call<List<TranslatedText>> translate(@Query("to") String lang, @Body myObject[] text);
}
