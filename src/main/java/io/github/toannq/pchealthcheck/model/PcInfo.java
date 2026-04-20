package io.github.toannq.pchealthcheck.model;

import java.math.BigDecimal;
import java.util.List;

public record PcInfo(String name,
                     String cpu,
                     int cores,
                     List<String> gpus,
                     int ram,
                     List<BigDecimal> diskSizes,
                     BigDecimal usagePercent,
                     int age,
                     boolean hasBattery) {
}
