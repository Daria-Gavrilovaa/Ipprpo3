package ru.student.diskusage.model;

/**
 * Модель данных, представляющая информацию об одном дисковом разделе.
 */
public class DiskInfo {
    private final String name;
    private final long totalBytes;
    private final long usedBytes;
    private final long freeBytes;
    private final int usePercent;

    public DiskInfo(String name, long totalBytes, long usedBytes, long freeBytes, int usePercent) {
        this.name = name;
        this.totalBytes = totalBytes;
        this.usedBytes = usedBytes;
        this.freeBytes = freeBytes;
        this.usePercent = usePercent;
    }

    public String getName() { return name; }
    public long getTotalBytes() { return totalBytes; }
    public long getUsedBytes() { return usedBytes; }
    public long getFreeBytes() { return freeBytes; }
    public int getUsePercent() { return usePercent; }
}