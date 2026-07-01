# Ubuntu quickstart: minimal login/public/game server

This note describes the shortest path for a local test deployment whose only goal is to let a client reach the login server, get a game-server entry, and enter the game. The old shell scripts in this repository were written for CentOS-style hosts and production packaging; on Ubuntu it is usually simpler to build the jars, import the SQL manually, then start the Java processes directly.

## 1. Components needed for a minimal login flow

| Component | Module | Default ports | Main config | Required database(s) |
| --- | --- | --- | --- | --- |
| Login / agent server | `AgentServer` | HTTP `3001`, socket `3002` | `AgentServer/config/server-config.xml` | `tzj_login`, `tzj_login_log` |
| Public server | `PublicServer` | game socket `9200`, background `9201` | `PublicServer/config/server-config.xml` | `tzj_public`, `tzj_public_log` |
| Game server | `GameServer` | game socket `9101`, recharge `8190`, background `8191` | `GameServer/config/server-config.xml` | `tzj_game`, `tzj_game_log`; also reads `tzj_login` via `db-public-data` |

Optional services (`SocialServer`, `StatLogServer`, web backends) are useful for a full production stack, but they are not the first things to start when the goal is only a basic local login/game smoke test.

## 2. Ubuntu packages

Use Java 8 if possible; the project and logs reference a JDK 1.8 runtime and the Ant build files produce executable jars.

```bash
sudo apt update
sudo apt install -y openjdk-8-jdk ant mysql-server net-tools psmisc
java -version
ant -version
mysql --version
```

If Ubuntu does not provide `openjdk-8-jdk` in your release, install a Temurin/Zulu JDK 8 package and make it the active `JAVA_HOME` before building.

## 3. Create and import the databases

Recommended automated path on Ubuntu:

```bash
MYSQL_ADMIN_USER=root MYSQL_ADMIN_PASS=<root_password> GAME_DB_USER=tzj GAME_DB_PASS=change_me ./Shell/ubuntu-server.sh init-db
```

This command creates/imports only the minimal local databases (`tzj_login`, `tzj_login_log`, `tzj_public`, `tzj_public_log`, `tzj_game`, `tzj_game_log`) and grants the game DB user access.

Manual path if you do not want to use the script: all full schema dumps live in `Sql/all`. The helper `tzj_create_all.sql` assumes the SQL files are located at `/data/game/sql`, so either copy the SQL files there or edit the `source` paths before importing.

For a minimal local setup you can import only the login, public and game schemas:

```bash
sudo mysql
```

```sql
CREATE DATABASE IF NOT EXISTS tzj_login DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_login_log DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_public DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_public_log DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_game DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_game_log DEFAULT CHARACTER SET utf8mb4;

CREATE USER IF NOT EXISTS 'tzj'@'127.0.0.1' IDENTIFIED BY 'change_me';
CREATE USER IF NOT EXISTS 'tzj'@'localhost' IDENTIFIED BY 'change_me';
GRANT ALL PRIVILEGES ON tzj_login.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_login_log.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_public.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_public_log.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_game.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_game_log.* TO 'tzj'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_login.* TO 'tzj'@'localhost';
GRANT ALL PRIVILEGES ON tzj_login_log.* TO 'tzj'@'localhost';
GRANT ALL PRIVILEGES ON tzj_public.* TO 'tzj'@'localhost';
GRANT ALL PRIVILEGES ON tzj_public_log.* TO 'tzj'@'localhost';
GRANT ALL PRIVILEGES ON tzj_game.* TO 'tzj'@'localhost';
GRANT ALL PRIVILEGES ON tzj_game_log.* TO 'tzj'@'localhost';
FLUSH PRIVILEGES;
```

Then import the dumps:

```bash
mysql -utzj -pchange_me tzj_login  < Sql/all/tzj_login.sql
mysql -utzj -pchange_me tzj_public < Sql/all/tzj_public.sql
mysql -utzj -pchange_me tzj_game   < Sql/all/tzj_game.sql
```

The log databases are created empty. The Java log service can create/check log tables at runtime; import a log dump only if you need historical or predefined log tables.

## 4. Update database credentials in XML

Recommended automated path on Ubuntu:

```bash
GAME_DB_USER=tzj GAME_DB_PASS=change_me GAME_PUBLIC_IP=127.0.0.1 ./Shell/ubuntu-server.sh config-db
```

Manual path if you do not want to use the script: replace the hard-coded production credentials in these files with the local MySQL user/password from step 3:

- `AgentServer/config/server-config.xml`
  - `db-login-data` -> `tzj_login`
  - `db-log-info` -> `tzj_login_log`
- `PublicServer/config/server-config.xml`
  - `db-game` -> `tzj_public`
  - `db-log-info` -> `tzj_public_log`
- `GameServer/config/server-config.xml`
  - `db-game` -> `tzj_game`
  - `db-public-data` -> `tzj_login`
  - `db-log-info` -> `tzj_game_log`

Keep the host as `127.0.0.1` for a single-machine smoke test. If MySQL 8 rejects the connection because of authentication plugin differences, reset the user with:

