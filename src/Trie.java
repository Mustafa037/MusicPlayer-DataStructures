import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Şarkı adına göre Trie'ye ekleme
    public void insert(String keyword, Song song) {
        TrieNode current = root;
        keyword = keyword.toLowerCase(); // Küçük/büyük harf duyarlılığını kaldırıyoruz

        for (char ch : keyword.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.songsAtThisNode.add(song);
    }

    // Kullanıcının girdiği kelimeyle (örnek: "ho") başlayan şarkıları bulma
    public List<Song> searchPrefix(String prefix) {
        TrieNode current = root;
        prefix = prefix.toLowerCase();

        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new ArrayList<>(); // Eşleşme yoksa boş liste dön
            }
            current = current.children.get(ch);
        }
        
        // Önek (prefix) bulundu. Şimdi bu düğümün altındaki tüm tamamlanmış kelimeleri (şarkıları) toplayalım.
        List<Song> results = new ArrayList<>();
        collectAllSongs(current, results);
        return results;
    }

    // Ağacın alt dallarına inip şarkıları toplayan yardımcı (Recursive) metot
    private void collectAllSongs(TrieNode node, List<Song> results) {
        if (node.isEndOfWord) {
            results.addAll(node.songsAtThisNode);
        }
        for (TrieNode child : node.children.values()) {
            collectAllSongs(child, results);
        }
    }
}