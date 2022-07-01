package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Database.Database;
import Request.Convertor;

public interface User {

    default String insertUser(Map<String, String> data) {
        Database.getDatabase().getTable("UsersAccount").insert(data);

        Map<String, String> emptyPosts = new HashMap<>();
        emptyPosts.put("username", data.get("username"));
        emptyPosts.put("savedPosts", "-");
        Database.getDatabase().getTable("UsersPosts").insert(emptyPosts);

        Map<String, String> emptyForums = new HashMap<>();
        emptyForums.put("username", data.get("username"));
        emptyForums.put("followedForums", "-");
        emptyForums.put("favoriteForums", "-");
        Database.getDatabase().getTable("UsersForums").insert(emptyForums);

        return "Done";
    }

    default String insertUserForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("followedForums", data.get("followedForums"));

        Database.getDatabase().getTable("UsersForums").insert(entry, condition);
        return "Done";
    }

    default String updateUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersAccount").update(data, condition);
        return "Done";
    }

    default String updateUserPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersPosts").update(data, condition);
        return "Done";
    }

    default String updateUserForums(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Database.getDatabase().getTable("UsersForums").update(data, condition);
        return "Done";
    }

    default String getUserAccount(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        return Convertor.mapToString(Database.getDatabase().getTable("UsersAccount").selectFirst(condition));
    }


    default String getUserSavedPosts(Map<String, String> data) {
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
            return new Controller().getPost(idMap);
        }).collect(Collectors.joining("\n"));
    }

    default String getUserFollowedForums(Map<String, String> data) {
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
            return new Controller().getForum(nameMap);
        }).collect(Collectors.joining("\n"));
    }

    default String getUserFavoriteForums(Map<String, String> data) {
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
            return new Controller().getForum(nameMap);
        }).collect(Collectors.joining("\n"));
    }

    default String checkUser(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("username").equals(data.get("username"));
        };

        Map<String, String> user = Database.getDatabase().getTable("UsersAccount").selectFirst(condition);
        if (user.isEmpty()) {
            return "No User Found";
        } else {
            if (user.get("password").equals(data.get("password"))) {
                return "UserFound";
            } else {
                return "Wrong Password";
            }
        }
    }

    default String changeUserUsername(Map<String, String> data) {
        String oldUsername = data.get("oldUsername");
        String newUsername = data.get("newUsername");

        Database.getDatabase().getTable("UsersAccount").replace(oldUsername, newUsername);
        Database.getDatabase().getTable("UsersPosts").replace(oldUsername, newUsername);
        Database.getDatabase().getTable("UsersForums").replace(oldUsername, newUsername);

        Database.getDatabase().getTable("PostsDetail").replace(oldUsername, newUsername);
        Database.getDatabase().getTable("PostsVotes").replace(oldUsername, newUsername);

        Database.getDatabase().getTable("ForumsDetail").replace(oldUsername, newUsername);

        Database.getDatabase().getTable("CommentsDetail").replace(oldUsername, newUsername);
        Database.getDatabase().getTable("CommentsVotes").replace(oldUsername, newUsername);

        return "Done";
    }

}
