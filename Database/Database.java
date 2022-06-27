package Database;

import java.util.*;

public class Database {
    private static Database database;
    private Map<String, Table> tables;

    private Database() {
        tables = new HashMap<String, Table>();
    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public void addTable(String name) {
        tables.put(name, new Table(name));
    }

    public Table getTable(String name) {
        if (!tables.containsKey(name)) {
            addTable(name);
        }
        return tables.get(name);
    }

    
    
}
