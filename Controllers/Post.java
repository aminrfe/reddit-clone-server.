package Controllers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    default String insertPostUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("PostsVotes").insert(entry, condition);
        return "Done";
    }

    default String insertPostDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("PostsVotes").insert(entry, condition);
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

    default String updatePostDetail(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsDetail").update(data, condition);
        return "Done";
    }

    default String updatePostVotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsVotes").update(data, condition);
        return "Done";
    }

    default String updatePostComments(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("PostsComments").update(data, condition);
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

    default String deletePostUpvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("upvotes", data.get("upvotes"));

        Database.getDatabase().getTable("PostsVotes").delete(entry, condition);
        return "Done";
    }

    default String deletePostDownvote(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("downvotes", data.get("downvotes"));

        Database.getDatabase().getTable("PostsVotes").delete(entry, condition);
        return "Done";
    }

    default String deletePostComment(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("comments", data.get("comments"));

        Database.getDatabase().getTable("PostsComments").delete(entry, condition);
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

    default String getPostUpvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("PostsVotes").selectFirst(condition);


        Map<String, String> result = new HashMap<>();
        result.put("upvotes", votes.get("upvotes"));
        return Convertor.mapToString(result);
    }

    default String getPostDownvotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> votes = Database.getDatabase().getTable("PostsVotes").selectFirst(condition);

        Map<String, String> result = new HashMap<>();
        result.put("downvotes", votes.get("downvotes"));
        return Convertor.mapToString(result);
    }

    default String getPostComments(Map<String, String> data) {
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
            return new Controller().getComment(idMap);
        }).collect(Collectors.joining("\n"));
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
