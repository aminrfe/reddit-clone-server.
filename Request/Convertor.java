package Request;

import java.util.*;

public class Convertor {
    
    public static Map<String, String> stringToMap(String data) {
        Map<String, String> map = new HashMap<String, String>();
        // System.err.println(data);
        String[] pairs = data.trim().split("\\|\\|");
        

        for (String pair : pairs) {
            int delIndex = pair.indexOf(":");
            // System.err.println(pair);
            // System.err.println(delIndex);
            String key = pair.substring(0, delIndex);
            String value = pair.substring(delIndex + 1);

            map.put(key, value);
        }
        return map;
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(":");
            sb.append(entry.getValue());
            sb.append("||");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public static Map<String, String> merge(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> result = new HashMap<String, String>();
        map1.forEach((key, value) -> {
            if (map2.containsKey(key)) {
                result.put(key, map2.get(key));
            } else {
                result.put(key, value);
            }
        });
        return result;
    }

    public static ArrayList<String> stringToList(String data) {
        ArrayList<String> list = new ArrayList<String>();
        String[] elements = data.trim().split(",");

        for (String element : elements) {
            list.add(element);
        }
        return list;
    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String element : list) {
            sb.append(element);
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
    
}
