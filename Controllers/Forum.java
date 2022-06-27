package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Database.Database;
import Request.Convertor;

public interface Forum {

    default String insertForum(Map<String, String> data) {
        Database.getDatabase().getTable("ForumsList").insert(data);

        Map<String, String> emptyPosts = new HashMap<>();
        emptyPosts.put("name", data.get("name"));
        emptyPosts.put("posts", "-");
        Database.getDatabase().getTable("ForumPosts").insert(emptyPosts);

        Map<String, String> forumDetails = new HashMap<>();
        forumDetails.put("name", data.get("name"));
        forumDetails.put("desc", data.get("desc"));
        forumDetails.put("admin", data.get("admin"));
        Database.getDatabase().getTable("ForumDetails").insert(forumDetails);

        return "Done";
    }

    default String insertForumPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("posts", data.get("posts"));

        Database.getDatabase().getTable("ForumPosts").insert(entry, condition);
        return "Done";
    }



    static String updateForumsList(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Database.getDatabase().getTable("ForumsList").update(data, condition);
        return "Done";
    }

    static String updateForumPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Database.getDatabase().getTable("ForumPosts").update(data, condition);
        return "Done";
    }

    default String updateForumDetails(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Database.getDatabase().getTable("ForumDetails").update(data, condition);
        return "Done";
    }

    default String getForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        return Convertor.mapToString(Database.getDatabase().getTable("ForumsList").selectFirst(condition));
    }

    default String getForumPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Map<String, String> posts = Database.getDatabase().getTable("ForumPosts").selectFirst(condition);

        if (posts.get("posts").equals("-")) {
            return "-";
        }
        List<String> postsIds = Convertor.stringToList(posts.get("posts"));
        
        return postsIds.stream().map(id -> {
            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
            return new Controller().getPost(idMap);
        }).collect(Collectors.joining("\n"));
    }

    default String getForumDetails(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };
        
        return Convertor.mapToString(Database.getDatabase().getTable("ForumDetails").selectFirst(condition));
    }

    default String deleteForumPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("posts", data.get("posts"));

        Database.getDatabase().getTable("ForumPosts").delete(entry, condition);
        return "Done";
    }


    

}
