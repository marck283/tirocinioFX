package it.disi.unitn.lasagna.tirociniofx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HelloController {

    private static final List<String> base64List = new ArrayList<>();

    private String vcodec, acodec, pix_fmt, img_ext, tts_text, language;

    @FXML
    protected void onFileChooserOpen(@NotNull MouseEvent e) {
        Locale l = Locale.getDefault();
        FileChooser chooser = new FileChooser();
        if(l == Locale.ITALIAN || l == Locale.ITALY) {
            chooser.setTitle("Scegli un'immagine...");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tutte le immagini...", "*.*"));
        } else {
            chooser.setTitle("Choose a picture...");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Images", "*.*"));
        }
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Button b = (Button)e.getSource();
        Stage stage = (Stage)b.getScene().getWindow();
        File res = chooser.showOpenDialog(stage);
        byte[] encoded;
        try {
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(res));
            base64List.add(new String(encoded, StandardCharsets.US_ASCII));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void videoCodecChosen(@NotNull String vcodec) {
        this.vcodec = vcodec;
    }

    public void audioCodecChosen(@NotNull String acodec) {
        this.acodec = acodec;
    }

    public void pix_fmt_chosen(@NotNull String pix_fmt) {
        this.pix_fmt = pix_fmt;
    }

    public void setImgExt(@NotNull String img_ext) {
        this.img_ext = img_ext;
    }

    public void setTTSText(@NotNull String tts_text) {
        this.tts_text = tts_text;
    }

    public void setLanguage(@NotNull String language) {
        this.language = language;
    }

    @FXML
    protected void onHelloButtonClick() {
        //Create output video. Write the JSON file, then call tirocinio-shadow-1.0-jar
    }

}