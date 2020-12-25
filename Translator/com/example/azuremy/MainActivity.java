package com.example.azuremy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Query;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LANG = "lang";

    SharedPreferences mSettings;

    String to_lang;

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // ответ от сервера в виде строки
            .baseUrl(AzureTranslationApi.API_URL) // адрес API сервера
            .build();

    AzureTranslationApi api = retrofit.create(AzureTranslationApi.class); // описываем, какие функции реализованы
    EditText to_translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Call<Translation> call = api.getLanguages(); // создаём объект-вызов
        call.enqueue(new LanguagesCallback());

        Translation json_langs = getTranslation();
        ArrayList<String> langs = json_langs.getFullName();

        Spinner lang_list = (Spinner) findViewById(R.id.lang_list);
        TextView selection = (TextView) findViewById(R.id.selection);
        to_translate = (EditText) findViewById(R.id.input);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang_list.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);
                selection.setText(item);
                to_lang = json_langs.getKey(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        lang_list.setOnItemSelectedListener(itemSelectedListener);

    }

    public void onMyButtonClick(View v) {
        //при нажатии кнопки обрабатываем тест, введенный пользователем
        //объект, который передаем в запрос, должен быть [{"Text":"Some text}](из документации)
        //передаем его и язык
        String input_text = to_translate.getText().toString();

        myObject myObj = new myObject();
        myObj.Text = input_text;

        Log.d("INPUT", "response: " + myObj.Text);

        myObject[] t;
        t = new myObject[1];
        t[0] = myObj;

        Call<List<TranslatedText>> input_call = api.translate(to_lang, t);
        input_call.enqueue(new TranslateCallback());
    }

    protected Translation getTranslation() {
        // добываем языки из APP_PREFERENCES_LANG
        Gson gson = new Gson();

        String strlangs = "";
        if (mSettings.contains(APP_PREFERENCES_LANG)) {
            strlangs = mSettings.getString(APP_PREFERENCES_LANG, "");
        }

        Translation json_langs = gson.fromJson(strlangs, Translation.class);

        return json_langs;
    }

    class LanguagesCallback implements Callback<Translation> {

        @Override
        public void onResponse(Call<Translation> call, Response<Translation> response) {

            if (response.isSuccessful()) {
                Log.d("mytag", "response: " + response.body());
                saveLang(response);

            } else {
                Log.d("mytag", "error " + response.code());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "error " + response.errorBody(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        protected void saveLang(Response<Translation> response) {
            //сохраняем полученные зыки в APP_PREFERENCES_LANG
            Gson gson = new Gson();
            String jsonLang = gson.toJson(response.body());

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_LANG, jsonLang);
            editor.apply();
        }


        @Override
        public void onFailure(Call<Translation> call, Throwable t) {
            Log.d("mytag", "error " + t.getLocalizedMessage());

            Toast toast = Toast.makeText(getApplicationContext(),
                    "error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    class TranslateCallback implements Callback<List<TranslatedText>> {

        @Override
        public void onResponse(Call<List<TranslatedText>> call, Response<List<TranslatedText>> response) {

            if (response.isSuccessful()) {
                Log.d("translate", "response: " + response.body());

            } else {
                Log.d("translate", "error " + response.code());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "error " + response.errorBody(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }



        @Override
        public void onFailure(Call<List<TranslatedText>> call, Throwable t) {
            Log.d("translate", "error " + t.getLocalizedMessage());

            Toast toast = Toast.makeText(getApplicationContext(),
                    "error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}