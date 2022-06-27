package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Database.Database;
import Request.Convertor;

public interface Post {

    static String insertPost(Map<String, String> data) {
        Database.getDatabase().getTable("PostsDetail").insert(data);

        Map<String, String> emptyVotes = new HashMap<>();
        emptyVotes.put("upvotes", "-");
        emptyVotes.put("downvotes", "-");
        Database.getDatabase().getTable("PostsVotes").insert(emptyVotes);

        Map<String, String> emptyComments = new HashMap<>();
        emptyComments.put("comments", data.get("-"));
        Database.getDatabase().getTable("PostsComments").insert(emptyComments);

        return "Done";
    }

    static String insertPostUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("postID").equals(data.get("postID"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("PostsVotes").insert(entry, condition);
        return "Done";
    }

    static String insertPostDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("postID").equals(data.get("postID"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("PostsVotes").insert(entry, condition);
        return "Done";
    }

    static String insertPostComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("postID").equals(data.get("postID"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("comments", data.get("comments"));

        Database.getDatabase().getTable("PostsComments").insert(entry, condition);
        return "Done";
    }

    static String updatePostDetail(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsDetail").update(data, condition);
        return "Done";
    }

    static String updatePostVotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsVotes").update(data, condition);
        return "Done";
    }

    static String updatePostComments(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsComments").update(data, condition);
        return "Done";
    }

    static String deletePost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsDetail").delete(condition);
        Database.getDatabase().getTable("PostsVotes").delete(condition);
        Database.getDatabase().getTable("PostsComments").delete(condition);
        return "Done";
    }

    static String deletePostUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("PostsVotes").delete(entry, condition);
        return "Done";
    }

    static String deletePostDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("PostsVotes").delete(entry, condition);
        return "Done";
    }

    static String deletePostComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("comments", data.get("comments"));

        Database.getDatabase().getTable("PostsComments").delete(entry, condition);
        return "Done";
    }
    
    static String getPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> result = Database.getDatabase().getTable("PostsDetail").selectFirst(condition);
        result = Convertor.merge(result, Database.getDatabase().getTable("PostsVotes").selectFirst(condition));
        result = Convertor.merge(result, Database.getDatabase().getTable("PostsComments").selectFirst(condition));
        
        return Convertor.mapToString(result);
    }

    static String getPostUpvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("PostsVotes").selectFirst(condition);


        Map<String, String> result = new HashMap<>();
        result.put("upvotes", votes.get("upvotes"));
        return Convertor.mapToString(result);
    }

    static String getPostDownvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("PostsVotes").selectFirst(condition);

        Map<String, String> result = new HashMap<>();
        result.put("downvotes", votes.get("downvotes"));
        return Convertor.mapToString(result);
    }

    static String getPostComments(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Map<String, String> comments = Database.getDatabase().getTable("PostsComments").selectFirst(condition);

        if (comments.get("comments").equals("-")) {
            return "-";
        }
        List<String> commentsIds = Convertor.stringToList(comments.get("comments"));
        
        return commentsIds.stream().map(id -> {
            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
            return Post.getPost(idMap);
        }).collect(Collectors.joining("\n"));
    }
}