package it.disi.unitn.lasagna.tirociniofx.supportedcodecs;

import javafx.scene.control.ChoiceBox;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class VideoCodecs extends SupportedCodecs {

    public VideoCodecs(@NotNull ChoiceBox<String> codecChoice) {
        super(codecChoice);
    }

    public void enumCodecs() throws IOException {
        //Use custom FFmpeg version
        String execFile = "";
        if(SystemUtils.IS_OS_LINUX) {
            execFile = "./src/cpp/enumCodecs/linux/video";
        } else {
            if(SystemUtils.IS_OS_WINDOWS) {
                execFile = "./src/cpp/enumCodecs/windows/video.exe";
            } else {
                System.err.println("Unsupported operating system.");
                System.exit(1);
            }
        }
        readFile(codecChoice, execFile, "./videoCodecs.txt");
    }

}
