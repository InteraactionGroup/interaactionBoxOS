package main;

import main.gaze.devicemanager.TobiiGazeDeviceManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.process.AugComProcess;
import main.process.GazePlayProcess;
import main.process.YoutubeProcess;

import java.awt.*;
import java.io.File;

public class HomeScreen extends BorderPane {

    private Stage primaryStage;
    public SecondStage secondStage;
    final TobiiGazeDeviceManager tgdm;

    HomeScreen(Stage primaryStage,TobiiGazeDeviceManager tgdm) {
        super();
        this.primaryStage = primaryStage;
        this.tgdm = tgdm;

        File f = new File("src/ressources/images/blured.jpg");
        ImageView backgroundBlured = new ImageView(new Image("file:" + f.getAbsolutePath()));

        backgroundBlured.setOpacity(0.9);

        backgroundBlured.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(primaryStage.heightProperty());

        this.getChildren().add(backgroundBlured);

        createSecondStage();
        HBox menuBar = createMenuBar();

        this.setCenter(menuBar);

        startMouseListener();
//        backgroundBlured.setOpacity(0.5);
//        this.setOpacity(0.5);
    }


    private HBox createMenuBar() {
        YoutubeProcess youtubeProcess = new YoutubeProcess();
        AugComProcess augComProcess = new AugComProcess();
        GazePlayProcess gazePlayProcess = new GazePlayProcess();

        ProgressButton youtubeProgressButton = youtubeProcess.createButton(this, secondStage);
        ProgressButton augComProcessButton =augComProcess.createButton(this, secondStage);
        ProgressButton gazePlayProcessButton =gazePlayProcess.createButton(this, secondStage);
        HBox menuBar = new HBox(
                youtubeProgressButton,
                augComProcessButton,
                gazePlayProcessButton
        );
        tgdm.addEventFilter(youtubeProgressButton.getButton());
        tgdm.addEventFilter(augComProcessButton.getButton());
        tgdm.addEventFilter(gazePlayProcessButton.getButton());

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);

        menuBar.spacingProperty().bind(this.widthProperty().divide(8));
        return menuBar;
    }

    private void createSecondStage() {
        secondStage = new SecondStage(primaryStage);
        tgdm.init(secondStage);
    }

    private void startMouseListener() {
        Thread t = new Thread(() -> {
            while (true) {
                checkMouse();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    synchronized private void checkMouse() {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();
        if (x > 500 && x < Screen.getPrimary().getBounds().getWidth() - 500 && y <= 10) {
            Platform.runLater(() -> {
                primaryStage.hide();
                StageUtils.displayUnclosable(secondStage);
            });
        }
    }

}