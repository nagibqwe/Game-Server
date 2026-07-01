#!/bin/sh

ps -ef | grep /data/robot/dist/RobotServer | grep -v grep | cut -c 9-15 | xargs kill -s 9
echo "kill old process success"

