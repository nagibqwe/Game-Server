#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MYSQL_HOST="${MYSQL_HOST:-127.0.0.1}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_ADMIN_USER="${MYSQL_ADMIN_USER:-root}"
MYSQL_ADMIN_PASS="${MYSQL_ADMIN_PASS:-}"
GAME_DB_USER="${GAME_DB_USER:-tzj}"
GAME_DB_PASS="${GAME_DB_PASS:-change_me}"
GAME_PUBLIC_IP="${GAME_PUBLIC_IP:-127.0.0.1}"
JAVA_BIN="${JAVA_BIN:-java}"
ANT_BIN="${ANT_BIN:-ant}"
PID_DIR="${PID_DIR:-$ROOT_DIR/.run}"
LOG_DIR="${LOG_DIR:-$ROOT_DIR/logs/ubuntu}"

mysql_admin=(mysql -h"$MYSQL_HOST" -P"$MYSQL_PORT" -u"$MYSQL_ADMIN_USER")
if [[ -n "$MYSQL_ADMIN_PASS" ]]; then
  mysql_admin+=(-p"$MYSQL_ADMIN_PASS")
fi
mysql_game=(mysql -h"$MYSQL_HOST" -P"$MYSQL_PORT" -u"$GAME_DB_USER" -p"$GAME_DB_PASS")

usage() {
  cat <<USAGE
Usage: $(basename "$0") <command>

Commands:
  deps-check      Check required Ubuntu tools (java, ant, mysql client, ss)
  init-db         Create local MySQL databases/users, import Sql/all dumps, seed APIServer
  config-db       Rewrite server XML DB credentials for local MySQL variables
  build           Build shared modules and Agent/Public/Game jars with Ant
  start           Start AgentServer, PublicServer and GameServer in Ubuntu layout
  stop            Stop the three servers using PID files/fallback jar matching
  restart         stop + start
  status          Show PID-file status and listening ports
  tail            Tail Ubuntu launch logs

Environment variables:
  MYSQL_HOST=127.0.0.1 MYSQL_PORT=3306 MYSQL_ADMIN_USER=root MYSQL_ADMIN_PASS=''
  GAME_DB_USER=tzj GAME_DB_PASS=change_me GAME_PUBLIC_IP=127.0.0.1
  JAVA_BIN=java ANT_BIN=ant PID_DIR=.run LOG_DIR=logs/ubuntu

Examples:
  MYSQL_ADMIN_USER=root MYSQL_ADMIN_PASS=rootpass GAME_DB_PASS=devpass ./Shell/ubuntu-server.sh init-db
  GAME_DB_PASS=devpass ./Shell/ubuntu-server.sh config-db
  ./Shell/ubuntu-server.sh build
  ./Shell/ubuntu-server.sh start
USAGE
}

need_cmd() {
  command -v "$1" >/dev/null 2>&1 || { echo "Missing command: $1" >&2; return 1; }
}

deps_check() {
  local missing=0
  for cmd in "$JAVA_BIN" "$ANT_BIN" mysql ss pgrep pkill sed awk python3; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
      echo "MISSING: $cmd"
      missing=1
    else
      echo "OK: $cmd -> $(command -v "$cmd")"
    fi
  done
  return "$missing"
}

run_mysql_admin_sql() {
  "${mysql_admin[@]}" <<SQL
CREATE DATABASE IF NOT EXISTS tzj_login DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_login_log DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_public DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_public_log DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_game DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_game_log DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS tzj_backend DEFAULT CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS '${GAME_DB_USER}'@'127.0.0.1' IDENTIFIED BY '${GAME_DB_PASS}';
CREATE USER IF NOT EXISTS '${GAME_DB_USER}'@'localhost' IDENTIFIED BY '${GAME_DB_PASS}';
ALTER USER '${GAME_DB_USER}'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY '${GAME_DB_PASS}';
ALTER USER '${GAME_DB_USER}'@'localhost' IDENTIFIED WITH mysql_native_password BY '${GAME_DB_PASS}';
GRANT ALL PRIVILEGES ON tzj_login.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_login_log.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_public.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_public_log.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_game.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_game_log.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_backend.* TO '${GAME_DB_USER}'@'127.0.0.1';
GRANT ALL PRIVILEGES ON tzj_login.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_login_log.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_public.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_public_log.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_game.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_game_log.* TO '${GAME_DB_USER}'@'localhost';
GRANT ALL PRIVILEGES ON tzj_backend.* TO '${GAME_DB_USER}'@'localhost';
FLUSH PRIVILEGES;
SQL
}

