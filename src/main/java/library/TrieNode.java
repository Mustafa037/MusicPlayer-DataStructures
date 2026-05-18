package library;

import model.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    List<Song> songsAtThisNode; // Arama burada bitiyorsa, eşleşen şarkılar

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        songsAtThisNode = new ArrayList<>();
    }
}