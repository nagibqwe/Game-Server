#!/bin/sh

# write packet info
SVNROMOTEPATH=http://192.168.1.8:8080/svn/qinji2/trunk/server/Q2
SVNWORKPATH=trunk
TARGETPACKET=lsm-1.0-`date + %Y%M%D`-v1

#begin check data
svn co $SVNROMOTEPATH $SVNWORKPATH



echo " make success"


