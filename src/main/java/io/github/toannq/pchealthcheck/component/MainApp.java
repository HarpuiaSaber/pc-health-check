package io.github.toannq.pchealthcheck.component;

import atlantafx.base.theme.CupertinoLight;
import io.github.toannq.pchealthcheck.collector.PcInfoCollector;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

  @Override
  public void start(Stage stage) {
    Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

    var pcInfo = PcInfoCollector.collect();

    var banner = new Banner(getHostServices(), pcInfo);
    var healthBox = new HealthBox();

    var content = new VBox(20);
    content.setPadding(new Insets(30));
    content.setPrefWidth(600);

    content.getChildren().addAll(banner, healthBox);

    var bodyLayout = new HBox();
    bodyLayout.setStyle("-fx-background-color: #f6f6f6;");
    bodyLayout.setPrefSize(900, 650);

    var sidebar = new Sidebar(pcInfo);

    bodyLayout.getChildren().addAll(sidebar, content);

    var title = new Label("PC health at a glance");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #222;");
    title.setPadding(new Insets(30, 0, 0, 40));

    var mainContainer = new VBox();
    mainContainer.setPrefSize(950, 700);
    mainContainer.setStyle("-fx-background-color: #f6f6f6;");

    mainContainer.getChildren().addAll(title, bodyLayout);

    displayMainWindow(stage, mainContainer);
  }

  private void displayMainWindow(Stage stage, VBox mainContainer) {
    var scene = new Scene(mainContainer, 900, 700);
    stage.setScene(scene);
    try (var resourceAsStream = getClass().getResourceAsStream("/icon.png")) {
      if (resourceAsStream != null) {
        stage.getIcons().add(new Image(resourceAsStream));
      }
    } catch (IOException e) {
      //do nothing
    }
    stage.setTitle("PC Health Check");
    stage.setResizable(false);
    stage.show();
  }

  public static void run(String[] args) {
    launch(args);
  }
}
