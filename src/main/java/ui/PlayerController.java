package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import library.DataController;
import model.Song;
import player.PlaybackManager;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class PlayerController {

    private DataController dataController;

    // Arkadaşlarımızın yazdığı müzik motoru ve veri yapıları yöneticisi
    private PlaybackManager playbackManager;

    // Ekranda o an listelenen şarkı nesnelerini hafızada tutmak için (Tıklama eşleşmesi için)
    private List<Song> currentSongsInList;

    private ObservableList<String> observableSongList;
    private boolean isPlaying = false; // Çalma durumunu takip etmek için

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> songListView;

    @FXML
    private Button prevButton;

    @FXML
    private Button playButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label nowPlayingLabel;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private ListView<String> playlistView;

    private ObservableList<String> observablePlaylistList;

    private List<Song> playlistSongs = new ArrayList<>();

    @FXML
    public void initialize() {
        dataController = new DataController();
        playbackManager = new PlaybackManager();
        currentSongsInList = new ArrayList<>();

        observableSongList = FXCollections.observableArrayList();
        songListView.setItems(observableSongList);

        // 1. DataController'daki tüm şarkıları PlaybackManager'ın DoublyLinkedList (playlist) yapısına besliyoruz
        List<Song> allSongs = dataController.getSortedSongs();
        for (Song sarki : allSongs) {
            playbackManager.addToPlaylist(sarki);
        }

        // 2. Program ilk açıldığında Merge Sort ile sıralanmış listeyi ekrana basıyoruz
        listAllSongsSorted();

        // 3. Listeden bir şarkıya ÇİFT tıklandığında müzik motorunu tetikleme ayarı
        songListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleSongSelection();
            }
        });
        observablePlaylistList = FXCollections.observableArrayList();
        playlistView.setItems(observablePlaylistList);

        playlistView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                playFromPlaylist(null);
            }
        });

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            playbackManager.setVolume(newVal.doubleValue());
        });
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            double current = playbackManager.getCurrentTime();
            double total = playbackManager.getTotalDuration();
            if (total > 0) {
                progressBar.setValue(current / total);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        loadPlaylist();
    }

    // Listeden bir şarkı çift tıklandığında çalışacak metot
    private void handleSongSelection() {
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();

        // Geçerli bir satıra mı tıklandı kontrolü
        if (selectedIndex >= 0 && selectedIndex < currentSongsInList.size()) {
            Song secilenSarki = currentSongsInList.get(selectedIndex);

            System.out.println("Şuan çalınıyor: " + secilenSarki.getTitle());
            playbackManager.play(secilenSarki);

            isPlaying = true;
            playButton.setText("⏸ Duraklat");
            nowPlayingLabel.setText("▶ " + secilenSarki.getTitle() + " - " + secilenSarki.getArtist());
        }
    }

    // Arama butonuna basıldığında (Trie Algoritması entegrasyonu)
    @FXML
    void handleSearch(ActionEvent event) {
        String arananKelime = searchField.getText().trim();

        if (arananKelime.isEmpty()) {
            listAllSongsSorted();
            return;
        }

        // Trie ağacında önek araması yapıyoruz
        List<Song> bulunanSarkilar = dataController.search(arananKelime);

        observableSongList.clear();
        currentSongsInList.clear(); // Hafızadaki listeyi de temizliyoruz

        if (bulunanSarkilar.isEmpty()) {
            observableSongList.add("Eşleşen şarkı bulunamadı...");
        } else {
            for (Song sarki : bulunanSarkilar) {
                observableSongList.add(sarki.getTitle() + " - " + sarki.getArtist());
                currentSongsInList.add(sarki); // Tıklama olayı için şarkıyı hafızaya alıyoruz
            }
        }
    }

    // Tüm kütüphaneyi alfabetik (Merge Sort) listeleme
    private void listAllSongsSorted() {
        observableSongList.clear();
        currentSongsInList.clear();

        List<Song> siraliSarkilar = dataController.getSortedSongs();
        for (Song sarki : siraliSarkilar) {
            observableSongList.add(sarki.getTitle() + " - " + sarki.getArtist());
            currentSongsInList.add(sarki);
        }
    }

    // ▶ Çal / Duraklat Butonu
    @FXML
    void togglePlay(ActionEvent event) {
        // Eğer şu an bir şarkı çalıyorsa duraklat
        if (isPlaying) {
            playbackManager.pause();
            playButton.setText("▶ Çal / Duraklat");
            isPlaying = false;
            System.out.println("Müzik duraklatıldı.");
        }
        // Eğer duraklatılmışsa devam et (Resume)
        else {
            // Eğer henüz hiçbir şarkı seçilmemişse listenin ilk şarkısını çal
            if (playbackManager.getCurrentSong() == null && !currentSongsInList.isEmpty()) {
                playbackManager.play(currentSongsInList.get(0));
                System.out.println("Liste başından başlanıyor: " + currentSongsInList.get(0).getTitle());
            } else {
                playbackManager.resume();
                System.out.println("Müziğe devam ediliyor.");
            }
            playButton.setText("⏸ Duraklat");
            isPlaying = true;
        }
    }

    // ⏭ Sonraki Butonu (Queue veya DoublyLinkedList'teki Next mantığı tetiklenir)
    @FXML
    void playNext(ActionEvent event) {
        playbackManager.next();
        isPlaying = true;
        playButton.setText("⏸ Duraklat");

        Song suAnCalan = playbackManager.getCurrentSong();
        if (suAnCalan != null) {
            System.out.println("Sonraki şarkıya geçildi: " + suAnCalan.getTitle());
            // Arayüzdeki listede çalan şarkıyı otomatik seçili hale getirelim (Görsel senkronizasyon)
            syncListViewSelection(suAnCalan);
            nowPlayingLabel.setText("▶ " + suAnCalan.getTitle() + " - " + suAnCalan.getArtist());
        }
    }

    // ⏮ Önceki Butonu (Stack yapısındaki geçmişten Pop yapar)
    @FXML
    void playPrevious(ActionEvent event) {
        playbackManager.previous();
        isPlaying = true;
        playButton.setText("⏸ Duraklat");

        Song suAnCalan = playbackManager.getCurrentSong();
        if (suAnCalan != null) {
            System.out.println("Önceki şarkıya geri dönüldü: " + suAnCalan.getTitle());
            syncListViewSelection(suAnCalan);
            nowPlayingLabel.setText("▶ " + suAnCalan.getTitle() + " - " + suAnCalan.getArtist());
        }
    }

    // Çalan şarkıyı listede mavi renkle seçili gösteren yardımcı fonksiyon
    private void syncListViewSelection(Song targetSong) {
        for (int i = 0; i < currentSongsInList.size(); i++) {
            if (currentSongsInList.get(i).getId() == targetSong.getId()) {
                songListView.getSelectionModel().select(i);
                break;
            }
        }
    }

    @FXML
    void handleShuffle(ActionEvent event) {
        playbackManager.shuffle();
        listAllSongsSorted();
        Song current = playbackManager.getCurrentSong();
        if (current != null) {
            nowPlayingLabel.setText("▶ " + current.getTitle() + " - " + current.getArtist());
            syncListViewSelection(current);
        }
    }

    @FXML
    void handleSongPlay(ActionEvent event) {
        handleSongSelection();
    }

    @FXML
    void addToPlaylist(ActionEvent event) {
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentSongsInList.size()) {
            Song secilen = currentSongsInList.get(selectedIndex);
            observablePlaylistList.add(secilen.getTitle() + " - " + secilen.getArtist());
            playlistSongs.add(secilen);
        }
        savePlaylist();
    }

    @FXML
    void addToQueue(ActionEvent event) {
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentSongsInList.size()) {
            Song secilen = currentSongsInList.get(selectedIndex);
            playbackManager.addToQueue(secilen);
            System.out.println("Sıraya eklendi: " + secilen.getTitle());
        }
    }

    @FXML
    void playFromPlaylist(ActionEvent event) {
        int selectedIndex = playlistView.getSelectionModel().getSelectedIndex();
        System.out.println("Seçilen index: " + selectedIndex);
        System.out.println("Playlist boyutu: " + playlistSongs.size());

        if (selectedIndex >= 0 && selectedIndex < playlistSongs.size()) {
            Song secilen = playlistSongs.get(selectedIndex);
            System.out.println("Çalınıyor: " + secilen.getTitle());
            playbackManager.play(secilen);
            isPlaying = true;
            playButton.setText("⏸ Duraklat");
            nowPlayingLabel.setText("▶ " + secilen.getTitle() + " - " + secilen.getArtist());
        }
    }

    @FXML
    void removeFromPlaylist(ActionEvent event) {
        int selectedIndex = playlistView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            observablePlaylistList.remove(selectedIndex);
            playlistSongs.remove(selectedIndex);
        }
        savePlaylist();
    }

    private void savePlaylist() {
        try (Writer writer = new FileWriter("playlist.json")) {
            new Gson().toJson(playlistSongs, writer);
        } catch (IOException e) {
            System.out.println("Playlist kaydedilemedi: " + e.getMessage());
        }
    }

    private void loadPlaylist() {
        File file = new File("playlist.json");
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Song>>(){}.getType();
            List<Song> loaded = new Gson().fromJson(reader, type);
            if (loaded != null) {
                for (Song song : loaded) {
                    playlistSongs.add(song);
                    observablePlaylistList.add(song.getTitle() + " - " + song.getArtist());
                }
            }
        } catch (IOException e) {
            System.out.println("Playlist yüklenemedi: " + e.getMessage());
        }
    }
}