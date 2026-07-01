# bi帮助

* 在xml目录下按照规定格式添加说明描述文件[glog_schema.xml]（_注意：格式需要参考《绿岸手游数据采集内容规范v2.0》_）
* 基础字段注意和前面已有的表保持一致的顺序
* 在IDEA打开Terminal，执行make mbi命令，会自动生成相应的结构，位于struct目录下
* 拼接字符串的类BIString也是自动生成的
* BiScript中添加具体的日志处理逻辑

* 如果是特殊的全局表，需要将表名配置在Server\GameServer\tool\special_tables.txt