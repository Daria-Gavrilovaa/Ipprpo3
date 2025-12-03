package ru.student.diskusage;

import org.junit.jupiter.api.Test;
import ru.student.diskusage.model.DiskInfo;
import ru.student.diskusage.service.DiskParser;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DiskParserTest {
    @Test
    void testLinuxParsing() {
        DiskParser parser = new DiskParser();
        // Эмулируем вывод команды df -h
        List<String> input = List.of(
            "Filesystem      Size  Used Avail Use% Mounted on",
            "/dev/sda1        100G   20G   80G  20% /"
        );
        
        List<DiskInfo> result = parser.parse(input, "Linux");
        
        assertEquals(1, result.size());
        assertEquals("/dev/sda1", result.get(0).getName());
        assertEquals(20, result.get(0).getUsePercent());
    }

    @Test
    void testWindowsParsing() {
        DiskParser parser = new DiskParser();
        // Эмулируем вывод wmic: Caption FreeSpace Size
        // C: 80GB free, 100GB total -> 20GB used -> 20%
        List<String> input = List.of(
            "Caption  FreeSpace     Size",
            "C:       85899345920   107374182400 " 
        );
        
        List<DiskInfo> result = parser.parse(input, "Windows 10");
        
        assertEquals(1, result.size());
        assertEquals("C:", result.get(0).getName());
        // Проверяем, что процент подсчитан верно (примерно 20%)
        assertEquals(20, result.get(0).getUsePercent()); 
    }
}