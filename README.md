# MMSpawnerTimerHD

一个专为 MythicMobs 刷怪点设计的倒计时监控插件，支持 PlaceholderAPI 变量绑定、实时状态显示与自定义消息。

## ✨ 功能特性

- **双计时系统**：支持 Cooldown（冷却）和 Warmup（预热）两种计时模式
  - **Cooldown**：怪物刷新后开始倒计时
  - **Warmup**：怪物死亡后开始倒计时
- **PAPI 变量支持**：提供多种变量格式，兼容记分板、全息图等显示插件
- **实时计算**：无需定时任务，按需计算剩余时间，零性能开销
- **自动加载**：插件启动时自动读取 MythicMobs 刷怪点配置
- **重载命令**：支持热重载配置
- **自定义消息**：状态文本可完全自定义

## 📥 安装要求

| 插件 | 必需 | 说明 |
|------|:----:|------|
| [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) | ✅ | 变量输出核心 |
| [MythicMobs](https://mythiccraft.io/index.php?resources/mythicmobs.1/) | ⚪ | 刷怪点 API（软依赖） |
| DecentHolograms | ⚪ | 全息图显示（可选） |

## 📋 变量列表

### Cooldown（冷却）相关

| 变量 | 说明 | 示例输出 |
|------|------|----------|
| `%mmspawner_cooldown_remaining_<名称>%` | 冷却剩余秒数 | `45` |
| `%mmspawner_cooldown_time_<名称>%` | 冷却格式化时间 | `00:45` |
| `%mmspawner_cooldown_<名称>%` | 冷却总时长/秒 | `300` |

### Warmup（预热）相关

| 变量 | 说明 | 示例输出 |
|------|------|----------|
| `%mmspawner_warmup_remaining_<名称>%` | 预热剩余秒数 | `30` |
| `%mmspawner_warmup_time_<名称>%` | 预热格式化时间 | `00:30` |
| `%mmspawner_warmup_<名称>%` | 预热总时长/秒 | `60` |

### 状态相关

| 变量 | 说明 | 示例输出 |
|------|------|----------|
| `%mmspawner_status_<名称>%` | 状态文本 | `冷却中` / `预热中` / `已刷新` |
| `%mmspawner_ready_<名称>%` | 是否就绪 | `true` / `false` |

## ⚙️ 配置文件

```yaml
# config.yml

# 时间显示格式
# HH - 小时, mm - 分钟, ss - 秒
time-format: "mm:ss"

# 消息配置
messages:
  # 刷怪点已就绪时显示
  ready: "已刷新"
  # warmup 倒计时中显示
  warmup: "预热中"
  # cooldown 倒计时中显示
  cooldown: "冷却中"
  # 刷怪点不存在时显示
  not-found: "N/A"
```

## 💻 命令

| 命令 | 说明 | 权限 |
|------|------|------|
| `/mmspawntimer reload` | 重载插件配置 | `mmspawntimer.reload` |

## 🔧 技术架构

```
MMSpawnerTimerHD/
├── MMSpawnerTimer.java      # 主类 - 插件入口与配置管理
├── MMSpawnerMonitor.java    # 监控器 - 刷怪点数据管理
├── SpawnerData.java         # 数据模型 - 刷怪点计时信息
├── MMListener.java          # 监听器 - MythicMobs 事件处理
├── MMPlaceholder.java       # 变量扩展 - PAPI 变量注册
└── reload.java              # 命令处理 - 重载命令
```

### 核心流程

1. **插件加载** → `MMSpawnerMonitor` 读取 `plugins/MythicMobs/Spawners` 配置
2. **怪物刷新** → `MMListener` 捕获 `MythicMobSpawnEvent`，记录 Cooldown 起始时间
3. **怪物死亡** → `MMListener` 捕获 `MythicMobDeathEvent`，记录 Warmup 起始时间
4. **变量请求** → `MMPlaceholder` 实时计算并返回剩余时间

## 📁 MythicMobs 刷怪点配置示例

```yaml
# plugins/MythicMobs/Spawners/boss_spawner.yml
boss_spawner:
  MobName: boss_dragon
  World: world
  SpawnRadius: 5
  MobsSpawned: 1
  MobLevel: 1
  Cooldown: 3600        # 冷却时间（秒）
  Warmup: 60            # 预热时间（秒）
  FirstSpawn: true
  AutoSpawn: true
```

## 🔨 编译构建

```bash
# 克隆项目
git clone https://github.com/your-repo/MMSpawnerTimerHD.git

# 构建
./gradlew build

# 输出文件
# build/libs/MMSpawnerTimerHD-*.jar
```

## 📜 开源协议

本项目基于 MIT 协议开源。

## 👨‍💻 作者

**Restond**

---

如果这个项目对你有帮助，欢迎 ⭐ Star 支持！
