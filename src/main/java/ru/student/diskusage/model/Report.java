package ru.student.diskusage.model;

import java.util.List;
import java.util.Map;

/**
 * Класс для формирования итогового отчета.
 */
public class Report {
    // Оставляем поле с подчеркиванием, чтобы в JSON ключ был "threshold_exceeded"
    private List<Map<String, Object>> threshold_exceeded;
    private Map<String, Long> summary;

    public Report(List<Map<String, Object>> threshold_exceeded, Map<String, Long> summary) {
        this.threshold_exceeded = threshold_exceeded;
        this.summary = summary;
    }

    // ИСПРАВЛЕНИЕ: Имя метода теперь в camelCase (без подчеркивания)
    public List<Map<String, Object>> getThresholdExceeded() {
        return threshold_exceeded;
    }

    public Map<String, Long> getSummary() {
        return summary;
    }
}