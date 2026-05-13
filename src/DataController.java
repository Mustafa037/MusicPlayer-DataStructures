import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // Merge Sort'un çalışması için gereken liste yapısı

public class DataController {
    
    // Veri yapıları
    private Map<Integer, Song> songLibrary;
    private Trie searchEngine; 

    public DataController() {
        songLibrary = new HashMap<>();
        searchEngine = new Trie(); 
        loadMockData();
    }

    // Kütüphaneye şarkı ekleme
    public void addSong(Song song) {
        songLibrary.put(song.getId(), song);
        searchEngine.insert(song.getTitle(), song); 
    }

    // ID'ye göre şarkı getirme
    public Song getSong(int id) {
        return songLibrary.get(id);
    }
    
    public int getTotalSongs() {
        return songLibrary.size();
    }

    // Arama Metodu
    public List<Song> search(String keyword) {
        return searchEngine.searchPrefix(keyword);
    }

    // ---------------- SIRALAMA (MERGE SORT) İŞLEMLERİ ----------------

    public List<Song> getSortedSongs() {
        List<Song> songList = new ArrayList<>(songLibrary.values());
        
        if (!songList.isEmpty()) {
            mergeSort(songList, 0, songList.size() - 1);
        }
        return songList;
    }

    private void mergeSort(List<Song> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right); 
        }
    }

    private void merge(List<Song> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Song> leftList = new ArrayList<>(n1);
        List<Song> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) leftList.add(list.get(left + i));
        for (int j = 0; j < n2; ++j) rightList.add(list.get(mid + 1 + j));

        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            if (leftList.get(i).getTitle().compareToIgnoreCase(rightList.get(j).getTitle()) <= 0) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }
        
        while (i < n1) { list.set(k, leftList.get(i)); i++; k++; }
        while (j < n2) { list.set(k, rightList.get(j)); j++; k++; }
    }

    // ---------------- TEST VERİLERİ ----------------
    private void loadMockData() {
        addSong(new Song(1, "Bohemian Rhapsody", "Queen", 354, "/music/bohemian.mp3"));
        addSong(new Song(2, "Hotel California", "Eagles", 390, "/music/hotel.mp3"));
        addSong(new Song(3, "Smells Like Teen Spirit", "Nirvana", 301, "/music/smells.mp3"));
        addSong(new Song(4, "Stairway to Heaven", "Led Zeppelin", 482, "/music/stairway.mp3"));
    }
}