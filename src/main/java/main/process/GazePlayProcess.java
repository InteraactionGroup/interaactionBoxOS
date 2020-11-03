package main.process;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.ProgressButton;
import main.SecondStage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class GazePlayProcess implements AppProcess {
    ProcessBuilder pb;

    @Override
    public Process start() {
        try {
            Process p = pb.inheritIO().start();
            Platform.exit();
            System.exit(0);
            return p;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init() {
        pb = createGazePlayBuilder();
    }

    private ProcessBuilder createGazePlayBuilder() {
        String javaBin =  "C:\\Program Files (x86)\\GazePlay\\lib\\jre\\bin\\java.exe";
        String classpath = "C:\\Program Files (x86)\\GazePlay\\lib\\*";

        LinkedList<String> commands = new LinkedList<>(Arrays.asList(javaBin, "-cp", classpath, "net.gazeplay.GazePlayLauncher"));

        System.out.println(javaBin + " " + "-cp" + " " + classpath + " " + "net.gazeplay.GazePlayLauncher");

        return new ProcessBuilder(commands);
    }

    @Override
    public ProgressButton createButton(BorderPane borderPane, SecondStage stage) {
        ProgressButton pb = new ProgressButton();
        File f = new File("src/ressources/images/gazeplayicon.png");
        ImageView logo = new ImageView(new Image("file:" + f.getAbsolutePath()));
        pb.getButton().setRadius(100);
        logo.setFitWidth(pb.getButton().getRadius() * 0.7);
        logo.setFitHeight(pb.getButton().getRadius() * 0.7);
        logo.fitWidthProperty().bind(pb.getButton().radiusProperty().multiply(0.7));
        logo.fitHeightProperty().bind(pb.getButton().radiusProperty().multiply(0.7));
        logo.setPreserveRatio(true);
        pb.setImage(logo);
        pb.assignIndicator((e) -> {
            stage.proc = this.start();
        },500);
        // Button button = initButton("src/ressources/images/angular.png", borderPane);
        this.init();
        pb.active();
        return pb;
    }

}