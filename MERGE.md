# MERGE.md · 床位管理模块合并交付清单

> **给组长**：本文件说明合并本模块时**你必须拉取哪些文件**、哪些文件可以不要、拉下来怎么放、必须改哪几处配置、以及合并后的验收步骤。
> 后端代码在 `Bed Management/`（Spring Boot 3.5.16 + MyBatis-Plus + Swagger），前端代码在 `care-center-ui-main/`（Vue 3 + Vite + Element Plus）。
> 本文件与代码在同一次提交里，拉取 main 分支即可看到全部内容。

---

## 0. 模块概览（先看这个）

| 项目 | 内容 |
| :--- | :--- |
| 后端技术栈 | Spring Boot 3.5.16 / Java 17 / MyBatis-Plus 3.5.17 / MySQL 8（演示可切 H2）/ springdoc-openapi 2.9.1 / Lombok |
| 后端根包名 | `com.neusoft.yiyang` |
| 接口统一前缀 | `server.servlet.context-path: /api` → 真实地址 `http://localhost:8080/api/bed/**` |
| 统一响应体 | `{ code, msg, data }`，`code = 200` 表示业务成功；业务失败返回 **HTTP 400** + msg |
| 前端技术栈 | Vue 3 + Vite 5 + Element Plus + axios（`axios.defaults.baseURL = '/api'`） |
| 前端页面 | 床位示意图（`src/views/BedMap.vue`）、床位管理（`src/views/BedManage.vue`），顶部菜单在 `src/App.vue` |
| 接口数量 | 5 个（见 [第 7 节](#7-接口清单)） |
| 数据库表 | `room`、`bed`、`beddetails`、`customer`（建库脚本 `Bed Management/sql/yiyang_bed.sql`） |
| 免装 MySQL 跑起来 | `java -jar target/bed-management-1.0.0.jar --spring.profiles.active=h2`（H2 内存库，启动时自动重建演示数据） |

⚠️ 两个最容易踩的点（第 4、5 节会展开）：
1. 后端目录名 `Bed Management` **带空格**，命令行 / 脚本里必须加引号；建议合并进主项目时重命名为 `bed-management`。
2. 本模块自带 `context-path: /api`。如果主项目**已经**有全局 `/api` 前缀，必须删掉这一行，否则会变成 `/api/api/bed/**`。

---

## 1. 必须拉取的文件（核心清单）

### 1.1 后端 —— 启动与配置（3 个）

| 文件 | 作用 | 合并注意 |
| :--- | :--- | :--- |
| `Bed Management/pom.xml` | 依赖清单（web / validation / mybatis-plus / mysql / h2 / springdoc / lombok） | 见 4.1：确认 Spring Boot 版本与主项目一致；`maven-compiler-plugin` 的 `annotationProcessorPaths` **不能删** |
| `Bed Management/src/main/java/com/neusoft/yiyang/BedManagementApplication.java` | 启动类，含 `@MapperScan("com.neusoft.yiyang.mapper")` | 主项目已有启动类时**不要**直接拷这个文件，只需把 `@MapperScan` 合并到主启动类 |
| `Bed Management/src/main/resources/application.yml` | 端口 8080 / MySQL 数据源 / MyBatis-Plus / jackson / swagger 路径 | 见 4.2：url、用户名、密码、端口、context-path 都要按主项目改 |

### 1.2 后端 —— 通用层 `common/`（6 个）

| 文件 | 作用 |
| :--- | :--- |
| `common/Result.java` | 统一响应体 `{code, msg, data}` |
| `common/ResultCode.java` | 状态码常量（200 / 400 / 500） |
| `common/BusinessException.java` | 业务异常 |
| `common/GlobalExceptionHandler.java` | 全局异常处理（业务异常→HTTP 400；其它→HTTP 500） |
| `common/BedStatus.java` | 床位状态常量：1 空闲 / 2 有人 / 3 外出（对应 `bed.bed_status`） |
| `common/BedDetailsStatus.java` | 使用记录状态常量：`is_deleted` 0 生效 / 1 历史，以及页签取值 1 / 0 |

> 若主项目已有自己的 `Result` / 全局异常处理器，**只保留一份**，把本模块代码改成主项目的写法（见 5.4）。

### 1.3 后端 —— 其余 Java 源码（23 个）

| 目录 | 文件 |
| :--- | :--- |
| `config/`（2） | `OpenApiConfig.java`（Swagger 文档信息）、`WebMvcConfig.java`（跨域） |
| `controller/`（1） | `BedController.java`（5 个接口入口） |
| `dto/`（3） | `BedDetailsQueryDTO.java`、`BedSwapDTO.java`、`UpdateEndTimeDTO.java` |
| `entity/`（4） | `Room.java`、`Bed.java`、`BedDetails.java`、`Customer.java` |
| `mapper/`（4） | `RoomMapper.java`、`BedMapper.java`、`BedDetailsMapper.java`、`CustomerMapper.java` |
| `service/`（2） | `BedService.java`、`BedManageService.java` |
| `service/impl/`（2） | `BedServiceImpl.java`、`BedManageServiceImpl.java` |
| `util/`（1） | `DateUtils.java`（前端日期字符串兼容处理） |
| `vo/`（4） | `RoomBedsVO.java`、`BedVO.java`、`BedStatisticsVO.java`、`BedDetailsVO.java` |

> 以上文件都在 `Bed Management/src/main/java/com/neusoft/yiyang/` 下，**共 30 个 .java**。

### 1.4 后端 —— MyBatis 映射、演示配置与数据库脚本（6 个文件）

| 文件 | 作用 | 合并注意 |
| :--- | :--- | :--- |
| `src/main/resources/mapper/BedMapper.xml` | 床位统计聚合查询 | `mapper-locations` 指向 `classpath*:/mapper/**/*.xml`，主项目若已配置同路径，直接放进去即可 |
| `src/main/resources/mapper/BedDetailsMapper.xml` | 床位使用详情多条件联查（beddetails + customer） | 同上 |
| `src/main/resources/application-h2.yml` | H2 演示 profile（免装 MySQL 时使用） | 不需要演示模式可以不拿这个文件 |
| `Bed Management/sql/yiyang_bed.sql` | MySQL 建库建表 + 演示数据（**主项目用 MySQL 时执行这个**） | 见 4.4：与主项目同名表必须字段一致 |
| `src/main/resources/db/h2-schema.sql` | H2 演示用建表脚本 | 不想装 H2 可以不拿这 2 个脚本，并删掉 pom 里的 h2 依赖 |
| `src/main/resources/db/h2-data.sql` | H2 演示数据（与 sql/yiyang_bed.sql 一致） | 同上 |

### 1.5 前端（10 个文件，缺一不可）

| 文件 | 作用 |
| :--- | :--- |
| `care-center-ui-main/package.json` | 依赖与脚本（`npm run dev` / `npm run build`） |
| `care-center-ui-main/package-lock.json` | 锁定依赖版本（**建议一起拿**，避免版本漂移） |
| `care-center-ui-main/index.html` | 入口 HTML（引 `/favicon.svg`） |
| `care-center-ui-main/public/favicon.svg` | 页面图标（index.html 引用） |
| `care-center-ui-main/vite.config.js` | 端口 5173 + 代理 `/api → http://localhost:8080` |
| `care-center-ui-main/src/main.js` | 挂载 Vue、注册 Element Plus 与图标、`axios.defaults.baseURL = '/api'` |
| `care-center-ui-main/src/App.vue` | 外壳：顶部菜单、主题、全局毛玻璃样式、路由切换 `BedMap` / `BedManage` |
| `care-center-ui-main/src/views/BedMap.vue` | 床位示意图页面 |
| `care-center-ui-main/src/views/BedManage.vue` | 床位管理页面（查询 / 修改结束时间 / 床位调换） |
| `care-center-ui-main/src/components/SettingsDrawer.vue` | 右上角设置抽屉（背景壁纸、透明度、深浅色） |

---

## 2. 可以不拉取的文件（附原因，不用纠结）

| 文件 / 目录 | 为什么不拿 |
| :--- | :--- |
| `README.md`、`Show.md` | 本模块的说明书与项目讲解手册，是给人看的文档，不是运行代码 |
| `docs/images/01-bed-map.svg` 等 | `Show.md` 里用的界面示意图 |
| `verify/api-test.ps1` | 32 项接口自测脚本（可选）。注意：**它会改动演示数据**（张伟的床位），跑完需重启后端恢复 |
| `settings.xml` | Maven 阿里云镜像配置参考，组长本机已配镜像就不需要 |
| `.github/modernize/java-upgrade/hooks/scripts/*` | VS Code Java 插件自动生成的钩子脚本，与模块功能无关 |
| `.vscode/settings.json`、`care-center-ui-main/.vscode/extensions.json` | IDE 个人配置 |
| `care-center-ui-main/README.md` | Vite 脚手架自带说明 |
| `care-center-ui-main/src/style.css` | 脚手架残留样式，全项目**无任何引用**，删掉不影响运行 |
| `care-center-ui-main/src/components/HelloWorld.vue` | 脚手架示例组件，没有任何地方 import |
| `care-center-ui-main/src/assets/hero.png`、`vue.svg`、`vite.svg` | 只被 `HelloWorld.vue` 引用，随它一起不要 |
| `care-center-ui-main/public/icons.svg` | 只被 `HelloWorld.vue` 引用 |
| `target/`、`node_modules/`、`dist/` | 构建产物，永远不要提交 |

> 一句话：**第 1 节列出的 48 个文件（后端 38 + 前端 10）就是本模块的全部内容**，其余都是文档、脚手架残留和工具配置。

---

## 3. 拿到文件后怎么放

### 3.1 两条路线

- **路线 A（推荐，改动最小）**：保持包名 `com.neusoft.yiyang` 不变，把 `Bed Management/src/main/java/com/neusoft/yiyang/**` 整个包直接放进主后端的 `src/main/java/` 下；前端只把 2 个 `views` + `SettingsDrawer.vue` 放进去，再把 `App.vue` 里的**菜单和切换逻辑**合并到主前端布局里。
- **路线 B（先跑通再合并）**：先在组长电脑上把本模块**独立跑起来**（后端 `--spring.profiles.active=h2` 免装数据库 + 前端 `npm run dev`），接口和页面都确认没问题后，再按 3.2 的对照表逐个搬运。

### 3.2 目录对照表

| 本模块路径 | 合并到哪里 |
| :--- | :--- |
| `Bed Management/src/main/java/com/neusoft/yiyang/**`（30 个 .java） | `<主后端>/src/main/java/com/neusoft/yiyang/**` |
| `Bed Management/src/main/resources/application.yml` | 配置项**合并进**主后端同名文件（不要整个覆盖） |
| `.../src/main/resources/application-h2.yml`、`db/*.sql` | 需要"免装 MySQL 演示"时才放；否则可整段删掉 |
| `.../src/main/resources/mapper/*.xml` | `<主后端>/src/main/resources/mapper/` |
| `Bed Management/sql/yiyang_bed.sql` | 在主项目数据库里执行一次 |
| `care-center-ui-main/src/views/BedMap.vue`、`BedManage.vue` | `<主前端>/src/views/` |
| `care-center-ui-main/src/components/SettingsDrawer.vue` | `<主前端>/src/components/` |
| `care-center-ui-main/src/main.js`、`vite.config.js`、`index.html` | 参考其内容**合并进**主前端对应文件（不要覆盖） |
| `care-center-ui-main/package.json` | 把 4 个依赖合并进主前端：`vue`、`vite`、`element-plus`、`@element-plus/icons-vue`、`axios` |

### 3.3 一键导出脚本（可选，省得一个个找）

在仓库根目录（含本文件的目录）用 PowerShell 运行，会把"必须拉取的文件"整包复制到上一级的 `merge-pick/` 里：

```powershell
$src = (Get-Location).Path
$dst = Join-Path (Split-Path $src -Parent) 'merge-pick'
Remove-Item $dst -Recurse -Force -ErrorAction SilentlyContinue
$files = @(
  'Bed Management/pom.xml',
  'Bed Management/sql/yiyang_bed.sql',
  'Bed Management/src/main/java/com/neusoft/yiyang',
  'Bed Management/src/main/resources/application.yml',
  'Bed Management/src/main/resources/application-h2.yml',
  'Bed Management/src/main/resources/db',
  'Bed Management/src/main/resources/mapper',
  'care-center-ui-main/index.html',
  'care-center-ui-main/package.json',
  'care-center-ui-main/package-lock.json',
  'care-center-ui-main/vite.config.js',
  'care-center-ui-main/public/favicon.svg',
  'care-center-ui-main/src/main.js',
  'care-center-ui-main/src/App.vue',
  'care-center-ui-main/src/views',
  'care-center-ui-main/src/components/SettingsDrawer.vue'
)
foreach ($f in $files) {
  $from = Join-Path $src $f
  $to   = Join-Path $dst $f
  New-Item (Split-Path $to -Parent) -ItemType Directory -Force | Out-Null
  Copy-Item $from $to -Recurse -Force
}
Write-Host "已导出到 $dst"
```

---

## 4. 合并时必须改的 5 处配置

### 4.1 `pom.xml`

- **Spring Boot 版本与主项目对齐**：本模块当前是 `3.5.16` / `java.version=17`。主项目用别的版本就以主项目为准，模块代码没用任何版本专属特性。
- `maven-compiler-plugin` 里的 `annotationProcessorPaths`（lombok）**务必保留**：JDK 23 起 javac 不再隐式扫描 classpath 上的注解处理器，删掉它会报一堆「找不到符号 getXxx() / log」。
- 不需要「免装 MySQL 演示」时，可以删掉 `com.h2database:h2` 依赖，并删掉 `application-h2.yml` + `resources/db/` 两个脚本。

### 4.2 `application.yml`（就改这几行）

```yaml
server:
  port: 8080              # ① 与主项目端口冲突就改
  servlet:
    context-path: /api    # ② 主项目已有全局 /api 前缀 → 删掉这一行（否则变成 /api/api）
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yiyang?...   # ③ 改成主项目的库名
    username: root                                # ④ 改成主项目的账号
    password: 123456                              # ⑤ 改成主项目的密码
```

其余配置（jackson 日期格式、`map-underscore-to-camel-case` 等）主项目通常已有，**重复了就保留主项目那一份**。

### 4.3 前端 3 处

| 文件 | 要改的地方 |
| :--- | :--- |
| `vite.config.js` | `server.port: 5173` 是否被占；`proxy['/api'].target` 指向主项目后端端口 |
| `src/main.js` | `axios.defaults.baseURL = '/api'`，必须与后端 `context-path` 一致 |
| `src/App.vue` | 本模块用**内部变量**切换两个页面（没有用 vue-router）；主项目若用 vue-router，把 `BedMap.vue` / `BedManage.vue` 注册成两条路由即可 |

### 4.4 数据库

1. 先执行 `Bed Management/sql/yiyang_bed.sql`（建库 + `room`/`bed`/`beddetails`/`customer` 四张表 + 演示数据）。
2. 主项目**已有** `room` / `bed` / `customer` 表时**不要重复建表**，只确认字段一致；不一致以主项目表结构为准，改 `mapper/*.xml` 里的列名。
3. 三个字段含义别搞错（模块逻辑强依赖）：
   - `bed.bed_status`：1 空闲 / 2 有人 / 3 外出
   - `beddetails.is_deleted`：0 正在使用 / 1 历史记录（**不是** MyBatis-Plus 的逻辑删除，所以实体上没有 `@TableLogic`）
   - 「床位管理」页签取值与 `is_deleted` **正好相反**：页签 1 → is_deleted 0，页签 0 → is_deleted 1

### 4.5 包名（可能不用改）

主项目根包名不是 `com.neusoft.yiyang` 时：把 `com/neusoft/yiyang` 目录改名，全局替换包声明与 import，并同步 `application.yml` 的 `mybatis-plus.type-aliases-package`、`logging.level`，以及启动类上的 `@MapperScan("com.neusoft.yiyang.mapper")`。

---

## 5. 冲突高风险点（按优先级排）

| # | 风险点 | 会出现什么现象 | 怎么处理 |
| :--: | :--- | :--- | :--- |
| 1 | **双重 `/api` 前缀** | 接口全部 404，路径变成 `/api/api/bed/**` | 删掉模块 `context-path`，或改前端 `baseURL`（二选一） |
| 2 | 两套统一响应体 | 前端 `res.data.code === 200` 判断失效 | 只保留主项目的 `Result`，把模块里引用改过去 |
| 3 | 两套全局异常处理器 | 异常返回格式不统一（有的 400 有的 500 结构不同） | 只保留一个 `@RestControllerAdvice` |
| 4 | 重复的 CORS 配置 | 跨域头重复、启动告警 | 主项目已配就删掉模块的 `WebMvcConfig` |
| 5 | springdoc / Swagger 版本冲突 | 文档页 404 或启动失败 | 用主项目已有版本，模块的 `OpenApiConfig` 只保留标题信息或直接删 |
| 6 | `@MapperScan` 重复或包名不同 | MyBatis 报 `Invalid bound statement` / 找不到 Mapper | 合并到主启动类，包名跟着改 |
| 7 | 同名实体类 / VO | 编译报 duplicate class | 以主项目实体为准，改模块 import |
| 8 | 端口占用 | 8080 或 5173 被占 | 改 `server.port` / `server.port`（前端 vite） |
| 9 | 前端两套 `App.vue` 打架 | 菜单丢失或页面不显示 | **只合并菜单项和组件挂载**，不要覆盖主 `App.vue` |
| 10 | 同名表但结构不同 | SQL 报 `Unknown column` | 以主项目表结构为准，改 XML 列名 |

---

## 6. 合并后 5 步验收

1. **建库**：`mysql -u root -p < "Bed Management/sql/yiyang_bed.sql"`
2. **启动后端**：在 IDEA 里运行主项目启动类（或 `mvn spring-boot:run`）。暂时不想连 MySQL 时，可先单独跑本模块验证：`java -jar target/bed-management-1.0.0.jar --spring.profiles.active=h2`
3. **看文档**：打开 `http://localhost:8080/api/swagger-ui/index.html`，确认 5 个接口都在
4. **接口自测**（PowerShell，预期统计 48 / 40 / 6 / 2）：

```powershell
Invoke-RestMethod 'http://localhost:8080/api/bed/statistics'              # total 48, free 40, occupied 6, out 2
Invoke-RestMethod 'http://localhost:8080/api/bed/map?floor=1'             # 6 个房间，每间 4 张床
Invoke-RestMethod 'http://localhost:8080/api/bed/manage/details?status=1' # 8 条正在使用的记录
```

5. **前端**：`cd care-center-ui-main; npm install; npm run dev` → 打开 `http://localhost:5173`，把「床位示意图 / 床位管理」两个页面和三个弹窗（修改结束时间、床位调换、右上角设置）各点一遍。

> 可选：`verify/api-test.ps1` 是 32 项接口回归自测，需要后端在跑；注意它会改动演示数据，跑完重启后端即可恢复。

---

## 7. 接口清单

| # | 方法 | 路径（含统一前缀） | 说明 |
| :--: | :--- | :--- | :--- |
| 1 | GET | `/api/bed/statistics` | 床位统计：总 / 空闲 / 有人 / 外出 |
| 2 | GET | `/api/bed/map?floor=1` | 床位示意图数据：按楼层返回房间及其床位 |
| 3 | GET | `/api/bed/manage/details?status=1&customerName=&startDate=` | 床位使用详情多条件查询（`status` 1 正在使用 / 0 使用历史） |
| 4 | PUT | `/api/bed/manage/updateEndTime` | 修改床位使用结束时间（body：`{id, endDate}`） |
| 5 | POST | `/api/bed/manage/swap` | 床位调换（body：`{customerId, newFloor, newRoomNo, newBedId, swapDate}`，只能当天调换当天办理） |

---

## 附：本模块合并前已通过的自测记录

| 项目 | 结果 |
| :--- | :--- |
| 后端编译 | `mvn -s settings.xml -B -DskipTests clean package` → **BUILD SUCCESS**（30 个源文件） |
| 免装数据库启动 | `java -jar target/bed-management-1.0.0.jar --spring.profiles.active=h2` → 启动成功 |
| 接口 1 | `/api/bed/statistics` → `{total:48, free:40, occupied:6, out:2}` |
| 接口 2 | `/api/bed/map?floor=1` → HTTP 200 |
| 接口 3 | `/api/bed/manage/details?status=1` → 8 条记录 |
| 文档 / 控制台 | `/api/swagger-ui/index.html`、`/api/h2-console` → 均 HTTP 200 |
| 代码卫生 | 已清理教学式/口语化注释与装饰横幅，仅保留 Javadoc 与关键业务规则说明 |


