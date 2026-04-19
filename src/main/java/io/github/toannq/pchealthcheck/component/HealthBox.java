package io.github.toannq.pchealthcheck.component;

import io.github.toannq.pchealthcheck.util.CmdHelper;
import javafx.scene.layout.VBox;

public class HealthBox extends VBox {
  public HealthBox() {
    setSpacing(10);
    var windowsBackupRow = new HealthRow("fas-history", "Windows Backup", "Go to settings",
        () -> CmdHelper.run("ms-settings:backup", "Could not open the Windows Backup settings"));
    var windowsUpdateRow = new HealthRow("fas-sync", "Windows Update", "Go to settings",
        () -> CmdHelper.run("ms-settings:windowsupdate", "Could not open the Windows Update settings"));
    var batteryCapacityRow = new HealthRow("fas-battery-half", "Battery capacity", "Go to settings",
        () -> CmdHelper.run("ms-settings:powersleep", "Could not open the Battery capacity settings"));
    var storageCapacityRow = new HealthRow("fas-folder", "Storage capacity", "14% full, Go to settings",
        () -> CmdHelper.run("ms-settings:storagesense", "Could not open the Storage capacity settings"));
    var StartupTimeRow = new HealthRow("fas-stopwatch", "Startup time", "Go to settings",
        () -> CmdHelper.run("ms-settings:startupapps", "Could not open the Startup time setting"));

    getChildren().addAll(windowsBackupRow, windowsUpdateRow, batteryCapacityRow, storageCapacityRow, StartupTimeRow);
  }
}
