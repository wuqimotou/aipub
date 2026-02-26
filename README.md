# aipub

基于 Spring Boot + LangChain4j 的 AI 对话服务 Demo

## 项目简介

aipub 是一个基于 Spring Boot 3.2.6 和 LangChain4j 1.0.0-beta4 实现的 AI 对话服务项目。该项目演示了如何构建一个具备以下功能的 AI 应用：

- **AI 对话服务**：基于大语言模型的智能对话，支持普通对话和流式对话
- **对话记忆**：保存用户对话上下文，实现多轮对话
- **RAG 检索增强**：基于 Pinecone 向量数据库的知识库检索
- **函数调用**：支持 AI 调用外部工具函数
- **定时任务**：自动清理对话历史和刷新知识库

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.6 |
| LangChain4j | 1.0.0-beta4 |
| MySQL | 8.0+ |
| Redis | - |
| MyBatis Plus | 3.5.11 |
| Pinecone | - |
| XxlJob | 3.0.0 |
| Knife4j | 4.3.0 |

## 项目结构

```
aipub
├── src/main/java/mt/aipub/
│   ├── bot/                    # AI 服务接口定义
│   │   ├── Waiter.java
│   │   └── impl/WaiterImpl.java
│   ├── config/                 # 配置类
│   │   ├── BotConfiguration.java
│   │   ├── ChatMemoryConfiguration.java
│   │   ├── ContentRetrieverConfiguration.java
│   │   ├── EmbeddingStoreConfiguration.java
│   │   ├── XxlJobConfig.java
│   │   └── model/
│   ├── controller/             # REST 控制器
│   │   └── ChatController.java
│   ├── entity/                 # 实体类
│   │   ├── ChatRequest.java
│   │   └── ChatResponse.java
│   ├── functionCall/           # 函数调用
│   │   └── WaiterFunctionCall.java
│   ├── job/                    # 定时任务
│   │   ├── ChatMemoryAutoJobHandle.java
│   │   └── RefreshRAGDocumentsJobHandle.java
│   ├── mapper/                 # MyBatis 映射
│   │   └── ChatMemoryMapper.java
│   ├── service/                # 业务服务
│   │   ├── WaiterChatService.java
│   │   └── impl/WaiterChatServiceImpl.java
│   ├── store/                  # 对话记忆存储
│   │   └── WaiterChatMemoryStore.java
│   ├── tool/                   # 工具类
│   │   ├── DateTool.java
│   │   └── MathTool.java
│   └── constant/               # 常量定义
│       └── ApiKeyConstant.java
├── src/main/resources/
│   ├── documents/              # RAG 文档
│   │   └── wineMenu.md         # 酒品菜单
│   ├── mapper/                 # MyBatis XML
│   │   └── ChatMemoryMapper.xml
│   ├── promptTemplate/         # 提示词模板
│   │   └── waiterSystemPrompt.txt
│   ├── application.yml         # 应用配置
│   └── logback.xml             # 日志配置
└── pom.xml                     # Maven 依赖
```

## 核心功能

### 1. AI 对话服务

项目实现了一个"服务员"角色 (Waiter)，用于与用户进行酒品点单相关的对话。

- **普通对话**：`POST /chat/waiter/chat`
- **流式对话**：`POST /chat/waiter/streamChat`
- **清除记忆**：`DELETE /chat/waiter/clear`

### 2. RAG 检索增强

基于 Pinecone 向量数据库构建酒品菜单知识库，支持 AI 在回答问题时检索相关知识。

### 3. 函数调用 (Function Call)

支持 AI 调用外部工具函数：
- **DateTool**：获取当前日期和时间
- **MathTool**：数学计算

### 4. 定时任务 (XxlJob)

- **ChatMemoryAutoJobHandle**：自动清理过期的对话记忆
- **RefreshRAGDocumentsJobHandle**：定期刷新 RAG 文档

## 配置说明

主要配置在 `src/main/resources/application.yml` 中：

```yaml
server:
  port: 8081

spring:
  data:
    redis:
      host: 172.16.1.104
      password: 1234
      port: 6379
  datasource:
    url: jdbc:mysql://172.16.1.104:3306/aipub
    username: root
    password: 1234
```

## 依赖服务

项目依赖以下服务：
- **MySQL**：存储对话历史
- **Redis**：缓存和会话管理
- **Pinecone**：向量数据库（用于 RAG）
- **XxlJob**：分布式任务调度平台
- **阿里云百炼 (DashScope)**：LLM 模型服务

## 构建与运行

```bash
# 构建项目
./mvnw clean package

# 运行项目
./mvnw spring-boot:run

# 或运行打包后的 JAR
java -jar target/aipub-0.0.1-SNAPSHOT.jar
```

## API 文档

项目集成了 Knife4j，启动后访问：
- Swagger UI: `http://localhost:8081/doc.html`

## 示例对话

```
用户: 请问有什么酒可以选择？
服务员: 您好！我们这里有白酒、鸡尾酒和洋酒等多种选择。请问您想了解哪一种呢？

用户: 来一杯茅台酒多少钱？
服务员: 茅台酒是88元/杯。

用户: 再来一杯莫吉托
服务员: 好的，莫吉托是9元/杯。
```
