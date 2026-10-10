# Show.md —— 项目讲解手册（写给「完全小白」的你和我）

> **这份文档解决什么问题？**
> 你把这个仓库发给同学，或者当着同学的面打开项目时，最常见的三个尴尬是：
> 1. 打开一个文件夹，二十几个 `.java` 文件，**不知道哪个是重点**；
> 2. 同学问「这个文件干啥的？」，你只能说「反正能跑」；
> 3. 同学问「这几十个文件是怎么串起来的？」，你也说不清。
>
> **`Show.md` 就是答案。** 它把仓库里**每一个文件**、每一张表、每一个接口、
> 每一处「为什么这么写」全部摊开，用「零基础也能懂」的中文讲一遍。
> 你不需要背，讲到哪一节翻到哪一节就行。

---

## 📖 怎么用这份文档

| 场景 | 看哪几节 |
| :--- | :--- |
| 只有 30 秒，先让人知道我做的是啥 | **第 0 节**（电梯演讲） |
| 同学想快速看懂结构 | **第 1 节**（全景图）+ **第 2 节**（文件清单） |
| 同学问「数据存哪、怎么存的」 | **第 3 节**（数据库设计） |
| 有人逐行问后端代码 | **第 4 节**（后端 30 个 Java 文件逐个讲） |
| 有人问前端怎么写的 | **第 5 节**（前端 8 个文件逐个讲） |
| 有人想看「功能是怎么实现的」 | **第 6 节**（5 个接口内部流程） |
| 有人问「这些文件怎么串起来」 | **第 7 节**（一条请求的完整生命周期） |
| 展示你的「思维」和「取舍」 | **第 8、9 节**（数据结构 + 7 个关键决策） |
| 想改代码但不知道改哪 | **第 12 节**（改功能索引表） |
| 报错了 | **第 13 节**（报错对照表） |

> 💡 文中所有行号（例如 `L106-L203`）指的是**该文件中的行号**，
> 在 VS Code 里按 `Ctrl + G` 输入行号可以直接跳过去。

---

## 目录

