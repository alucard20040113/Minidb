# Minidb

Minidb 是一个 Java 实现的简单的数据库，部分原理参照自 MySQL、PostgreSQL 和 SQLite。实现了以下功能：

- 数据的可靠性和数据恢复
- 两段锁协议（2PL）实现可串行化调度
- MVCC
- 两种事务隔离级别（读提交和可重复读）
- 死锁处理
- 简单的表和字段管理
- 简陋的 SQL 解析（因为懒得写词法分析和自动机，就弄得比较简陋）
- 基于 socket 的 server 和 client

## 运行方式

注意首先需要在 pom.xml 中调整编译版本，如果导入 IDE，请更改项目的编译版本以适应你的 JDK

首先执行以下命令编译源码：

```shell
mvn compile
```

### Windows 系统

创建数据库（使用相对路径，在当前目录的 temp 文件夹中创建数据库）：

```
mvn exec:java "-Dexec.mainClass=top.kunkun.mydb.backend.Launcher" "-Dexec.args=-create ./data/mydb"
```

启动数据库服务：

```
mvn exec:java "-Dexec.mainClass=top.kunkun.mydb.backend.Launcher" "-Dexec.args=-open ./data/mydb"
```

在另一个终端启动客户端连接数据库：

```
mvn exec:java "-Dexec.mainClass=top.kunkun.mydb.client.Launcher"
```

会启动一个交互式命令行，就可以在这里输入类 SQL 语法，回车会发送语句到服务，并输出执行的结果。

## 支持的 SQL 语法

MyDB 包含一个简易的 SQL 解析器，支持基础的 DDL（数据定义语言）、DML（数据操纵语言）以及事务控制语句。

> **注意：** > 1. 语句暂不支持以分号（`;`）结尾。
> 2. 字符串类型的值请使用双引号 `"` 或单引号 `'` 括起来。

### 1. 事务控制 (Transaction Control)

MyDB 支持两种隔离级别：读提交（Read Committed）和可重复读（Repeatable Read）。

* **开启事务**
    * 默认开启（读提交）：`begin`
    * 显式指定读提交：`begin isolation level read committed`
    * 显式指定可重复读：`begin isolation level repeatable read`
* **提交事务**：`commit`
* **回滚事务**：`abort`

### 2. 数据定义语言 (DDL)

#### 创建表 (CREATE TABLE)
创建表时可以同时指定字段类型并创建索引。支持的数据类型有：`int32`, `int64`, `string`。

**语法：**
```sql
create table <表名> <字段名1> <类型1>, <字段名2> <类型2>, ... (index <索引字段1> <索引字段2> ...)

```

**示例：**

```sql
create table student id int32, name string, uid int64, (index name id uid)

```

#### 删除表 (DROP TABLE)

**语法：**

```sql
drop table <表名>

```

#### 查看所有表 (SHOW)

显示当前数据库中所有的表信息。

**语法：**

```sql
show

```

### 3. 数据操纵语言 (DML)

条件过滤（Where 子句）支持的基础逻辑运算符为：`and`, `or`；比较运算符为：`=`, `>`, `<`。

#### 插入数据 (INSERT)

**语法：**

```sql
insert into <表名> values <值1> <值2> ...

```

**示例：**

```sql
insert into student values 5 "kunkun" 22

```

#### 查询数据 (SELECT)

支持查询特定字段或使用 `*` 查询所有字段。

**语法：**

```sql
select * from <表名> [where <条件>]
select <字段1>, <字段2> from <表名> [where <条件>]

```

**示例：**

```sql
select name, id from student where id > 1 and id < 4

```

#### 更新数据 (UPDATE)

**语法：**

```sql
update <表名> set <字段名> = <新值> [where <条件>]

```

**示例：**

```sql
update student set name = "kunkun" where id = 5

```

#### 删除数据 (DELETE)

**语法：**

```sql
delete from <表名> [where <条件>]

```

**示例：**

```sql
delete from student where name = "kunkun"

```

```

...
