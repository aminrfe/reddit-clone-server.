package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Database.Database;
import Request.Convertor;

public interface User {

    static String insertUser(Map<String, String> data) {
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

    static String updateUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserAccounts").update(data, condition);
        return "Done";
    }

    static String updateUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserPosts").update(data, condition);
        return "Done";
    }

    static String updateUserForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserForums").update(data, condition);
        return "Done";
    }

    static String deleteUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserAccounts").delete(condition);
        return "Done";
    }

    static String deleteUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserPosts").delete(condition);
        return "Done";
    }

    static String deleteUserForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UserForums").delete(condition);
        return "Done";
    }

    static String getUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        return Convertor.mapToString(Database.getDatabase().getTable("UserAccounts").selectFirst(condition));
    }

    static String getUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> posts = Database.getDatabase().getTable("UserPosts").selectFirst(condition);

        if (posts.get("posts").equals("-")) {
            return "-";
        }
        List<String> postsIds = Convertor.stringToList(posts.get("posts"));
        
        return postsIds.stream().map(id -> {
            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
            return Post.getPost(idMap);
        }).collect(Collectors.joining("\n"));
    }


    static String getUserSavedPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> posts = Database.getDatabase().getTable("UserPosts").selectFirst(condition);

        if (posts.get("savedPosts").equals("-")) {
            return "-";
        }
        List<String> postsIds = Convertor.stringToList(posts.get("savedPosts"));
        
        return postsIds.stream().map(id -> {
            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
            return Post.getPost(idMap);
        }).collect(Collectors.joining("\n"));
    }

    static String getUserFollowedForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> forums = Database.getDatabase().getTable("UserForums").selectFirst(condition);

        if (forums.get("followedForums").equals("-")) {
            return "-";
        }
        List<String> forumsNames = Convertor.stringToList(forums.get("followedForums"));
        
        return forumsNames.stream().map(name -> {
            Map<String, String> nameMap = new HashMap<>();
            nameMap.put("name", name);
            return Forum.getForum(nameMap);
        }).collect(Collectors.joining("\n"));
    }

    static String getUserFavoriteForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> forums = Database.getDatabase().getTable("UserForums").selectFirst(condition);

        if (forums.get("favoriteForums").equals("-")) {
            return "-";
        }
        List<String> forumsNames = Convertor.stringToList(forums.get("favoriteForums"));

        return forumsNames.stream().map(name -> {
            Map<String, String> nameMap = new HashMap<>();
            nameMap.put("name", name);
            return Forum.getForum(nameMap);
        }).collect(Collectors.joining("\n"));
    }

}