- [第 0 节 · 30 秒电梯演讲](#第-0-节--30-秒电梯演讲)
- [第 1 节 · 项目全景图（一张图看懂前后端）](#第-1-节--项目全景图一张图看懂前后端)
- [第 2 节 · 顶层目录：每个文件干啥的](#第-2-节--顶层目录每个文件干啥的)
- [第 3 节 · 数据库设计（4 张表 + 关系 + 演示数据）](#第-3-节--数据库设计4-张表--关系--演示数据)
- [第 4 节 · 后端逐文件讲解（30 个 Java 文件）](#第-4-节--后端逐文件讲解30-个-java-文件)
- [第 5 节 · 前端逐文件讲解](#第-5-节--前端逐文件讲解)
- [第 6 节 · 5 个接口内部功能详解](#第-6-节--5-个接口内部功能详解)
- [第 7 节 · 一条请求的完整生命周期](#第-7-节--一条请求的完整生命周期)
- [第 8 节 · Entity / DTO / VO 三兄弟到底啥区别](#第-8-节--entity--dto--vo-三兄弟到底啥区别)
- [第 9 节 · 逻辑思维：我做的 7 个关键决策](#第-9-节--逻辑思维我做的-7-个关键决策)
- [第 10 节 · 前后端字段对照表](#第-10-节--前后端字段对照表)
- [第 11 节 · 注解速查表](#第-11-节--注解速查表)
- [第 12 节 · 想改功能时该打开哪个文件](#第-12-节--想改功能时该打开哪个文件)
- [第 13 节 · 报错对照表](#第-13-节--报错对照表)
- [第 14 节 · 展示话术（3 分钟版 / 7 分钟版）](#第-14-节--展示话术3-分钟版--7-分钟版)

---

## 第 0 节 · 30 秒电梯演讲

> 「这是我做的**东软颐养中心管理系统 —— 床位管理模块**。
> 它一共两块功能：**床位示意图**（按楼层看每个房间每张床是空闲、有人还是外出）和
> **床位管理**（按姓名/日期/状态查入住记录、改结束时间、给老人**调换床位**）。
> 后端是 **Spring Boot 3 + MyBatis-Plus**，前端是 **Vue 3 + Element Plus**，
> 一共对外提供 **5 个接口**。数据库 4 张表：房间、床位、床位使用记录、客户。
> 我写了 **32 项接口自动化自测**，包括 8 种失败场景，跑一遍就知道有没有改坏。
> 另外仓库里还有一个 **H2 内存库演示模式**——别人电脑上不装 MySQL 也能跑起来。」

**一句话定位**：一个 **前后端分离的 CRUD + 业务校验** 模块，难点在**「床位调换」要跨 4 张表保持数据一致**。

---

## 第 1 节 · 项目全景图（一张图看懂前后端）

```
┌──────────────────────────── 你的电脑 ────────────────────────────┐
│                                                                  │
│  ① 浏览器                                                        │
│     打开 http://localhost:5173                                   │
│            │                                                     │
│            ▼                                                     │
│  ② 前端进程（Vite 开发服务器，端口 5173）                          │
│     care-center-ui-main/                                         │
│     ├─ index.html       → 加载 src/main.js                       │
│     ├─ main.js          → axios.defaults.baseURL = '/api'        │
│     ├─ App.vue          → 顶部菜单 + 切换两个页面                  │
│     └─ views/BedMap.vue、views/BedManage.vue                     │
│            │                                                     │
│            │  浏览器发出 axios.get('/api/bed/statistics')         │
│            ▼                                                     │
│  ③ Vite 代理（vite.config.js 里的 proxy）                          │
│     看到路径以 /api 开头 → 转发给 http://localhost:8080            │
│     ⚠ 不做路径重写：/api/bed/xxx 原样转发                          │
│            │                                                     │
│            ▼                                                     │
│  ④ 后端进程（Spring Boot 内嵌 Tomcat，端口 8080）                  │
│     Bed Management/                                              │
│     context-path = /api   ← 所以真实地址是 /api/bed/**            │
│     Controller → Service → Mapper → MyBatis-Plus → JDBC          │
│            │                                                     │
│            ▼                                                     │
│  ⑤ 数据库（二选一）                                               │
│     · MySQL 8：jdbc:mysql://localhost:3306/yiyang   （默认）      │
│     · H2 内存库：jdbc:h2:mem:yiyang                 （演示用）     │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

**两条最容易搞混的「连接规则」，必须记住**：

| 规则 | 位置 | 说明 |
| :--- | :--- | :--- |
| 前端请求前缀 `/api` | `main.js` L11 `axios.defaults.baseURL = '/api'` | 所以页面里写 `axios.get('/bed/statistics')`，真实发出的是 `/api/bed/statistics` |
| 后端也要有 `/api` | `application.yml` L10 `server.servlet.context-path: /api` | 前端代理不重写路径，所以后端**必须**自己带 `/api` 前缀，否则 404 |

> 🔴 **最常见的 404 原因**：这两处**必须同时是 `/api`**。只改一边，接口立刻 404。

---

## 第 2 节 · 顶层目录：每个文件干啥的

```
d:\床位管理（2）
├── Bed Management\          ① 后端工程（Spring Boot 3 + MyBatis-Plus）
├── care-center-ui-main\     ② 前端工程（Vue 3 + Element Plus + Vite）
├── verify\                  ③ 接口自动化自测脚本（32 项断言）
├── docs\images\             ④ 界面截图目录（README 顶部预览图）
├── settings.xml             ⑤ Maven 阿里云镜像配置（可选）
├── .gitattributes           ⑥ 换行符 + 二进制文件规则
├── .gitignore               ⑦ 哪些文件不上传 GitHub
├── README.md                ⑧ 交付说明 + 2600 行零基础复习手册
└── Show.md                  ⑨ 本文件：项目讲解手册
```

### 2.1 根目录逐个文件

| 文件 / 目录 | 大小 | 干什么用的 | 要看的必要性 |
| :--- | :--- | :--- | :--- |
| `Bed Management\` | —— | **后端全部代码**。名字里有空格是历史原因，命令行里要加引号 | ⭐⭐⭐ 必看 |
| `care-center-ui-main\` | —— | **前端全部代码**。`care-center-ui` 是原主题模板名，`-main` 是包一层目录 | ⭐⭐⭐ 必看 |
| `verify\api-test.ps1` | 187 行 | **PowerShell 自测脚本**，对 5 个接口发 32 次请求并校验结果 | ⭐⭐⭐ 演示神器 |
| `docs\images\` | 4 个文件 | 3 张占位图（`.svg`）+ `README.md` 替换说明 | ⭐ 交作业用 |
| `settings.xml` | 1.2 KB | Maven 走阿里云镜像，**国内下载依赖快很多**。构建时 `mvn -s ..\settings.xml` 指定 | ⭐⭐ 网络差时必看 |
| `.gitattributes` | 1.4 KB | ①仓库统一存 LF、Windows 检出转 CRLF；②`*.png`/`*.jar` 标为 binary 防止被「转换」损坏；③声明 `package-lock.json` 是生成文件，不算语言占比 | ⭐ 体现工程素养 |
| `.gitignore` | 1.8 KB | 不上传：`target/`、`node_modules/`、`dist/`、`*.mv.db`、日志、IDE 缓存 | ⭐ 体现工程素养 |
| `README.md` | 139 KB | 前 300 行是**交付说明**（怎么跑、接口清单、数据库、常见问题），后 2300 行是**零基础复习手册**（19 节知识点 + 8 个踩坑复盘 + 30 道自测题） | ⭐⭐⭐ |
| `.github\modernize\` | —— | 编辑器 AI 插件生成的目录，**与业务无关**，可以直接忽略 | ✕ |
| `.vscode\settings.json` | 122 B | 编辑器配置，与业务无关 | ✕ |

### 2.2 后端 `Bed Management\` 目录（只列业务相关）

```
Bed Management\
├── pom.xml                              Maven 依赖清单（我用到了哪些库）
├── sql\yiyang_bed.sql                   MySQL 建库建表 + 演示数据（205 行）
└── src\main\
    ├── java\com\neusoft\yiyang\         包名 = 公司.项目名
    │   ├── BedManagementApplication.java  启动类（程序入口）
    │   ├── common\       公共层：统一返回体、状态常量、异常
    │   ├── config\       配置层：Swagger、跨域
    │   ├── controller\   接口层：对外暴露 HTTP 地址
    │   ├── service\      业务接口 + impl\ 业务实现
    │   ├── mapper\       数据访问层：和数据库对话
    │   ├── entity\       数据库表对应的 Java 类
    │   ├── dto\          前端传进来的参数对象
    │   ├── vo\           后端返回给前端的对象
    │   └── util\         工具类（日期处理）
    └── resources\
        ├── application.yml             主配置（MySQL、端口、MyBatis、Jackson）
        ├── application-h2.yml          演示配置（内存库，不用装 MySQL）
        ├── db\h2-schema.sql / h2-data.sql   H2 的建表 + 数据
        └── mapper\BedMapper.xml / BedDetailsMapper.xml   手写 SQL 放这里
```

### 2.3 前端 `care-center-ui-main\` 目录

```
care-center-ui-main\
├── package.json          依赖清单 + 3 条命令（dev / build / preview）
├── vite.config.js        端口 5173 + /api 代理到 8080
├── index.html            页面骨架，只有 <div id="app"> 和一个 script
├── public\               favicon.svg、icons.svg（不需要打包处理的静态文件）
└── src\
    ├── main.js           入口：挂载 Vue、装 Element Plus、设置 axios.baseURL
    ├── App.vue           外壳：顶部菜单 + 切换页面 + 主题/背景图
    ├── components\SettingsDrawer.vue   右上角「外观设置」抽屉
    ├── views\BedMap.vue      页面①：床位示意图
    ├── views\BedManage.vue   页面②：床位管理
    ├── assets\、style.css、components\HelloWorld.vue   ← 脚手架残留，业务无关（见 5.7 节）
    └── node_modules\     第三方依赖（不上传 GitHub）
```

---

## 第 3 节 · 数据库设计（4 张表 + 关系 + 演示数据）

### 3.1 四张表分别装什么

| 表名 | 中文 | 一句话解释 | 谁在用 |
| :--- | :--- | :--- | :--- |
| `room` | 房间表 | 有哪些房间、每个房间在几层 | 床位示意图的「楼层 → 房间」 |
| `bed` | 床位表 | 有哪些床位、每张床现在什么状态 | 示意图的「房间 → 床位」和统计 |
| `beddetails` | 床位使用记录表 | 谁、什么时候、住了哪张床、走了没有 | 床位管理列表、改结束时间 |
| `customer` | 客户（老人）表 | 老人档案 + 他现在住哪 | 列表显示姓名性别；调换时同步位置 |

### 3.2 表结构（MySQL 版，见 `sql\yiyang_bed.sql`）

```sql
room                                  bed
┌──────────────────────────┐          ┌───────────────────────────────────┐
│ id         int  PK 自增  │◄──┐      │ id         int  PK 自增           │
│ room_floor varchar(10)   │   │      │ room_no    int     ← 指向房间号   │
│ room_no    int  UNIQUE   │   └──────┤ bed_status int     1空闲2有人3外出│
│   （101~106 / 201~206）  │  通过房间号│ remarks    varchar(255)  备注     │
└──────────────────────────┘  关联，   │ bed_no     varchar(20)  1~4       │
                              不是 id   └───────────────────────────────────┘
                                                     ▲
                                                     │ bed_id
beddetails                                           │
┌────────────────────────────────────┐               │
│ id          int  PK 自增           │               │
│ start_date  date      入住时间      │               │
│ end_date    date      退住时间(null=还在住)         │
│ bed_details varchar   文本描述      │               │
│ customer_id int   → customer.id    │               │
│ bed_id      int   → bed.id ────────┼───────────────┘
│ is_deleted  int   0正在使用/1历史   │
└────────────────────────────────────┘
                    ▲
                    │ customer_id
customer            │
┌────────────────────────────────────┐
│ id  is_deleted  customer_name      │
│ customer_sex  0男 1女              │
│ room_no  varchar  ← 冗余存房间号    │
│ bed_id   int      ← 冗余存床位 id   │
│ building_no  varchar  ← 冗余存楼号  │
│ （其余年龄/身份证/电话/血型/家属…） │
└────────────────────────────────────┘
```

**关键点（也是「为什么调换床位要更新 4 张表」的根本原因）**：

- `bed` 表**没有** `room_id`，只有 `room_no`（房间号）→ 所以 Java 里查房间用的是 `Room::getRoomNo`。
- `customer` 表里**冗余**存了一份「他现在住哪」（`room_no` / `bed_id` / `building_no`）。
  这是数据库设计文档给的表结构，**冗余的好处**是查客户档案时不用联表；
  **代价**是床位一旦变动，必须**同步更新**，否则数据不一致。这一点直接决定了 `swapBed` 要写 5 个库操作。
- `customer.room_no` 是 `varchar(20)`，而 `room.room_no` 是 `int`
  → 所以代码里要写 `String.valueOf(newRoom.getRoomNo())` 做类型转换（`BedManageServiceImpl` L196）。

### 3.3 状态值的「坑」——两套 0/1 含义正好相反

| 字段 | 取值 | 含义 | 谁在用 |
| :--- | :--- | :--- | :--- |
| `bed.bed_status` | 1 / 2 / 3 | 空闲 / 有人 / 外出 | 示意图颜色、统计数字 |
| `beddetails.is_deleted` | **0** / **1** | 0 = 正在使用，1 = 已失效（历史） | 列表查询条件 |
| 前端「床位管理」页签 `status` | **1** / **0** | 1 = 正在使用，0 = 使用历史 | `BedManage.vue` 的 `el-tabs` |

> ⚠️ **最容易写错的地方**：页签的 `1` 对应 `is_deleted = 0`，**正好反着**。
> 所以 `BedManageServiceImpl` L59–L62 专门做了换算，`common\BedDetailsStatus.java` 里也写了 4 个常量
> （`IN_USE=0`、`HISTORY=1`、`TAB_IN_USE=1`、`TAB_HISTORY=0`）来防止自己写错。
> 另外 `is_deleted` **不是** MyBatis-Plus 的逻辑删除字段，所以**不能**加 `@TableLogic`
> （见 `BedDetails.java` L17–L18 的注释：它是业务状态，加了注解会被框架自动过滤掉数据）。

### 3.4 演示数据（MySQL 与 H2 完全一致）

| 数据 | 内容 |
| :--- | :--- |
| 房间 | 12 个：一层 `101~106`、二层 `201~206` |
| 床位 | **48 张**，每房间 4 张。生成公式：`id = (房间id - 1) * 4 + 第几号床` |
| 床位状态 | **空闲 40 / 有人 6 / 外出 2**。有人 = id `2,6,10,26,30,41`；外出 = id `3,27` |
| 客户 | 8 位老人（张伟、李秀英、王守义、刘桂芳、陈国强、赵春梅、孙建国、周雅琴） |
| 使用记录 | **11 条**：id 1~8 是 `is_deleted=0`（正在使用），id 9~11 是 `is_deleted=1`（历史，专给「使用历史」页签演示） |

> 张伟（客户 1）是演示主角：当前住 **101 房间 2 号床**；自测脚本会把他调到 **101 房间 4 号床**。

**两个 SQL 脚本的分工**：

| 脚本 | 用途 | 执行方式 |
| :--- | :--- | :--- |
| `sql\yiyang_bed.sql` | **MySQL 正式建库**。用 `CREATE DATABASE IF NOT EXISTS` + `CREATE TABLE IF NOT EXISTS` + `INSERT IGNORE`，**可重复执行**，不会覆盖你已有数据 | `mysql -uroot -p < yiyang_bed.sql` |
| `src\main\resources\db\h2-schema.sql`<br>`src\main\resources\db\h2-data.sql` | **H2 演示**。表结构一致，去掉 `ENGINE`/`COMMENT` 等 MySQL 专有语法；每次启动自动重建 | 启动时加 `--spring.profiles.active=h2` 即可 |

`sql\yiyang_bed.sql` 最后还有 2 段**自检查询**（L188–L205）：执行完能立刻看到
`total=48 / free=40 / occupied=6 / out=2` 和「正在使用 8 行」，**用来确认数据导入成功**。

---

## 第 4 节 · 后端逐文件讲解（30 个 Java 文件）

### 4.0 一张表看完全部后端文件

| # | 文件（相对 `Bed Management\src\main\`） | 行数 | 一句话职责 | 展示优先级 |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `java\...\BedManagementApplication.java` | 22 | 启动类，程序入口 | ⭐ |
| 2 | `java\...\common\Result.java` | 61 | **统一响应体** `{code, msg, data}` | ⭐⭐ |
| 3 | `java\...\common\ResultCode.java` | 18 | 三个状态码常量 200 / 400 / 500 | ⭐ |
| 4 | `java\...\common\BusinessException.java` | 18 | 业务异常（校验失败就抛它） | ⭐ |
| 5 | `java\...\common\GlobalExceptionHandler.java` | 49 | **全局异常处理器**（把异常翻译成 HTTP 400/500） | ⭐⭐⭐ |
| 6 | `java\...\common\BedStatus.java` | 18 | 床位状态常量 1/2/3 | ⭐ |
| 7 | `java\...\common\BedDetailsStatus.java` | 25 | 使用记录状态常量 0/1 + 页签 1/0 | ⭐⭐ |
| 8 | `java\...\config\OpenApiConfig.java` | 26 | Swagger 文档标题/版本 | ⭐ |
| 9 | `java\...\config\WebMvcConfig.java` | 29 | 跨域 CORS 配置 | ⭐ |
| 10 | `java\...\controller\BedController.java` | 77 | **5 个接口的入口** | ⭐⭐⭐ |
| 11 | `java\...\dto\BedDetailsQueryDTO.java` | 23 | 列表查询条件（status/姓名/日期） | ⭐ |
| 12 | `java\...\dto\UpdateEndTimeDTO.java` | 22 | 改结束时间的入参（id + endDate） | ⭐ |
| 13 | `java\...\dto\BedSwapDTO.java` | 32 | 调换床位的入参（客户/楼层/房间/床位/日期） | ⭐⭐ |
| 14 | `java\...\entity\Room.java` | 29 | `room` 表映射 | ⭐ |
| 15 | `java\...\entity\Bed.java` | 35 | `bed` 表映射 | ⭐ |
| 16 | `java\...\entity\BedDetails.java` | 46 | `beddetails` 表映射（含 is_deleted 说明） | ⭐ |
| 17 | `java\...\entity\Customer.java` | 89 | `customer` 表映射（字段最多） | ⭐ |
| 18 | `java\...\vo\RoomBedsVO.java` | 23 | 示意图用：房间 + 床位列表 | ⭐ |
| 19 | `java\...\vo\BedVO.java` | 28 | 示意图用：一个小方块（id/bedNo/bedStatus） | ⭐ |
| 20 | `java\...\vo\BedStatisticsVO.java` | 26 | 统计结果（total/free/occupied/out） | ⭐ |
| 21 | `java\...\vo\BedDetailsVO.java` | 38 | 列表一行数据（7 个字段） | ⭐⭐ |
| 22 | `java\...\mapper\RoomMapper.java` | 12 | 房间的数据访问（继承即拥 CRUD） | ⭐ |
| 23 | `java\...\mapper\BedMapper.java` | 23 | 床位的数据访问 + 统计查询 | ⭐ |
| 24 | `java\...\mapper\BedDetailsMapper.java` | 31 | 使用记录访问 + 多条件联查 | ⭐⭐ |
| 25 | `java\...\mapper\CustomerMapper.java` | 12 | 客户的数据访问 | ⭐ |
| 26 | `java\...\service\BedService.java` | 27 | 示意图业务接口（2 个方法） | ⭐ |
| 27 | `java\...\service\BedManageService.java` | 37 | 床位管理业务接口（3 个方法） | ⭐ |
| 28 | `java\...\service\impl\BedServiceImpl.java` | 84 | 示意图实现（统计 + 组装房间床位） | ⭐⭐ |
| 29 | `java\...\service\impl\BedManageServiceImpl.java` | 219 | **核心业务实现**（查询/改时间/调换） | ⭐⭐⭐⭐ |
| 30 | `java\...\util\DateUtils.java` | 61 | 日期字符串兼容处理（防时区差一天） | ⭐⭐ |
| 31 | `resources\application.yml` | 65 | **主配置**（端口/context-path/MySQL/MyBatis/Jackson） | ⭐⭐⭐ |
| 32 | `resources\application-h2.yml` | 27 | H2 演示配置 | ⭐⭐ |
| 33 | `resources\mapper\BedMapper.xml` | 26 | 统计 SQL（一次聚合查 4 个数） | ⭐⭐ |
| 34 | `resources\mapper\BedDetailsMapper.xml` | 30 | 列表 SQL（联表 + 动态条件） | ⭐⭐ |
| 35 | `resources\db\h2-schema.sql` / `h2-data.sql` | 62 / 58 | H2 建表 + 演示数据 | ⭐ |
| 36 | `pom.xml`（在工程根目录） | 153 | 依赖与构建配置 | ⭐⭐ |
| 37 | `sql\yiyang_bed.sql`（在工程根目录） | 205 | MySQL 建库 + 数据 | ⭐⭐ |

> 合计：**30 个 Java 文件** + 2 个 Mapper XML + 2 个 yml + 2 个 SQL 脚本 + 1 个 `pom.xml`。
> 下面的 4.1~4.13 就按这个顺序逐个讲。

> 一个「文件类型」的口诀：
> **entity 是数据库的镜子，DTO 是前端的嘴（说进来的话），VO 是前端的眼（看出去的数据），
> Mapper 是手（去数据库拿/改），Service 是脑子（判断该不该做），Controller 是门（决定谁能进）。**

### 4.1 启动类 `BedManagementApplication.java`（22 行）

```java
@SpringBootApplication                    // ① 声明这是 Spring Boot 应用
@MapperScan("com.neusoft.yiyang.mapper")  // ② 扫描 mapper 包，自动生成 Mapper 的实现类
public class BedManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BedManagementApplication.class, args);  // ③ 启动内嵌 Tomcat
    }
}
```

| 注解 | 作用（小白版） |
| :--- | :--- |
| `@SpringBootApplication` | 等于「扫描本包及子包的 `@Controller`/`@Service` 等注解，并自动装配 Spring Boot 的一堆默认配置」 |
| `@MapperScan("...mapper")` | 告诉 MyBatis「`mapper` 包里那些只有接口没有实现类的 Mapper，运行时给它们自动生成实现」 |

> 🔎 **为什么接口不用写实现类？** 因为 MyBatis-Plus 在启动时用**动态代理**帮你造了一个实现，
> 这个实现做的事就是「把方法调用翻译成 SQL」。这就是「面向接口编程」最直观的体现。

### 4.2 公共层 `common\`（6 个文件，是整个项目的「公共语言」）

#### ① `Result.java`（61 行）—— 统一返回体

**为什么需要它？** 如果每个接口返回的 JSON 格式都不一样，前端就得为每个接口写一套解析代码。
所以约定：**所有接口都返回 `{code, msg, data}` 三件套**。

```json
{ "code": 200, "msg": "操作成功", "data": { "total": 48, "free": 40, "occupied": 6, "out": 2 } }
```

```java
public class Result<T> {          // <T> 是「泛型」：data 可以是任何类型
    private Integer code;         // 业务状态码
    private String  msg;          // 提示信息
    private T       data;         // 真正的业务数据

    public static <T> Result<T> success()                    { ... }  // 成功，无数据
    public static <T> Result<T> success(T data)               { ... }  // 成功，带数据
    public static <T> Result<T> success(String msg, T data)   { ... }  // 成功，带 msg
    public static <T> Result<T> error(String msg)             { ... }  // 失败（500）
    public static <T> Result<T> error(Integer code, String msg){ ... }  // 失败（自定码）
}
```

**小白解释**：
- 静态方法 `Result.success(...)` 相当于「快捷构造器」，让 Controller 里只写一行 `return Result.success(数据);`。
- 泛型 `<T>` 让同一个类能装不同数据：`Result<BedStatisticsVO>`、`Result<List<RoomBedsVO>>`、`Result<Void>`。
- `Result<Void>` 表示「成功，但没有 data」（比如修改、调换这种写操作）。

#### ② `ResultCode.java`（18 行）—— 状态码常量

```java
public interface ResultCode {
    Integer SUCCESS = 200;      // 业务成功
    Integer BAD_REQUEST = 400;  // 参数错误 / 数据不存在 / 状态冲突
    Integer ERROR = 500;        // 系统异常
}
```

> 用 `interface` 存常量是 Java 的传统写法（interface 里的字段默认 `public static final`），
> 好处是 `ResultCode.SUCCESS` 比直接写 `200` 更不容易写错、更好读。

#### ③ `BusinessException.java`（18 行）—— 业务异常

```java
public class BusinessException extends RuntimeException {   // 继承运行时异常
    public BusinessException(String message) { super(message); }
    public BusinessException(String message, Throwable cause) { super(message, cause); }
}
```

**小白解释**：当业务规则不满足时（例如「只能调换到空闲的床位」），
Service 里直接 `throw new BusinessException("...")`，
**不需要**在每个方法里写 try-catch —— 统一由下一个文件接住。

#### ④ `GlobalExceptionHandler.java`（49 行）—— 全局异常处理器 ⭐

```java
@Slf4j                    // 自动生成 log 对象
@RestControllerAdvice     // 「全项目的异常都到我这里来」
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)          // ① 业务异常 → HTTP 400
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ResponseEntity.badRequest().body(Result.error(ResultCode.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)  // ② @NotNull 校验失败 → HTTP 400
    public ResponseEntity<Result<Void>> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst().map(fieldError -> fieldError.getDefaultMessage())
                .orElse("参数校验失败");
        return ResponseEntity.badRequest().body(Result.error(ResultCode.BAD_REQUEST, msg));
    }

    @ExceptionHandler(Exception.class)                   // ③ 兜底：其它异常 → HTTP 500
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常", e);   // 完整堆栈只打到日志里，不返回给前端（安全）
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("系统异常，请稍后重试"));
    }
}
```

**这段代码是整个项目「健壮性」的体现，展示时值得单独讲 1 分钟**：

| 设计点 | 说明 |
| :--- | :--- |
| 用 HTTP 状态码区分成败 | 成功 200、业务失败 **400**、系统异常 **500**。这样前端 `try/catch` 就能分流 |
| 弹窗文案直接可用 | 失败时把 `msg` 放在响应体里，前端 `error.response.data.msg` 直接弹 `ElMessage.error(...)` |
| 参数校验自动翻译 | DTO 上写 `@NotNull(message="客户ID不能为空")`，框架自动拦下，处理器把 message 取出来返回 |
| **不把堆栈给前端** | `e.getMessage()` 可能暴露表名/SQL；所以业务异常只回自己的 msg，系统异常统一回「系统异常，请稍后重试」，细节进日志 |

#### ⑤ `BedStatus.java`（18 行）—— 床位状态常量

```java
public interface BedStatus {
    int FREE = 1;      // 空闲（示意图绿色）
    int OCCUPIED = 2;  // 有人（示意图蓝色）
    int OUT = 3;       // 外出（示意图橙色）
}
```

#### ⑥ `BedDetailsStatus.java`（25 行）—— 使用记录状态常量（含那个「反着的 0/1」）

```java
public interface BedDetailsStatus {
    int IN_USE  = 0;       // beddetails.is_deleted = 0 → 这条记录正在生效
    int HISTORY = 1;       // beddetails.is_deleted = 1 → 已结束（使用历史）
    int TAB_IN_USE  = 1;   // 前端页签：1 = 正在使用（注意！与 is_deleted 相反）
    int TAB_HISTORY = 0;   // 前端页签：0 = 使用历史
}
```

> 展示时可以这么说：「这两组常量看着多余，但正因为页面传 `1`、数据库存 `0`，
> 我把它写成了常量，**这样我以后再看代码就不用重新推一遍**，也不容易写反。」

### 4.3 配置层 `config\`（2 个文件）

#### ① `OpenApiConfig.java`（26 行）—— Swagger 接口文档

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bedManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("东软颐养中心管理系统 —— 床位管理模块接口文档")
                .description("包含「床位示意图」与「床位管理」两大功能的接口，"
                        + "统一返回体为 {code, msg, data}，code=200 表示业务成功。")
                .version("1.0.0")
                .contact(new Contact().name("床位管理模块")));
    }
}
```

**用途**：浏览器打开 `http://localhost:8080/api/swagger-ui.html`，
五个接口会自动列出来，**可以直接在网页上点「Try it out」发请求** —— 这是演示时最方便的「不用写代码就能测接口」的工具。

> `@Bean` 的含义：这个方法返回的对象交给 Spring 容器管理（单例），
> 需要的地方「要一个 OpenAPI 对象」时，Spring 就把这个给你。

#### ② `WebMvcConfig.java`（29 行）—— 跨域配置

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);   // 预检请求结果缓存 1 小时
    }
}
```

**什么是跨域？** 浏览器的安全规则：`http://localhost:5173`（前端）向
`http://localhost:8080`（后端）发请求，**端口不同就算「跨域」**，默认会被浏览器拦下。

| 场景 | 会不会跨域 | 本项目的处理 |
| :--- | :--- | :--- |
| 前端开发（Vite 代理 `/api`） | **不跨域**（浏览器以为在请求 5173 自己） | 靠 `vite.config.js` 的代理 |
| 直接把前端打包后放到 8080 的静态目录 | 不跨域 | 同源 |
| 用 Postman / 浏览器直接访问 8080 | 不跨域（不是浏览器发起） | —— |
| 前端放到别的端口、后端不代理 | **跨域** | 靠这份 `WebMvcConfig` |

> 文件注释里还留了一句重要说明：**本模块不做登录鉴权**，JWT 由「用户管理」模块统一提供；
> 将来要加拦截器，就在这里注册 `HandlerInterceptor`，拦截除 `/auth/**` 外的请求。
> 这句话是**给自己和同学看的「接口预留」，体现模块化协作意识**。

### 4.4 实体层 `entity\`（4 个文件，数据库表的 Java 镜像）

4 个实体和 4 张表一一对应，**属性名用驼峰，数据库用下划线**，靠配置自动转换（见 4.10 节）：

| Java 属性 | 数据库字段 | 怎么转换的 |
| :--- | :--- | :--- |
| `bedStatus` | `bed_status` | `application.yml` 里 `map-underscore-to-camel-case: true` |
| `roomNo` | `room_no` | 同上 |
| `startDate` | `start_date` | 同上 |

#### ① `Room.java`（29 行）

```java
@Data                      // Lombok：自动生成 getter/setter/toString/equals/hashCode
@TableName("room")         // 告诉 MyBatis-Plus：这个类对应 room 表
public class Room {
    @TableId(type = IdType.AUTO)   // 主键，自增（交给数据库生成）
    private Integer id;
    private String  roomFloor;     // 楼层：1 一层、2 二层
    private Integer roomNo;        // 房间编号：101、201 …
}
```

#### ② `Bed.java`（35 行）

```java
@Data @TableName("bed")
public class Bed {
    @TableId(type = IdType.AUTO) private Integer id;
    private Integer roomNo;      // 房间编号（注意：不是 room_id！）
    private Integer bedStatus;   // 1 空闲 2 有人 3 外出
    private String  remarks;     // 备注
    private String  bedNo;       // 床位编号：1/2/3/4
}
```

#### ③ `BedDetails.java`（46 行）

```java
@Data @TableName("beddetails")
public class BedDetails {
    @TableId(type = IdType.AUTO) private Integer id;
    private LocalDate startDate;    // 入住时间
    private LocalDate endDate;      // 退住时间（还在住就是 null）
    private String  bedDetails;     // "606栋-1层-101房间-2号床位"
    private Integer customerId;     // → customer.id
    private Integer bedId;          // → bed.id
    private Integer isDeleted;      // 0 正在使用 / 1 已失效
}
```

> 🔴 **类注释里专门写了一句**：「这里的 `is_deleted` 不是 MyBatis-Plus 的逻辑删除标记，
> 而是业务状态，因此**没有**使用 `@TableLogic`」。
> 为什么强调？因为如果加了 `@TableLogic`，框架会自动在**每次查询后面加 `is_deleted = 0`**，
> 那么「使用历史」（`is_deleted = 1`）就永远查不出来了 —— **这就是「用错注解会静默出错」的典型**。

#### ④ `Customer.java`（89 行，字段最多）

床位模块只用到 5 个字段，但**把设计文档里的字段全写进来了**：

| 本模块用到的字段 | 用在哪 |
| :--- | :--- |
| `customerName` | 列表显示姓名、按姓名模糊查询 |
| `customerSex` | 列表显示性别（0 男 1 女） |
| `roomNo` | 调换后同步新房间号（varchar 要和 int 互转） |
| `bedId` | 调换后同步新床位 id |
| `buildingNo` | 调换后同步楼号（本项目固定 606） |

其余字段（`customerAge`、`idcard`、`checkinDate`、`expirationDate`、`contactTel`、
`psychosomaticState`、`attention`、`birthday`、`height`、`weight`、`bloodType`、
`filepath`、`userId`、`levelId`、`familyMember`）**本模块不参与业务**，
写进来是为了**让「客户管理 / 入住登记」模块的同学可以直接复用这个类**。

> 展示时这句能加分：「我多写的字段不是没用，是**给其它模块预留的接口**，
> 团队协作时省得他再去建一个一模一样的实体。」

### 4.5 入参层 `dto\`（3 个文件，前端说进来的话）

> **DTO = Data Transfer Object（数据传输对象）**：专门用来接住前端传来的参数。
> 为什么不直接用实体 `Bed` 接参数？因为前端传的字段和表结构**往往对不上**
> （比如调换要传「楼层 + 房间号 + 床位 id」三个字段，横跨两张表）。

#### ① `BedDetailsQueryDTO.java`（23 行）—— 列表查询条件

```java
@Data
public class BedDetailsQueryDTO {
    private Integer status;        // 1 正在使用 / 0 使用历史（前端页签）
    private String  customerName;  // 姓名，模糊查询，如 "张"
    private String  startDate;     // 入住日期，如 "2025-01-01"（查该日期及之后入住）
}
```

> 这三个字段都是**可选**的（没有 `@NotNull`），因为是「多条件组合查询」。
> GET 请求的参数会自动按**同名规则**绑到这个对象上：
> `?status=1&customerName=张&startDate=2025-01-01` → 自动填入这三个属性（**不需要写 `@RequestParam`**）。

#### ② `UpdateEndTimeDTO.java`（22 行）—— 修改结束时间

```java
@Data
public class UpdateEndTimeDTO {
    @NotNull(message = "床位使用详情ID不能为空")
    private Integer id;       // beddetails.id
    private String  endDate;  // yyyy-MM-dd
}
```

#### ③ `BedSwapDTO.java`（32 行）—— 床位调换 ⭐

```java
@Data
public class BedSwapDTO {
    @NotNull(message = "客户ID不能为空")
    private Integer customerId;  // 谁要换

    private String  newFloor;    // 新楼层：1 / 2
    private Integer newRoomNo;   // 新房间号：101 …

    @NotNull(message = "请选择要调换的空闲床位")
    private Integer newBedId;    // 新床位 id（必填）

    private String  swapDate;    // yyyy-MM-dd，后端会校验「必须是今天」
}
```

**为什么 `newBedId` 必填而 `newFloor/newRoomNo` 不是？**
因为**床位 id 才是数据库里唯一确定的东西**，楼层/房间号只是用来做**交叉校验**的
（防止有人绕过前端、直接调接口把 A 房间的人塞到 B 房间的床位 id 上）。

> 小白重点：`@NotNull` 只在 **Controller 方法参数前加了 `@Valid`** 时才生效（见 `BedController` L66、L73）。
> 少了 `@Valid`，注解等于写着好看、**不生效** —— 这是新手最常犯的错之一。

### 4.6 出参层 `vo\`（4 个文件，后端给前端看的数据）

> **VO = View Object（视图对象）**：专门用来返回给前端。
> 为什么不直接返回实体？因为前端要的字段常常**不是一张表能凑出来的**。

| VO | 行数 | 给谁用 | 字段 |
| :--- | :--- | :--- | :--- |
| `BedStatisticsVO` | 26 | 示意图顶部 4 个标签 | `total` / `free` / `occupied` / `out` |
| `RoomBedsVO` | 23 | 示意图的一个房间卡片 | `roomNo` + `List<BedVO> beds` |
| `BedVO` | 28 | 示意图里的一个小方块 | `id` / `bedNo` / `bedStatus` |
| `BedDetailsVO` | 38 | 床位管理表格的一行 | `id` / `customerId` / `customerName` / `customerSex` / `bedDetails` / `startDate` / `endDate` |

**两个值得讲的细节**：

1. `RoomBedsVO` 里的 `beds` 字段**初始化成了空集合**：
   ```java
   private List<BedVO> beds = new ArrayList<>();
   ```
   这样即使某个房间没有床位，返回的也是 `"beds": []` 而不是 `null` —— **前端 `v-for` 不用做判空**。

2. `BedVO` 上同时用了三个 Lombok 注解：
   ```java
   @Data @NoArgsConstructor @AllArgsConstructor
   public class BedVO { ... }
   ```
   因为 `BedServiceImpl` L77 用的是 `new BedVO(id, bedNo, bedStatus)` 这种**全参构造**写法，
   一行就能创建一个对象，所以必须显式加 `@AllArgsConstructor`
   （`@Data` 默认只给无参构造）。

3. `BedDetailsVO` 的注释写得很直白：**「字段名必须与前端 `BedManage.vue` 的 `el-table-column` 保持一致」**：
   ```java
   @Schema(description = "床位使用详情 ID（beddetails.id），修改结束时间时使用")
   private Integer id;
   ```
   因为这个 `id` 前端要拿去调「修改结束时间」和「床位调换」两个接口，
   **字段名一改，前端就白屏/报错**（详见第 10 节字段对照表）。

### 4.7 数据访问层 `mapper\`（4 个接口 + 2 个 XML）

> **Mapper 就是「和数据库对话的翻译官」**：Java 方法 → SQL。
> 本项目的策略是：**简单的用 MyBatis-Plus 自动生成，复杂的（统计/联表）手写 XML**。

#### ① `RoomMapper.java` / `② CustomerMapper.java`（各 12 行）—— 极简

```java
@Mapper
public interface RoomMapper extends BaseMapper<Room> { }
```

**就这两行，为什么就能查数据库了？** 因为 `BaseMapper<T>` 已经自带了一整套方法：

| 继承来的方法 | 对应 SQL | 本项目用在哪 |
| :--- | :--- | :--- |
| `selectById(id)` | `SELECT * FROM room WHERE id = ?` | 调换时查新床位、查客户 |
| `selectList(wrapper)` | `SELECT * FROM room WHERE ...` | 示意图查某楼层所有房间 |
| `selectOne(wrapper)` | `SELECT * FROM room WHERE ... LIMIT 1` | 调换时用 `room_no` 反查房间 |
| `insert(entity)` | `INSERT INTO ...` | 调换时新增一条使用记录 |
| `updateById(entity)` | `UPDATE ... WHERE id = ?` | 改结束时间、改床位状态 |
| `deleteById(id)` | `DELETE FROM ... WHERE id = ?` | 本项目不用（用状态代替删除） |

#### ③ `BedMapper.java`（23 行）—— 多了一个统计方法

```java
@Mapper
public interface BedMapper extends BaseMapper<Bed> {
    /** 统计总床位 / 空闲 / 有人 / 外出 数量 */
    BedStatisticsVO selectStatistics();
}
```

> ⚠️ 注意：**这个方法只有声明、没有实现**。实现写在
> `resources\mapper\BedMapper.xml` 里，靠「`namespace` = 接口全类名 + `id` = 方法名」对应起来。

#### ④ `BedDetailsMapper.java`（31 行）—— 多条件联查

```java
@Mapper
public interface BedDetailsMapper extends BaseMapper<BedDetails> {
    List<BedDetailsVO> selectDetailsList(@Param("isDeleted") Integer isDeleted,
                                         @Param("customerName") String customerName,
                                         @Param("startDate") String startDate);
}
```

**`@Param("isDeleted")` 是什么？** 把参数起个名字，让 XML 里能用 `#{isDeleted}` 引用它。
**不给名字的话**，MyBatis 只能用 `#{param1}`、`#{arg0}` 这种「猜名字」，代码会很难读。

#### ⑤ `resources\mapper\BedMapper.xml`（26 行）—— 统计 SQL（一次查完 4 个数）⭐

```xml
<!-- 统计结果列的别名刻意用 xxx_cnt，避免 free / out 在部分数据库里是保留字
     （H2 的 OUT 就是关键字），再用 resultMap 映射到 VO 的属性名。 -->
<resultMap id="statisticsMap" type="com.neusoft.yiyang.vo.BedStatisticsVO">
    <result column="total_cnt"    property="total"/>
    <result column="free_cnt"     property="free"/>
    <result column="occupied_cnt" property="occupied"/>
    <result column="out_cnt"      property="out"/>
</resultMap>

<select id="selectStatistics" resultMap="statisticsMap">
    SELECT COUNT(*)                                                     AS total_cnt,
           COALESCE(SUM(CASE WHEN bed_status = 1 THEN 1 ELSE 0 END), 0) AS free_cnt,
           COALESCE(SUM(CASE WHEN bed_status = 2 THEN 1 ELSE 0 END), 0) AS occupied_cnt,
           COALESCE(SUM(CASE WHEN bed_status = 3 THEN 1 ELSE 0 END), 0) AS out_cnt
    FROM bed
</select>
```

**这段 SQL 有 3 个「值得说的点」**：

| 点 | 说明 |
| :--- | :--- |
| `SUM(CASE WHEN ... THEN 1 ELSE 0 END)` | 「条件计数」的经典写法：把满足条件记 1、不满足记 0，再求和 |
| `COALESCE(..., 0)` | 表里一张床位都没有时 `SUM` 会返回 `NULL`，用 COALESCE 兜底成 0，**前端就不会显示 `undefined`** |
| 别名用 `_cnt` + `resultMap` | `free`、`out` 在 H2 里是**关键字**，直接用会报语法错；所以列名避开关键字，再用 `resultMap` 映射回好看的属性名 |

> 为什么不查 4 次数据库（4 条 `COUNT`）？因为**一次聚合查询**就能拿到 4 个数，
> 少 3 次网络往返 —— 这就是「有意识做性能优化」的小例子。

#### ⑥ `resources\mapper\BedDetailsMapper.xml`（30 行）—— 列表联查 + 动态条件 ⭐

```xml
<select id="selectDetailsList" resultType="com.neusoft.yiyang.vo.BedDetailsVO">
    SELECT bd.id          AS id,
           bd.customer_id AS customerId,
           c.customer_name AS customerName,
           c.customer_sex  AS customerSex,
           bd.bed_details  AS bedDetails,
           bd.start_date   AS startDate,
           bd.end_date     AS endDate
    FROM beddetails bd
             LEFT JOIN customer c ON c.id = bd.customer_id
    WHERE bd.is_deleted = #{isDeleted}
    <if test="customerName != null and customerName != ''">
        AND c.customer_name LIKE CONCAT('%', #{customerName}, '%')
    </if>
    <if test="startDate != null and startDate != ''">
        AND bd.start_date &gt;= #{startDate}
    </if>
    ORDER BY bd.start_date DESC, bd.id DESC
</select>
```

| 技术点 | 小白解释 |
| :--- | :--- |
| `LEFT JOIN customer` | 用**左连接**把客户表接上，这样才能拿到「姓名、性别」；用 LEFT 而不是 INNER，是**即使客户被删了、记录也照常显示** |
| `<if test="...">` | **动态 SQL**：条件传了才拼进 SQL，没传就自动省略 —— 这就是「多条件组合查询」的实现方式，不用写 4 个方法 |
| `LIKE CONCAT('%', #{x}, '%')` | 模糊查询，拼成 `%张%`；用 `CONCAT` 而不是 `'%'+#{x}+'%'`，**避免了 SQL 注入风险** |
| `&gt;=` | XML 里不能直接写 `>`，要写成实体 `&gt;` |
| `#{isDeleted}` vs `${isDeleted}` | `#{}` 是**预编译占位符 `?`（安全）**；`${}` 是**字符串拼接（危险，会 SQL 注入）**。本项目的值一律用 `#{}` |
| `ORDER BY start_date DESC, id DESC` | 双字段排序：日期相同的再按 id 倒序，保证**顺序稳定不跳动** |

### 4.8 业务接口 `service\`（2 个文件）—— 为什么还要「接口」？

```java
public interface BedService {                 // 床位示意图（需求 1）
    BedStatisticsVO statistics();             // 床位统计
    List<RoomBedsVO> listRoomBeds(String floor);  // 按楼层查房间 + 床位
}

public interface BedManageService {           // 床位管理（需求 2）
    List<BedDetailsVO> listDetails(BedDetailsQueryDTO query);  // 多条件查询
    void updateEndTime(UpdateEndTimeDTO dto);                  // 改结束时间
    void swapBed(BedSwapDTO dto);                              // 床位调换
}
```

**小白疑问：Controller 直接调用实现类不行吗？为什么中间要隔一个接口？**

| 理由 | 说明（答辩可用） |
| :--- | :--- |
| ① 面向接口编程 | Controller 只依赖「能干什么」，不依赖「怎么干」，将来换实现（比如改成查缓存）不用动 Controller |
| ② 按需拆分 | 我把「看图」和「管事」拆成两个接口，**对应需求的 2 个功能点**，职责清晰、不会越写越乱 |
| ③ 便于测试/协作 | 别人可以写一个假的 `BedService` 实现来做单元测试；团队里也能各写各的实现 |

> 这两个接口的方法上面都写了**中文注释**（比如「旧床位记录失效、旧床位变空闲、新床位变有人、新增一条使用记录」），
> 这正是展示时最好用的「功能说明」，**不用看实现就能讲清业务流程**。

### 4.9 示意图实现 `BedServiceImpl.java`（84 行）

```java
@Slf4j                     // 自动获得 log 对象
@Service                   // 交给 Spring 管理，名字叫 bedServiceImpl
@RequiredArgsConstructor   // Lombok：给 final 字段生成「构造器注入」
public class BedServiceImpl implements BedService {

    private final BedMapper  bedMapper;      // 注意是 final
    private final RoomMapper roomMapper;
```

**`@RequiredArgsConstructor` 是干什么的？** 它生成这样的构造器：
```java
public BedServiceImpl(BedMapper bedMapper, RoomMapper roomMapper) { ... }
```
Spring 看到构造器需要两个参数，就自动把容器里的 `BedMapper`、`RoomMapper` 塞进来。
**这样写有三个好处**：①不用写 `@Autowired`；②字段是 `final`（不可被改，更安全）；
③**依赖通过构造器暴露出来，一眼能看出这个类依赖谁** —— 比满屏 `@Autowired` 清爽得多。

#### 方法 1：`statistics()`（L34–L46）

```java
BedStatisticsVO statistics = bedMapper.selectStatistics();
// 数据库里一张床位都没有时 SUM 会返回 null，这里兜底成 0，避免前端显示 undefined
if (statistics == null) {
    statistics = new BedStatisticsVO();
    statistics.setTotal(0);  statistics.setFree(0);
    statistics.setOccupied(0); statistics.setOut(0);
}
return statistics;
```

> **这就是「防御性编程」**：即使数据库是空的，也不会给前端返回 `null`，
> 前端 4 个标签永远显示数字（0），不会出现 `undefined`。

#### 方法 2：`listRoomBeds(String floor)`（L48–L83）—— 三步走 ⭐

```java
if (!StringUtils.hasText(floor)) { return Collections.emptyList(); }  // 空参数直接返回空

// 第 1 步：按楼层查出所有房间（room 表）
List<Room> rooms = roomMapper.selectList(new LambdaQueryWrapper<Room>()
        .eq(Room::getRoomFloor, floor.trim())
        .orderByAsc(Room::getRoomNo));

// 第 2 步：一次查出这些房间下的所有床位（bed 表），避免 N+1 查询
List<Integer> roomNos = rooms.stream().map(Room::getRoomNo).collect(Collectors.toList());
List<Bed> beds = bedMapper.selectList(new LambdaQueryWrapper<Bed>()
        .in(Bed::getRoomNo, roomNos)
        .orderByAsc(Bed::getRoomNo).orderByAsc(Bed::getId));

// 第 3 步：按房间号分组，组装成前端需要的 [{roomNo, beds:[{id, bedNo, bedStatus}]}]
Map<Integer, List<Bed>> bedGroup = beds.stream().collect(Collectors.groupingBy(Bed::getRoomNo));
```

**这段代码是「性能意识」的展示重点，务必会讲**：

| 概念 | 小白解释 |
| :--- | :--- |
| `LambdaQueryWrapper` | MyBatis-Plus 的「条件构造器」。`new LambdaQueryWrapper<Room>().eq(Room::getRoomFloor, floor)` 最终生成 `WHERE room_floor = ?`。**用方法引用 `Room::getRoomFloor` 而不是字符串 `"room_floor"`，改字段名时编译器会报错**，不会出现「字段改名后 SQL 静默失效」 |
| **N+1 查询问题** | 笨办法是「查出 6 个房间，再循环 6 次查每个房间的床位」= 1 + 6 = 7 次数据库往返。我改成第 2 步**一次 `IN` 查询**把 6 个房间的床位全查出来，总共只 **2 次查询** |
| `Collectors.groupingBy` | 把床位列表按 `roomNo` 分组，得到 `Map<Integer, List<Bed>>`，即 `{101: [床1,床2,床3,床4], 102: [...]}` |
| `getOrDefault(..., Collections.emptyList())` | 某房间没有床位时给个空列表，**避免 `null` 导致后续代码报空指针** |
| `new BedVO(bed.getId(), bed.getBedNo(), bed.getBedStatus())` | 只把前端需要的 3 个字段挑出来，**不要把整张表的数据都扔给前端** |

返回的数据长这样（前端直接 `v-for` 两层循环就能画出来）：
```json
{ "code": 200, "msg": "操作成功", "data": [
  { "roomNo": 101, "beds": [ {"id":1,"bedNo":"1","bedStatus":1}, {"id":2,"bedNo":"2","bedStatus":2} ] },
  { "roomNo": 102, "beds": [ ... ] }
]}
```

### 4.10 核心业务实现 `BedManageServiceImpl.java`（219 行，全项目最该讲的文件）⭐⭐⭐⭐

**开头的四个依赖 + 一个配置注入**：

```java
@Slf4j @Service @RequiredArgsConstructor
public class BedManageServiceImpl implements BedManageService {

    private final BedDetailsMapper bedDetailsMapper;   // 使用记录表
    private final BedMapper        bedMapper;          // 床位表
    private final RoomMapper       roomMapper;         // 房间表
    private final CustomerMapper   customerMapper;     // 客户表  ← 4 张表全都要动

    /** 所属楼房编号，配置在 application.yml 的 yiyang.building-no */
    @Value("${yiyang.building-no:606}")
    private String buildingNo;
}
```

| 代码 | 小白解释 |
| :--- | :--- |
| 4 个 `private final XxxMapper` | **这个类要动 4 张表** —— 从依赖就能看出「调换床位」是跨表业务 |
| `@Value("${yiyang.building-no:606}")` | 从配置文件读「楼房编号」，**冒号后面的 `606` 是默认值**（配置漏了也不会崩）。写死 `606` 也能跑，但哪天换成 607 栋就要改代码、重新打包 —— 放进配置就只改一行 yml |

#### 方法 1：`listDetails(BedDetailsQueryDTO query)`（L53–L71）—— 多条件查询

```java
BedDetailsQueryDTO condition = query == null ? new BedDetailsQueryDTO() : query;

// 前端「床位管理」页签：1 = 正在使用、0 = 使用历史（注意与 is_deleted 的 0/1 正好相反）。
// 不传 status 或非法值时，默认查「正在使用」。
int tabStatus = condition.getStatus() == null ? BedDetailsStatus.TAB_IN_USE : condition.getStatus();
int isDeleted = (tabStatus == BedDetailsStatus.TAB_HISTORY)
        ? BedDetailsStatus.HISTORY     // 0 -> 1
        : BedDetailsStatus.IN_USE;     // 其它任何值 -> 0（默认「正在使用」）

String customerName = StringUtils.hasText(condition.getCustomerName())
        ? condition.getCustomerName().trim() : null;
// 前端日期控件可能传 ISO 字符串，统一截取成 yyyy-MM-dd
String startDate = DateUtils.normalize(condition.getStartDate());

return bedDetailsMapper.selectDetailsList(isDeleted, customerName, startDate);
```

**这段 15 行代码里有 4 个「防御性」细节，非常适合展示**：

| 细节 | 为什么这么写 |
| :--- | :--- |
| `query == null ? new BedDetailsQueryDTO() : query` | 调用方如果传 `null` 也不会**空指针崩溃** |
| `status == null` 时默认「正在使用」 | 前端第一次进入页面不传 `status`，也要能显示数据 |
| `status = 9` 这种非法值也走默认分支 | 自测脚本第 121 行专门测了这个：`?status=9` 依然返回张伟（不报错、不空） |
| `trim()` + `normalize()` | 去掉姓名两侧空格（`" 张 "` → `"张"`）；日期统一成 `yyyy-MM-dd`，**解决「时区导致差一天」**（详见 4.12 节） |

#### 方法 2：`updateEndTime(UpdateEndTimeDTO dto)`（L76–L100）—— 改结束时间

```java
@Transactional(rollbackFor = Exception.class)
public void updateEndTime(UpdateEndTimeDTO dto) {
    if (dto == null || dto.getId() == null) {
        throw new BusinessException("床位使用详情ID不能为空");          // ① 参数兜底
    }

    BedDetails record = bedDetailsMapper.selectById(dto.getId());
    if (record == null) {
        throw new BusinessException("床位使用详情不存在");               // ② 记录必须存在
    }

    LocalDate endDate = DateUtils.parseRequired(dto.getEndDate(), "结束时间");
    if (record.getStartDate() != null && endDate.isBefore(record.getStartDate())) {
        throw new BusinessException("结束时间不能早于入住时间：" + record.getStartDate());  // ③ 业务规则
    }

    // 只更新结束时间，其它字段一律不动（需求要求「只能修改结束时间」）
    BedDetails update = new BedDetails();
    update.setId(record.getId());
    update.setEndDate(endDate);
    bedDetailsMapper.updateById(update);                              // ④ 精确更新

    log.info("修改床位使用结束时间：beddetailsId={}, endDate={}", record.getId(), endDate);
}
```

**④「只更新结束时间」这一点是全项目最容易讲出彩的地方**：

- **笨写法**：`updateById(record)` —— 把从数据库查出来的整个对象扔回去更新，
  会**把所有字段都写一遍**（包括 `bed_details`、`bed_id`、`customer_id`）。
  万一在查出来之后有别的功能改了这条记录，**你的更新会把它覆盖回去（丢更新）**。
- **我的写法**：**新建一个空对象，只 set 要改的字段**，MyBatis-Plus 生成的 SQL 就是
  ```sql
  UPDATE beddetails SET end_date = ? WHERE id = ?
  ```
  **只动一列**，天然避免覆盖别人的修改，也符合「需求只允许改结束时间」的约束 —— **需求约束在代码层面被强制了**。

#### 方法 3：`swapBed(BedSwapDTO dto)`（L105–L203）—— 床位调换（**压轴**）⭐⭐⭐⭐

**一句话流程**：把老人从旧床换到新床，同时**让 4 张表的数据全部保持一致**，任何一步失败就全部撤销。

```java
@Override
@Transactional(rollbackFor = Exception.class)      // ← 事务：要么全成功，要么全失败
public void swapBed(BedSwapDTO dto) { ... }
```

**10 个步骤逐条对照**（括号里是行号）：

| 步骤 | 行号 | 做什么 | 失败会怎样 |
| :--- | :--- | :--- | :--- |
| 3.1 | L116–L120 | 校验 `swapDate` 必须是**今天**（需求：只能当天调换当天办理） | 400「床位调换只能当天调换当天办理」 |
| 3.2 | L123–L126 | 客户必须存在（`customerMapper.selectById`） | 400「客户不存在，客户ID：x」 |
| 3.3 | L129–L135 | 新床位必须存在 **且状态 = 1 空闲** | 400「目标床位不存在」/「只能调换到空闲的床位」 |
| 3.4 | L138–L149 | 用 `room_no` 反查房间，交叉校验**房间号**和**楼层**是否和前端选的一致 | 400「所选床位不属于所选房间 / 所选楼层」 |
| 3.5 | L152–L162 | 查该客户**当前正在使用**的记录（`is_deleted=0`，取最新一条）；再判断「新床 == 旧床」 | 400「该客户当前没有正在使用的床位」/「新床位与当前床位相同，无需调换」 |
| 3.6 | L164–L168 | **旧记录失效**：`is_deleted 0→1`，`end_date` 改成今天 | —— |
| 3.7 | L171–L176 | **旧床位释放**：旧床 `bed_status 2→1`（有人 → 空闲） | —— |
| 3.8 | L179–L182 | **新床位占用**：新床 `bed_status 1→2`（空闲 → 有人） | —— |
| 3.9 | L185–L191 | **新增使用记录**：`start_date = 今天`、`end_date = null`、`is_deleted = 0` | —— |
| 3.10 | L194–L199 | **同步客户表**：新 `room_no`、新 `bed_id`、`building_no` | —— |

**一共 5 次写数据库操作**（步骤 3.6 ~ 3.10），**跨 4 张表**：

```
  3.6  UPDATE beddetails SET is_deleted=1, end_date=今天 WHERE id=旧记录   ┐
  3.7  UPDATE bed      SET bed_status=1            WHERE id=旧床位        │
  3.8  UPDATE bed      SET bed_status=2            WHERE id=新床位        ├─ 一个事务
  3.9  INSERT INTO beddetails (×5 个字段)                                  │
  3.10 UPDATE customer SET room_no=?, bed_id=?, building_no=? WHERE id=?  ┘
```

#### 为什么必须加 `@Transactional(rollbackFor = Exception.class)`？

设想：3.6~3.8 都成功了（旧记录失效、旧床变空闲、新床变有人），
到 3.9 突然数据库报错（比如磁盘满 / 字段超长）。

**没有事务**的后果：旧床已经变空闲、新床已经变有人，但**谁也没有入住记录**
→ 这张「有人」的床永远释放不出来，数据彻底乱了，只能人工去数据库改。

**有事务**的后果：Spring 把 5 个操作包在一个数据库事务里，
**任何一步抛异常，前面 4 步全部回滚**，数据回到调换前的状态，用户看到一条错误提示即可。

> 🔴 **`rollbackFor = Exception.class` 不能省**（这是必答题）：
> Spring 默认**只对 `RuntimeException` 和 `Error` 回滚**。
> 如果将来某一步抛的是受检异常（比如 `IOException`），**默认不回滚**，那事务就白加了。
> 写上 `rollbackFor = Exception.class` 表示「**任何异常都回滚**」，一劳永逸。
> 本项目抛的 `BusinessException` 继承自 `RuntimeException`（默认就会回滚），
> 但显式写出来**更保险、更能表明我懂这个坑**。

#### 三个「反向操作」和「日期」的坑（自测脚本帮我把它们钉住了）

| 坑 | 具体表现 | 我的处理 |
| :--- | :--- | :--- |
| **床位状态双写** | 调换要改**两张床**的状态，而且方向**相反**：旧床 `2→1`、新床 `1→2`。**少写一个就会出现「两张床都显示有人」或「都没人」** | 3.7 和 3.8 分开写、各写一个 `updateById`，清楚到不用注释也看得懂 |
| **is_deleted 0/1 反向** | 旧记录要置 **1**（历史），新记录要置 **0**（生效） | 用常量 `BedDetailsStatus.HISTORY` / `IN_USE`，**绝不写字面量 0/1** |
| **楼层/房间交叉校验** | 前端下拉是「楼层 → 房间 → 空闲床位」三级联动，但接口可以被直接调用，可能传「101 房间 + 201 房间的床位 id」 | 3.4 **用新床位反查出它真正的房间**，再和前端传的 `newRoomNo`/`newFloor` 比对，不一致就报错 |

> 💡 **答辩金句**：「前端的三级联动**只是体验**，**真正的约束必须在后端**。
> 所以我用『新床位 id』反查它真正属于哪个房间、哪个楼层，跟前端传的值比对，
> 这样即使有人绕过页面直接调接口，数据也不会错。」

#### 私有方法 `buildBedDetails(roomNo, bedNo)`（L212–L218）—— 拼描述文本

```java
private String buildBedDetails(Integer roomNo, String bedNo) {
    Room room = roomMapper.selectOne(new LambdaQueryWrapper<Room>()
            .eq(Room::getRoomNo, roomNo)
            .last("LIMIT 1"));
    String floor = room == null ? "" : room.getRoomFloor();
    return String.format("%s栋-%s层-%s房间-%s号床位", buildingNo, floor, roomNo, bedNo);
}
```

- 生成 `606栋-1层-101房间-2号床位` 这种**人能看懂的描述**，直接存进 `beddetails.bed_details`。
- `.last("LIMIT 1")` 是 MyBatis-Plus 的「在 SQL 末尾追加」方法，
  因为 `selectOne` 要求「最多一条」，加 `LIMIT 1` 更稳（房间号本来也唯一）。
- `room == null ? "" : room.getRoomFloor()`：**查不到房间也不崩**，楼层留空。

### 4.11 接口层 `BedController.java`（77 行）—— 5 个接口的门

```java
@Tag(name = "床位管理", description = "床位示意图 + 床位管理")   // Swagger 分组名
@RestController                    // = @Controller + @ResponseBody，返回对象自动转 JSON
@RequestMapping("/bed")            // 类级别的路径前缀
@RequiredArgsConstructor
public class BedController {

    private final BedService       bedService;        // 示意图业务
    private final BedManageService bedManageService;  // 床位管理业务

    @Operation(summary = "床位统计", description = "统计总床位以及空闲 / 有人 / 外出的床位数量")
    @GetMapping("/statistics")
    public Result<BedStatisticsVO> statistics() {
        return Result.success(bedService.statistics());
    }

    @Operation(summary = "床位示意图数据", description = "按楼层查询房间及其床位，床位调换弹窗的楼层/房间下拉也复用该接口")
    @GetMapping("/map")
    public Result<List<RoomBedsVO>> bedMap(@RequestParam(value = "floor", defaultValue = "1") String floor) {
        return Result.success(bedService.listRoomBeds(floor));
    }

    @Operation(summary = "床位使用详情列表", description = "按客户姓名（模糊）、入住日期、使用状态多条件组合查询")
    @GetMapping("/manage/details")
    public Result<List<BedDetailsVO>> details(BedDetailsQueryDTO query) {   // GET 参数自动绑定
        return Result.success(bedManageService.listDetails(query));
    }

    @Operation(summary = "修改床位使用结束时间", description = "只能修改结束时间")
    @PutMapping("/manage/updateEndTime")
    public Result<Void> updateEndTime(@Valid @RequestBody UpdateEndTimeDTO dto) {
        bedManageService.updateEndTime(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "床位调换", description = "只能当天调换当天办理，且只能调换到空闲床位")
    @PostMapping("/manage/swap")
    public Result<Void> swap(@Valid @RequestBody BedSwapDTO dto) {
        bedManageService.swapBed(dto);
        return Result.success("床位调换成功", null);
    }
}
```

**Controller 里最该讲的 4 个点**：

| 点 | 说明 |
| :--- | :--- |
| **Controller 只有 1~2 行** | 校验、业务、拼描述全在 Service。**Controller 不该有业务逻辑**，它只做「收参数 → 调 Service → 包 Result」 |
| `@Valid` | **决定 DTO 上的 `@NotNull` 生不生效**（写操作的两个接口都加了） |
| `@RequestParam(defaultValue = "1")` | `GET /bed/map` 不传 `floor` 时默认查一层，**接口更抗造** |
| 真实地址 = `context-path` + `@RequestMapping` + `@GetMapping` | `/api` + `/bed` + `/map` = **`http://localhost:8080/api/bed/map`** |

**5 个接口一览表（记住这张表就够答辩了）**：

| # | 方法 | 真实地址（前缀 http://localhost:8080） | 作用 | 成功返回 |
| :--- | :--- | :--- | :--- | :--- |
| 1 | GET | `/api/bed/statistics` | 床位统计 | `{total,free,occupied,out}` |
| 2 | GET | `/api/bed/map?floor=1` | 按楼层取房间+床位 | `[{roomNo, beds:[…]}]` |
| 3 | GET | `/api/bed/manage/details?status=1&customerName=张` | 多条件查列表 | `[{id,customerId,customerName,…}]` |
| 4 | PUT | `/api/bed/manage/updateEndTime` | 改结束时间 | `"修改成功"` |
| 5 | POST | `/api/bed/manage/swap` | 床位调换 | `"床位调换成功"` |

### 4.12 工具类 `DateUtils.java`（61 行）—— 解决「日期差一天」

```java
public static String normalize(String dateStr) {
    if (!StringUtils.hasText(dateStr)) return null;
    String trimmed = dateStr.trim();
    return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;   // 只取前 10 位
}

public static LocalDate parse(String dateStr) {
    String normalized = normalize(dateStr);
    if (normalized == null) return null;
    try {
        return LocalDate.parse(normalized, FORMATTER);   // FORMATTER = yyyy-MM-dd
    } catch (Exception e) {
        throw new BusinessException("日期格式不正确，正确格式为 yyyy-MM-dd：" + dateStr);
    }
}

public static LocalDate parseRequired(String dateStr, String fieldName) { ... }  // 解析 + 非空校验
```

**为什么需要这个类？（这是最有技术含量的「小文件」）**

Element Plus 的 `el-date-picker` 如果用默认设置，会把日期转成 **ISO 带时区字符串**：
`2025-10-10T16:00:00.000Z`。而 `Z` 表示 UTC，
**东八区（+8）把它解析成本地时间就是第二天 00:00** → 传「10 月 10 日」变成「10 月 11 日」，**差一天**。

三种解法里我用了两种（双保险）：

| 解法 | 说明 | 我的选择 |
| :--- | :--- | :--- |
| 前端 `value-format="YYYY-MM-DD"` | 让控件直接输出 `2025-10-10` | ✅ 用了（`BedManage.vue`） |
| 后端 `substring(0, 10)` 截断 | 不管传什么，只取前 10 位字符 | ✅ **也用了** |
| 直接 `LocalDate.parse()` | 遇到带 `T`/`Z` 的字符串直接抛异常 | ❌ |

> 💡 **展示金句**：「前端我配了 `value-format`，但**我不能假设调用方一定按我的前端来调**，
> 所以后端也做了兼容 —— **前端是为了体验，后端是为了正确**。」
> 自测脚本第 140–141 行专门验证了这条：传 `2025-01-10T16:00:00.000Z` 会被正确识别为 `2025-01-10`。

### 4.13 配置文件（`application.yml` / `application-h2.yml` / `pom.xml`）

#### `application.yml`（65 行）—— 主配置，逐块讲

```yaml
server:
  port: 8080
  servlet:
    context-path: /api        # ← 全项目统一前缀，必须与前端 axios.baseURL 一致

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/yiyang?...&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456          # ← 交作业改成你本机 MySQL 密码

  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    serialization:
      write-dates-as-timestamps: false   # ← 日期不要输出成 1728518400000 这种时间戳
    default-property-inclusion: always

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml           # 手写 SQL 的位置
  type-aliases-package: com.neusoft.yiyang.entity
  configuration:
    map-underscore-to-camel-case: true                    # bed_status → bedStatus
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 控制台打印 SQL（调试神器）
  global-config:
    banner: false
    db-config:
      id-type: auto                                       # 主键跟随数据库自增

springdoc:
  api-docs:   { path: /v3/api-docs }
  swagger-ui: { path: /swagger-ui.html, tags-sorter: alpha, operations-sorter: alpha }

logging:
  level:
    com.neusoft.yiyang: debug

yiyang:
  building-no: "606"          # 自定义业务配置：所属楼房编号
```

| 配置项 | 为什么要写 | 不写会怎样 |
| :--- | :--- | :--- |
| `context-path: /api` | 前端代理不重写路径，后端必须自己也带 `/api` | 全部接口 404 |
| `serverTimezone=Asia/Shanghai` | 不指定时区，MySQL 驱动可能按 UTC 解析 | 日期差 8 小时 |
| `allowPublicKeyRetrieval=true` | MySQL 8 默认 `caching_sha2_password` 认证，缺这个参数本地会报「Public Key Retrieval is not allowed」 | 连不上数据库 |
| `write-dates-as-timestamps: false` | 否则日期会输出成时间戳/数组 | 前端显示异常 |
| `map-underscore-to-camel-case: true` | 数据库 `bed_status` ↔ Java `bedStatus` 自动映射 | **所有字段都是 `null`**（超经典坑） |
| `log-impl: StdOutImpl` | 控制台能直接看到执行的 SQL 和参数 —— 演示时最好的「可视化」 | 只能靠猜 |

#### `application-h2.yml`（27 行）—— 免装 MySQL 的演示模式

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:yiyang;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: ""
  sql:
    init:
      mode: always
      schema-locations: classpath:db/h2-schema.sql   # 建表
      data-locations:   classpath:db/h2-data.sql     # 塞演示数据
      encoding: UTF-8
  h2:
    console:
      enabled: true
      path: /h2-console                              # 网页版数据库客户端
```

| 参数 | 含义 |
| :--- | :--- |
| `MODE=MySQL` | 用 MySQL 语法模式，**同一套 SQL 习惯两边都能跑** |
| `mem:yiyang` | **内存**数据库，重启就没了 → 每次启动都是干净的演示数据 |
| `DB_CLOSE_DELAY=-1` | 最后一个连接关闭后**不要销毁数据库**（否则一打开网页控制台就发现库没了） |
| `sql.init.mode: always` | 每次启动都执行建表 + 插数据脚本 |
| `/h2-console` | 网页版数据库客户端：`http://localhost:8080/api/h2-console`（**注意带上 `/api`**） |

> 展示时的杀手锏：**「同学电脑上不一定有 MySQL，所以我做了 H2 演示模式，
> 只要装了 JDK 就能跑，加一个参数 `--spring.profiles.active=h2` 就行。」**

#### `pom.xml`（153 行）—— 依赖清单，重点看 4 处

| 位置 | 内容 | 说明 |
| :--- | :--- | :--- |
| L7–L12 | `spring-boot-starter-parent 3.5.16` | 继承父 POM，**所有依赖版本由 Spring Boot 统一管理**，自己不用写版本号 |
| L20–L26 | `java.version 17` | 用 JDK 17；同时声明 `mybatis-plus 3.5.17`、`springdoc 2.9.1`、`lombok 1.18.42` |
| L88–L106 | `maven-compiler-plugin` 显式声明 `annotationProcessorPaths` | **踩坑后加的**：JDK 23 起 `javac` 不再自动扫描 classpath 上的注解处理器，只加 Lombok 依赖不配 processor path，**getter/setter/log 全都不会生成**，编译报一堆「找不到符号」。显式声明后 JDK 17~25 都能编译 |
| L123–L152 | 阿里云镜像 `repositories` | 国内下载依赖慢，换成阿里云仓库 |

**依赖清单（每个都对应一个功能，展示时按需引用）**：

| 依赖 | 作用 |
| :--- | :--- |
| `spring-boot-starter-web` | REST 接口 + 内嵌 Tomcat |
| `spring-boot-starter-validation` | 让 `@NotNull` / `@NotBlank` 生效 |
| `mybatis-plus-spring-boot3-starter` | 单表 CRUD 自动生成 + 条件构造器（**Spring Boot 3 专用 starter**） |
| `mysql-connector-j` | MySQL 驱动（`runtime` 作用域：编译时不需要） |
| `h2` | 内存数据库（只给演示 profile 用，业务代码不依赖） |
| `springdoc-openapi-starter-webmvc-ui` | Swagger UI 接口文档 |
| `lombok` | 编译期生成 getter/setter/构造器/日志，减少样板代码（`optional`，打包时排除） |
| `spring-boot-starter-test` | 测试（`test` 作用域） |

---

## 第 5 节 · 前端逐文件讲解

### 5.0 前端文件一览

| # | 文件 | 行数 | 作用 | 优先级 |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `index.html` | 13 | 页面骨架：一个 `<div id="app">` + 加载 `main.js` | ⭐ |
| 2 | `src\main.js` | 19 | **入口**：创建 Vue 应用、装 Element Plus、设 `axios.baseURL` | ⭐⭐⭐ |
| 3 | `src\App.vue` | 199 | **外壳**：顶部菜单 + 页面切换 + 明暗主题 + 背景图 + 毛玻璃 | ⭐⭐ |
| 4 | `src\components\SettingsDrawer.vue` | 127 | 右上角「外观设置」抽屉（主题 / 壁纸 / 透明度） | ⭐ |
| 5 | `src\views\BedMap.vue` | 118 | **页面①床位示意图**：楼层选择 + 4 个统计标签 + 房间床位格子 | ⭐⭐⭐ |
| 6 | `src\views\BedManage.vue` | 207 | **页面②床位管理**：查询表单 + 页签 + 表格 + 2 个弹窗 | ⭐⭐⭐⭐ |
| 7 | `vite.config.js` | 18 | 端口 5173 + `/api` 代理到 8080 + `base: './'` | ⭐⭐ |
| 8 | `package.json` | 21 | 依赖 + 3 条脚本命令 | ⭐ |
| 9 | `src\style.css`、`src\components\HelloWorld.vue`、`src\assets\*` | —— | **脚手架残留，未被引用，不影响运行**（见 5.7） | ✕ |

### 5.1 入口 `src\main.js`（19 行）—— 全局只做 3 件事

```js
import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'                    // Element Plus 默认（亮色）样式
import 'element-plus/theme-chalk/dark/css-vars.css'    // 暗色主题的 CSS 变量
import axios from 'axios'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
axios.defaults.baseURL = '/api'          // ★ 所有 axios 请求自动带 /api 前缀

// 全局注册所有图标（这样模板里可以直接写 <el-icon><Setting /></el-icon>）
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus)
app.mount('#app')                        // 挂载到 index.html 的 <div id="app">
```

| 关键行 | 小白解释 |
| :--- | :--- |
| `axios.defaults.baseURL = '/api'` | **全项目只写一次**。之后视图里写 `axios.get('/bed/statistics')`，真实请求是 `/api/bed/statistics`。改后端地址只改这一行 |
| `import 'element-plus/dist/index.css'` | 不加这行，整个页面的按钮/表格会「裸奔」没样式 |
| 全局注册图标 | `Object.entries()` 遍历所有图标组件挨个 `app.component()`。**代价是打包体积大一点**，好处是模板里随便用、不用一个个 import |

> ❓ **为什么本项目没有单独的 `request.js`（axios 封装）？**
> 因为一共只有 5 个接口、2 个页面，直接在视图里 `axios.xxx + try/catch` 更直观。
> **如果接口涨到 20 个以上，就应该抽出 `request.js`**，统一加 token、统一错误提示 —— 这是「按规模做设计」的取舍，答辩时可以主动说。

### 5.2 外壳 `src\App.vue`（199 行）—— 一个「不用刷新页面」的切换器

**模板部分（4 块）**：

```html
<div class="app-container">
  <!-- ① 全屏背景图层：有背景图才渲染，透明度来自设置 -->
  <div class="bg-layer" v-if="globalBgUrl"
       :style="{ backgroundImage: `url(${globalBgUrl})`, opacity: globalBgOpacity }"></div>

  <!-- ② 顶部导航栏：两个菜单项 -->
  <div class="header-bar">
    <el-menu mode="horizontal" :default-active="activeMenu" @select="handleMenuSelect">
      <el-menu-item index="1">床位示意图</el-menu-item>
      <el-menu-item index="2">床位管理</el-menu-item>
    </el-menu>
    <div class="right-action">
      <el-button circle icon="Setting" @click="settingsVisible = true"
                 :class="{ 'rotate-anim': settingsVisible }" />
    </div>
  </div>

  <!-- ③ 内容区：动态组件 + 切换动画 -->
  <div class="content">
    <Transition name="slide-fade" mode="out-in">
      <component :is="activeMenu === '1' ? BedMap : BedManage" :key="activeMenu" />
    </Transition>
  </div>

  <!-- ④ 设置抽屉 -->
  <SettingsDrawer v-model="settingsVisible" ... />
</div>
```

**这段模板最值得讲的 4 个点**：

| 代码 | 小白解释 |
| :--- | :--- |
| `<component :is="...">` | **动态组件**：一个标签根据变量渲染不同组件。点菜单改 `activeMenu`，页面就换了 —— 这就是「单页应用（SPA）**不用刷新页面**」的实现方式 |
| `:key="activeMenu"` | **强制重建组件**。不加 `:key`，Vue 会复用同一个组件实例，可能出现「切到床位管理还残留上一个页面数据」的问题。加 `:key` 后每次切换都是全新实例，`onMounted` 会重新执行、重新拉数据 |
| `<Transition name="slide-fade" mode="out-in">` | 切换动画：旧页面淡出（`out-in` = 先出后进），CSS 里定义了 `.slide-fade-enter-from/leave-to` |
| `:class="{ 'rotate-anim': settingsVisible }"` | 设置按钮打开时旋转 180°，**小细节提升观感** |

**脚本部分的 5 个 `ref`（页面状态）**：

```js
const activeMenu = ref('2')                                   // 默认显示「床位管理」
const settingsVisible = ref(false)                            // 抽屉是否打开
const themeMode = ref(localStorage.getItem('themeMode') || 'light')       // 主题
const globalBgUrl = ref(localStorage.getItem('bgUrl') || '')              // 背景图
const globalBgOpacity = ref(parseFloat(localStorage.getItem('bgOpacity')) || 0.8)  // 透明度
```

**为什么用 `localStorage`？** 因为刷新页面后 `ref` 会重置成初始值。
存到浏览器本地，**刷新后主题/背景还在** —— 这就是「用户体验」的一个小例子。

**三个「副作用」监听**：

```js
// ① 有背景图就给 body 加 has-bg 类（CSS 里靠这个类才启用毛玻璃）
watch(globalBgUrl, (val) => {
  val ? document.body.classList.add('has-bg') : document.body.classList.remove('has-bg')
}, { immediate: true })   // immediate: true → 组件初始化时立刻执行一次

// ② 主题切换：白天去掉 dark 类、黑夜加上、跟随系统看系统偏好
const applyTheme = (mode) => {
  if (mode === 'dark') document.documentElement.classList.add('dark')
  else if (mode === 'light') document.documentElement.classList.remove('dark')
  else if (mode === 'system') {
    window.matchMedia('(prefers-color-scheme: dark)').matches
      ? document.documentElement.classList.add('dark')
      : document.documentElement.classList.remove('dark')
  }
}

// ③ 系统主题变化时（用户在操作系统里切换了深色模式）实时响应
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => { ... })
```

**明暗主题是怎么实现的？** 靠给 `<html>` 加一个 `dark` 类，
再加上 `main.js` 里引入的 `dark/css-vars.css`：
Element Plus 的所有颜色都是 CSS 变量（`--el-bg-color`、`--el-text-color-primary`…），
**只要换掉变量值，全站颜色一起变**。本项目还在 `App.vue` 里定义了 `--glass-bg`、`--glass-border` 等变量，
`.dark` 状态下换成深色版本 —— 这就是「换肤」的标准做法。

### 5.3 设置抽屉 `src\components\SettingsDrawer.vue`（127 行）

**它演示了 Vue 3 的「双向绑定 + 子传父」标准写法**：

```js
const props = defineProps({          // 父组件传进来的 4 个属性
  modelValue: Boolean,               // 抽屉是否显示（配合 v-model）
  currentTheme: String,
  currentBgUrl: String,
  currentBgOpacity: Number
})
const emit = defineEmits(['update:modelValue', 'update:theme', 'update:bgUrl', 'update:bgOpacity'])

watch(() => props.modelValue, (val) => { visible.value = val })   // 父 → 子
watch(visible, (val) => { emit('update:modelValue', val) })       // 子 → 父
```

| 概念 | 小白解释 |
| :--- | :--- |
| `defineProps` / `defineEmits` | `<script setup>` 里的宏，用来声明「我收什么参数」「我能发什么事件」 |
| `v-model="settingsVisible"` | 等价于 `:modelValue="settingsVisible"` + `@update:modelValue="settingsVisible = $event"`。**这是 Vue 的 v-model 约定**，所以子组件必须发 `update:modelValue` 事件 |
| 两个 watch | 父变了同步给子（`visible`），子变了通知父（`emit`），形成双向同步 |
| `FileReader.readAsDataURL(file)` | 本地图片**读成 base64 字符串**塞进 `localStorage`，所以**不需要后端上传接口**。代价是图片不能太大，所以有 `if (file.size / 1024 / 1024 > 2) return false` 的 **2MB 限制**（返回 false 会阻止 el-upload 上传） |

> 这块是「前端小功能」的加分项：**主题 + 壁纸 + 透明度 + 本地缓存**，全在前端完成，
> 不影响业务表的任何数据。

### 5.4 页面① `src\views\BedMap.vue`（118 行）—— 床位示意图 ⭐⭐⭐

**它只用了 2 个接口、2 次请求**：

```js
const currentFloor = ref('1')
const stats = ref({ total: 0, free: 0, occupied: 0, out: 0 })   // 给 4 个 el-tag 用
const roomList = ref([])                                        // 给房间卡片用
const loading = ref(false)

const fetchStatistics = async () => {
  try {
    const res = await axios.get('/bed/statistics')
    if (res.data.code === 200) stats.value = res.data.data
  } catch (error) { console.error(error) }
}

const fetchBedMap = async () => {
  loading.value = true
  try {
    const res = await axios.get(`/bed/map?floor=${currentFloor.value}`)
    if (res.data.code === 200) roomList.value = res.data.data
  } catch (error) { console.error(error) } finally { loading.value = false }
}

onMounted(() => { fetchStatistics(); fetchBedMap() })
```

**「三色格子」是怎么画出来的？—— 前端唯一需要维护的映射表**：

```js
const getBedStatusClass = (status) =>
  ({ 1: 'status-free', 2: 'status-occupied', 3: 'status-out' }[status] || 'status-free')

const getBedStatusText = (status) =>
  ({ 1: '空闲', 2: '有人', 3: '外出' }[status] || '未知')
```

```css
.status-free     { background-color: #67C23A; }   /* 绿 = 空闲 */
.status-occupied { background-color: #409EFF; }   /* 蓝 = 有人 */
.status-out      { background-color: #E6A23C; }   /* 橙 = 外出 */
```

| 技术点 | 说明 |
| :--- | :--- |
| **`[status] \|\| 默认值`** | 用对象当「查表器」，比 `if/else if/else` 短得多；`\|\|` 兜底，防止未知状态显示空白 |
| `v-for="room in roomList" :key="room.roomNo"` | 外层循环房间；**`:key` 用唯一值（roomNo）**，Vue 才能高效更新 DOM |
| `v-for="bed in room.beds" :key="bed.id"` | 内层循环床位，key 用 `bed.id` |
| `:class="getBedStatusClass(bed.bedStatus)"` | **动态 class**：状态一变颜色自动变（数据驱动视图，不用手写 `document.querySelector`） |
| `v-loading="loading"` | Element Plus 的加载动画，请求期间卡片上转圈 |
| `<el-empty v-if="roomList.length === 0" description="当前楼层暂无床位数据" />` | 空数据时显示友好提示，而不是一片空白 |
| `@change="fetchBedMap"` | 切换楼层下拉框**立即重新请求**（`el-select` 的 change 事件） |
| `stats.value = res.data.data` | 先把 `code === 200` 判断了再取 `data`，**避免后端返回错误时把错误对象当成统计数据显示** |

> **前后端配合的关键**：前端数格子用的 `roomNo` / `bedNo` / `bedStatus` 三个字段，
> 正好就是后端 `BedVO` 提供的三个字段 —— **一个接口刚好画出整层楼**。

### 5.5 页面② `src\views\BedManage.vue`（207 行）—— 床位管理 ⭐⭐⭐⭐

**页面结构：一张卡片里装了「查询表单 + 页签 + 表格」，外面挂两个弹窗。**

```html
<el-card class="search-card">
  <!-- ① 查询表单（inline 横排） -->
  <el-form :inline="true" :model="queryParams">
    <el-form-item label="客户姓名">
      <el-input v-model="queryParams.customerName" placeholder="请输入姓名" clearable />
    </el-form-item>
    <el-form-item label="入住日期">
      <!-- value-format：让 v-model 直接拿到 'yyyy-MM-dd' 字符串。
           否则 v-model 是 Date 对象，会被 JSON 序列化成带时区的 ISO 字符串
           （例如 2025-10-11 00:00 会变成 2025-10-10T16:00:00.000Z），查询时会差一天 -->
      <el-date-picker v-model="queryParams.startDate" type="date"
                      placeholder="选择日期" value-format="YYYY-MM-DD" />
    </el-form-item>
    <el-form-item><el-button type="primary" @click="fetchDetails">查询</el-button></el-form-item>
  </el-form>

  <!-- ② 两个页签：「正在使用」= 1，「使用历史」= 0 -->
  <el-tabs v-model="activeStatus" @tab-click="handleTabChange">
    <el-tab-pane label="正在使用" name="1"></el-tab-pane>
    <el-tab-pane label="使用历史" name="0"></el-tab-pane>
  </el-tabs>

  <!-- ③ 表格 -->
  <el-table :data="tableData" border v-loading="loading">
    <el-table-column prop="customerName" label="客户姓名" />
    <el-table-column prop="customerSex"  label="性别" width="80">
      <template #default="scope">{{ scope.row.customerSex === 0 ? '男' : '女' }}</template>
    </el-table-column>
    <el-table-column prop="bedDetails" label="床位详情" />
    <el-table-column label="入住日期">
      <template #default="scope">{{ scope.row.startDate ? scope.row.startDate.split('T')[0] : '' }}</template>
    </el-table-column>
    <el-table-column label="结束日期">
      <template #default="scope">{{ scope.row.endDate ? scope.row.endDate.split('T')[0] : '' }}</template>
    </el-table-column>
    <!-- ★ 只在「正在使用」页签显示操作列 -->
    <el-table-column label="操作" width="200" v-if="activeStatus === '1'">
      <template #default="scope">
        <el-button size="small" type="warning" @click="openEditDialog(scope.row)">修改</el-button>
        <el-button size="small" type="primary" @click="openSwapDialog(scope.row)">床位调换</el-button>
      </template>
    </el-table-column>
  </el-table>
</el-card>
```

**4 个值得讲的细节**：

| 细节 | 小白解释 |
| :--- | :--- |
| `value-format="YYYY-MM-DD"` | **后端防差一天的第一道防线**（第二道在 `DateUtils.normalize`）。不加这个，日期控件会把 `Date` 对象序列化成 ISO 字符串 |
| `scope.row.startDate.split('T')[0]` | 后端返回的是 `2025-03-01`（`LocalDate`），但为了兼容各种格式，**统一按 `T` 切开取日期部分**，稳 |
| `customerSex === 0 ? '男' : '女'` | 数据库存 0/1，**展示层翻译成中文**（展示归展示、存储归存储） |
| `v-if="activeStatus === '1'"` | **操作列只在「正在使用」页签出现**。因为历史记录不能改结束时间、也不能调换（业务上讲不通），**在前端就不给入口，减少误操作** |

**「正在使用 / 使用历史」页签怎么驱动数据？**

```js
const activeStatus = ref('1')        // 默认「正在使用」
const handleTabChange = () => { fetchDetails() }   // 切页签 → 重新查

const fetchDetails = async () => {
  loading.value = true
  try {
    const params = { status: activeStatus.value }                 // 1 或 0
    if (queryParams.customerName) params.customerName = queryParams.customerName
    if (queryParams.startDate)    params.startDate = queryParams.startDate
    const res = await axios.get('/bed/manage/details', { params })
    if (res.data.code === 200) tableData.value = res.data.data
  } catch (error) { console.error(error) } finally { loading.value = false }
}
```

> ⚠️ **注意 `params` 的组装方式**：只在有值时才放进 `params`。
> 如果无脑传 `{status:'1', customerName:'', startDate:''}`，
> 后端会因为「客户姓名是空字符串」而走入「不加该条件」的分支（`StringUtils.hasText` 判断），
> **其实也不会错** —— 但只传有值的参数更清晰，**网络请求 URL 也干净**。

**弹窗 1：「修改结束时间」（`el-dialog` + `el-date-picker`）**

```js
const editDialogVisible = ref(false)
const editForm = reactive({ id: null, endDate: '' })

const openEditDialog = (row) => {
  editForm.id = row.id              // 把这一行的 beddetails.id 存起来
  editForm.endDate = row.endDate    // 回显当前结束时间
  editDialogVisible.value = true
}

const submitEdit = async () => {
  try {
    await axios.put('/bed/manage/updateEndTime', editForm)     // {id, endDate}
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    fetchDetails()                                             // ★ 重新拉列表，界面上立刻看到结果
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '修改失败')    // ★ 直接弹后端给的提示
  }
}
```

| 细节 | 说明 |
| :--- | :--- |
| `reactive({...})` | 表单用 `reactive` 而不是 `ref`，因为它是**一个对象**，`v-model="editForm.endDate"` 直接绑属性 |
| `error.response?.data?.msg` | **`?.` 可选链**：万一后端没返回响应体（比如断网），也不会因为「读取 undefined 的 data」而报二次错误；再 `\|\| '修改失败'` 兜底 |
| `fetchDetails()` | 写操作成功后**重新查询**，让表格反映数据库真实状态（而不是自己在前端改 `tableData`，容易和数据库不一致） |

**弹窗 2：「床位调换」—— 三级联动（本页最有设计感的部分）** ⭐

```js
const swapDialogVisible = ref(false)
const swapForm = reactive({ customerId: null, newFloor: '1', newRoomNo: '', newBedId: null, swapDate: '' })
const roomOptions = ref([])        // 新楼层下的所有房间
const freeBedOptions = ref([])     // 所选房间里的空闲床位

// 把 Date 转成本地时区的 yyyy-MM-dd（★ 避免 UTC 差一天）
const formatLocalDate = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

// ① 选楼层 → 清空下级、请求该楼层的房间
const handleFloorChange = async (floor) => {
  swapForm.newRoomNo = ''
  swapForm.newBedId = null
  freeBedOptions.value = []
  const res = await axios.get(`/bed/map?floor=${floor}`)   // ★ 复用示意图接口！
  if (res.data.code === 200) roomOptions.value = res.data.data
}

// ② 打开弹窗：带上客户 id、默认一层、今天就调换
const openSwapDialog = async (row) => {
  swapForm.customerId = row.customerId
  swapForm.newFloor = '1'
  swapForm.newRoomNo = ''
  swapForm.newBedId = null
  swapForm.swapDate = formatLocalDate(new Date())
  freeBedOptions.value = []
  swapDialogVisible.value = true
  await handleFloorChange('1')
}

// ③ 选房间 → 从该房间的床位里筛出「空闲」的
const fetchFreeBeds = (roomNo) => {
  const room = roomOptions.value.find(r => r.roomNo === roomNo)
  if (room) freeBedOptions.value = room.beds.filter(b => b.bedStatus === 1)
}

// ④ 提交
const submitSwap = async () => {
  if (!swapForm.newBedId) return ElMessage.warning('请选择新床位')
  try {
    await axios.post('/bed/manage/swap', swapForm)
    ElMessage.success('床位调换成功')
    swapDialogVisible.value = false
    fetchDetails()
  } catch (error) { ElMessage.error(error.response?.data?.msg || '调换失败') }
}
```

**三级联动的数据流（一定要会画这张图）**：

```
① 选「楼层」  → handleFloorChange(floor)
                → GET /bed/map?floor=1          ← 复用「床位示意图」的接口，不用新写一个
                → roomOptions = [{roomNo:101, beds:[...]}, ...]
② 选「房间」  → fetchFreeBeds(roomNo)
                → 从 roomOptions 里 find 出这个房间
                → freeBedOptions = beds.filter(b => b.bedStatus === 1)   ← 只留下 status=1 空闲
③ 选「床位」  → swapForm.newBedId = bed.id      ← 传的是 bed.id（后端真正需要的）
④ 点「确认调换」→ POST /bed/manage/swap  {customerId, newFloor, newRoomNo, newBedId, swapDate}
```

| 设计点 | 为什么这么做 |
| :--- | :--- |
| **复用 `/bed/map` 接口做楼层/房间下拉** | 不新增接口就能拿到「房间 + 床位 + 状态」，**接口数量保持 5 个**（少一个接口 = 少一份维护成本） |
| 前端只显示 `bedStatus === 1` 的床位 | **只让用户选空闲床**，从源头避免「选了有人住的床再报错」的糟糕体验 |
| 切换楼层/房间时**清空下级选择** | 防止出现「楼层选了二层、床位还是上次一层选的」这种脏数据 |
| `formatLocalDate(new Date())` | 后端要求「只能当天调换」，如果直接 `toISOString()` 拿 UTC 时间，**凌晨 0~8 点会算成昨天** → 被后端拒绝。所以手写成本地日期 |
| 前端 `if (!swapForm.newBedId) return ElMessage.warning(...)` | **前端先友好提示一次**；但真正的约束仍在后端（后端 `@NotNull` + 业务校验） |
| 提交成功后 `fetchDetails()` | 列表刷新：旧记录从「正在使用」消失、进入「使用历史」；`bedDetails` 文本也变成新床位 |

> 💡 **一句话总结这个弹窗**：「下拉框里出现的每一个床位，都是**当时数据库里真正空闲**的床位；
> 而提交到后端后，后端**还会再查一次** —— 因为在你填弹窗的这几秒里，床位有可能被别人占了。
> **前端保证体验，后端保证正确**」，这句话能给老师留下很深印象。

### 5.6 前端「配置三件套」

#### `vite.config.js`（18 行）—— 前端能连上后端的全部秘密

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],        // 让 Vite 能编译 .vue 单文件组件
  base: './',              // 打包后资源用相对路径（打包产物直接双击 index.html 也能打开）
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',   // ★ 只要请求路径以 /api 开头，就转给后端
        changeOrigin: true
        // 注意：这里没有 rewrite —— 所以 /api/bed/x 原样转发，
        // 后端的 context-path 也必须是 /api，两边才对得上
      }
    }
  }
})
```

| 配置 | 小白解释 |
| :--- | :--- |
| `plugins: [vue()]` | 没有它，`.vue` 文件浏览器是不认识的 |
| `base: './'` | 打包后 `index.html` 里引用的 js/css 用相对路径，**放到任何目录都能打开** |
| `proxy` | **开发期解决跨域的标准做法**：浏览器只跟 5173 说话，5173 再偷偷把请求转给 8080（服务器之间没有跨域限制） |
| `changeOrigin: true` | 转发时把请求头里的 `Host` 改成目标地址（某些后端会校验 Host） |
| 没有 `rewrite` | **这就是为什么后端必须有 `context-path: /api`** —— 前端 `/api` 原样传给后端，后端也带 `/api`，刚好接上 |
| `port: 5173` | 如果 5173 被占用，Vite 会自动换成 5174 并在控制台告诉你（**别慌，不是报错**） |

#### `package.json`（21 行）—— 依赖 + 命令

```json
{
  "name": "care-center-ui",
  "type": "module",
  "scripts": { "dev": "vite", "build": "vite build", "preview": "vite preview" },
  "dependencies": {
    "@element-plus/icons-vue": "^2.3.2",   // 图标
    "axios": "^1.20.0",                    // 发 HTTP 请求
    "element-plus": "^2.14.7",             // UI 组件库
    "vue": "^3.5.42"                       // 框架本体
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^6.0.8",        // Vite 的 Vue 插件
    "vite": "^8.3.0"                       // 构建工具
  }
}
```

| 命令 | 作用 | 什么时候用 |
| :--- | :--- | :--- |
| `npm install` | 按 `package.json` 下载依赖到 `node_modules/` | 第一次拿到项目 / 换了电脑 |
| `npm run dev` | 启动开发服务器（热更新，改代码浏览器立刻刷新） | **开发/演示** |
| `npm run build` | 打包成 `dist/`（压缩、合并、优化） | 要交付/上线 |
| `npm run preview` | 本地预览打包结果 | 交作业前自检 |

> `^` 表示「这个版本及以上，但不跨大版本」：`^3.5.42` = `>=3.5.42 <4.0.0`。
> `package-lock.json` 记录了**实际安装的精确版本**，保证别人 `npm install` 装到的和你完全一样。

#### `index.html`（13 行）—— 唯一的 HTML

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />                                     <!-- 中文不乱码的前提 -->
    <link rel="icon" type="image/svg+xml" href="/favicon.svg" />  <!-- public/favicon.svg -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>care-center-ui</title>
  </head>
  <body>
    <div id="app"></div>                                          <!-- ★ Vue 的挂载点 -->
    <script type="module" src="/src/main.js"></script>            <!-- 入口脚本 -->
  </body>
</html>
```

