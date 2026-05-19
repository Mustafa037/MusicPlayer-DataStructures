package player;

import datastructures.DoublyLinkedList;
import datastructures.Queue;
import datastructures.Stack;
import model.Song;

public class PlaybackManager {

    private AudioService audioService;
    private DoublyLinkedList playlist;
    private Queue upNext;
    private Stack history;

    public PlaybackManager() {
        audioService = new AudioService();
        playlist = new DoublyLinkedList();
        upNext = new Queue();
        history = new Stack();
    }

    public void play(Song song) {
        if (playlist.getCurrent() != null) {
            history.push(playlist.getCurrent());
        }

        // YENİ: Listenin içindeki current ibresini çalan şarkıya senkronize ediyoruz!
        playlist.setCurrentBySong(song);

        audioService.play(song.getFilePath());
    }

    public void playCurrent() {
        Song current = playlist.getCurrent();
        if (current != null) {
            audioService.play(current.getFilePath());
        }
    }

    public void next() {
        history.push(playlist.getCurrent());

        if (!upNext.isEmpty()) {
            Song nextSong = upNext.dequeue();
            audioService.play(nextSong.getFilePath());
        } else {
            Song nextSong = playlist.next();
            audioService.play(nextSong.getFilePath());
        }
    }

    public void previous() {
        if (!history.isEmpty()) {
            Song prevSong = history.pop();

            // YENİ: Geçmişten şarkı alındığında listenin ibresini de o şarkıya çekiyoruz!
            playlist.setCurrentBySong(prevSong);

            audioService.play(prevSong.getFilePath());
        }
    }

    public void shuffle() {
        Song current = playlist.getCurrent();
        java.util.List<Song> songs = new java.util.ArrayList<>();

        for (int i = 0; i < playlist.getSize(); i++) {
            songs.add(playlist.next());
        }

        java.util.Collections.shuffle(songs);

        playlist = new DoublyLinkedList();
        for (Song song : songs) {
            playlist.add(song);
        }

        if (current != null) {
            audioService.play(playlist.getCurrent().getFilePath());
        }
    }

    public void pause() { audioService.pause(); }
    public void resume() { audioService.resume(); }
    public void stop() { audioService.stop(); }

    public void addToPlaylist(Song song) { playlist.add(song); }
    public void addToQueue(Song song) { upNext.enqueue(song); }

    public void setVolume(double volume) { audioService.setVolume(volume); }
    public double getCurrentTime() { return audioService.getCurrentTime(); }
    public double getTotalDuration() { return audioService.getTotalDuration(); }
    public Song getCurrentSong() { return playlist.getCurrent(); }
}