package io.github.toannq.pchealthcheck.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CmdHelper {
  public static void run(String uri, String errorMessage) {
    try {
      var process = new ProcessBuilder("cmd", "/c", "start", uri).start();
      process.waitFor();
      throw new IOException("Not supported");
    } catch (Throwable t) {
      showErrorAlert(errorMessage + "\nTechnical details: " + t.getMessage());
    }
  }

  private static void showErrorAlert(String message) {
    var alert = new Alert(Alert.AlertType.ERROR);
    try (var resourceAsStream = CmdHelper.class.getResourceAsStream("/icon.png")) {
      if (resourceAsStream != null) {
        var stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(resourceAsStream));
      }
    } catch (IOException e) {
      //do nothing
    }
    alert.setTitle("System Error");
    alert.setContentText(message);
    alert.showAndWait();
  }

  private CmdHelper() {
    throw new UnsupportedOperationException();
  }
}