> **为什么只有一个空 div？** 因为整个页面都是 Vue 在运行时用 JS 渲染出来的（SPA 的特点），
> **服务器只需要返回这一个空壳**，剩下的交互全在浏览器里完成。

### 5.7 脚手架残留文件（**不用管，但要能解释**）

| 文件 | 状态 | 说明 |
| :--- | :--- | :--- |
| `src\style.css`（296 行） | **没有被任何文件 import** | Vite 官方模板自带的示例样式（紫色主题 `--accent: #aa3bff`），本项目的样式用的是 Element Plus + 各组件 `<style scoped>` |
| `src\components\HelloWorld.vue`（95 行） | **没有被任何文件引用** | Vite 模板的示例组件（里面 import 了 `../assets/hero.png`） |
| `src\assets\hero.png`、`vue.svg`、`public\icons.svg` | 只被上面那个残留组件引用 | 模板自带素材 |
| `src\components\SettingsDrawer.vue` | ✅ **真在用** | 被 `App.vue` 引用 |

**被问到时的标准回答**：
> 「这几个是 Vite 官方脚手架的示例文件。我确认过它们没有被任何地方 import，
> **删掉也不影响运行**。我保留它们是为了减少和模板的差异、方便对比排查；
> 如果老师希望仓库更干净，我可以马上删掉（`src\style.css`、`HelloWorld.vue`、`assets\` 三个都删）。」

---

## 第 6 节 · 5 个接口内部功能详解

> 每个接口都按同一套模板讲：**① 谁调用 → ② 参数 → ③ 内部流程 → ④ 执行的 SQL → ⑤ 返回什么 → ⑥ 失败情况**。

### 接口 1：`GET /api/bed/statistics` —— 床位统计

| 项目 | 内容 |
| :--- | :--- |
| 谁调用 | `BedMap.vue` 的 `fetchStatistics()`（页面加载时、切楼层时） |
| 参数 | 无 |
| 入口 | `BedController.statistics()` → `BedServiceImpl.statistics()` → `BedMapper.selectStatistics()` |
| SQL | `BedMapper.xml` 里的一次聚合查询（`COUNT(*)` + 3 个 `SUM(CASE WHEN…)`） |
| 返回 | `{"code":200,"msg":"操作成功","data":{"total":48,"free":40,"occupied":6,"out":2}}` |

**为什么要拆成「Controller → Service → Mapper → XML」四层？**
因为每一层职责单一：**Controller 收请求、Service 处理业务（这里还要兜底 null）、Mapper 定义方法、XML 写 SQL**。
如果只有 3 行代码就想「直接写在 Controller 里」，**换数据库方言、加缓存、写单元测试时就会很难受**。

### 接口 2：`GET /api/bed/map?floor=1` —— 按楼层取房间和床位

| 项目 | 内容 |
| :--- | :--- |
| 谁调用 | ① `BedMap.vue` 画示意图；② `BedManage.vue` 调换弹窗的「楼层/房间/空闲床位」下拉 |
| 参数 | `floor`（字符串 `"1"`/`"2"`，不传默认 `"1"`） |
| 入口 | `BedController.bedMap()` → `BedServiceImpl.listRoomBeds()` |
| SQL | **2 条**：① `SELECT * FROM room WHERE room_floor = ? ORDER BY room_no`；② `SELECT * FROM bed WHERE room_no IN (…) ORDER BY room_no, id` |
| 返回 | `[{"roomNo":101,"beds":[{"id":1,"bedNo":"1","bedStatus":1},…]}, …]` |
| 失败情况 | `floor` 传空 → 返回 `[]`；楼层不存在（如 `9`）→ 返回 `[]`（**不是报错**，前端显示「暂无床位数据」） |

**这个接口的「一石二鸟」**：同一个接口既画示意图、又当下拉框数据源。
**代价**是它返回的字段比下拉框需要的多一点（多了 `bedStatus`，其实下拉框正好要用），
**收益**是不用多写一个 `/bed/rooms` 接口 —— 这就是「接口复用」的取舍。

### 接口 3：`GET /api/bed/manage/details` —— 多条件查询使用详情

| 项目 | 内容 |
| :--- | :--- |
| 谁调用 | `BedManage.vue` 的 `fetchDetails()`（页面加载、点查询、切页签、写操作成功后刷新） |
| 参数（都可选） | `status`（1 正在使用 / 0 使用历史，默认 1）、`customerName`（模糊）、`startDate`（该日期及之后入住） |
| 入口 | `BedController.details()` → `BedManageServiceImpl.listDetails()` → `BedDetailsMapper.selectDetailsList()` |
| SQL | `beddetails LEFT JOIN customer`，**动态拼接**可选条件，`ORDER BY start_date DESC, id DESC` |
| 返回 | `[{"id":1,"customerId":1,"customerName":"张伟","customerSex":0,"bedDetails":"606栋-1层-101房间-2号床位","startDate":"2025-03-01","endDate":null}, …]` |

**参数 status 的「三层翻译」链路（最容易讲混，建议画图）**：

```
前端页签 name="1"（正在使用）
      ↓  axios.get('/bed/manage/details', { params: { status: '1' } })
