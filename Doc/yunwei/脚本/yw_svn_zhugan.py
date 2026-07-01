#!/usr/bin/env python
#encoding:utf-8


import os,sys,time
import svn.remote
import svn.local
import datetime
import shutil

def send_dingding(message,send_to):
    q = {"msgtype": "text", "text": {"content": "{0}".format(message)}}
    send_list = {
       'ceshi':'',
       'cehua':'9bc73d3eb01910f0260e8be33bf435526feeb6f7ac211a50be1773bb8dd3c1d7',
       'yunwei':'838d0b24c5342ec93a840e5fd48164d4c952c9db2cc154c2ee2cc94bcc5dd580',
       #'all': '57ffe18923067b860bed54d007360fec860e68d2aae64bd8425e33c2fab0ba73',
       'all': '82c88e7a05b1f206853edfecaf3a6a7aff2501d040c9d94d830e02a202bbeccb',
    }
    send_leve = {
        '1' : ['yunwei',],
        '2' : ['cehua','yunwei'],
        'all': ['all'],
    }
    for send in send_leve[send_to]:
        arg = """
        curl 'https://oapi.dingtalk.com/robot/send?access_token={1}' -H 'Content-Type: application/json' -d "{0}"
        """.format(q,send_list[send])
        os.system(arg)

def goujian(dirname):
    r = svn.remote.RemoteClient("http://10.0.1.151/svn/MMORPG/TianZhiJin/trunk/Server",username="luowei",password="yZ1r67T7")
    #r = svn.remote.RemoteClient("http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.1.0_0701_2020.06.19_Main/trunk/Server",username="luowei",password="yZ1r67T7")
    #r = svn.remote.RemoteClient("http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.1.0.0_1230_2019.12.28/Server",username="luowei",password="yZ1r67T7")
    r.checkout(dirname)
    os.chdir(dirname)
    cmd = "source /etc/profile && /usr/local/ant/bin/ant -f build-ant.xml | tee build.log"
    dirs = ["GameCfg","GameCore","Message","GameServer","PublicServer","AgentServer"]
    for d in dirs:
        message = '{0} 开始构建{1}'.format(datetime.datetime.now(),d)
        send_dingding(message,'all')
        os.chdir(os.path.join(dirname,d))
        status = os.system(cmd)
        if status == 0:
            message = '{0} 构建{1}成功'.format(datetime.datetime.now(),d)
            lines = ''
            with open("./build.log",'r') as f:
                lines = f.readlines()
            lin = []
            for m in lines:
                if '(' not in m or ')' not in m:
                    lin.append(m)
            l = '\n'.join(lin)
            for line in lines:
                print('111111111',line)
                if '错误' in line or 'errors' in line:
                    message = '{0} 构建{1}失败，构建失败信息: {2}'.format(datetime.datetime.now(),d,l)
                    print("error:",message)
            send_dingding(message,'all')
        else:
            message = '{0} 构建{1}失败'.format(datetime.datetime.now(),d)
            send_dingding(message,'all')
    return dirname 


def stop_game(servername):
    servername_dir = "/data/Qxkj_Server/{0}/gameserver".format(servername)
    if os.path.exists(servername_dir):
        cmd = "source /etc/profile && cd /data/Qxkj_Server/{0}/gameserver && sh manager.sh stop ".format(servername)
        status = os.system(cmd)
        if status == 0:
            print("{0} 停服成功".format(servername))
        else:
            print("{0} 停服失败".format(servername))
    time.sleep(10)

def update(servername,dirname):
    Game_dir = os.path.join(dirname,"GameServer")
    os.chdir(Game_dir)
    servername_dir = "/data/Qxkj_Server/{0}/gameserver".format(servername)
    if os.path.exists(servername_dir):
        shutil.rmtree(servername_dir)
    shutil.move(os.path.join(Game_dir,"gameserver"),"/data/Qxkj_Server/{0}".format(servername))


def start_game(servername):
    src =  "/data/Qxkj_Server/config/{0}.xml".format(servername)
    dest = "/data/Qxkj_Server/{0}/gameserver/config/server-config.xml".format(servername)
    shutil.copy(src,dest)
    shutil.copy("/data/Qxkj_Server/manager.sh","/data/Qxkj_Server/{0}/gameserver/".format(servername))
    cmd = "rm -rf /data/Qxkj_Server/{0}/gameserver/bin/*".format(servername)
    os.system(cmd)
    cmd = "rm -rf /data/Qxkj_Server/{0}/gameserver/script/*".format(servername)
    os.system(cmd)
    cmd = "mkdir -pv  /data/Qxkj_Server/{0}/gameserver/script/config".format(servername)
    os.system(cmd)
    cmd = "source /etc/profile && cd /data/Qxkj_Server/{0}/gameserver/ && sh manager.sh start".format(servername)
    os.system(cmd)


if __name__ == "__main__":
    servername = sys.argv[1]
    start_time = datetime.datetime.now()
    #message = '{0} 开始更新{1}'.format(start_time,servername)
    #send_dingding(message,'all')
    dirname = "/data/" + str(time.strftime("%Y%m%d%H%M%S", time.localtime()))
    goujian(dirname)
    #dirname = "/data/20191031102919" 
    stop_game(servername)
    update(servername,dirname)
    start_game(servername) 
    shutil.rmtree(dirname)
    end_time = datetime.datetime.now()
    message = '{0} 更新{1}完成 总耗时:{2} 秒'.format(end_time,servername,(end_time-start_time).seconds)
    send_dingding(message,'all')
