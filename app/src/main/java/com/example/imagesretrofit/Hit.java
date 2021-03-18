package com.example.imagesretrofit;

import android.graphics.Bitmap;

// Класс для описания результатов поиска
public class Hit {
    int id;
    String previewURL;
}

/*
пример данных ответа API Pixabay
{
    "id":4997424,
    "pageURL":"https://pixabay.com/photos/animal-asia-background-bad-4997424/",
    "type":"photo",
    "tags":"animal, asia, background",
    "previewURL":"https://cdn.pixabay.com/photo/2020/04/03/04/47/animal-4997424_150.jpg",
    "previewWidth":150,
    "previewHeight":112,
    "webformatURL":"https://pixabay.com/get/52e9dc444e50a814f1dc84609629317f143adfe6524c704c7d2878d2904cc45b_640.jpg",
    "webformatWidth":640,
    "webformatHeight":480,
    "largeImageURL":"https://pixabay.com/get/52e9dc444e50a814f6da8c7dda793678143edbe450506c4870277cd7914ac459bb_1280.jpg",
    "imageWidth":3072,
    "imageHeight":2304,
    "imageSize":677212,
    "views":99,
    "downloads":80,
    "favorites":0,
    "likes":5,
    "comments":1,
    "user_id":9690153,
    "user":"rezendeluan",
    "userImageURL":
    "https://cdn.pixabay.com/user/2019/01/07/17-50-00-617_250x250.jpg"
},
...
 */