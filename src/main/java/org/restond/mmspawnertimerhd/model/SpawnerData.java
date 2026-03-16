package org.restond.mmspawnertimerhd.model;

/**
 * 刷怪点数据模型
 * 存储单个 MythicMobs 刷怪点的计时信息
 */
public class SpawnerData {
    private final String spawnerName;
    private long lastSpawnTime;
    private long lastDeathTime;
    private int cooldownSeconds;
    private int warmupSeconds;

    public SpawnerData(String spawnerName, int cooldownSeconds, int warmupSeconds) {
        this.spawnerName = spawnerName;
        this.cooldownSeconds = cooldownSeconds;
        this.warmupSeconds = warmupSeconds;
        this.lastSpawnTime = System.currentTimeMillis();
        this.lastDeathTime = 0;
    }

    /**
     * 获取 cooldown 剩余秒数（刷新后开始倒计时）
     */
    public int getCooldownRemainingSeconds() {
        if (cooldownSeconds <= 0) return 0;
        long elapsed = (System.currentTimeMillis() - lastSpawnTime) / 1000;
        return Math.max(0, cooldownSeconds - (int) elapsed);
    }

    /**
     * 获取 warmup 剩余秒数（死亡后开始倒计时）
     */
    public int getWarmupRemainingSeconds() {
        if (lastDeathTime == 0) return 0; // 未死亡过
        if (warmupSeconds <= 0) return 0;
        long elapsed = (System.currentTimeMillis() - lastDeathTime) / 1000;
        return Math.max(0, warmupSeconds - (int) elapsed);
    }

    public int getWarmupSeconds() {
        return this.warmupSeconds;
    }

    /**
     * 记录怪物刷新（开始 cooldown 倒计时）
     */
    public void recordSpawn() {
        this.lastSpawnTime = System.currentTimeMillis();
    }

    /**
     * 记录怪物死亡（开始 warmup 倒计时）
     */
    public void recordDeath() {
        this.lastDeathTime = System.currentTimeMillis();
    }

    /**
     * 检查 cooldown 是否结束
     */
    public boolean isCooldownReady() {
        return getCooldownRemainingSeconds() <= 0;
    }

    /**
     * 检查 warmup 是否结束（是否可以刷新新怪物）
     */
    public boolean isWarmupReady() {
        return getWarmupRemainingSeconds() <= 0;
    }

    /**
     * 获取格式化的时间
     */
    public static String formatTime(int seconds, String format) {
        seconds = Math.max(0, seconds);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return format
                .replace("HH", String.format("%02d", hours))
                .replace("mm", String.format("%02d", minutes))
                .replace("ss", String.format("%02d", secs));
    }

    public void setWarmupSeconds(int warmupSeconds) {
        this.warmupSeconds = warmupSeconds;
    }

    public int getCooldownSeconds() {
        return this.cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public long getLastSpawnTime() {
        return this.lastSpawnTime;
    }

    public void setLastSpawnTime(long lastSpawnTime) {
        this.lastSpawnTime = lastSpawnTime;
    }

    public String getSpawnerName() {
        return this.spawnerName;
    }

    public long getLastDeathTime() {
        return this.lastDeathTime;
    }
}
