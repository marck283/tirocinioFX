package it.disi.unitn.lasagna.tirociniofx;

import it.disi.unitn.lasagna.tirociniofx.supportedcodecs.AudioCodecs;
import it.disi.unitn.lasagna.tirociniofx.supportedcodecs.VideoCodecs;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

public class HelloApplication extends Application {

    private void videoCodecChoiceSetDefault(@NotNull ChoiceBox<String> codecChoice) {
        codecChoice.setItems(FXCollections.observableArrayList("a64_multi", "a64_multi5", "alias_pix", "amv",
                "apng", "asv1", "asv2", "libaom-av1", "librav1e", "libsvtav1", "av1_nvenc", "av1_qsv", "av1_amf",
                "avrp", "avui", "ayuv", "bitpacked", "bmp", "cfhd", "cinepak", "cljr", "dnxhd", "dpx", "dvvideo",
                "exr", "ffv1", "ffvhuff", "fits", "flashsv", "flashsv2", "flv", "gif", "h261", "h263", "h263p",
                "h264", "hap", "hdr", "hevc", "huffyuv", "jpeg200", "libopenjpg", "jpegls", "ljpeg", "magicyuv",
                "mjpeg", "mpeg1video", "mpeg2video", "mpeg2_qsv", "mpeg4", "msmpeg4v2", "msmpeg4", "msvideo1",
                "pam", "pbm", "pcx", "pfm", "pgm", "pgmyuv", "phm", "png", "ppm", "prores", "qoi", "qtrle", "r10k",
                "r210", "rawvideo", "roq", "rpza", "rv10", "rv20", "sgi", "smc", "snow", "speedhq", "sunrast",
                "svq1", "targa", "libtheora", "tiff", "utvideo", "v210", "v308", "v408", "v410", "vbn", "vnull",
                "libvpx", "libvpx-vp9", "vp9_qsv", "wbmp", "libwebp-anim", "libwebp", "wmv1", "wmv2",
                "wrapped_avframe", "xbm", "xface", "xwd", "y41p", "yuv4", "zlib", "zmbv"));
    }

    private void audioCodecChoiceSetDefault(@NotNull ChoiceBox<String> codecChoice) {
        codecChoice.setItems(FXCollections.observableArrayList("aac", "ac3", "ac3_fixed", "flac", "opus", "libfdk_aac",
                "libmp3lame", "libopencore-amrnb", "libopus", "libshine", "libtwolame", "libvo-amrwbenc", "libvorbis",
                "mjpeg", "wavpack"));
    }

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        HelloController helloController = fxmlLoader.getController();
        Scene scene = new Scene(root, 1280, 960);

        Locale l = Locale.getDefault();
        CheckBox ffmpegVersionCheckBox = (CheckBox)scene.getRoot().lookup("#ffmpegVersionCheckBox"),
        nnflag = (CheckBox)scene.getRoot().lookup("#nnflag");
        Label header = (Label)scene.getRoot().lookup("#header"), pix_fmt_label = (Label)scene.getRoot().lookup("#pix_fmt_label"),
                tts_label = (Label)scene.getRoot().lookup("#tts_label"),
                lan_label = (Label)scene.getRoot().lookup("#lan_label"),
                img_ext_label = (Label)scene.getRoot().lookup("#img_ext_label"),
                pictures = (Label)scene.getRoot().lookup("#pictures");
        Button file_chooser_button = (Button)scene.getRoot().lookup("#file_chooser_button");
        if(l == Locale.ITALY || l == Locale.ITALIAN) {
            header.setText("Benvenuti in Video Creator!");
            ffmpegVersionCheckBox.setText("Usa versione personalizzata di FFmpeg");
            pix_fmt_label.setText("Formato pixel:");
            tts_label.setText("Testo da convertire in audio:");
            lan_label.setText("Lingua:");
            nnflag.setText("Usare una rete neurale");
            pictures.setText("Immagini");
            img_ext_label.setText("Estensione immagini:");
            file_chooser_button.setText("Scegli immagine...");
        } else {
            header.setText("Welcome to Video Creator!");
            ffmpegVersionCheckBox.setText("Use custom FFmpeg version.");
            pix_fmt_label.setText("Pixel format:");
            tts_label.setText("Text to Speech:");
            lan_label.setText("Language:");
            nnflag.setText("Use a neural network");
            pictures.setText("Pictures");
            img_ext_label.setText("Image extension:");
            file_chooser_button.setText("Choose picture...");
        }

        ChoiceBox<String> videoCodecChoice = (ChoiceBox<String>)scene.getRoot().lookup("#codecChoice"),
        audioCodecChoice = (ChoiceBox<String>)scene.getRoot().lookup("#acodecChoice");

        videoCodecChoice.getSelectionModel().selectedIndexProperty().addListener((cl, i, ni) -> {
            int newInt = ni.intValue();
            String res = "";
            if(newInt >= 0) {
                res = videoCodecChoice.getItems().get(newInt);
            }
            helloController.videoCodecChosen(res);
        });

        audioCodecChoice.getSelectionModel().selectedIndexProperty().addListener((cl, i, ni) -> {
            int newInt = ni.intValue();
            String res = "";
            if(newInt >= 0) {
                res = audioCodecChoice.getItems().get(newInt);
            }
            helloController.audioCodecChosen(res);
        });

        TextField pix_fmt_text_field = (TextField)scene.getRoot().lookup("#pix_fmt_text");
        pix_fmt_text_field.textProperty().addListener((cl, old, newv) -> helloController.pix_fmt_chosen(newv));

        TextField img_ext_field = (TextField)scene.getRoot().lookup("#img_ext_text");
        img_ext_field.textProperty().addListener((cl, old, newv) -> helloController.setImgExt(newv));

        TextField tts_text_field = (TextField)scene.getRoot().lookup("#tts_text");
        tts_text_field.textProperty().addListener((cl, old, newv) -> helloController.setTTSText(newv));

        TextField language_field = (TextField)scene.getRoot().lookup("#language");
        language_field.textProperty().addListener((cl, old, newv) -> helloController.setLanguage(newv));

        ffmpegVersionCheckBox.setOnMouseClicked(e -> {
            if(ffmpegVersionCheckBox.isSelected()) {
                VideoCodecs videoCodecs = new VideoCodecs(videoCodecChoice);
                AudioCodecs audioCodecs = new AudioCodecs(audioCodecChoice);
                try {
                    videoCodecs.enumCodecs();
                    audioCodecs.enumCodecs();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                //Use default FFmpeg version
                videoCodecChoiceSetDefault(videoCodecChoice);
                audioCodecChoiceSetDefault(audioCodecChoice);
            }
        });

        //Use default FFmpeg version
        videoCodecChoiceSetDefault(videoCodecChoice);
        audioCodecChoiceSetDefault(audioCodecChoice);

        stage.setTitle("Video Creator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}