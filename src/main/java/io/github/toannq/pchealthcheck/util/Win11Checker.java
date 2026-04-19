package io.github.toannq.pchealthcheck.util;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import io.github.toannq.pchealthcheck.model.CheckResult;
import io.github.toannq.pchealthcheck.model.PcInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Win11Checker {

  public static final BigDecimal REQUIRED_DISK_SPACE = BigDecimal.valueOf(64L);

  public static List<CheckResult> checkRequirements(PcInfo pc) {
    var results = new ArrayList<CheckResult>();

    boolean secureBoot = isSecureBootEnabled();
    results.add(new CheckResult(secureBoot, "This PC supports Secure Boot.", secureBoot ? "Secure Boot is enabled." : "Secure Boot is disabled or not supported."));

    boolean tpmReady = isTpm20Ready();
    results.add(new CheckResult(tpmReady, "TPM 2.0 enabled on this PC.", tpmReady ? "TPM: TPM 2.0" : "TPM 2.0 not detected."));

    results.add(new CheckResult(pc.ram() >= 4, "System memory (RAM)", "Installed: " + pc.ram() + " GB (Required: 4 GB)"));
    results.add(new CheckResult(pc.cores() >= 2, "Processor cores", "Cores: " + pc.cores() + " (Required: 2 Cores)"));

    var maxDisk = pc.diskSizes().stream()
        .max(BigDecimal::compareTo)
        .orElse(BigDecimal.ZERO);
    var diskOk = maxDisk.compareTo(REQUIRED_DISK_SPACE) >= 0;
    results.add(new CheckResult(diskOk, "Storage", maxDisk + " GB (Required: 64 GB)"));

    var cpuSupported = isCpuSupported(pc.cpu());
    results.add(new CheckResult(cpuSupported, "Processor compatibility", "Model: " + pc.cpu()));

    return results;
  }

  public static boolean isTpm20Ready() {
    return Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Enum\\ACPI\\MSFT0101");
  }

  public static boolean isSecureBootEnabled() {
    try {
      return Advapi32Util.registryGetIntValue(WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\SecureBoot\\State", "UEFISecureBootEnabled") == 1;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isCpuSupported(String cpu) {
    var name = cpu.toLowerCase();
    if (name.contains("intel")) {
      if (name.contains("ultra")) return true;
      var gen = extractIntelGen(name);
      return gen >= 8;
    }
    if (name.contains("ryzen")) {
      var series = extractRyzenSeries(name);
      return series >= 2000;
    }

    return true;
  }

  private static int extractIntelGen(String cpu) {
    cpu = cpu.toLowerCase();
    var genMatcher = java.util.regex.Pattern
        .compile("(\\d{1,2})th gen")
        .matcher(cpu);
    if (genMatcher.find()) {
      return Integer.parseInt(genMatcher.group(1));
    }
    var modelMatcher = java.util.regex.Pattern
        .compile("i[3579]-(\\d{4,5})")
        .matcher(cpu);
    if (modelMatcher.find()) {
      var model = modelMatcher.group(1);
      if (model.length() == 4) {
        return model.charAt(0) - '0';
      }
      if (model.length() == 5) {
        return Integer.parseInt(model.substring(0, 2));
      }
    }
    return -1;
  }

  private static int extractRyzenSeries(String cpu) {
    var m = java.util.regex.Pattern.compile("(\\d{4})").matcher(cpu);
    if (m.find()) return Integer.parseInt(m.group(1));
    return -1;
  }

  private Win11Checker() {
    throw new UnsupportedOperationException();
  }
}
