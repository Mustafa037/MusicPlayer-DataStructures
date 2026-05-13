import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataController data = new DataController();

        System.out.println("--- Alfabetik Sirali Sarki Listesi ---");
        
        List<Song> sortedLibrary = data.getSortedSongs();
        
        for(Song s : sortedLibrary) {
            System.out.println(s.getTitle() + " - " + s.getArtist());
        }
    }
}