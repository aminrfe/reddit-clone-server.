package Controllers;

import java.util.*;
import java.util.function.Predicate;

import Database.Database;
import Request.Convertor;

public interface Comment {
    
    default String insertComment(Map<String, String> data) {
        Database.getDatabase().getTable("CommentsDetail").insert(data);

        Map<String, String> emptyVotes = new HashMap<>();
        emptyVotes.put("id", data.get("id"));
        emptyVotes.put("upvotes", "-");
        emptyVotes.put("downvotes", "-");
        Database.getDatabase().getTable("CommentsVotes").insert(emptyVotes);

        return "Done";
    }

    default String updateCommentVotes(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };

        Database.getDatabase().getTable("CommentsVotes").update(data, condition);
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
