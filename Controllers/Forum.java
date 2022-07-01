package Controllers;

import java.util.*;
import java.util.function.Predicate;

import Database.Database;
import Request.Convertor;

public interface Forum {

    default String insertForum(Map<String, String> data) {
        Database.getDatabase().getTable("ForumsDetail").insert(data);

        Map<String, String> emptyPosts = new HashMap<>();
        emptyPosts.put("name", data.get("name"));
        emptyPosts.put("posts", "-");
        Database.getDatabase().getTable("ForumsPosts").insert(emptyPosts);

        return "Done";
    }

    default String insertForumPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("posts", data.get("posts"));

        Database.getDatabase().getTable("ForumsPosts").insert(entry, condition);
        return "Done";
    }

    default String updateForumDetail(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Database.getDatabase().getTable("ForumsDetail").update(data, condition);
        return "Done";
    }

    default String updateForumPosts(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Database.getDatabase().getTable("ForumsPosts").update(data, condition);
        return "Done";
    }

    default String getForum(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("name").equals(data.get("name"));
        };

        Map<String, String> result = Database.getDatabase().getTable("ForumsDetail").selectFirst(condition);
        System.err.println(result);
        result = Convertor.merge(result, Database.getDatabase().getTable("ForumsPosts").selectFirst(condition));
        System.err.println(result);

        return Convertor.mapToString(result);
    }



}
