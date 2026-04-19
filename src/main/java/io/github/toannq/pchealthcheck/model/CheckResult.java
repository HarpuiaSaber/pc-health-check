package io.github.toannq.pchealthcheck.model;

public record CheckResult(boolean isPassed, String title, String detail) {
}
