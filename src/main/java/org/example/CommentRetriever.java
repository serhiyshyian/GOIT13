package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CommentRetriever {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        CommentRetriever app = new CommentRetriever();

        int userId = 1; // Замените на реальный ID пользователя
        String fileName = "user-" + userId + "-post-comments.json";

        String commentsJson = app.retrieveAndSaveComments(userId, fileName);
        System.out.println("Comments saved to " + fileName);
    }

    public String retrieveAndSaveComments(int userId, String fileName) throws IOException {
        String userPostsUrl = BASE_URL + "/users/" + userId + "/posts";
        String userPostsResponse = sendGetRequest(userPostsUrl);
        JsonArray userPosts = gson.fromJson(userPostsResponse, JsonArray.class);

        JsonObject lastPost = userPosts.get(userPosts.size() - 1).getAsJsonObject();
        int postId = lastPost.get("id").getAsInt();

        String commentsUrl = BASE_URL + "/posts/" + postId + "/comments";
        String commentsResponse = sendGetRequest(commentsUrl);

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(commentsResponse);
        }

        return commentsResponse;
    }

    private String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }
}

