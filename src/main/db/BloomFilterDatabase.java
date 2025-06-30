package src.main.db;

import java.util.HashMap;
import java.util.Map;

public class BloomFilterDatabase {
    private final Map<String, String> database;
    private final Bloomfilter bloomFilter;

    public BloomFilterDatabase(int bloomSize, int numHashes) {
        this.database = new HashMap<>();
        this.bloomFilter = new Bloomfilter(bloomSize, numHashes);
    }

    public void insert(String key, String value) {
        database.put(key, value);
        bloomFilter.add(key);
    }

    public String query(String key) {
        if (!bloomFilter.check(key)) {
            return null; // Definitely not present
        }
        return database.getOrDefault(key, null); // Might be false positive
    }
}
