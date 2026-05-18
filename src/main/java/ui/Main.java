package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        File fxmlDosyasi = new File("src/main/java/ui/player.fxml");

        if (!fxmlDosyasi.exists()) {
            System.err.println("KRİTİK HATA: player.fxml dosyası bulunamadı!");
            return;
        }

        URL fxmlUrl = fxmlDosyasi.toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // CSS dosyamızı pencereye giydiren satır
        scene.getStylesheets().add(new File("src/main/java/ui/style.css").toURI().toURL().toExternalForm());

        primaryStage.setTitle("Veri Yapıları - Müzik Çalar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}