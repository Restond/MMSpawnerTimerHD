package org.restond.mmspawnertimerhd.model;

/**
 * 刷怪点数据模型
 * 存储单个 MythicMobs 刷怪点的计时信息
 */
public class SpawnerData {
    private final String spawnerName;
    private long lastSpawnTime;
    private int cooldownSeconds;
    private int remainingSeconds;

    public SpawnerData(String spawnerName, int cooldownSeconds) {
        this.spawnerName = spawnerName;
        this.cooldownSeconds = cooldownSeconds;
        this.remainingSeconds = cooldownSeconds;
        this.lastSpawnTime = System.currentTimeMillis();
    }

    public int getCooldownSeconds() {
        return this.cooldownSeconds;
    }

    public int getRemainingSeconds() {
        return this.remainingSeconds;
    }

    public long getLastSpawnTime() {
        return this.lastSpawnTime;
    }

    /**
     * 更新剩余时间（基于时间戳计算）
     */
    public void updateRemainingSeconds() {
        long currentTime = System.currentTimeMillis();
        long remainingMillis = (lastSpawnTime + (long) cooldownSeconds * 1000) - currentTime;
        this.remainingSeconds = (int) Math.max(0, remainingMillis / 1000);
    }

    /**
     * 重置计时器（怪物刷新时调用）
     */
    public void resetTimer() {
        this.lastSpawnTime = System.currentTimeMillis();
        this.remainingSeconds = cooldownSeconds;
    }

    /**
     * 检查刷怪点是否已就绪
     */
    public boolean isReady() {
        return remainingSeconds <= 0;
    }

    public void setLastSpawnTime(long lastSpawnTime) {
        this.lastSpawnTime = lastSpawnTime;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    /**
     * 获取格式化的剩余时间，支持自定义格式
     * @param format 格式字符串，如 "mm:ss" 或 "HH:mm:ss"
     */
    public String getFormattedTime(String format) {
        int seconds = Math.max(0, remainingSeconds);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return format
                .replace("HH", String.format("%02d", hours))
                .replace("mm", String.format("%02d", minutes))
                .replace("ss", String.format("%02d", secs));
    }

    public String getSpawnerName() {
        return spawnerName;
    }
}