后端 listDetails()：
      tabStatus = 1
      isDeleted = (tabStatus === TAB_HISTORY(0)) ? HISTORY(1) : IN_USE(0)   → 得到 0
      ↓  作为 #{isDeleted} 传进 SQL
SQL： WHERE bd.is_deleted = 0        ← 查「正在使用」的记录
```

**如果传 `status=0`（使用历史）**：`isDeleted = 1`，SQL 变成 `is_deleted = 1`，
查出的是 11 条记录里那 3 条历史记录（含 `end_date` 有值的）。**页签看到的「历史」，就是被 `swapBed` 或退住置为 1 的记录。**

**三种查询条件的实际 SQL 示例**：

```sql
-- ① 只查「正在使用」
SELECT … FROM beddetails bd LEFT JOIN customer c ON c.id = bd.customer_id
WHERE bd.is_deleted = 0 ORDER BY bd.start_date DESC, bd.id DESC;

-- ② 姓名 + 日期组合（两个 <if> 都命中）
… WHERE bd.is_deleted = 0 AND c.customer_name LIKE CONCAT('%','陈国强','%')
        AND bd.start_date >= '2025-06-18' ORDER BY …;

-- ③ 查不到（返回空数组，HTTP 依然 200）
{"code":200,"msg":"操作成功","data":[]}
```

### 接口 4：`PUT /api/bed/manage/updateEndTime` —— 修改结束时间

| 项目 | 内容 |
| :--- | :--- |
| 谁调用 | `BedManage.vue` 的「修改」按钮 → 弹窗 → `submitEdit()` |
| 请求体 | `{"id": 1, "endDate": "2026-03-01"}` |
| 入口 | `BedController.updateEndTime()` → `BedManageServiceImpl.updateEndTime()` → `bedDetailsMapper.updateById()` |
| SQL | `UPDATE beddetails SET end_date = ? WHERE id = ?`（**只动一列**） |
| 成功返回 | `{"code":200,"msg":"修改成功","data":null}` |

**4 道校验（按顺序，任何一道不过就 400 返回，不会走到 SQL）**：

| 顺序 | 校验 | 失败信息 | 谁负责 |
| :--- | :--- | :--- | :--- |
| ① | `id` 不能为空 | 「床位使用详情ID不能为空」（来自 `@NotNull` 的 message） | DTO + `@Valid` + `GlobalExceptionHandler` |
| ② | 记录必须存在 | 「床位使用详情不存在」 | Service（`record == null`） |
| ③ | 日期格式必须合法 | 「日期格式不正确，正确格式为 yyyy-MM-dd：abc」 | `DateUtils.parse` |
| ④ | 结束时间 ≥ 入住时间 | 「结束时间不能早于入住时间：2025-03-01」 | Service |

> 自测脚本给这个接口写了 **6 项断言**，正好把上面 4 道校验 + 1 个成功 + 1 个「ISO 日期兼容」全测了。

### 接口 5：`POST /api/bed/manage/swap` —— 床位调换（最复杂的接口）

| 项目 | 内容 |
| :--- | :--- |
| 谁调用 | `BedManage.vue` 的「床位调换」按钮 → 弹窗三级联动 → `submitSwap()` |
| 请求体 | `{"customerId":1,"newFloor":"1","newRoomNo":101,"newBedId":4,"swapDate":"2026-10-10"}` |
| 入口 | `BedController.swap()` → `BedManageServiceImpl.swapBed()`（**10 步 + 事务**） |
| SQL | **2 查 + 5 写**（见下表） |
| 成功返回 | `{"code":200,"msg":"床位调换成功","data":null}` |

**调换一次到底执行了多少条 SQL？**（控制台能直接看到，演示时非常好用）

| # | 类型 | SQL（简化） | 目的 |
| :--- | :--- | :--- | :--- |
| 1 | SELECT | `SELECT * FROM customer WHERE id = 1` | 客户必须存在 |
| 2 | SELECT | `SELECT * FROM bed WHERE id = 4` | 新床位必须存在且空闲 |
| 3 | SELECT | `SELECT * FROM room WHERE room_no = 101 LIMIT 1` | 反查房间，校验楼层/房间 |
| 4 | SELECT | `SELECT * FROM beddetails WHERE customer_id = 1 AND is_deleted = 0 ORDER BY id DESC LIMIT 1` | 找当前正在使用的记录 |
| 5 | UPDATE | `UPDATE beddetails SET is_deleted = 1, end_date = '今天' WHERE id = 1` | 旧记录失效 |
| 6 | UPDATE | `UPDATE bed SET bed_status = 1 WHERE id = 2` | 旧床位释放 |
| 7 | UPDATE | `UPDATE bed SET bed_status = 2 WHERE id = 4` | 新床位占用 |
| 8 | SELECT | `SELECT * FROM room WHERE room_no = 101 LIMIT 1`（`buildBedDetails` 里又一次） | 取楼层拼描述 |
| 9 | INSERT | `INSERT INTO beddetails (start_date, bed_details, customer_id, bed_id, is_deleted) VALUES (…)` | 新增使用记录 |
| 10 | UPDATE | `UPDATE customer SET room_no='101', bed_id=4, building_no='606' WHERE id = 1` | 同步客户信息 |

**返回值的两种结局**：

```json
// 成功
{"code":200,"msg":"床位调换成功","data":null}

