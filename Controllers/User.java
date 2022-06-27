package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Database.Database;
import Request.Convertor;

public interface User {

    static String insertUser(Map<String, String> data) {
        Database.getDatabase().getTable("UsersAccount").insert(data);

        Map<String, String> emptyPosts = new HashMap<>();
        emptyPosts.put("username", data.get("username"));
        emptyPosts.put("posts", "-");
        emptyPosts.put("savedPosts", "-");
        Database.getDatabase().getTable("UsersPosts").insert(emptyPosts);

        Map<String, String> emptyForums = new HashMap<>();
        emptyForums.put("username", data.get("username"));
        emptyForums.put("followedForums", "-");
        emptyForums.put("favoriteForums", "-");
        Database.getDatabase().getTable("UsersForums").insert(emptyForums);

        return "Done";
    }

    static String insertUserPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("posts", data.get("posts"));

        Database.getDatabase().getTable("UsersPosts").insert(entry, condition);
        return "Done";
    }

    static String insertUserForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("followedForums", data.get("followedForums"));

        Database.getDatabase().getTable("UsersForums").insert(entry, condition);
        return "Done";
    }

    static String updateUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersAccount").update(data, condition);
        return "Done";
    }

    static String updateUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersPosts").update(data, condition);
        return "Done";
    }

    static String updateUserForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersForums").update(data, condition);
        return "Done";
    }

    static String deleteUser(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersAccount").delete(condition);
        Database.getDatabase().getTable("UsersPosts").delete(condition);
        Database.getDatabase().getTable("UsersForums").delete(condition);
        return "Done";
    }

    static String deleteUserPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("posts", data.get("posts"));

        Database.getDatabase().getTable("UsersPosts").delete(entry, condition);
        return "Done";
    }

    static String deleteUserSavedPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("savedPosts", data.get("savedPosts"));

        Database.getDatabase().getTable("UsersPosts").delete(entry, condition);
        return "Done";
    }

    static String deleteUserForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("followedForums", data.get("followedForums"));

        Database.getDatabase().getTable("UsersForums").delete(entry, condition);
        return "Done";
    }

    static String deleteUserFavoriteForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("favoriteForums", data.get("favoriteForums"));

        Database.getDatabase().getTable("UsersForums").delete(entry, condition);
        return "Done";
    }

    static String getUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        return Convertor.mapToString(Database.getDatabase().getTable("UsersAccount").selectFirst(condition));
    }

    static String getUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> posts = Database.getDatabase().getTable("UsersPosts").selectFirst(condition);

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

        Map<String, String> posts = Database.getDatabase().getTable("UsersPosts").selectFirst(condition);

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

        Map<String, String> forums = Database.getDatabase().getTable("UsersForums").selectFirst(condition);

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

        Map<String, String> forums = Database.getDatabase().getTable("UsersForums").selectFirst(condition);

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
