#!/bin/bash



MYSQL_INSTALL_DIR=/usr/local/mysql  # mysql安装目录
mysql_package=mysql-5.6.44.tar.gz
package_dir=/data/Qxkj_Scripts/
IS_CINTINUE='y';

# 下载mysql
#install_mysql() {
#	read -p "是否下载？【y/n】" IS_CINTINUE
#	if [[ $IS_CINTINUE == 'y' ]]; then
#		yum install -y wget && wget -c http://dev.mysql.com/get/Downloads/MySQL-5.6/mysql-5.6.29.tar.gz;
#		cp mysql-5.6.29.tar.gz $MYSQL_INSTALL_DIR
#	else
#		echo "请您自行下载安装包！";
#		exit;
#	fi
#}
#
## 先切换到用户目录
#cd ~
#echo "======> 检测 $MYSQL_INSTALL_DIR 目录是否存在 <======";
#if [ -d $MYSQL_INSTALL_DIR ]; then 
#	echo "======> success！ 目录已经存在！ <======";
#else
#	echo "======> warning！ 目录不存在，将创建！ <======";
#	mkdir -p $MYSQL_INSTALL_DIR
#fi
#
#if [ $# != 0 ]; then
#	if [ -f $1 ]; then
#		echo "======> 将安装文件复制到制定目录 <======";
#		cp $1 $MYSQL_INSTALL_DIR
#		if [ $? -eq 0 ]; then
#			echo "======> 复制成功！ <======";
#			sleep 2s
#		else
#			echo "======> 复制失败！ <======";
#			install_mysql
#			exit;
#		fi
#	else
#		echo "======> error! 文件不存在 <======";
#		install_mysql
#		
#	fi
#else
#	install_mysql
#fi


echo "======> 依赖安装！ <======";
yum -y install gcc make cmake ncurses-devel libxml2-devel libtool-ltdl-devel gcc-c++ autoconf automake bison zlib-devel
echo "======> 解压文件 <======";
cd ${package_dir} && tar -zxvf ${mysql_package}
if [ $? -eq 0 ];then
	echo "======> 解压成功！ <======";
	sleep 2s
	cd mysql-5.6.44
	echo "======> 安装前的设置 <======";
	cmake . -DCMAKE_INSTALL_PREFIX=/usr/local/mysql/ \
	-DMYSQL_DATADIR=/data/Qxkj_App/mysql_3306/data \
	-DMYSQL_UNIX_ADDR=/data/Qxkj_App/mysql_3306/mysql_3306.sock \
	-DMYSQL_TCP_PORT=3306 \
	-DDEFAULT_CHARSET=utf8 \
	-DDEFAULT_COLLATION=utf8_general_ci \
	-DEXTRA_CHARSETS=all \
	-DWITH_MYISAM_STORAGE_ENGINE=1 \
	-DWITH_INNOBASE_STORAGE_ENGINE=1 \
	-DWITH_FEDERATED_STORAGE_ENGINE=1 \
	-DWITH_BLACKHOLE_STORAGE_ENGINE=1 \
	-DWITHOUT_EXAMPLE_STORAGE_ENGINE=1 \
	-DWITH_FAST_MUTEXES=1 \
	-DWITH_ZLIB=bundled \
	-DENABLED_LOCAL_INFILE=1 \
	-DWITH_READLINE=1 \
	-DWITH_EMBEDDED_SERVER=1 \
	-DWITH_DEBUG=0 

	if [ $? -eq 0 ];then
		echo "======> 设置成功！开始编译安装，大约需要20分钟，耐心等待...<======"; 
		sleep 2s
		make && make install 
		if [ $? -eq 0 ]; then
			cd ~
			echo "======> 编译成功！开始设置组和用户！ <======";
			sleep 2s
			groupadd mysql && 
	  		useradd mysql -g mysql -M -s /sbin/nologin &&
	  		chown mysq:mysql /data/Qxkj_App/mysql_3306
	  	else
	  		echo "======> 编译失败！ <======";
	  		exit;
	  	fi
	else
		echo "======> 设置失败！ <======";
		exit;
	fi
else
	echo "======> 解压失败！<=======";
	exit;

fi


echo "======> 开始配置mysql <======";
cd /usr/local/mysql

echo "======> 初始化mysql <======";
mkdir -pv /data/Qxkj_App/mysql_3306/data
chown -R mysql.mysql /data/Qxkj_App/mysql_3306
./scripts/mysql_install_db --user=mysql --datadir=/data/Qxkj_App/mysql_3306/data
#cp support-files/my-default.cnf  /etc/my.cnf

sleep 2s

echo "======> 开始修改mysql的配置文件 <======";
mv /etc/my.cnf /etc/my.cnf.bak
cat >> /etc/my.cnf <<EOF
[client]
port = 3306
default-character-set=utf8mb4

 

[mysqld]
secure_file_priv="/"