seed_api_server() {
  echo "Seeding APIServer t_server row for group cn/serverId 1001 ..."
  "${mysql_game[@]}" tzj_backend <<SQL
DELETE FROM t_server WHERE serverId = 1001;
INSERT INTO t_server (serverId, serverName, groupName, WorldIP, worldPort, isHeFu, hefuServerID, serverType, isDeleted, isShow, serverOpenTime, openState, heartTime, registerNum)
VALUES (1001, 'Local Ubuntu GameServer', 'cn', '${GAME_PUBLIC_IP}', 8191, 0, 0, 1, 0, 0, NOW(), 1, NOW(), 0);
SQL
}

init_db() {
  need_cmd mysql
  echo "Creating local databases and grants on ${MYSQL_HOST}:${MYSQL_PORT} ..."
  run_mysql_admin_sql
  echo "Importing base schemas from Sql/all ..."
  "${mysql_game[@]}" tzj_login < "$ROOT_DIR/Sql/all/tzj_login.sql"
  "${mysql_game[@]}" tzj_public < "$ROOT_DIR/Sql/all/tzj_public.sql"
  "${mysql_game[@]}" tzj_game < "$ROOT_DIR/Sql/all/tzj_game.sql"
  "${mysql_game[@]}" tzj_backend < "$ROOT_DIR/Sql/all/tzj_backend.sql"
  seed_api_server
  echo "Done. Log DBs are intentionally empty; runtime log tables are created/checked by the servers."
}

config_db() {
  need_cmd python3
  python3 - "$ROOT_DIR" "$MYSQL_HOST" "$MYSQL_PORT" "$GAME_DB_USER" "$GAME_DB_PASS" "$GAME_PUBLIC_IP" <<'PY'
from pathlib import Path
import re, sys
root, host, port, user, password, public_ip = sys.argv[1:]
root = Path(root)
files = {
    'AgentServer/config/server-config.xml': {
        'tzj_login': 'tzj_login',
        'tzj_login_log': 'tzj_login_log',
    },
    'PublicServer/config/server-config.xml': {
        'tzj_public': 'tzj_public',
        'tzj_public_log': 'tzj_public_log',
    },
    'GameServer/config/server-config.xml': {
        'tzj_game': 'tzj_game',
        'tzj_login': 'tzj_login',
        'tzj_game_log': 'tzj_game_log',
    },
}
properties_files = ['Web/APIServer/src/main/resources/custom/db_backend.properties']
for rel in files:
    path = root / rel
    text = path.read_text(encoding='utf-8')
    text = re.sub(r'jdbc:mysql://[^:/?]+:\d+/(tzj_login_log|tzj_login|tzj_public_log|tzj_public|tzj_game_log|tzj_game)\?',
                  lambda m: f'jdbc:mysql://{host}:{port}/{m.group(1)}?', text)
    text = re.sub(r'username="[^"]*"', f'username="{user}"', text)
    text = re.sub(r'password="[^"]*"', f'password="{password}"', text)
    if rel == 'GameServer/config/server-config.xml':
        text = re.sub(r'<public publicIp="[^"]*" publicPort="9200"/>',
                      f'<public publicIp="{public_ip}" publicPort="9200"/>', text)
        text = re.sub(r'<gameServerIp gameServerIp="[^"]*"/>',
                      f'<gameServerIp gameServerIp="{public_ip}"/>', text)
    if rel in ('AgentServer/config/server-config.xml', 'PublicServer/config/server-config.xml'):
        text = re.sub(r'<gameServerIp gameServerIp="[^"]*"/>',
                      f'<gameServerIp gameServerIp="{public_ip}"/>', text)
    path.write_text(text, encoding='utf-8')
    print(f'updated {rel}')
for rel in properties_files:
    path = root / rel
    text = path.read_text(encoding='utf-8')
    text = re.sub(r'db\.backend\.url=.*', f'db.backend.url=jdbc:mysql://{host}:{port}/tzj_backend?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8', text)
    text = re.sub(r'db\.backend\.username=.*', f'db.backend.username={user}', text)
    text = re.sub(r'db\.backend\.password=.*', f'db.backend.password={password}', text)
    path.write_text(text, encoding='utf-8')
    print(f'updated {rel}')
PY
}

