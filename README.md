# LET THERE BE LIGHT (LTBL) — Fabric 版

纯指令的光照 / 刷怪点查看工具：`/ltbl` 打开后，会用粒子标出附近**当前会刷怪**的方块位置，
方便玩家插火把、铺半砖。

- 本分支（`fabric-26.3`）：Fabric 服务端模组，目标 Minecraft **26.3**、Fabric Loader 0.19.5+、Java 25。
- `master` 分支：原作者 FJYsmall 的 Bukkit 插件（CraftBukkit 1.14.4），保留不动。

## 构建

需要 JDK 25（`build.gradle` 里按 Java 25 编译）；构建前确认 `JAVA_HOME` 指向 JDK 25：

```powershell
set JAVA_HOME=C:\path\to\jdk-25
gradlew.bat build
# 产物：build/libs/ltbl-2.0.0+26.3.jar
```

```bash
# git-bash / WSL
export JAVA_HOME=/path/to/jdk-25
./gradlew build
```

首次构建会下载 Gradle、Fabric Loom 和 Minecraft 26.3 的依赖。Loom 1.18 起使用 Mojang 官方映射，
不再需要 yarn mappings。构建脚本给 Gradle 的 JVM 加了
`-Djdk.tls.namedGroups=x25519,secp256r1,secp384r1`：JDK 25 默认启用 ML-KEM 混合密钥交换，
在部分代理或网络环境下请求 `maven.fabricmc.net` 会直接断开 TLS 握手，关掉它即可。

## 安装

1. 服务器需要 Fabric Loader 0.19.5+ 和 **Fabric API**（26.3 对应 `0.164.x`，以 [Fabric 官网](https://fabricmc.net/develop) 列出的版本为准）。
2. 把 `ltbl-*.jar` 放进服务端 `mods/` 目录，重启服务器。
3. 模组是 `"environment": "server"`，只在服务端运行；纯原版客户端即可看到粒子，不需要装任何东西。

## 指令

所有指令都按玩家单独生效，开关状态不写盘，玩家下线或服务器重启后自动关闭。

| 指令 | 说明 |
| --- | --- |
| `/ltbl` | 打开 / 关闭自己的光照显示 |
| `/ltbl on` / `/ltbl off` | 明确打开 / 关闭 |
| `/ltbl status` | 查看当前半径、光照阈值、粒子、扫描一圈所需时间 |
| `/ltbl radius <8-64>` | 改自己的扫描半径（默认 32 格，跟随玩家移动） |
| `/ltbl light <0-15>` | 改自己的光照阈值；`/ltbl light auto` 恢复按维度自动判定 |
| `/ltbl particle <名字>` | 改自己的粒子样式，见下表；不带参数查看当前值和可选列表 |

阈值自动值时取自维度数据包：

| 维度 | 方块光照阈值 |
| --- | --- |
| 主世界 | 0（1.18 之后怪物只在方块光照 0 处生成） |
| 下界 | 7 |
| 末地 | 0 |

算法是 `min(monster_spawn_block_light_limit, monster_spawn_light_level 的最大值)`，所以改了数据包、
换了维度类型，阈值也会跟着变。

粒子样式（`/ltbl particle` 或配置里的 `particle`）：

| 名字 | 观感 |
| --- | --- |
| `barrier`（默认） | 屏障方块图标，最接近旧版；按世界光照渲染，但图标对比度高，无光处也能辨认 |
| `end_rod` | 白色小球；客户端对 `END_ROD` 硬编码 `0xF000F0`，暗处可见性最保险 |
| `glow` | 小圆点，随生命周期提亮 |
| `flame` | 橙色火苗，会向上飘 |

## 配置文件

首次启动在服务端运行目录下生成 `config/ltbl.json`（容器部署时通常位于挂载的数据目录里）：

```json
{
  "radius": 32,
  "sectionsPerTick": 2,
  "maxParticlesPerTick": 400,
  "particleViewDistance": 24.0,
  "particle": "barrier"
}
```

| 字段 | 说明 |
| --- | --- |
| `radius` | 扫描半径（格），玩家可用 `/ltbl radius` 临时覆盖 |
| `sectionsPerTick` | 每 tick 扫描几个 16×16×16 区块段。越大刷新越快、开销越高 |
| `maxParticlesPerTick` | 每 tick 最多为单个玩家发多少个粒子包，防止大面积黑暗时刷屏 |
| `particleViewDistance` | 只标记眼睛 24 格内的方块（客户端粒子上限 32 格） |
| `particle` | `barrier`（默认，屏障图标）、`end_rod`、`glow`、`flame` |

## 工作原理与开销

- 扫描以 **16×16×16 区块段**为单位切分，每 tick 只处理 `sectionsPerTick` 个，扫完一段就立刻为该段
  发粒子，所以画面是从玩家身边一圈圈铺开的，不会整块跳变。
- 完全跑在服务端主线程（原 Bukkit 版用的是异步任务，`getBlockAt` 跨线程访问世界并不安全）。
- 只读已加载的区块（`getChunkNow`），不会为了显示强制加载 / 生成区块；没加载的区域不显示。
- 参考开销（26.3 专用服务器、半径 32、约 25 个已加载区块）：一整圈约 **15 ms** CPU，按默认
  `sectionsPerTick=2` 摊到 62 tick 上约 0.25 ms/tick/玩家；洞穴密集区候选方块更多，大致同量级。
- 出生点刷怪判定简化自原版 `Monster.isDarkEnoughToSpawn`：可站立（无碰撞体积、无流体）+ 脚下
  是完整上表面 + 头顶有 2 格空间 + 方块光照不超过阈值。蘑菇岛不标记。

## 与旧 Bukkit 版的差异

| | 旧版（master） | 本分支 |
| --- | --- | --- |
| 平台 | Bukkit / CraftBukkit 1.14.4 | Fabric 26.3 |
| 扫描线程 | 异步任务（跨线程读世界） | 服务端主线程，分段摊平 |
| 光照阈值 | 固定 ≤7 | 按维度自动判定，可指令覆盖 |
| 维度 | 只支持主世界 | 主世界 / 下界 / 末地 |
| 粒子 | `Particle.BARRIER` | `barrier`（`BLOCK_MARKER` 模拟），另有 `end_rod`/`glow`/`flame` 可选 |
| 大范围黑暗 | 每圈可能一次发几千个包 | 每 tick 限量，按区块段分批 |
| 生物群系 / 方块黑名单 | 手写几百个方块类型 | 用碰撞体积 / 完整面判定，随版本自动适配 |

## 已知限制

- 粒子只在 `particleViewDistance` 内可见，超出客户端 32 格粒子距离的部分不会渲染；想看远处请走近。
- 只有玩家能执行 `/ltbl`（控制台执行会提示）。
- 不做持久化：重启、重连都要重新 `/ltbl`。
- 阈值判定按原版规则，如果服务器有自定义刷怪插件/数据包改了刷怪逻辑，以实际为准。