// 失败（HTTP 400，前端直接 ElMessage.error 弹出）
{"code":400,"msg":"只能调换到空闲的床位","data":null}
{"code":400,"msg":"床位调换只能当天调换当天办理","data":null}
{"code":400,"msg":"所选床位不属于所选房间","data":null}
{"code":400,"msg":"该客户当前没有正在使用的床位，无法调换","data":null}
{"code":400,"msg":"新床位与当前床位相同，无需调换","data":null}
```

**调换后的「连锁反应」，用来看数据一致性（自测脚本就是这样验的）**：

| 数据 | 调换前 | 调换后 |
| :--- | :--- | :--- |
| `beddetails`（张伟，正在使用） | 记录 1：101 房间 2 号床，`is_deleted=0` | 变成 `is_deleted=1` 且 `end_date=今天` → 进「使用历史」 |
| `beddetails`（新增） | —— | 新记录：101 房间 4 号床，`is_deleted=0` |
| `bed`（2 号床） | `bed_status=2`（有人） | `bed_status=1`（空闲） |
| `bed`（4 号床） | `bed_status=1`（空闲） | `bed_status=2`（有人） |
| `customer`（张伟） | `room_no='101'`, `bed_id=2` | `room_no='101'`, `bed_id=4` |
| `/bed/statistics` | `occupied=6` | **还是 6**（旧床释放 + 新床占用，总数不变）✅ 这就是自测脚本第 178 行的断言 |

---

## 第 7 节 · 一条请求的完整生命周期

> 以「张伟从 101 房间 2 号床调换到 4 号床」为例，把**从鼠标点击到数据库落库**的全过程走一遍。
> 这一段是「各个文件如何联系在一起」的**标准答案**，建议照着数字念。

```
①【用户】在 BedManage.vue 的列表里点「床位调换」
      → openSwapDialog(row)：拿到 row.customerId = 1