```sql
ALTER USER 'tzj'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'change_me';
ALTER USER 'tzj'@'localhost' IDENTIFIED WITH mysql_native_password BY 'change_me';
FLUSH PRIVILEGES;
```

## 5. Server list / client routing data

The login server authenticates against `tzj_login`; the game server also reads `tzj_login` through `db-public-data`. To make the client see a test game server, ensure the login database contains a server-list row that matches the game server config:

- game `serverId`: `1001`
- game socket IP/port: `127.0.0.1:9101` for local client, or the Ubuntu host LAN/WAN IP if the client runs elsewhere
- platform/group: `cn`
- login-server id (`lsId`): `1`

Important: `tzj_login.servername` is only a rename table (`serverId`, `changeName`, `changeTime`, `roleId`), not the platform server list. The broader platform/server-list tables are in `tzj_platformkits` (`sdk_server`, `sdk_server_extra`, `sdk_server_list`, `sdk_login_server`) and belong to the web/platform backend, not the minimal three-process smoke test.

For the minimal Java-only smoke test, inspect what the imported login schema actually contains before adding data:

```bash
mysql -utzj -pchange_me -e "USE tzj_login; SHOW TABLES;"
mysql -utzj -pchange_me -e "USE tzj_login; SHOW COLUMNS FROM userlogin;"
mysql -utzj -pchange_me -e "USE tzj_login; SHOW COLUMNS FROM rolelogin;"
mysql -utzj -pchange_me -e "USE tzj_login; SHOW COLUMNS FROM servername;"
```

The account cache is stored in `userlogin`; per-role login summaries are stored in `rolelogin`; optional server renames are stored in `servername`. If your client obtains the visible server list from the platform HTTP backend instead of the agent server, import `Sql/all/tzj_platformkits.sql` as well and add rows to `sdk_server`, `sdk_server_extra`, `sdk_server_list`, and `sdk_login_server`.

## 6. Build order

Recommended automated path:

```bash
./Shell/ubuntu-server.sh build
```

Manual path: build the shared jars first, then the runnable servers:

```bash
(cd GameCore && ant -f build-ant.xml)
(cd GameCfg && ant -f build-ant.xml)
(cd Message && ant -f build-ant.xml)
(cd AgentServer && ant -f build-ant.xml)
(cd PublicServer && ant -f build-ant.xml)
(cd GameServer && ant -f build-ant.xml)
```

Expected runnable jars after a successful build:

- `AgentServer/dist/AgentServer.jar`
- `PublicServer/publicserver/PublicServer.jar`
- `GameServer/gameserver/GameServer.jar`

## 7. Start order on Ubuntu

Recommended automated path:

```bash
./Shell/ubuntu-server.sh start
./Shell/ubuntu-server.sh status
```

Manual path: start the dependencies before the game server:

```bash
(cd AgentServer/dist && nohup java -server -jar AgentServer.jar > agentserver.out 2>&1 &)
(cd PublicServer/publicserver && nohup java -server -jar PublicServer.jar > publicserver.out 2>&1 &)
(cd GameServer/gameserver && nohup java -server -jar GameServer.jar > gameserver.out 2>&1 &)
```

Check listening ports and logs:

```bash
ss -lntp | egrep ':(3001|3002|9101|9200|8190|8191|9201)\b'
tail -n 200 AgentServer/dist/agentserver.out
tail -n 200 PublicServer/publicserver/publicserver.out
tail -n 200 GameServer/gameserver/gameserver.out
```

Stop processes by jar name:

```bash
pkill -f 'AgentServer.jar'
pkill -f 'PublicServer.jar'
pkill -f 'GameServer.jar'
```

The same script also supports `stop`, `restart` and `tail`.

## 8. CentOS script notes for Ubuntu

- Do not run `Shell/make.sh` as-is for local Ubuntu setup: it checks out old SVN paths and packages production tarballs.
- `Shell/start.sh` and `Shell/manager.sh` expect an already packaged directory layout (`gameserver/`, `worldserver/`) and copy production config templates; the current repository modules build to `GameServer/gameserver`, `PublicServer/publicserver`, and `AgentServer/dist` instead.
- Scripts that use `ifconfig` need `net-tools` on Ubuntu, or should be changed to `ip addr`.
- Scripts with `#!/sbin/bash` should use `#!/usr/bin/env bash` on Ubuntu if they are made executable.
- The JVM flags in old scripts allocate 2-4 GB heaps and use Java 8 GC flags. For a small smoke test, start with lower heap sizes only after confirming the server starts normally.

## 9. Minimal troubleshooting checklist

1. MySQL connection errors: verify XML credentials, host grants for both `localhost` and `127.0.0.1`, and MySQL authentication plugin.
2. Login works but no game server appears: verify the server-list table in `tzj_login` points to `serverId=1001`, `lsId=1`, and the reachable game IP/port.
3. Client sees server but cannot enter: verify `GameServer/config/server-config.xml` has the same `serverIdList`/`serverInfo serverId` and that port `9101` is listening.
4. Public-server errors: verify `GameServer/config/server-config.xml` points `<public publicIp="127.0.0.1" publicPort="9200"/>` at the running public server.
5. Backend heartbeat errors to `localhost:8080/TzjBackend` can be ignored for the first local smoke test unless the process exits because of them.
