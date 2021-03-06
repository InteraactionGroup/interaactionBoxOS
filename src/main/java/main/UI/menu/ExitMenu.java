package main.UI.menu;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import main.utils.UtilsOS;

import java.io.IOException;

public class ExitMenu extends BorderPane {

    public ExitMenu(GraphicalMenus graphicalMenus) {
        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(1, Color.BLACK)};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);

        this.getChildren().add(r);


        Button shutdownButton = createTopBarButton(
                "Yes, shutdown the box",
                "images/on-off-button.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    try {
                        shutdown();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Platform.exit();
                    System.exit(0);
                }
        );

        Button exitButton = createTopBarButton(
                "[For tests purpose] Exit the box only",
                "images/cross.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    Platform.exit();
                    System.exit(0);
                }
        );

        Button cancelButton = createTopBarButton(
                "Cancel",
                "images/cross.png",
                (e) -> {
                    graphicalMenus.primaryStage.getScene().setRoot(graphicalMenus.getHomeScreen());
                }
        );

        HBox hbox = new HBox(cancelButton, exitButton, shutdownButton);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.spacingProperty().bind(this.widthProperty().divide(20));

        Label exitPrompt = new Label();
        exitPrompt.setText("Do you really want to shutdown the box ?");
        exitPrompt.setFont(new Font(30));
        exitPrompt.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        exitPrompt.setTextFill(Color.BLACK);

        VBox vbox = new VBox(exitPrompt, hbox);
        vbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(vbox, Pos.CENTER);
        vbox.spacingProperty().bind(this.heightProperty().divide(10));

        this.setCenter(vbox);

    }

    static Button createTopBarButton(String text, String imagePath, EventHandler eventhandler) {
        Button optionButton = new Button(text);
        optionButton.setPrefHeight(50);
        optionButton.setMaxHeight(50);
        optionButton.setStyle(
                "-fx-border-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-background-color: transparent; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-text-fill: Black"
        );
        ImageView graphic = new ImageView(imagePath);
        graphic.setPreserveRatio(true);
        graphic.setFitHeight(30);
        optionButton.setGraphic(graphic);
        optionButton.setOnMouseClicked(eventhandler);
        return optionButton;
    }

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;

        if (UtilsOS.isUnix()) {
            shutdownCommand = "shutdown -h now";
        } else if (UtilsOS.isWindows()) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        Platform.exit();
        System.exit(0);
    }
}
