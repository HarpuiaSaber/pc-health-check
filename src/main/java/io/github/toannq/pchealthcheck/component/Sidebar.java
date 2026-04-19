package io.github.toannq.pchealthcheck.component;

import io.github.toannq.pchealthcheck.model.PcInfo;
import io.github.toannq.pchealthcheck.util.CmdHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

public class Sidebar extends VBox {
  public Sidebar(PcInfo pcInfo) {
    loadImage();
    setDisplay(pcInfo);
  }

  private void loadImage() {
    try {
      var imageStream = getClass().getResourceAsStream("/laptop.png");
      if (imageStream != null) {
        var img = new Image(imageStream);
        var pcImage = new ImageView(img);
        pcImage.setFitWidth(160);
        pcImage.setPreserveRatio(true);
        getChildren().add(pcImage);
      } else {
        loadDefaultImage();
      }
    } catch (Exception e) {
      loadDefaultImage();
    }
  }

  private void loadDefaultImage() {
    var fallbackIcon = new FontIcon("fas-laptop");
    fallbackIcon.setIconSize(100);
    fallbackIcon.setIconColor(javafx.scene.paint.Color.GRAY);
    getChildren().add(fallbackIcon);
  }

  private void setDisplay(PcInfo pcInfo) {
    setPadding(new Insets(30));
    setPrefWidth(500);
    setSpacing(15);
    setAlignment(Pos.TOP_LEFT);

    var nameLabel = new Label(pcInfo.name());
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

    var nodes = new ArrayList<Node>();
    nodes.add(new Label("CPU: " + pcInfo.cpu()));
    var gpus = pcInfo.gpus();
    for (int i = 0, gpusSize = gpus.size(); i < gpusSize; i++) {
      nodes.add(new Label("GPU " + (i + 1) + ": " + gpus.get(i)));
    }
    nodes.add(new Label("RAM: " + pcInfo.ram() + " GB"));
    var list = new ArrayList<Label>();
    var diskSizes = pcInfo.diskSizes();
    for (int i = 0, diskSizesSize = diskSizes.size(); i < diskSizesSize; i++) {
      list.add(new Label("Disk " + (i + 1) + ": " + diskSizes.get(i).toPlainString() + " GB"));
    }
    nodes.addAll(list);
    nodes.add(new Label(describeAge(pcInfo.age())));

    var details = new VBox(5, nodes.toArray(Node[]::new));
    details.setOpacity(0.7);

    var renameLink = new Hyperlink("Rename your PC");
    renameLink.setOnAction(e -> CmdHelper.run("ms-settings:about" ,"Could not open the About setting"));

    getChildren().addAll(nameLabel, details, renameLink);
  }

  private String describeAge(int age) {
    return (age <= 0) ? "New PC" : age + (age == 1 ? " year old" : " years old");
  }
}