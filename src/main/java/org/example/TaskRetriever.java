package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskRetriever {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        TaskRetriever app = new TaskRetriever();

        int userId = 1; // Замените на реальный ID пользователя
        Task[] openTasks = app.getOpenTasks(userId);

        System.out.println("Open tasks for user " + userId + ":");
        for (Task task : openTasks) {
            System.out.println(task);
        }
    }

    public Task[] getOpenTasks(int userId) throws IOException {
        String tasksUrl = BASE_URL + "/users/" + userId + "/todos";
        Task[] allTasks = getTasksFromUrl(tasksUrl);

        return filterOpenTasks(allTasks);
    }

    private Task[] getTasksFromUrl(String url) throws IOException {
        HttpURLConnection connection = openConnection(url);
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(reader, Task[].class);
        }
    }

    private HttpURLConnection openConnection(String url) throws IOException {
        URL apiUrl = new URL(url);
        return (HttpURLConnection) apiUrl.openConnection();
    }

    private Task[] filterOpenTasks(Task[] tasks) {
        return java.util.stream.Stream.of(tasks)
                .filter(task -> !task.isCompleted())
                .toArray(Task[]::new);
    }

    public static class Task {
        private int userId;
        private int id;
        private String title;
        private boolean completed;

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "userId=" + userId +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", completed=" + completed +
                    '}';
        }
    }
}

