 
## 停服更新:

### 停服更新游戏服以及跨服
	- 	停服(双方约定)
	-	备份(程序排除logs目录)
	-	备份数据库(逻辑库)
	-	覆盖更新文件并删除gameserver/bin/* 和 gameserver/script/*
	
### 停服更新公共服
	- 	停服(双方约定)
	-	备份(程序排除logs目录)
	-	备份数据库(逻辑库)
	-	覆盖更新文件并删除publicserver/bin/* 和 publicserver/script/*
	
### 停服更新登录服
	- 	停服(双方约定)
	-	备份(程序排除logs目录)
	-	备份数据库(逻辑库)
	-	覆盖更新文件并删除agentserver/bin/* 和 agentserver/script/*
	

## 热更新:

### 热更新游戏服&跨服:
		- 解压后对应的目录  gameserver/script/
		- 覆盖替换gameserver/script/
		- 通过GM后台刷新更新指令(GM命令每次会在更新文档中说明ID或者name)

### 热更公共服:
		- 解压后对应的目录  publicserver/script/
		- 覆盖替换publicserver/script/
		- 通过GM后台刷新更新指令(GM命令每次会在更新文档中说明ID或者name)
		
### 热更新登录服:
		- 解压后对应的目录  agentserver/script/
		- 覆盖替换agentserver/script/
		- 通过GM后台刷新更新指令 (GM命令每次会在更新文档中说明ID或者name)
		