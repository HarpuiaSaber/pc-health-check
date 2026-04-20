package io.github.toannq.pchealthcheck.component;

import io.github.toannq.pchealthcheck.util.CmdHelper;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.ArrayList;

public class HealthBox extends VBox {
  public HealthBox(BigDecimal usagePercent, boolean hasBattery) {
    setSpacing(10);

    var rows = new ArrayList<HealthRow>();
    rows.add(new HealthRow("fas-history", "Windows Backup", "Go to settings", () -> CmdHelper.run("ms-settings:backup", "Could not open the Windows Backup settings")));
    rows.add(new HealthRow("fas-sync", "Windows Update", "Go to settings", () -> CmdHelper.run("ms-settings:windowsupdate", "Could not open the Windows Update settings")));
    if (hasBattery) {
      rows.add(new HealthRow("fas-battery-half", "Battery capacity", "Go to settings", () -> CmdHelper.run("ms-settings:powersleep", "Could not open the Battery capacity settings")));
    }
    rows.add(new HealthRow("fas-folder", "Storage capacity", usagePercent.toPlainString() + "% full, Go to settings", () -> CmdHelper.run("ms-settings:storagesense", "Could not open the Storage capacity settings")));
    rows.add(new HealthRow("fas-stopwatch", "Startup time", "Go to settings", () -> CmdHelper.run("ms-settings:startupapps", "Could not open the Startup time setting")));

    getChildren().addAll(rows);
  }
}
