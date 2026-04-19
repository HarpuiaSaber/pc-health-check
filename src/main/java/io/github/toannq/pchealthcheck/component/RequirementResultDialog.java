package io.github.toannq.pchealthcheck.component;

import io.github.toannq.pchealthcheck.model.CheckResult;
import io.github.toannq.pchealthcheck.model.PcInfo;
import io.github.toannq.pchealthcheck.util.Win11Checker;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RequirementResultDialog {

  private static List<CheckResult> results;

  public static void show(Stage owner, HostServices hostServices, PcInfo pcInfo) {
    var dialog = new Stage();
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.initOwner(owner);
    dialog.setTitle("PC Health Check");

    var root = new VBox(25);
    root.setPadding(new Insets(25));
    root.setPrefWidth(520);
    root.setStyle("-fx-background-color: white;");

    var header = new HBox(15);
    header.setAlignment(Pos.CENTER_LEFT);

    if (results == null) {
      results = Win11Checker.checkRequirements(pcInfo);
    }
    var allPassed = results.stream().allMatch(CheckResult::isPassed);

    var mainCheck = new Label(allPassed ? "✔" : "✘");
    mainCheck.setStyle("-fx-font-size: 22px; -fx-text-fill: " + (allPassed ? "#228b22;" : "#d32f2f;"));

    var title = new Label("This PC meets Windows 11 requirements");
    title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    header.getChildren().addAll(mainCheck, title);

    var subTitle = new Label("Great news — Windows 11 is here! Check device specifications for apps and features.");
    subTitle.setWrapText(true);

    var listContainer = new VBox(20);

    for (var res : results) {
      listContainer.getChildren().add(createDetailedRow(res));
    }

    var scrollPane = new ScrollPane(listContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(320);
    scrollPane.setStyle("-fx-background: white; -fx-background-color: white; -fx-border-color: #eee;");

    var footer = new HBox(10);
    footer.setAlignment(Pos.CENTER_RIGHT);

    var btnStyle = "-fx-background-color: #0078d4; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 4;";

    var btnHide = new Button("Hide all results");
    btnHide.setStyle(btnStyle);
    btnHide.setOnAction(e -> dialog.close());

    var btnMore = new Button("More about Windows 11 requirements");
    btnMore.setStyle(btnStyle);
    btnMore.setOnAction(e -> hostServices.showDocument("https://www.microsoft.com/en-us/windows/windows-11-specifications"));

    footer.getChildren().addAll(btnHide, btnMore);

    root.getChildren().addAll(header, subTitle, scrollPane, footer);

    var scene = new Scene(root);
    dialog.setScene(scene);
    try (var resourceAsStream = RequirementResultDialog.class.getResourceAsStream("/icon.png")) {
      if (resourceAsStream != null) {
        dialog.getIcons().add(new Image(resourceAsStream));
      }
    } catch (IOException e) {
      //do nothing
    }
    dialog.setResizable(false);
    dialog.showAndWait();
  }

  private static VBox createDetailedRow(CheckResult res) {
    var container = new VBox(2);
    var topRow = new HBox(12);
    topRow.setAlignment(Pos.CENTER_LEFT);

    var icon = new Label(res.isPassed() ? "✔" : "✘");
    icon.setStyle("-fx-font-size: 18px; " +
        "-fx-font-weight: bold; " +
        "-fx-text-fill: " + (res.isPassed() ? "#228b22;" : "#d32f2f;"));

    var lblTitle = new Label(res.title());
    lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

    topRow.getChildren().addAll(icon, lblTitle);

    var lblDetail = new Label(res.detail());
    lblDetail.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
    lblDetail.setPadding(new Insets(0, 0, 0, 28));

    container.getChildren().addAll(topRow, lblDetail);
    return container;
  }
}