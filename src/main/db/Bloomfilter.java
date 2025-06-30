package src.main.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class Bloomfilter {
    private final BitSet bitSet;
    private final int size;
    private final int numHashes;
    private static final String[] algorithms = {"MD5", "SHA-1", "SHA-256"};


    public Bloomfilter(int size, int numHashes) {
        this.size = size;
        this.numHashes = numHashes;
        this.bitSet = new BitSet(size);
    }

    public void add(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            bitSet.set(hash);
        }
    }

    public boolean check(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    private int[] getHashes(String item) {
        int[] hashes = new int[numHashes];

        for (int i = 0; i < numHashes; i++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithms[i % algorithms.length]);
                byte[] digest = md.digest(item.getBytes());
                int hash = 0;
                for (byte b : digest) {
                    hash = (hash * 31) + (b & 0xff);
                }
                hashes[i] = Math.abs(hash) % size;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Hash algorithm not available", e);
            }
        }
        return hashes;
    }
}