skip-slave-start
character-set-server=utf8mb4
slave-skip-errors = 1062
log-slave-updates
server-id=146
performance_schema = on
port = 3306
basedir = /usr/local/mysql
socket = /data/Qxkj_App/mysql_3306/mysq3306.sock
pid-file = /data/Qxkj_App/mysql_3306/mysq3306.pid
datadir = /data/Qxkj_App/mysql_3306/data
skip-external-locking
skip-name-resolve
max_connections = 4000
max_connect_errors = 4000
back_log=600
wait_timeout=36000
interactive_timeout=36000
default_storage_engine=innodb
character_set_server=utf8mb4
key_buffer_size = 128M
max_allowed_packet = 128M
table_open_cache = 2048
 
 
thread_cache_size = 1000
query_cache_size = 256M
thread_concurrency = 48
 
log-bin = mysql-bin
relay-log = relay-log
relay_log_index = relay-log.index
 
log-warnings = 1
log-error =/data/Qxkj_App/mysql_3306/mysql.err
 
slow_query_log = 1
long-query-time = 2
slow_query_log_file = /data/Qxkj_App/mysql_3306/slow.log

max_binlog_size = 1G
max_relay_log_size = 1G
 
 
expire_logs_days = 7
binlog_cache_size = 64M
 
 
replicate-wild-ignore-table = mysql.%
replicate-wild-ignore-table = test.%
 
relay-log-purge = 1
 
 
key_buffer_size = 64M
sort_buffer_size = 16M
read_buffer_size = 16M
join_buffer_size = 16M
read_rnd_buffer_size = 64M
bulk_insert_buffer_size = 64M
myisam_sort_buffer_size = 64M
myisam_max_sort_file_size = 256M
myisam_repair_threads = 1
myisam_recover_options
 
lower_case_table_names=1
group_concat_max_len = 64K
transaction_isolation = REPEATABLE-READ
 
innodb_file_per_table
innodb_file_format = Barracuda
 
innodb_additional_mem_pool_size = 100M
innodb_buffer_pool_size = 512M 
innodb_data_home_dir = /data/Qxkj_App/mysql_3306/data
innodb_data_file_path = ibdata1:10M:autoextend
innodb_file_io_threads = 4
innodb_read_io_threads =48
innodb_write_io_threads =48
innodb_io_capacity =600
innodb_thread_concurrency = 16
innodb_flush_log_at_trx_commit = 2
tmp_table_size = 128M
 
innodb_log_buffer_size = 16M
innodb_log_file_size = 512M
innodb_log_files_in_group = 2

 
innodb_max_dirty_pages_pct = 90
innodb_lock_wait_timeout = 36000
 
log-bin=mysql-bin
binlog_format=mixed
innodb_flush_method=O_DIRECT
 
[mysqldump]
quick
max_allowed_packet = 16M
 
[mysql]
no-auto-rehash
 
[myisamchk]
key_buffer_size = 64M
sort_buffer_size = 64M
read_buffer = 2M
write_buffer = 2M
 
[xtrabackup]
default-character-set = utf8
[client]
#port = 3306
#socket = /tmp/mysql.sock
#default-character-set = utf8

[mysql]
#default-character-set = utf8

[mysqld3307]
port = 3307 
basedir = /usr/local/mysql
datadir = /data/Qxkj_App/mysql3307/data
socket  = /data/Qxkj_App/mysql3307/mysql.sock
slow_query_log_file = /data/Qxkj_App/mysql3307/slow.log
log-error = /data/Qxkj_App/mysql3307/error.log
log-bin = /data/Qxkj_App/mysql3307/mysql-bin
sync_binlog = 1
binlog_format = row
transaction_isolation = REPEATABLE-READ
innodb_buffer_pool_size = 100m


[mysqld_multi]
mysqld=/usr/local/mysql/bin/mysqld_safe
mysqladmin=/usr/local/mysql/bin/mysqladmin


[mysqldump]
quick
max_allowed_packet = 32M

EOF

#sed -i 's/\# basedir \= ...../basedir \= \/usr\/local\/mysql/g' /etc/my.cnf
#sed -i 's/\[mysqld\]/&\ndefault-storage-engine=InnoDB/' /etc/my.cnf
#sed -i 's/\[mysqld\]/&\nlower_case_table_names=1/' /etc/my.cnf


echo "======> 制作服务启动 <======";
/bin/cp -rf support-files/mysql.server /etc/init.d/mysqld
if [ $? -eq 0 ]; then
	echo "======> 服务设置成功！ <======";
	cd ~
	service mysqld start
	sleep 2s
	service mysqld stop
	sleep 2s
else
	echo "======> 服务设置失败！ <======";
fi

echo "======> 添加到开机启动项！ <======";
chkconfig --add mysqld
if [ $? -eq 0 ]; then
	echo "======> 设置开机启动项成功！ <======";
else
	echo "======> 设置开机启动项失败！ <======";
fi


service mysqld stop
echo "======> 配置环境变量 <======";
echo "export PATH=$PATH:/usr/local/mysql/bin/" >>/etc/profile
source /etc/profile

echo "启动mysql服务 ======>"；
service mysqld start