build_all() {
  need_cmd "$ANT_BIN"
  for module in GameCore GameCfg Message AgentServer PublicServer GameServer; do
    echo "==> Building $module"
    (cd "$ROOT_DIR/$module" && "$ANT_BIN" -f build-ant.xml)
  done
}

start_one() {
  local name="$1" dir="$2" jar="$3" opts="${4:-}"
  mkdir -p "$PID_DIR" "$LOG_DIR"
  local pid_file="$PID_DIR/$name.pid"
  if [[ -s "$pid_file" ]] && kill -0 "$(cat "$pid_file")" >/dev/null 2>&1; then
    echo "$name already running: $(cat "$pid_file")"
    return 0
  fi
  if pgrep -f "$jar" >/dev/null 2>&1; then
    echo "$name appears to be running: $(pgrep -f "$jar" | tr '\n' ' ')"
    return 0
  fi
  [[ -f "$dir/$jar" ]] || { echo "Missing jar: $dir/$jar. Run build first." >&2; return 1; }
  echo "Starting $name ..."
  (cd "$dir" && nohup "$JAVA_BIN" -server $opts -jar "$jar" > "$LOG_DIR/$name.out" 2>&1 & echo $! > "$pid_file")
  sleep 1
  if kill -0 "$(cat "$pid_file")" >/dev/null 2>&1; then
    echo "$name started: $(cat "$pid_file")"
  else
    echo "$name failed to stay up; see $LOG_DIR/$name.out" >&2
    return 1
  fi
}

start_all() {
  start_one agentserver "$ROOT_DIR/AgentServer/dist" AgentServer.jar "${AGENT_JAVA_OPTS:-}"
  start_one publicserver "$ROOT_DIR/PublicServer/publicserver" PublicServer.jar "${PUBLIC_JAVA_OPTS:-}"
  start_one gameserver "$ROOT_DIR/GameServer/gameserver" GameServer.jar "${GAME_JAVA_OPTS:-}"
}

stop_one() {
  local name="$1" jar="$2" pid_file="$PID_DIR/$name.pid"
  if [[ -s "$pid_file" ]] && kill -0 "$(cat "$pid_file")" >/dev/null 2>&1; then
    echo "Stopping $name pid $(cat "$pid_file")"
    kill "$(cat "$pid_file")" || true
    rm -f "$pid_file"
  fi
  pkill -f "$jar" >/dev/null 2>&1 || true
}

stop_all() {
  stop_one gameserver GameServer.jar
  stop_one publicserver PublicServer.jar
  stop_one agentserver AgentServer.jar
}

status_all() {
  mkdir -p "$PID_DIR"
  for name in agentserver publicserver gameserver; do
    local pid_file="$PID_DIR/$name.pid"
    if [[ -s "$pid_file" ]] && kill -0 "$(cat "$pid_file")" >/dev/null 2>&1; then
      echo "RUNNING $name pid $(cat "$pid_file")"
    else
      echo "STOPPED $name"
    fi
  done
  ss -lntp 2>/dev/null | awk '/:(3001|3002|9101|9200|8190|8191|9201)\>/ {print}' || true
}

tail_logs() {
  mkdir -p "$LOG_DIR"
  tail -n 100 -F "$LOG_DIR"/*.out
}

cmd="${1:-}"
case "$cmd" in
  deps-check) deps_check ;;
  init-db) init_db ;;
  config-db) config_db ;;
  build) build_all ;;
  start) start_all ;;
  stop) stop_all ;;
  restart) stop_all; start_all ;;
  status) status_all ;;
  tail) tail_logs ;;
  -h|--help|help|'') usage ;;
  *) echo "Unknown command: $cmd" >&2; usage; exit 2 ;;
esac