②【前端】弹窗三级联动
      选楼层「一层」→ handleFloorChange('1')
      → axios.get('/bed/map?floor=1')
      （baseURL='/api' 自动拼 → 真实请求 /api/bed/map?floor=1）
      选房间 101 → fetchFreeBeds(101) → 只显示 bedStatus===1 的床位
      选 4 号床 → swapForm.newBedId = 4
      点「确认调换」→ 先检查 newBedId 不为空

③【Vite 代理】vite.config.js 看到路径以 /api 开头
      → 把请求转发到 http://localhost:8080/api/bed/manage/swap（路径不改写）

④【Tomcat + Spring】后端 context-path = /api 匹配上
      → 去掉 /api，剩下 /bed/manage/swap 去匹配 Controller 映射
      → 命中 BedController.swap(@Valid @RequestBody BedSwapDTO dto)

⑤【参数绑定 + 校验】
      Jackson 把 JSON 转成 BedSwapDTO 对象
      @Valid 触发 @NotNull 校验（customerId、newBedId）
      校验失败 → 抛 MethodArgumentNotValidException → 被 GlobalExceptionHandler 接住 → HTTP 400

⑥【Spring 事务开启】@Transactional(rollbackFor = Exception.class)
      → 后续所有 SQL 都在同一个事务里

