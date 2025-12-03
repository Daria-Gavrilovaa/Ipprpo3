package ru.student.diskusage.service;

import ru.student.diskusage.model.DiskInfo;
import ru.student.diskusage.util.SizeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер вывода команд df (Linux) и PowerShell (Windows).
 */
public class DiskParser {

    public List<DiskInfo> parse(List<String> lines, String osName) {
        List<DiskInfo> disks = new ArrayList<>();
        boolean isWindows = osName.toLowerCase().contains("win");

        for (String line : lines) {
            line = line.trim();
            // Игнорируем пустые строки и заголовки
            if (line.isEmpty() || line.startsWith("Filesystem") || line.startsWith("Caption") || line.startsWith("-")) continue;

            if (isWindows) {
                // PowerShell: C:       85899345920   107374182400
                String[] parts = line.split("\\s+");
                // Нам нужно хотя бы 3 элемента: Имя, Свободно, Всего
                if (parts.length >= 3) {
                    try {
                        String name = parts[0];
                        long free = Long.parseLong(parts[1]);
                        long total = Long.parseLong(parts[2]);
                        long used = total - free;
                        int percent = (total > 0) ? (int) ((used * 100) / total) : 0;
                        disks.add(new DiskInfo(name, total, used, free, percent));
                    } catch (NumberFormatException e) {
                        // Пропускаем строки, где нет чисел
                    }
                }
            } else {
                // Linux logic
                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    try {
                        String name = parts[0];
                        long total = SizeConverter.parseSize(parts[1]);
                        long used = SizeConverter.parseSize(parts[2]);
                        long free = SizeConverter.parseSize(parts[3]);
                        String percentStr = parts[4].replace("%", "");
                        int percent = percentStr.matches("\\d+") ? Integer.parseInt(percentStr) : 0;

                        disks.add(new DiskInfo(name, total, used, free, percent));
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
        return disks;
    }
}