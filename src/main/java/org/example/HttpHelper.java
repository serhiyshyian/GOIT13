package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    public static HttpURLConnection openConnection(String url) throws IOException {
        URL apiUrl = new URL(url);
        return (HttpURLConnection) apiUrl.openConnection();
    }
}

