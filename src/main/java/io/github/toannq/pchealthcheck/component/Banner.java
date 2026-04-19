package io.github.toannq.pchealthcheck.component;

import io.github.toannq.pchealthcheck.model.PcInfo;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Banner extends VBox {
  public Banner(HostServices hostServices, PcInfo pcInfo) {
    setPadding(new Insets(20));
    try (var resourceAsStream = getClass().getResourceAsStream("/background.png")) {
      if (resourceAsStream != null) {
        setBackground(new Background(new BackgroundImage(new Image(resourceAsStream),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false,
                false,
                true,
                true
            ))));
      }
    } catch (IOException e) {
      setStyle("-fx-background-color: #e6f3ff; -fx-background-radius: 12;");
    }
    var introducing = new Hyperlink("Introducing Windows 11");
    introducing.setStyle("-fx-text-fill: #201f1e; -fx-font-weight: bold; -fx-font-size: 20px;");
    introducing.setOnAction(e -> hostServices.showDocument("https://www.microsoft.com/en-us/windows/windows-11"));
    introducing.setPadding(new Insets(0, 0, 10, 0));

    var label = new Label("Let's check if this PC meets the system requirements.");
    label.setStyle("-fx-text-fill: #201f1e; -fx-font-size: 16px;");
    label.setPadding(new Insets(0, 0, 10, 0));

    var checkRequirements = new Button("Check system requirements");
    checkRequirements.getStyleClass().add("accent");
    checkRequirements.setOnAction(e -> {
      RequirementResultDialog.show((Stage) checkRequirements.getScene().getWindow(), hostServices, pcInfo);
    });

    getChildren().addAll(introducing, checkRequirements);
  }
}
