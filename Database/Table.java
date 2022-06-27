package Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

import Request.Convertor;

public class Table {
    Path path;

    public Table(String name) {

        String innerFolder = "";
        if (name.startsWith("User")) {
            innerFolder = "User";
        } else if (name.startsWith("Post")) {
            innerFolder = "Post";
        } else if (name.startsWith("Comment")) {
            innerFolder = "Comment";
        } else if (name.startsWith("Forum")) {
            innerFolder = "Forum";
        }

        path = Paths.get("Database" + File.separator + innerFolder + File.separator + name + ".txt");
        System.err.println("path: " + path);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.err.println("Could not create file: " + name);
            e.printStackTrace();
        }
    }

    public void insert(Map<String, String> data) {
        try {
            Files.writeString(path, Convertor.mapToString(data) + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not insert data");
        }
    }

    public void insert(AbstractMap.SimpleEntry<String, String> data, Predicate<Map<String, String>> condition) {
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (condition.test(Convertor.stringToMap(lines.get(i)))) {
                    Map<String, String> mapLine = Convertor.stringToMap(lines.get(i));
                    List<String> elements = new ArrayList<>(Convertor.stringToList(mapLine.get(data.getKey())));
                    elements.add(data.getValue());

                    if (elements.contains("-")) {
                        elements.remove("-");
                    }
                    mapLine.put(data.getKey(), Convertor.listToString(elements));

                    lines.set(i, Convertor.mapToString(mapLine));
                }
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("Could not delete data");
        }
    }

    public void update(Map<String, String> data, Predicate<Map<String, String>> condition) {
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (condition.test(Convertor.stringToMap(lines.get(i)))) {
                    Map<String, String> mapLine = Convertor.stringToMap(lines.get(i));
                    Map<String, String> mergedData = Convertor.merge(mapLine, data);
                    
                    lines.set(i, Convertor.mapToString(mergedData));
                }
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("Could not update data");
        }
    }

    public void delete(Predicate<Map<String, String>> condition) {
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (condition.test(Convertor.stringToMap(lines.get(i)))) {
                    lines.remove(i);
                }
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("Could not delete data");
        }
    }

    public void delete(AbstractMap.SimpleEntry<String, String> data, Predicate<Map<String, String>> condition) {
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                if (condition.test(Convertor.stringToMap(lines.get(i)))) {
                    Map<String, String> mapLine = Convertor.stringToMap(lines.get(i));
                    List<String> elements = new ArrayList<>(Convertor.stringToList(mapLine.get(data.getKey())));
                    elements.remove(data.getValue());

                    if (elements.size() == 0) {
                        elements.add("-");
                    }

                    mapLine.put(data.getKey(), Convertor.listToString(elements));

                    lines.set(i, Convertor.mapToString(mapLine));
                }
            }
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("Could not delete data");
        }
    }

    public List<Map<String, String>> selectAll(Predicate<Map<String, String>> condition) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (condition.test(Convertor.stringToMap(line))) {
                    result.add(Convertor.stringToMap(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not select data");
        }
        return result;
    }

    public Map<String, String> selectFirst(Predicate<Map<String, String>> condition) {
        Map<String, String> result = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (condition.test(Convertor.stringToMap(line))) {
                    result = Convertor.stringToMap(line);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not select data");
        }
        return result;
    }


    public List<Map<String, String>> getAll() {
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            Scanner sc = new Scanner(Files.newBufferedReader(path));
            while (sc.hasNextLine()) {
                data.add(Convertor.stringToMap(sc.nextLine()));
            }
            sc.close();
        } catch (IOException e) {
            System.err.println("Could not get data");
        }
        return data;
    }
    
}
