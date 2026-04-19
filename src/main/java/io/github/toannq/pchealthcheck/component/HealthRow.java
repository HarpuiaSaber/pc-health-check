package io.github.toannq.pchealthcheck.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class HealthRow extends HBox {
  public HealthRow(String iconLiteral, String title, String status, Runnable action) {
    setMouseAction(action);
    setDisplay(iconLiteral, title, status);
  }

  private void setMouseAction(Runnable action) {
    this.setOnMouseEntered(e -> {
      this.setEffect(new DropShadow(5, Color.LIGHTGRAY));
      this.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10;");
    });

    this.setOnMouseExited(e -> {
      this.setEffect(null);
      this.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    });

    this.setOnMouseClicked(e -> {
      if (action != null) action.run();
    });
  }

  private void setDisplay(String iconLiteral, String title, String status) {
    setFocusTraversable(false);
    setPadding(new Insets(20));
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(15);
    setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-cursor: hand;");

    var icon = new FontIcon(iconLiteral);
    icon.setIconSize(20);
    icon.setIconColor(Color.web("#0067c0"));

    var lblTitle = new Label(title);
    lblTitle.setStyle("-fx-font-weight: 500;");

    var lblStatus = new Label(status);
    lblStatus.setOpacity(0.5);

    var spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    getChildren().addAll(icon, lblTitle, spacer, lblStatus, new FontIcon("fas-chevron-right"));
  }
}