package com.example.azuremy;

import java.util.ArrayList;
import java.util.Map;

public class Translation {
    Map<String, Language> translation;

    public String toString(){
        String languages = "";
        for (String l: translation.keySet()) {
            languages +=l + ":";
        }
        return languages;
    }

    public ArrayList<String> getFullName(){
        //достаю полное имя языка, чтобы в спиннере выглядело норм
        ArrayList<String> languages = new ArrayList<String>();

        for(Map.Entry<String, Language> item : translation.entrySet()){
            languages.add(item.getValue().name);
        }

        return languages;
    }

    public String getKey(String value) {
        //функция получения ключа по значению
        String res="";
        for (Map.Entry<String, Language> item: translation.entrySet()) {
            if (item.getValue().name.equals(value)) {
                res = item.getKey();
            }
        }

        return res;
    }

}
