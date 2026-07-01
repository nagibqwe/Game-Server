#!/bin/bash

curl 'https://oapi.dingtalk.com/robot/send?access_token=57ffe18923067b860bed54d007360fec860e68d2aae64bd8425e33c2fab0ba73' -H 'Content-Type: application/json' -d '{"msgtype": "text", "text": {"content": "%s"}}' "${1}"
