package Models;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import Database.Database;

public class User {
    private final Map<String, String> data;
    
    public User(Map<String, String> data) {
        this.data = data;
    }

    public String get(String key) {
        return data.get(key);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public Map<String, String> getData() {
        return data;
    }

    public String addUser() {
        Database.getDatabase().getTable("UserAccounts").insert(data);

        Map<String, String> emptyPosts = new HashMap<>();
        emptyPosts.put("username", data.get("username"));
        emptyPosts.put("posts", "-");
        emptyPosts.put("savedPosts", "-");
        Database.getDatabase().getTable("UserPosts").insert(emptyPosts);

        Map<String, String> emptyForums = new HashMap<>();
        emptyForums.put("username", data.get("username"));
        emptyForums.put("followedForums", "-");
        emptyForums.put("favoriteForums", "-");
        Database.getDatabase().getTable("UserForums").insert(emptyForums);

        return "Done";
    }

    public String updateUserAccount() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserAccounts").update(data, condition);
        return "Done";
    }

    public String updateUserPosts() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserPosts").update(data, condition);
        return "Done";
    }

    public String updateUserForums() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserForums").update(data, condition);
        return "Done";
    }

    public String deleteUserAccount() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserAccounts").delete(condition);
        return "Done";
    }

    public String deleteUserPosts() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserPosts").delete(condition);
        return "Done";
    }

    public String deleteUserForums() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        Database.getDatabase().getTable("UserForums").delete(condition);
        return "Done";
    }

    public Map<String, String> getUserAccount() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        return Database.getDatabase().getTable("UserAccounts").selectFirst(condition);
    }

    public Map<String, String> getUserPosts() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        return Database.getDatabase().getTable("UserPosts").selectFirst(condition);
    }

    public Map<String, String> getUserForums() {
        Predicate<Map<String, String>> condition = (data) -> {
            return data.get("username").equals(this.data.get("username"));
        };

        return Database.getDatabase().getTable("UserForums").selectFirst(condition);
    }

}
