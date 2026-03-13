# MMSpawnerTimerHD

一个专为 MythicMobs 刷怪点设计的倒计时监控插件，支持 PlaceholderAPI 变量绑定、实时状态显示与数据持久化。

## ✨ 功能特性

- **实时倒计时**：精确追踪 MythicMobs 刷怪点的冷却时间
- **PAPI 变量支持**：提供多种变量格式，兼容记分板、全息图等显示插件
- **数据持久化**：服务器重启后自动恢复倒计时进度
- **自定义配置**：支持时间格式、消息文本等自定义
- **低性能开销**：优化的任务调度，避免主线程卡顿

## 📥 安装要求

| 插件 | 必需 | 说明 |
|------|:----:|------|
| [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) | ✅ | 变量输出核心 |
| [MythicMobs](https://mythiccraft.io/index.php?resources/mythicmobs.1/) | ⚪ | 刷怪点 API（软依赖） |
| DecentHolograms | ⚪ | 全息图显示（可选） |

## 📋 变量列表

| 变量                           | 说明     | 示例输出             |
|------------------------------|--------|------------------|
| `%mmspawner_remaining_<名称>%` | 剩余秒数   | `45`             |
| `%mmspawner_time_<名称>%`      | 格式化时间  | `00:45`          |
| `%mmspawner_status_<名称>%`    | 状态文本   | `计时中` / `已刷新`    |
| `%mmspawner_ready_<名称>%`     | 是否就绪   | `true` / `false` |
| `%mmspawner_config_text%`    | 倒计时文本 | `等待刷怪中...`         |

## ⚙️ 配置文件

```yaml
# config.yml
countdown:
  text: "等待刷怪中..."

# 时间格式 (HH=小时, mm=分钟, ss=秒)
time-format: "mm:ss"

# 消息配置
messages:
  ready: "已刷新"
  counting: "计时中"
  not-found: "N/A"

```

## 🔧 技术架构

```
MMSpawnerTimerHD/
├── MMSpawnerTimer.java      # 主类 - 插件入口与配置管理
├── MMSpawnerMonitor.java    # 监控器 - 数据管理与任务调度
├── SpawnerData.java         # 数据模型 - 刷怪点计时信息
├── MMListener.java          # 监听器 - MythicMobs 事件处理
└── MMPlaceholder.java       # 变量扩展 - PAPI 变量注册
```

### 核心流程

1. **怪物刷新** → `MMListener` 捕获 `MythicMobSpawnEvent`
2. **记录数据** → `MMSpawnerMonitor` 存储/更新刷怪点信息
3. **定时更新** → 每秒执行 `updateCountdown()` 计算剩余时间
4. **变量请求** → `MMPlaceholder` 响应 PAPI 变量查询

## 📊 数据持久化

插件自动保存刷怪点数据到 `data.yml`：

```yaml
spawners:
  boss_dragon:
    lastSpawn: 1709500000000
    cooldown: 3600
  dungeon_mob:
    lastSpawn: 1709500100000
    cooldown: 1800
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
