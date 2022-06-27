package Controllers;

import java.util.Map;
import java.util.function.Predicate;

import Database.Database;
import Request.Convertor;

public interface Post {
    
    static String getPost(Map<String, String> data) {
        Predicate<Map<String, String>> condition = (newData) -> {
            return newData.get("id").equals(data.get("id"));
        };
        Map<String, String> result = Database.getDatabase().getTable("PostDetail").selectFirst(condition);
        result = Convertor.merge(result, Database.getDatabase().getTable("PostVotes").selectFirst(condition));
        result = Convertor.merge(result, Database.getDatabase().getTable("PostComments").selectFirst(condition));
        
        return Convertor.mapToString(result);
    }
}
