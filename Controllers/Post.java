package Controllers;

import java.util.*;
import java.util.function.Predicate;

import Database.Database;
import Request.Convertor;

public interface Post {

    default String insertPost(Map<String, String> data) {
        Database.getDatabase().getTable("PostsDetail").insert(data);

        Map<String, String> emptyVotes = new HashMap<>();
        emptyVotes.put("id", data.get("id"));
        emptyVotes.put("upvotes", "-");
        emptyVotes.put("downvotes", "-");
        Database.getDatabase().getTable("PostsVotes").insert(emptyVotes);

        Map<String, String> emptyComments = new HashMap<>();
        emptyComments.put("id", data.get("id"));
        emptyComments.put("comments", "-");
        Database.getDatabase().getTable("PostsComments").insert(emptyComments);

        return "Done";
    }

    default String insertPostComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("comments", data.get("comments"));

        Database.getDatabase().getTable("PostsComments").insert(entry, condition);
        return "Done";
    }

    default String updatePostVotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsVotes").update(data, condition);
        return "Done";
    }

    default String deletePost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsDetail").delete(condition);
        Database.getDatabase().getTable("PostsVotes").delete(condition);
        Database.getDatabase().getTable("PostsComments").delete(condition);
        return "Done";
    }
    
    default String getPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> result = Database.getDatabase().getTable("PostsDetail").selectFirst(condition);
        System.err.println(result);
        result = Convertor.merge(result, Database.getDatabase().getTable("PostsVotes").selectFirst(condition));
        System.err.println(result);
        result = Convertor.merge(result, Database.getDatabase().getTable("PostsComments").selectFirst(condition));
        System.err.println(result);
        
        return Convertor.mapToString(result);
    }

    default String genPostId(Map<String, String> data) {
        Map<String, String> lastPost = Database.getDatabase().getTable("PostsDetail").selectLast();
        
        if (lastPost.isEmpty()) {
            return "p1";
        }
        String lastId = lastPost.get("id").replace("p", "");
        int id = Integer.parseInt(lastId) + 1;
        return "p" + id;
    }
}
