package it.disi.unitn.lasagna.tirociniofx.supportedcodecs;

import javafx.scene.control.ChoiceBox;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public abstract class SupportedCodecs {

    protected final ChoiceBox<String> codecChoice;

    protected Map<String, String> m;

    SupportedCodecs(@NotNull ChoiceBox<String> codecChoice) {
        this.codecChoice = codecChoice;
        m = new HashMap<>();
    }

    private boolean checkExecutable(@NotNull String execFile) {
        if (!Files.isExecutable(Paths.get(execFile))) {
            Locale l = Locale.getDefault();
            if (l == Locale.ITALY || l == Locale.ITALIAN) {
                System.err.println("Non e' possibile eseguire il file " + execFile + ". Si prega di controllarne i permessi " +
                        "di esecuzione e l'esistenza.");
            } else {
                System.err.println("Cannot execute file " + execFile + ". Please check the user's permissions and that " +
                        "the file exists.");
            }

            return false;
        }

        return true;
    }

    protected void executeCML(@NotNull DefaultExecutor executor, @NotNull DefaultExecuteResultHandler execResHandler,
                            @NotNull CommandLine cmdline) {
        try {
            executor.execute(cmdline, execResHandler);
            Thread t1 = new Thread(() -> {
                try {
                    executor.setExitValue(0);
                    execResHandler.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();
            t1.join();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void performCheck(@NotNull CommandLine cmdline) throws IOException {
        try {
            if (!checkExecutable(cmdline.getExecutable())) {
                throw new AccessDeniedException("File is not executable");
            }

            Path tempFile = Files.createTempFile("ffmpeg-java-temp", ".txt");
            BufferedOutputStream outstream = new BufferedOutputStream(Files.newOutputStream(tempFile,
                    StandardOpenOption.WRITE));
            PumpStreamHandler streamHandler = new PumpStreamHandler(outstream, System.err);
            DefaultExecutor executor = DefaultExecutor.builder().get();
            executor.setStreamHandler(streamHandler);

            DefaultExecuteResultHandler execResHandler = new DefaultExecuteResultHandler();
            executeCML(executor, execResHandler, cmdline);
        } catch (InvalidPathException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    public abstract void enumCodecs() throws IOException;

    public void readFile(@NotNull ChoiceBox<String> codecChoice, @NotNull String execFile,
                         @NotNull String pathToInputFile) throws IOException {
        File supportedCodecs = new File(pathToInputFile);
        if(!supportedCodecs.exists()) {
            m.put("execFile", execFile);
            performCheck(CommandLine.parse("${execFile}", m));

            Scanner scanner = new Scanner(supportedCodecs);
            codecChoice.getItems().clear();
            while(scanner.hasNext()) {
                String el = scanner.next();
                if(!codecChoice.getItems().contains(el)) {
                    codecChoice.getItems().add(el);
                }
            }
        }

        supportedCodecs.delete();
    }

}