⑦【Service 业务校验】（BedManageServiceImpl.swapBed）
      3.1 swapDate 必须是今天
      3.2 客户存在       → customerMapper.selectById(1)      → SELECT customer
      3.3 新床位存在且空闲 → bedMapper.selectById(4)          → SELECT bed
      3.4 反查房间 + 校验楼层/房间 → roomMapper.selectOne      → SELECT room
      3.5 找当前使用记录  → bedDetailsMapper.selectOne        → SELECT beddetails
      任何一条不满足 → throw new BusinessException("...")
                     → 事务回滚 → GlobalExceptionHandler → HTTP 400 + msg

⑧【4 张表的写操作】（全部在同一个事务里）
      3.6 bedDetailsMapper.updateById(closeOld)   → UPDATE beddetails（旧记录 is_deleted=1, end_date=今天）
      3.7 bedMapper.updateById(oldBed)            → UPDATE bed（旧床 bed_status=1）
      3.8 bedMapper.updateById(occupyNewBed)      → UPDATE bed（新床 bed_status=2）
      3.9 bedDetailsMapper.insert(newRecord)      → INSERT beddetails（新记录）
      3.10 customerMapper.updateById(updateCustomer) → UPDATE customer（房间号/床位号/楼号）

⑨【MyBatis-Plus → JDBC → 数据库】
      每条语句都被翻译成真实 SQL（application.yml 里配了 StdOutImpl，控制台能逐条看到）
      数据库执行完 → 提交事务（COMMIT）

⑩【返回】Controller 把结果包成 Result.success("床位调换成功", null)
      → 序列化成 JSON：{"code":200,"msg":"床位调换成功","data":null}
      → HTTP 200

⑪【前端】axios 的 await 拿到响应
      → ElMessage.success('床位调换成功')
      → 关闭弹窗
      → fetchDetails() 重新查询列表
        → GET /api/bed/manage/details?status=1
        → 张伟的 bedDetails 变成 "606栋-1层-101房间-4号床位"
        → 表格自动重新渲染（Vue 响应式：数据变了，视图自己变）
```

**把整条链路「翻译」成一句话（答辩必备）**：

> 「浏览器发一个 `/api/bed/manage/swap` 的请求，
> **Vite 代理**把它转给后端的 8080 端口，
> **Spring MVC** 根据 `context-path(/api)` + `@RequestMapping(/bed)` + `@PostMapping(/manage/swap)` 找到 `BedController.swap`；
> **Jackson** 把 JSON 变成 `BedSwapDTO`，**`@Valid`** 先做参数校验；
> **Service** 里 10 步业务校验 + 5 次数据库写操作被 **`@Transactional`** 包成一个原子操作；
> **MyBatis-Plus** 把每一步变成 SQL 交给 MySQL；
> 成功后 **Controller** 用统一的 **`Result`** 包成 `{code,msg,data}` 返回；
> **前端**拿到 `code=200` 就弹成功提示并刷新列表 —— **14 个文件，就这样串成了一条链**。」

### 一条请求经过了哪些文件？（速查清单）

| # | 文件 | 角色 |
| :--- | :--- | :--- |
| 1 | `care-center-ui-main\src\views\BedManage.vue` | 发起请求 |
| 2 | `care-center-ui-main\src\main.js` | 提供 `/api` 前缀 |
| 3 | `care-center-ui-main\vite.config.js` | 代理转发 |
| 4 | `Bed Management\src\main\resources\application.yml` | 提供 `context-path: /api` |
| 5 | `...\controller\BedController.java` | 路由匹配 |
| 6 | `...\dto\BedSwapDTO.java` | 接收参数 |
| 7 | `...\common\GlobalExceptionHandler.java` | 异常 → HTTP 400/500 |
| 8 | `...\service\impl\BedManageServiceImpl.java` | 业务校验 + 事务 |
| 9 | `...\mapper\CustomerMapper.java` / `BedMapper` / `RoomMapper` / `BedDetailsMapper` | 定义数据库操作 |
| 10 | `...\entity\Customer.java` / `Bed.java` / `Room.java` / `BedDetails.java` | 表和对象的映射 |
| 11 | `...\common\BedStatus.java` / `BedDetailsStatus.java` | 状态常量 |
| 12 | `...\util\DateUtils.java` | 日期解析 |
| 13 | `...\common\Result.java` | 统一返回体 |
| 14 | `Bed Management\sql\yiyang_bed.sql` 或 `db\h2-*.sql` | 数据最终落在这里 |

---

## 第 8 节 · Entity / DTO / VO 三兄弟到底啥区别

**小白最常见的困惑**：为什么同一个「床位」要有三个类？直接用一个不行吗？

| | Entity（实体） | DTO（入参） | VO（出参） |
| :--- | :--- | :--- | :--- |
| 全称 | Entity / PO | Data Transfer Object | View Object |
| 对应谁 | **数据库表的形状** | **前端传上来的形状** | **前端要看的形状** |
| 本项目例子 | `Bed`、`Customer`、`Room`、`BedDetails` | `BedDetailsQueryDTO`、`UpdateEndTimeDTO`、`BedSwapDTO` | `BedVO`、`RoomBedsVO`、`BedStatisticsVO`、`BedDetailsVO` |
| 加注解 | `@TableName`、`@TableId` | `@NotNull`、`@Schema` | `@Schema` |
| 方向 | 双向（读写表） | 前端 → 后端 | 后端 → 前端 |

**用「调换床位」这个功能看三者的分工**：

```
前端弹窗里用户选的是：楼层层号 + 房间号 + 床位号
                        ↓ 组装成
BedSwapDTO { customerId, newFloor, newRoomNo, newBedId, swapDate }   ← 入参 DTO（跨了两张表的字段）
                        ↓ Service 处理
Customer { id, roomNo, bedId, buildingNo, … }        ← Entity（直接映射 customer 表）
Bed { id, roomNo, bedStatus, … }                     ← Entity（直接映射 bed 表）
BedDetails { id, customerId, bedId, isDeleted, … }   ← Entity（直接映射 beddetails 表）
                        ↓ 组装成
BedDetailsVO { id, customerId, customerName, customerSex, bedDetails, startDate, endDate }
                        ↑ 为什么需要 VO？
                          因为 customerName / customerSex 来自 customer 表，
                          而 id / startDate / endDate 来自 beddetails 表，
                          **没有任何一张表能单独凑出这一行** → 必须用 VO 承接联表查询结果
```

**三条「为什么不能偷懒」的理由**（答辩可用）：

| 理由 | 说明 |
| :--- | :--- |
| ① 安全 | Entity 里有 `password`、`userId` 这类字段时，**直接返回 Entity 会泄露敏感数据**。VO 只挑该给的字段 |
| ② 灵活 | 前端要「姓名 + 性别 + 床位描述」跨两张表，Entity 做不到，VO 可以 |
| ③ 解耦 | 数据库字段改名时，**只改 Entity 的映射**，接口返回的 JSON 字段名（VO）可以保持不变 → 前端不用跟着改 |

---

## 第 9 节 · 逻辑思维：我做的 7 个关键决策

> 这一节是「展示你脑子」的地方 —— **不是背代码，而是讲为什么**。
> 每一条都可以独立当一个小故事讲 30 秒。

### 决策 1：把「床位示意图」和「床位管理」拆成两个 Service

- **现象**：这两个功能的数据来源都是 `bed` 表，完全可以写成一个 `BedService`。
- **我的选择**：拆成 `BedService`（看图）和 `BedManageService`（管事）。
- **理由**：需求本身就分两块；拆开后**每个类只有 2~3 个方法，一眼看完**。
  将来「示意图」要加缓存，也不会污染「床位管理」的代码。
- **可讲的取舍**：拆分的代价是类变多，但**类多是「按职责多」，不是「乱多」**。

### 决策 2：`beddetails.is_deleted` 故意不用 `@TableLogic`

- **现象**：MyBatis-Plus 有「逻辑删除」功能，字段名配上 `is_deleted` 就能自动过滤已删除数据。
- **我的选择**：**不用**，并把这个决定写在注释里。
- **理由**：这个字段在本项目里是**业务状态**（0 正在使用 / 1 使用历史），
  「使用历史」页签**正是要查 `is_deleted = 1` 的数据**。
  如果加了 `@TableLogic`，框架会自动追加 `is_deleted = 0`，**历史记录永远查不出来**。
- **可讲的点**：**框架的便利功能不能盲目用，先看语义对不对。**

### 决策 3：`updateEndTime` 只更新 `end_date` 一列

- **现象**：最省事的写法是「查出来 → 改 → `updateById(整个对象)`」。
- **我的选择**：新建空对象，**只 set 要改的那个字段**。
- **理由**：①生成的 SQL 只有一列，**不会覆盖别人刚改的其它字段**；
  ②需求要求「只能修改结束时间」，**代码层面物理性地做到了这一点**（想改都改不了别的列）。
- **可讲的点**：**把需求约束变成「代码结构上的不可能」，比写注释提醒自己更可靠。**

### 决策 4：用「一次聚合查询」代替 4 次 `COUNT`

- **现象**：统计 4 个数字，最直观的是写 4 条 `SELECT COUNT(*) ... WHERE bed_status = ?`。
- **我的选择**：**一条 SQL** 用 `SUM(CASE WHEN ...)` 一次算完，配合 `COALESCE` 兜底 0。
- **理由**：**4 次数据库往返 → 1 次**；而且接口层就一次调用。
- **可讲的点**：这也是「知道 SQL 有 `CASE WHEN`」的价值 —— **基础语法学扎实，性能优化就是顺手的事**。

### 决策 5：示意图「查房间 + 查床位」两步走，避免 N+1

- **现象**：先查 6 个房间，再循环查每个房间的床位 → **1 + 6 = 7 次查询**（N+1 问题）。
- **我的选择**：第 2 步用 `IN (房间号列表)` **一次查完**所有床位，再用 `groupingBy` 在内存里分组 → **2 次查询**。
- **理由**：这是后端最经典的性能坑之一；数据越多差距越大（12 个房间时是 1+12）。
- **可讲的点**：还能顺口说出「**这就是我以后要学缓存/索引的地方**」，显得有前瞻性。

### 决策 6：日期「前端配 `value-format` + 后端 `substring(0,10)`」双保险

- **现象**：`el-date-picker` 默认给 `Date` 对象，序列化成 ISO 字符串（带 `Z`）→ 东八区**差一天**。
- **我的选择**：**两边都处理**。前端 `value-format="YYYY-MM-DD"` 让传参干净；
  后端 `DateUtils.normalize()` 再截前 10 位兜底。
- **理由**：**前端只能约束「自己的页面」，不能约束「所有调用方」**。
  接口是要给别人（Postman、别的模块、以后的小程序）用的，**后端必须自己稳**。
- **可讲的点**：自测脚本第 140 行专门验证了「传 ISO 字符串也不会错」。

### 决策 7：`@Transactional(rollbackFor = Exception.class)` + 后端二次校验

- **现象**：「调换」要写 5 次数据库、跨 4 张表；前端已经保证「只能选空闲床位」。
- **我的选择**：①给 `swapBed` 加事务，**任何异常全部回滚**；
  ②后端**再查一次**床位状态、还要反查房间/楼层做交叉校验。
- **理由**：**前端的时间窗是不安全的** —— 用户在弹窗里选床的那几秒，床位可能被别人占了。
  后端必须「执行前再确认一次」，并保证「要么全部成功、要么什么都不变」。
- **可讲的点**：这是本项目的**技术核心**，也是最能说明「我懂业务一致性」的地方。

---

## 第 10 节 · 前后端字段对照表

> **字段名对不上 = 页面空白 = 你查半天查不出来。** 这几张表可以直接当「改代码时的检查清单」。

### 10.1 床位示意图（`GET /bed/map` → `BedMap.vue`）

| 后端 `BedVO` / `RoomBedsVO` | 前端使用位置 | 用途 |
| :--- | :--- | :--- |
| `roomNo` | `v-for="room in roomList" :key="room.roomNo"` + `{{ room.roomNo }}` | 房间卡片标题 |
| `beds`（数组） | `v-for="bed in room.beds"` | 房间里的床位 |
| `beds[].id` | `:key="bed.id"` | Vue 的 key |
| `beds[].bedNo` | `{{ bed.bedNo }}` | 显示「1/2/3/4 号床」 |
| `beds[].bedStatus` | `getBedStatusClass()` / `getBedStatusText()` | **决定颜色和文字** |

### 10.2 床位统计（`GET /bed/statistics` → `BedMap.vue`）

| 后端 `BedStatisticsVO` | 前端 | 显示 |
| :--- | :--- | :--- |
| `total` | `{{ stats.total }}` | `总床位: 48`（灰色 tag） |
| `free` | `{{ stats.free }}` | `空闲: 40`（绿色 tag） |
| `occupied` | `{{ stats.occupied }}` | `有人: 6`（红色 tag） |
| `out` | `{{ stats.out }}` | `外出: 2`（橙色 tag） |

> ⚠️ **注意前端 tag 的颜色和后端状态的颜色不完全一致**：统计区「有人」用的是 Element 的 `danger`（红），
> 而示意图格子里「有人」是蓝色 `#409EFF`。**这不是 bug，是设计选择**（统计区用 Element 语义色更醒目）。

### 10.3 床位管理列表（`GET /bed/manage/details` → `BedManage.vue`）

| 后端 `BedDetailsVO` | 前端使用位置 | 说明 |
| :--- | :--- | :--- |
| `id` | `openEditDialog(row)` → `editForm.id` | **改结束时间必须带它** |
| `customerId` | `openSwapDialog(row)` → `swapForm.customerId` | **调换必须带它**（注意不是 `id`！） |
| `customerName` | `prop="customerName"` | 客户姓名列 |
| `customerSex` | `scope.row.customerSex === 0 ? '男' : '女'` | 性别列（0 男 1 女） |
| `bedDetails` | `prop="bedDetails"` | 「606栋-1层-101房间-2号床位」 |
| `startDate` | `scope.row.startDate.split('T')[0]` | 入住日期 |
| `endDate` | `scope.row.endDate ? … : ''` | 结束日期（**null 时显示空**） |

### 10.4 两个写接口的入参（**最容易写错的地方**）

| 接口 | 前端发的对象 | 后端 DTO |
| :--- | :--- | :--- |
| `PUT /bed/manage/updateEndTime` | `editForm = { id, endDate }` | `UpdateEndTimeDTO { id, endDate }` |
| `POST /bed/manage/swap` | `swapForm = { customerId, newFloor, newRoomNo, newBedId, swapDate }` | `BedSwapDTO { customerId, newFloor, newRoomNo, newBedId, swapDate }` |

> 🔴 **`id` 与 `customerId` 千万别弄反**：
> `id` 是 **`beddetails` 这条记录的 id**（改结束时间用），
> `customerId` 是 **老人的 id**（调换用）。
> 前端 `openEditDialog` 用 `row.id`、`openSwapDialog` 用 `row.customerId` —— **写成同一个字段就会串数据**。

### 10.5 状态值对照总表（打印出来贴在桌上）

| 场景 | 数字 | 中文 | 出现位置 |
| :--- | :--- | :--- | :--- |
| `bed.bed_status` | **1** | 空闲 | 示意图绿格、统计 `free` |
| | **2** | 有人 | 示意图蓝格、统计 `occupied` |
| | **3** | 外出 | 示意图橙格、统计 `out` |
| `beddetails.is_deleted` | **0** | 正在使用 | 「正在使用」页签的数据 |
| | **1** | 使用历史 | 「使用历史」页签的数据 |
| 前端页签 `status` | **1** | 正在使用 | 对应 `is_deleted = 0` ⚠️ 相反 |
| | **0** | 使用历史 | 对应 `is_deleted = 1` ⚠️ 相反 |
| `customer.customer_sex` | **0** | 男 | 表格性别列 |
| | **1** | 女 | 表格性别列 |
| `room.room_floor` | `"1"` | 一层 | 楼层下拉 |
| | `"2"` | 二层 | 楼层下拉 |

---

## 第 11 节 · 注解速查表

> 有人指着代码问「这个小老鼠符号（@）是啥？」，翻这张表就行。

### 后端注解

| 注解 | 加在哪 | 一句话作用 |
| :--- | :--- | :--- |
| `@SpringBootApplication` | 启动类 | 开启自动配置 + 组件扫描 |
| `@MapperScan("...")` | 启动类 | 扫描并自动生成 Mapper 实现 |
| `@RestController` | Controller | 返回对象自动转 JSON |
| `@RequestMapping("/bed")` | Controller | 统一路径前缀 |
| `@GetMapping` / `@PostMapping` / `@PutMapping` | 方法 | 对应 HTTP 方法（查询 / 新增 / 修改） |
| `@RequestParam` | 方法参数 | 取 URL 上的 `?key=value` |
| `@RequestBody` | 方法参数 | 取请求体里的 JSON |
| `@Valid` | 方法参数 | **让 DTO 上的校验注解生效** |
| `@Operation` / `@Tag` / `@Schema` | 方法 / 类 / 字段 | 给 Swagger 文档写说明 |
| `@Service` | 实现类 | 交给 Spring 管理（业务层） |
| `@Configuration` | 配置类 | 声明这是配置类 |
| `@Bean` | 配置类的方法 | 把返回值注册进 Spring 容器 |
| `@RequiredArgsConstructor` | 类 | Lombok：为 `final` 字段生成构造器 → **构造器注入** |
| `@Transactional(rollbackFor = Exception.class)` | 方法 | 开事务，**任何异常都回滚** |
| `@Value("${yiyang.building-no:606}")` | 字段 | 从配置读值，`:` 后是默认值 |
| `@Slf4j` | 类 | Lombok：自动生成 `log` 对象 |
| `@Data` | 实体/DTO/VO | Lombok：生成 getter/setter/toString/equals/hashCode |
| `@NoArgsConstructor` / `@AllArgsConstructor` | 类 | Lombok：生成无参 / 全参构造器 |
| `@TableName("bed")` | 实体 | 指定对应的表名 |
| `@TableId(type = IdType.AUTO)` | 主键字段 | 主键自增 |
| `@Mapper` | Mapper 接口 | 声明这是 MyBatis 的 Mapper |
| `@Param("name")` | Mapper 方法参数 | 给参数起名，供 XML 用 `#{name}` 引用 |
| `@NotNull(message = "...")` | DTO 字段 | 不能为 null（配合 `@Valid`） |
| `@RestControllerAdvice` | 异常处理器 | 全局拦截 Controller 抛出的异常 |
| `@ExceptionHandler(Xxx.class)` | 方法 | 指定「我处理哪种异常」 |
| `@TableLogic` | 字段 | MyBatis-Plus 逻辑删除（**本项目故意没用**） |

