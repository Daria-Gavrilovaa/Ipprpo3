package ru.student.diskusage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.student.diskusage.model.DiskInfo;
import ru.student.diskusage.model.Report;
import ru.student.diskusage.service.CommandExecutor;
import ru.student.diskusage.service.DiskParser;
import ru.student.diskusage.util.SizeConverter;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Главный класс приложения.
 * Управляет потоком выполнения.
 *
 * @author Daria Gavrilova
 * @version 1.0
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Disk Usage Report Utility | Student: Daria Gavrilova");

        try {
            // 1. Настройка
            int threshold = 90;
            try (InputStream input = App.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (input != null) {
                    Properties props = new Properties();
                    props.load(input);
                    String thresholdStr = props.getProperty("usage.threshold.percent");
                    if (thresholdStr != null) threshold = Integer.parseInt(thresholdStr);
                }
            } catch (Exception e) {
                // Ignore
            }

            // 2. Определение ОС и команды (ПЕРЕХОД НА POWERSHELL)
            String os = System.getProperty("os.name");
            String command;

            if (os.toLowerCase().contains("win")) {
                // Используем PowerShell для получения данных: Имя, Свободно, Всего
                command = "powershell -Command \"Get-CimInstance Win32_LogicalDisk | Select-Object Caption, FreeSpace, Size | Format-Table -HideTableHeaders\"";
            } else {
                command = "df -h";
            }

            // 3. Получение данных
            CommandExecutor executor = new CommandExecutor();
            List<String> output = executor.execute(command);

            // ДЛЯ ОТЛАДКИ: Если снова будет 0, раскомментируйте строку ниже, чтобы увидеть, что выдает система
            // output.forEach(System.out::println);

            DiskParser parser = new DiskParser();
            List<DiskInfo> disks = parser.parse(output, os);

            // 4. Фильтрация
            int finalThreshold = threshold;
            List<Map<String, Object>> exceeded = disks.stream()
                    .filter(d -> d.getUsePercent() > finalThreshold)
                    .map(d -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("filesystem", d.getName());
                        map.put("total", SizeConverter.toGb(d.getTotalBytes()) + "G");
                        map.put("used_percent", d.getUsePercent());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 5. Агрегация
            long totalSum = disks.stream().mapToLong(DiskInfo::getTotalBytes).sum();
            long usedSum = disks.stream().mapToLong(DiskInfo::getUsedBytes).sum();

            Map<String, Long> summary = new HashMap<>();
            summary.put("total_gb", SizeConverter.toGb(totalSum));
            summary.put("total_used_gb", SizeConverter.toGb(usedSum));

            // 6. Вывод
            Report report = new Report(exceeded, summary);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(report));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}