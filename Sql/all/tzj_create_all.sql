

/*游戏全组件库` */

-- 登录服
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_login` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_login_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_login`;
select "use tzj_login";
source /data/game/sql/tzj_login.sql;

-- 游戏服
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_game` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_game_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_game`;
select "use tzj_game";
source /data/game/sql/tzj_game.sql;

-- 战斗服
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_fight` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_fight_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_fight`;
select "use tzj_fight";
source /data/game/sql/tzj_game.sql;

-- 公共服九零一起玩 www.90175.com
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_public` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_public_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_public`;
select "use tzj_public";
source /data/game/sql/tzj_public.sql;

-- 社交服
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_social` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_social_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_social`;
select "use tzj_social";
source /data/game/sql/tzj_social.sql;


/*游戏后台组件库`*/

-- GM后台数据库
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_backend` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_backend`;
select "use tzj_backend";
source /data/game/sql/tzj_backend.sql;

-- 客户端日志采集库
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_clientlogkits` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_clientlogkits`;
select "use tzj_clientlogkits";
source /data/game/sql/tzj_clientlogkits.sql;

-- 图片审核库九 零一起玩 www.90 175.com
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_photocheckkits` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_photocheckkits`;
select "use tzj_photocheckkits";
source /data/game/sql/tzj_photocheckkits.sql;

-- 服务器列表库
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_platformkits` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_platformkits`;
select "use tzj_platformkits";
source /data/game/sql/tzj_platformkits.sql;

-- 新GM后台库
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_gm` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_gm`;
select "use tzj_gm";
source /data/game/sql/tzj_gm.sql;

-- 统计数据分析库
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tzj_stat_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
use `tzj_stat_log`;
select "use tzj_stat_log";
source /data/game/sql/tzj_stat_log.sql;

