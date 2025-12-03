package ru.student.diskusage.util;

/**
 * Утилита для конвертации строк размера (10G, 500M) в байты.
 */
public class SizeConverter {
    public static long parseSize(String sizeStr) {
        if (sizeStr.matches("\\d+")) return Long.parseLong(sizeStr); 
        
        long multiplier = 1;
        String upper = sizeStr.toUpperCase();
        if (upper.endsWith("G") || upper.endsWith("GB")) multiplier = 1024L * 1024 * 1024;
        else if (upper.endsWith("M") || upper.endsWith("MB")) multiplier = 1024L * 1024;
        else if (upper.endsWith("K") || upper.endsWith("KB")) multiplier = 1024L;
        
        String numberPart = sizeStr.replaceAll("[^0-9.]", "");
        if (numberPart.isEmpty()) return 0;
        return (long) (Double.parseDouble(numberPart) * multiplier);
    }
    
    public static long toGb(long bytes) {
        return bytes / (1024L * 1024 * 1024);
    }
}