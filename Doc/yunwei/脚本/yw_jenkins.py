import jenkins
import sys,os
import time
import datetime

def send_dingding(message,send_to):
    print('@@@@@@',send_to)
    q = {"msgtype": "text", "text": {"content": "{0}".format(message)}}
    send_list = {
       'ceshi':'',
       'cehua':'9bc73d3eb01910f0260e8be33bf435526feeb6f7ac211a50be1773bb8dd3c1d7',
       'yunwei':'838d0b24c5342ec93a840e5fd48164d4c952c9db2cc154c2ee2cc94bcc5dd580',
       'all': '57ffe18923067b860bed54d007360fec860e68d2aae64bd8425e33c2fab0ba73',
    }
    send_leve = {
        '1' : ['yunwei',],
        '2' : ['cehua','yunwei'],
        'all': ['all'],
    }
    for send in send_leve[send_to]:
        print(send)
        arg = """
        curl 'https://oapi.dingtalk.com/robot/send?access_token={1}' -H 'Content-Type: application/json' -d "{0}"
        """.format(q,send_list[send])
        os.system(arg)

def init():
    jenkins_server_url = 'http://10.0.0.160:8080'
    user_id = 'admin'
    api_token = '1147e24475329439f095c86f1969f72d67'
    server = jenkins.Jenkins(jenkins_server_url, username=user_id, password=api_token)
    return server

def build_job(server,job_name):
    server.build_job(job_name)


if __name__ == '__main__':
    params = sys.argv[1] 
    server_name = sys.argv[2]
    server = init() 
    build_job(server,params)
    time.sleep(10) 
    #print(server.get_job_info(params))
    build_number = server.get_job_info(params)['lastBuild']['number']
    message = '{0} 开始构建项目{1},构建任务为:{2},构建号为:{3}'.format(datetime.datetime.now(),server_name,params,build_number)
    send_dingding(message,'all')
    start_time = datetime.datetime.now()
    while True:
        if server.get_build_info(params, build_number)['result'] == 'SUCCESS':
            end_time = datetime.datetime.now()
            use_time = (end_time - start_time).seconds
            message = '{0} 项目{1}#{3}构建成功,构建总用时:{2}秒.\n 准备执行停服更新操作'.format(datetime.datetime.now(),server_name,use_time,build_number)
            send_dingding(message,'all')
            print('构建完成，准备执行更新操作')
            sys.exit(0)
        elif server.get_build_info(params, build_number)['result'] == "FAILURE":
            message = '{0} 项目{1}构建失败,构建任务为:{2},构建号为:{3},查看详细原因: http://10.0.0.160:8080/job/{2}/{3}/console  更新操作即将退出'.format(datetime.datetime.now(),server_name,params,build_number)
            send_dingding(message,'all')
            sys.exit(0)
        else:
            time.sleep(5)
