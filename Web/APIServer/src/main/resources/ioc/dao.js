var ioc = {
		conf : {
            type : "org.nutz.ioc.impl.PropertiesProxy",
            fields : {
                paths : ["custom/"]
            }
        },
        dataSource : {
            type : "com.alibaba.druid.pool.DruidDataSource",
            events : {
                create : "init",
                depose : 'close'
            },
            fields : {
                url : {java:"$conf.get('db.backend.url')"},
                username : {java:"$conf.get('db.backend.username')"},
                password : {java:"$conf.get('db.backend.password')"},
                testWhileIdle : true,
                validationQuery : {java:"$conf.get('db.backend.validationQuery')"},
                maxActive : {java:"$conf.get('db.backend.maxActive')"}
            }
        },
        dao : {
            type : "org.nutz.dao.impl.NutDao",
            args : [{refer:"dataSource"}]
        },
        loginDataSource : {
            type : "com.alibaba.druid.pool.DruidDataSource",
            events : {
                create : "init",
                depose : 'close'
            },
            fields : {
                url : {java:"$conf.get('db.login.url')"},
                username : {java:"$conf.get('db.login.username')"},
                password : {java:"$conf.get('db.login.password')"},
                testWhileIdle : true,
                validationQuery : {java:"$conf.get('db.login.validationQuery')"},
                maxActive : {java:"$conf.get('db.login.maxActive')"}
            }
        },
        loginDao : {
            type : "org.nutz.dao.impl.NutDao",
            args : [{refer:"loginDataSource"}]
        },
        loginLogDataSource : {
            type : "com.alibaba.druid.pool.DruidDataSource",
            events : {
                create : "init",
                depose : 'close'
            },
            fields : {
                url : {java:"$conf.get('db.loginLog.url')"},
                username : {java:"$conf.get('db.loginLog.username')"},
                password : {java:"$conf.get('db.loginLog.password')"},
                testWhileIdle : true,
                validationQuery : {java:"$conf.get('db.loginLog.validationQuery')"},
                maxActive : {java:"$conf.get('db.loginLog.maxActive')"}
            }
        },
        loginLogDao : {
            type : "org.nutz.dao.impl.NutDao",
            args : [{refer:"loginLogDataSource"}]
        }
};