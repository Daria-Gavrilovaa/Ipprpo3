package ru.student.diskusage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для выполнения системных команд.
 */
public class CommandExecutor {
    /**
     * Выполняет команду и возвращает вывод построчно.
     * @param command команда для исполнения
     * @return список строк вывода
     */
    public List<String> execute(String command) {
        List<String> output = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error executing command: " + command, e);
        }
        return output;
    }
}