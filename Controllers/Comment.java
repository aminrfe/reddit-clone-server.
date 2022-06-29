package Controllers;

import java.util.*;
import java.util.function.Predicate;

import Database.Database;
import Request.Convertor;

public interface Comment {
    
    default String insertComment(Map<String, String> data) {
        Database.getDatabase().getTable("CommentsDetail").insert(data);

        Map<String, String> emptyVotes = new HashMap<>();
        emptyVotes.put("upvotes", "-");
        emptyVotes.put("downvotes", "-");
        Database.getDatabase().getTable("CommentsVotes").insert(emptyVotes);

        return "Done";
    }

    default String insertCommentUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("CommnetsVotes").insert(entry, condition);
        return "Done";
    }

    default String insertCommentDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("CommnetsVotes").insert(entry, condition);
        return "Done";
    }

    default String updateCommentDetail(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("CommentsDetail").update(data, condition);
        return "Done";
    }

    default String updateCommentVotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("CommentsVotes").update(data, condition);
        return "Done";
    }

    default String deleteComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("CommentsDetail").delete(condition);
        Database.getDatabase().getTable("CommentsVotes").delete(condition);
        return "Done";
    }

    default String deleteCommentUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("CommentsVotes").delete(entry, condition);
        return "Done";
    }

    default String deleteCommentDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("CommentsVotes").delete(entry, condition);
        return "Done";
    }

    default String getComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> result = Database.getDatabase().getTable("CommentsDetail").selectFirst(condition);
        result = Convertor.merge(result, Database.getDatabase().getTable("CommentsVotes").selectFirst(condition));
        
        return Convertor.mapToString(result);
    }

    default String getCommentUpvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("CommentsVotes").selectFirst(condition);


        Map<String, String> result = new HashMap<>();
        result.put("upvotes", votes.get("upvotes"));
        return Convertor.mapToString(result);
    }

    default String getCommentDownvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("CommentsVotes").selectFirst(condition);

        Map<String, String> result = new HashMap<>();
        result.put("downvotes", votes.get("downvotes"));
        return Convertor.mapToString(result);
    }

    default String genCommentId(Map<String, String> data) {
        Map<String, String> lastPost = Database.getDatabase().getTable("CommentsDetail").selectLast();
        
        if (lastPost.isEmpty()) {
            return "c1";
        }
        String lastId = lastPost.get("id").replace("c", "");
        int id = Integer.parseInt(lastId) + 1;
        return "c" + id;
    }
}
