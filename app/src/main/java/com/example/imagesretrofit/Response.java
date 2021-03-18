package com.example.imagesretrofit;

public class Response {
    int total;
    int totalHits;
    Hit[] hits;

    @Override
    public String toString() {

        return "totalHits = " + totalHits;
    }
}
