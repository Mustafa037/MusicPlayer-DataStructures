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
    private Song lastPlayed;

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
        playlist.setCurrentBySong(song);
        lastPlayed = song;
        audioService.play(song.getFilePath());
    }

    public void playCurrent() {
        Song current = playlist.getCurrent();
        if (current != null) {
            lastPlayed = current;
            audioService.play(current.getFilePath());
        }
    }

    public void next() {
        history.push(playlist.getCurrent());

        if (!upNext.isEmpty()) {
            lastPlayed = upNext.dequeue();
            audioService.play(lastPlayed.getFilePath());
        } else {
            Song nextSong = playlist.next();
            lastPlayed = nextSong;
            audioService.play(nextSong.getFilePath());
        }
    }

    public void previous() {
        if (!history.isEmpty()) {
            Song prevSong = history.pop();
            playlist.setCurrentBySong(prevSong);
            lastPlayed = prevSong;
            audioService.play(prevSong.getFilePath());
        }
    }

    public void shuffle() {
        java.util.List<Song> songs = new java.util.ArrayList<>();

        for (int i = 0; i < playlist.getSize(); i++) {
            songs.add(playlist.next());
        }

        java.util.Collections.shuffle(songs);

        playlist = new DoublyLinkedList();
        for (Song song : songs) {
            playlist.add(song);
        }

        lastPlayed = playlist.getCurrent();
        if (lastPlayed != null) {
            audioService.play(lastPlayed.getFilePath());
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

    public Song getCurrentSong() {
        if (lastPlayed != null) return lastPlayed;
        return playlist.getCurrent();
    }
}