package it.disi.unitn.lasagna.tirociniofx.supportedcodecs;

import javafx.scene.control.ChoiceBox;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AudioCodecs extends SupportedCodecs {

    public AudioCodecs(@NotNull ChoiceBox<String> codecChoice) {
        super(codecChoice);
    }

    @Override
    public void enumCodecs() throws IOException {
        String execFile = "";
        if(SystemUtils.IS_OS_LINUX) {
            execFile = "./src/cpp/enumCodecs/linux/audio";
        } else {
            if(SystemUtils.IS_OS_WINDOWS) {
                execFile = "./src/cpp/enumCodecs/windows/audio.exe";
            } else {
                System.err.println("Unsupported operating system.");
                System.exit(1);
            }
        }
        readFile(codecChoice, execFile, "./audioCodecs.txt");
    }
}
