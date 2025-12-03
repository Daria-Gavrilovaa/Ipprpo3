package ru.student.diskusage.model;

import java.util.List;
import java.util.Map;

/**
 * Класс для формирования итогового отчета.
 */
public class Report {
    private List<Map<String, Object>> threshold_exceeded;
    private Map<String, Long> summary;

    public Report(List<Map<String, Object>> threshold_exceeded, Map<String, Long> summary) {
        this.threshold_exceeded = threshold_exceeded;
        this.summary = summary;
    }
    
    // Геттеры можно добавить при необходимости, но для Gson они необязательны при сериализации
    public List<Map<String, Object>> getThreshold_exceeded() { return threshold_exceeded; }
    public Map<String, Long> getSummary() { return summary; }
}