package io.github.toannq.pchealthcheck.collector;

import io.github.toannq.pchealthcheck.model.PcInfo;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PcInfoCollector {
  public static final BigDecimal GIGABYTE_SIZE = BigDecimal.valueOf(1024 * 1024 * 1024L);

  public static PcInfo collect() {
    var systemInfo = new SystemInfo();
    var hardware = systemInfo.getHardware();
    var centralProcessor = hardware.getProcessor();

    var pcName = systemInfo.getOperatingSystem().getNetworkParams().getHostName();
    var cpu = centralProcessor.getProcessorIdentifier().getName();
    var cores = centralProcessor.getLogicalProcessorCount();
    var gpus = hardware.getGraphicsCards().isEmpty() ? null : hardware.getGraphicsCards().stream().map(GraphicsCard::getName).toList();
    var ram = BigDecimal.valueOf(hardware.getMemory().getTotal()).divide(GIGABYTE_SIZE, RoundingMode.HALF_UP).intValue();
    var diskSize = hardware.getDiskStores().isEmpty() ? List.of(BigDecimal.ZERO)
        : hardware.getDiskStores().stream()
          .map(hwDiskStore -> BigDecimal.valueOf(hwDiskStore.getSize()).divide(GIGABYTE_SIZE, 2, RoundingMode.HALF_UP))
          .toList();
    var currentYear = java.time.LocalDate.now().getYear();
    var biosDate = systemInfo.getHardware().getComputerSystem().getFirmware().getReleaseDate();
    var yearFromBios = java.time.LocalDate.parse(biosDate).getYear();
    var age = currentYear - yearFromBios;

    return new PcInfo(pcName, cpu, cores, gpus, ram, diskSize, age);
  }
}
