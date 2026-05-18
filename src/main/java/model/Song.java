package model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private int durationInSeconds;
    private String filePath;

    public Song(int id, String title, String artist, int durationInSeconds, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationInSeconds = durationInSeconds;
        this.filePath = filePath;
    }

    // Getter Metotları (Diğer geliştiriciler bunlara erişecek)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDuration() { return durationInSeconds; }
    public String getFilePath() { return filePath; }

    @Override
    public String toString() {
        return id + " - " + title + " (" + artist + ")";
    }
}