### 前端常用写法

| 写法 | 一句话作用 |
| :--- | :--- |
| `ref(值)` | 存一个「会触发视图更新的」基本值/数组/对象 |
| `reactive({...})` | 存一个响应式对象（表单常用） |
| `v-model="x"` | 双向绑定（输入框改 → 变量变；变量变 → 输入框变） |
| `v-for="item in list" :key="item.id"` | 循环渲染，`:key` 给唯一标识 |
| `v-if` / `v-show` | 条件渲染 / 条件显示（v-if 是移除 DOM，v-show 是 `display:none`） |
| `:class="..."` / `:style="..."` | 动态 class / 动态样式 |
| `@click` / `@change` / `@tab-click` | 事件绑定 |
| `onMounted(() => {...})` | 组件挂载完成后执行（一般用来发首次请求） |
| `watch(source, cb)` | 监听变化后做副作用（如写 `localStorage`） |
| `defineProps` / `defineEmits` | 声明父传子参数 / 子向父发事件 |
| `{{ }}` | 插值：把变量显示到页面上 |
| `scope.row.xxx` | `el-table` 自定义列里取当前行数据 |
| `?.`（可选链） | 中间某层是 `null/undefined` 也不报错 |
| `\|\|`（兜底） | 左边为空时用右边的默认值 |

---

## 第 12 节 · 想改功能时该打开哪个文件

> 老师/同学常问「如果要加个功能，你改哪？」
> **这张表就是答案。** 记住它，你就不再是「只会跑项目的人」，而是「能改项目的人」。

### 常见需求 → 改动清单

| 我想做的事 | 要改的文件（按顺序） |
| :--- | :--- |
| **加一个「退住」功能**（记录失效 + 床位释放） | ① `BedManageService` 加方法声明 → ② `BedManageServiceImpl` 写实现（`@Transactional`，参考 `swapBed` 的 3.6/3.7 两步）→ ③ `BedController` 加 `@PostMapping("/manage/checkout")` → ④ 前端 `BedManage.vue` 表格加按钮和确认弹窗 |
| **加一个字段**（比如床位加「朝向」） | ① 数据库加列（`sql/yiyang_bed.sql` + `db/h2-schema.sql`）→ ② `Bed.java` / `BedVO.java` 加属性 → ③ 前端表格加列 |
| **加一个查询条件**（比如按床位状态筛列表） | ① `BedDetailsQueryDTO` 加字段 → ② `BedDetailsMapper` 方法加 `@Param` → ③ `BedDetailsMapper.xml` 加一个 `<if>` → ④ `BedManageServiceImpl.listDetails` 传参 → ⑤ 前端加输入框 |
| **改楼层数量**（一层~三层） | ① `room` 表插数据（三层房间）→ ② 前端 `BedMap.vue` 和 `BedManage.vue` 的楼层下拉加 `<el-option label="三层" value="3" />` （**后端不用改**，因为 `floor` 是参数） |
| **改颜色**（空闲改成蓝色） | `BedMap.vue` 的 `<style scoped>` 里改 `.status-free { background-color: … }` |
| **改接口前缀**（`/api` → `/yiyang`） | ① 后端 `application.yml` 的 `context-path` → ② 前端 `main.js` 的 `axios.defaults.baseURL` + `vite.config.js` 的 `proxy` 键名（**三处必须一起改**） |
| **改端口**（8080 → 9090） | ① `application.yml` 的 `server.port` → ② 前端 `vite.config.js` 的 `proxy.target` |
| **改楼房号**（606 → 607） | **只改 `application.yml` 的 `yiyang.building-no`**，然后重启（这就是把它写进配置的价值） |
| **加 Swagger 说明** | `BedController` 加 `@Operation`、DTO/VO 加 `@Schema` |
| **加接口自测** | `verify/api-test.ps1` 里照抄一个 `Check '...' -Method 'GET' -Path '...' -ExpectStatus 200 -ExpectText '...'` |
| **删掉脚手架残留** | 删 `src/style.css`、`src/components/HelloWorld.vue`、`src/assets/`（确认没人 import） |

### 「新增一个接口」的标准 5 步（万能模板）

```
① 建 DTO/VO   →  src/main/java/com/neusoft/yiyang/dto/ 或 vo/
② Mapper 方法  →  接口里加声明（简单查询用 BaseMapper，复杂查询加 XML）
③ Service     →  接口加方法声明 + impl 里写实现（校验 → 调用）
④ Controller  →  加 @GetMapping/@PostMapping 方法，一行 return Result.success(...)
⑤ 前端        →  视图里 axios 调用 + try/catch + ElMessage 提示
```

---

## 第 13 节 · 报错对照表

> **报错不要慌，先看这张表。** 这些都是真实踩过的坑（README 里还有 8 个踩坑复盘）。

| 现象 | 原因 | 怎么解决 |
| :--- | :--- | :--- |
| 页面请求全部 **404** | 前后端 `/api` 不一致 | 检查 ① `main.js` 的 `baseURL` ② `vite.config.js` 的 `proxy` 键 ③ `application.yml` 的 `context-path`，**三处都必须是 `/api`** |
| 后端显示 `Tomcat started on port 8080` 但接口 404 | 忘了带 `/api` | 真实地址是 `http://localhost:8080/api/...` |
| 前端 **`ERR_CONNECTION_REFUSED`** | 后端没启动 / 端口不对 | 先启动后端；或检查 `proxy.target` 的端口 |
| 前端页面 Ctrl+C 后 Vite 报 **端口被占用** | 旧进程没关干净 | Vite 会自动换 5174；或关掉旧进程 / 换端口 |
| 数据库里中文变成 **`?????`** | 连接串没写 `characterEncoding=utf8` 或库不是 `utf8mb4` | 用 `sql/yiyang_bed.sql` 建库（里面写了 `utf8mb4`） |
| PowerShell 执行 `api-test.ps1` 报「禁止运行脚本」 | Windows 默认执行策略限制 | 用 `powershell -ExecutionPolicy Bypass -File verify\api-test.ps1` |
| **接口返回字段全是 null** | 忘了 `map-underscore-to-camel-case: true` | 检查 MyBatis-Plus 配置 |
| 日期显示成 **`2025-03-01T16:00:00.000Z`** / 差一天 | `el-date-picker` 没配 `value-format` | 加 `value-format="YYYY-MM-DD"`（后端 `DateUtils` 也有兜底） |
| **编译报「找不到符号 getXxx()」** | Lombok 没生效（JDK 23+ 需要显式 processor path） | `pom.xml` 的 `maven-compiler-plugin` 里已配好 `annotationProcessorPaths`，别删 |
| 启动报 **`Public Key Retrieval is not allowed`** | MySQL 8 认证插件 | 连接串加 `allowPublicKeyRetrieval=true`（已有） |
| 启动报 **`Access denied for user 'root'@'localhost'`** | 数据库密码不对 | 改 `application.yml` 的 `spring.datasource.password` |
| 启动报 **`Unknown database 'yiyang'`** | 库没建 | 先执行 `sql/yiyang_bed.sql`，或改用 H2 模式 `--spring.profiles.active=h2` |
| 调换接口返回 **400「该客户当前没有正在使用的床位」** | 数据里这个客户没有 `is_deleted=0` 的记录（可能已经被调换过了） | 用 H2 模式重启（数据会重置），或查「使用历史」页签确认 |
| 调换接口返回 **400「床位调换只能当天调换当天办理」** | `swapDate` 不是今天（多半是 UTC 时区问题） | 检查前端 `formatLocalDate()` 是否被改成 `toISOString()`；自测脚本里 `$yesterday` 就是故意测这个 |
| 调换成功但**统计数字没变** | **这是正常的！** 旧床释放 + 新床占用，总数不变 | 看自测第 178 行断言：`occupied` 应该还是 6 |
| **H2 控制台 404** | 忘了带 `/api` | `http://localhost:8080/api/h2-console` |
| 打开 H2 控制台报 **「Database not found」** | `DB_CLOSE_DELAY=-1` 没配或连接串不一致 | 用 `application-h2.yml` 里那串 URL 原样粘贴 |
| 重启后 H2 数据「全变回去了」 | **内存库的正常行为**（本来就是每次重建） | 想保留数据就用 MySQL |
| `git status` 里出现 `node_modules`、`target` | `.gitignore` 没生效 / 路径写错 | 本项目 `.gitignore` 已覆盖；`git status --short` 确认 |

### 自测脚本 32 项断言清单（演示时可以边跑边念）

| # | 分组 | 项数 | 覆盖内容 |
| :--- | :--- | :--- | :--- |
| 1 | 床位统计 `/bed/statistics` | **4** | total=48、free=40、occupied=6、out=2 |
| 2 | 床位示意图 `/bed/map` | **3** | 一层有 101、二层有 206、不存在的楼层返回 `[]` |
| 3 | 列表查询 `/bed/manage/details` | **8** | 默认查、status=0、status=1、姓名模糊、日期、姓名+日期组合、查不到返回 `[]`、**非法 status=9 走默认** |
| 4 | 改结束时间 `/bed/manage/updateEndTime` | **6** | 成功、结束早于入住失败、记录不存在失败、id 为空失败、日期格式非法失败、**ISO 日期兼容** |
| 5 | 床位调换 `/bed/manage/swap` | **11** | 非当天失败、目标非空闲失败、不属于楼层失败、不属于房间失败、客户不存在失败、床位不存在失败、**成功**、调换后新床位生效、调换后旧记录进历史、**统计不变**、示意图仍正常 |
| —— | **合计** | **32** | 其中 **8 项是失败场景** |

---

## 第 14 节 · 展示话术（3 分钟版 / 7 分钟版）

### 14.1 三分钟速讲（只说，不演示）

> 「我做的是**东软颐养中心管理系统的床位管理模块**，负责两个页面：
> **床位示意图**（按楼层看 12 个房间、48 张床的三色状态）和
> **床位管理**（查入住记录、改结束时间、给老人调换床位）。
>
> 技术上：**后端 Spring Boot 3 + MyBatis-Plus + MySQL，前端 Vue 3 + Element Plus + Vite**，
> 一共 5 个接口、4 张表，代码分层是 Controller → Service → Mapper。
>
> 我最想讲的是**床位调换**这个功能：它一次要改 **4 张表、5 个写操作** ——
> 旧记录失效、旧床位释放、新床位占用、新增使用记录、同步客户信息。
> 我加了 **`@Transactional(rollbackFor = Exception.class)`** 保证要么全成功要么全回滚，
> 而且**后端会重新校验一遍**，因为前端选床的那几秒里，床位可能已经被别人占了。
>
> 另外我写了 **32 项接口自测**（含 8 个失败场景）和一个 **H2 内存库演示模式**，
> 别人电脑上**不装 MySQL 也能跑起来**。」

### 14.2 七分钟演示脚本（照着做就行）

| 时间 | 做什么 | 说什么（关键句） | 文件/命令 |
| :--- | :--- | :--- | :--- |
| 0:00–0:30 | 打开 GitHub 仓库首页 | 「这是完整交付物，README 顶部有技术栈徽章，克隆后三步就能跑」 | `README.md` |
| 0:30–1:30 | 启动后端（H2 模式）+ 前端 | 「我用 H2 内存库演示，**不用装 MySQL**，数据每次启动重建，保证演示干净」 | `java -jar target/bed-management-1.0.0.jar --spring.profiles.active=h2` |
| 1:30–2:30 | **页面一：床位示意图** | 「楼层下拉 → 12 个房间 → 48 张床，绿空闲、蓝有人、橙外出；顶部 4 个数字是**一条 SQL 聚合查出来的**」 | `BedMap.vue` + `BedMapper.xml` |
| 2:30–3:30 | 切到**页面二**，演示查询 | 「姓名模糊 + 日期 + 状态，三个条件都是**可选**的，XML 里用 `<if>` 动态拼接」 | `BedManage.vue` + `BedDetailsMapper.xml` |
| 3:30–4:30 | 演示**改结束时间** | 「比如我传一个**比入住时间还早**的日期 → 后端返回 400『结束时间不能早于入住时间』，前端直接把这句话弹出来」 | `BedManageServiceImpl` L88–L91 |
| 4:30–6:00 | **压轴：床位调换** | 「张伟现在住 101 房间 2 号床，我把他换到 4 号床」→ 三级联动 → 确认 → 提示成功 → **列表自动刷新，2 号床记录进了『使用历史』** | `BedManageServiceImpl.swapBed` L105–L203 |
| 6:00–6:40 | **切到后端控制台**看 SQL | 「一次调换打了 5 条写 SQL，全部在一个事务里；如果第 3 条出错，前两条会被回滚」 | 控制台（`StdOutImpl` 打印） |
| 6:40–7:00 | **跑自测脚本** | 「32 项断言：4 + 3 + 8 + 6 + 11，含 8 个失败场景，**全绿**」 | `powershell -ExecutionPolicy Bypass -File verify\api-test.ps1` |

### 14.3 五个「必答题」与标准答案

**Q1：为什么调换床位要改 4 张表？**
> 「因为 `customer` 表里冗余存了一份『老人现在住哪』（`room_no`/`bed_id`/`building_no`），
> `bed` 表存床位状态，`beddetails` 存使用记录，`room` 表用来校验和拼描述。
> **同一条业务事实被拆到多张表里，就必须在同一个事务里一起改。**」

**Q2：为什么用 `@Transactional(rollbackFor = Exception.class)` 而不是默认的 `@Transactional`？**
> 「Spring 默认只对 `RuntimeException` 和 `Error` 回滚。如果哪一步抛的是受检异常，
> **默认不会回滚**，事务就白加了。写上 `rollbackFor` 表示**任何异常都回滚**。」

**Q3：前端已经保证只能选空闲床位了，后端为什么还要再查一次？**
> 「**前端的时间窗不安全**：用户在弹窗里选床的那几秒，床位可能被别的人占了。
> 而且接口是可以被绕过页面直接调用的（Postman 就能调）。
> **前端保证体验，后端保证正确。**」

**Q4：`is_deleted` 为什么不用 MyBatis-Plus 的 `@TableLogic`？**
> 「因为它的语义不是『删除』，而是**业务状态**：0 正在使用、1 使用历史。
> 我的『使用历史』页签**正是要查 `is_deleted = 1`**。
> 加了 `@TableLogic` 框架会自动追加 `is_deleted = 0`，**历史记录就永远查不出来了**。」

**Q5：如果让你加一个「退住」功能，你怎么做？**
> 「和调换是同一套模式，复用 `swapBed` 的 3.6/3.7 两步就够了：
> ① `beddetails` 那条记录 `is_deleted=1` + `end_date=今天`；
> ② `bed` 表状态改回 1（空闲）；
> ③ （可选）清空 `customer.bed_id`。
> 全部放在一个 `@Transactional` 方法里，Controller 加一个 `@PostMapping("/manage/checkout")`，
> 前端在操作列加个按钮 —— **大概 30 行代码**。」

### 14.4 三个「小彩蛋」（冷场时用来救场）

1. **H2 网页控制台**：`http://localhost:8080/api/h2-console`（URL/用户/密码 README 里有），
   可以在网页上**直接写 SQL 查表**，演示「数据库到底存了什么」。
2. **Swagger 页面**：`http://localhost:8080/api/swagger-ui.html`，
   点 `Try it out` 就能**在浏览器里发请求**，不用写代码。
3. **控制台 SQL 日志**：因为配了 `StdOutImpl`，**每次操作都能看到它执行的 SQL**，
   这是最直观的「代码 → SQL 的翻译过程」。

---

## 📌 附：一页速记卡（打印出来带走）

```
项目名：  东软颐养中心管理系统 —— 床位管理模块
后端：    Spring Boot 3.5.16 + MyBatis-Plus 3.5.17 + MySQL 8（或 H2）
前端：    Vue 3.5 + Element Plus 2.14 + Vite 8 + axios
接口数：  5 个（前缀统一 /api/bed）
  ① GET  /api/bed/statistics                 统计（空闲40/有人6/外出2）
  ② GET  /api/bed/map?floor=1                楼层 → 房间 → 床位
  ③ GET  /api/bed/manage/details             多条件查询（status/姓名/日期）
  ④ PUT  /api/bed/manage/updateEndTime      改结束时间（4 道校验）
  ⑤ POST /api/bed/manage/swap               床位调换（10 步 + 事务）
数据表：  room / bed / beddetails / customer（48 床、12 房间、8 老人、11 条记录）
核心文件：BedManageServiceImpl.java（219 行，swapBed 是压轴）
自测：    32 项断言（4+3+8+6+11），含 8 个失败场景
演示命令：java -jar target/bed-management-1.0.0.jar --spring.profiles.active=h2
          cd care-center-ui-main && npm run dev
          powershell -ExecutionPolicy Bypass -File verify\api-test.ps1
三个最容易被问的点：
  1. 为什么调换要改 4 张表 → customer 表冗余了房间号/床位号
  2. 为什么 rollbackFor = Exception.class → 默认只回滚 RuntimeException
  3. is_deleted 为什么不用 @TableLogic → 它是业务状态，历史记录要查 is_deleted=1
```

---

> 📚 **想继续深挖？**
> - 每一个知识点的原理（Java / HTTP / 数据库 / Vue / Git…）都在 **`README.md` 后半部分的「零基础复习手册」**（19 节 + 8 个踩坑复盘 + 30 道自测题）。
> - 只想跑起来看效果 → 看 **`README.md` 顶部的「克隆后 3 步跑起来」**。
> - 想换成真实截图 → 看 **`docs/images/README.md`**。
