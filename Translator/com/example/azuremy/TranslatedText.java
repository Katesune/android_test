package com.example.azuremy;

import java.util.ArrayList;
import java.util.Map;

class ToText {
    String to;
    String text;
}

// формат ответа [{"to", "text"} , {"to", "text"}]

public class TranslatedText {
   ArrayList<ToText> elem;

   public String getResult() {
       String res="";

       for (ToText t: elem) {
           res += "To language: " + t.to.toString() + "/n Your Text : "+t.text.toString();
       }

       return res;
   }
}
