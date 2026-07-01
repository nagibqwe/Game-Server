#!/sbin/bash
function encodeurl()
{
    encoded_str=`echo "$*" | awk 'BEGIN {
        split ("1 2 3 4 5 6 7 8 9 A B C D E F", hextab, " ")
        hextab [0] = 0
        for (i=1; i<=255; ++i) { 
            ord [ sprintf ("%c", i) "" ] = i + 0
        }
    }
    {
        encoded = ""
        for (i=1; i<=length($0); ++i) {
            c = substr ($0, i, 1)
            if ( c ~ /[a-zA-Z0-9.-]/ ) {
                encoded = encoded c             # safe character
            } else if ( c == " " ) {
                encoded = encoded "+"   # special handling
            } else {
                # unsafe character, encode it as a two-digit hex-number
                lo = ord [c] % 16
                hi = int (ord [c] / 16);
                encoded = encoded "%" hextab [hi] hextab [lo]
            }
        }
        print encoded
    }' 2>/dev/null`
}

serverid=`egrep "serverInfo" dist/config/server-config.xml | awk -F "serverId=" '{print $2}' | awk -F " " '{print $1}' |awk -F "\"" '{print $2}'`
platform=`egrep "serverInfo" dist/config/server-config.xml | awk -F "platfrom=" '{print $2}' | awk -F " " '{print $1}' |awk -F "\"" '{print $2}'`
#port=`egrep "serverInfo" dist/config/server-config.xml | awk -F "port=" '{print $2}' | awk -F " " '{print $1}' |awk -F "\"" '{print $2}'`
ip=`LC_ALL=C ifconfig|grep "inet addr:"|grep -v "127.0.0.1"|cut -d: -f2|awk '{print $1}'`

#echo $ip
httpPort=`egrep "login-data ver" dist/config/server-config.xml | awk -F "httpPort=" '{print $2}' | awk -F " " '{print $1}' | awk -F "\"" '{print $2}'`
echo "http=$httpPort"


serverName="$platform$serverid登陆服"
key="haowan123GETREQUESTKEY2015-10-21_17\\:31\\:00"
serverType=2
#`egrep "serverType" gameserver/dist/config/server-config.xml | awk -F "serverType=" '{print $2}' | awk -F " " '{print $1}' | awk -F "\"" '{print $2}'`
#echo $serverName
param="serverId=$serverid&serverName=$serverName&groupName=$platform&WorldIP=$ip&worldPort=$httpPort&isOfficial=$serverType"

md5=`echo -n $param$key | md5sum | awk '{print $1}'`

sparam="$param&sign=$md5"

echo $sparam

url="http://192.168.10.97:8080/LSMBackend/"
serverurl="server/addserver?"
dburl="dblog/addLog?"
mess=`curl $url$serverurl$sparam`

echo $mess

serverIpPort=`egrep "db-login-data" dist/config/server-config.xml | awk -F "url=" '{print $2}' | awk -F "/" '{print $3}'`
dbname=`egrep "db-login-data" dist/config/server-config.xml | awk -F "url=" '{print $2}' | awk -F "/" '{print $4}' | awk -F "?" '{print $1}'`
dbuser=`egrep "db-login-data" dist/config/server-config.xml | awk -F "username=" '{print $2}' | awk -F " " '{print $1}' | awk -F "\"" '{print $2}'`
dbpassword=`egrep "db-login-data" dist/config/server-config.xml | awk -F "password=" '{print $2}' | awk -F " " '{print $1}' | awk -F "\"" '{print $2}'`

encodeurl "$dbpassword"
dbpass="${encoded_str}"


param=`echo -n "serverId=$serverid&serverName=$serverName&groupName=$platform&serverType=$serverType&serverIpPort=$serverIpPort&dbname=$dbname&dbuser=$dbuser"`
dblogmd5=`echo -n "$param&dbpassword=$dbpassword$key" | md5sum | awk '{print $1}'`

sparam=`echo -n "$param&dbpassword=$dbpass&sign=$dblogmd5"`

echo $url$dburl$sparam

ms=`curl $url$dburl$sparam`
echo $ms


