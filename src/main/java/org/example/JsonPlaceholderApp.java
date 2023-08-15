package org.example;




import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;
public class JsonPlaceholderApp {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        JsonPlaceholderApp app = new JsonPlaceholderApp();


    }

    public User[] getAllUsers() throws IOException {
        String usersUrl = BASE_URL + "/users";
        String response = sendGetRequest(usersUrl);
        return gson.fromJson(response, User[].class);
    }

    public User getUserById(int userId) throws IOException {
        String userUrl = BASE_URL + "/users/" + userId;
        String response = sendGetRequest(userUrl);
        return gson.fromJson(response, User.class);
    }

    public User getUserByUsername(String username) throws IOException {
        String usersUrl = BASE_URL + "/users?username=" + username;
        String response = sendGetRequest(usersUrl);
        User[] users = gson.fromJson(response, User[].class);
        if (users.length > 0) {
            return users[0];
        }
        return null;
    }

    public User createUser(User user) throws IOException {
        String usersUrl = BASE_URL + "/users";
        String userJson = gson.toJson(user);
        String response = sendPostRequest(usersUrl, userJson);
        return gson.fromJson(response, User.class);
    }

    public User updateUser(int userId, User updatedUser) throws IOException {
        String userUrl = BASE_URL + "/users/" + userId;
        String userJson = gson.toJson(updatedUser);
        String response = sendPutRequest(userUrl, userJson);
        return gson.fromJson(response, User.class);
    }

    public boolean deleteUser(int userId) throws IOException {
        String userUrl = BASE_URL + "/users/" + userId;
        int responseCode = sendDeleteRequest(userUrl);
        return responseCode >= 200 && responseCode < 300;
    }

    private String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = HttpHelper.openConnection(url);
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    private String sendPostRequest(String url, String requestBody) throws IOException {
        HttpURLConnection connection = HttpHelper.openConnection(url);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }

    private String sendPutRequest(String url, String requestBody) throws IOException {
        HttpURLConnection connection = HttpHelper.openConnection(url);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }

    private int sendDeleteRequest(String url) throws IOException {
        HttpURLConnection connection = HttpHelper.openConnection(url);
        connection.setRequestMethod("DELETE");
        return connection.getResponseCode();
    }
}

