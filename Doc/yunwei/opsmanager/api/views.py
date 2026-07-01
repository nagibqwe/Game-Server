from django.shortcuts import render
from rest_framework.response import Response
from django.contrib.auth.models import User,Group
from rest_framework import viewsets
from .serializers import *
from assets.models import *
from django.http import  JsonResponse
from deploy.cmd_run import Sshcmd
import json,os

# Create your views here.

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer

class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer

class AssetsViewSet(viewsets.ModelViewSet):
    queryset = Assets.objects.all().order_by('-buy_time')
    serializer_class = AssetSerializer

class ProjectViewSet(viewsets.ModelViewSet):
    queryset = ProjectAsset.objects.all()
    serializer_class = ProjectSerializer

class StatusViewSet(viewsets.ModelViewSet):
    queryset = AssetStatus.objects.all()
    serializer_class = StatusSerializer

class MachineViewSet(viewsets.ModelViewSet):
    queryset = MachineRoom.objects.all()
    serializer_class = MachineSerializer

class RegionViewSet(viewsets.ModelViewSet):
    queryset = Region.objects.all()
    serializer_class = RegionSerializer

def excuse_cmd(request):
    servers = request.POST.get('server_names').split(',')[0:-1]
    print(servers)
    package_name = request.POST.get("package_name")
    descripe_text = request.POST.get("descr_text")
    servet_type = request.POST.get("servet_type")
    print(servet_type)
    ssh_cmd = Sshcmd()
    for server in servers:
        print(server)
        if servet_type == "gameserver" and server != "tzj_fenzhi":
            print(1)
            ssh_cmd.connect(hostname='10.0.0.59', port=22, username='root', password='jjker1314')
            print(2)
            run_name = 'export LC_CTYPE="zh_CN.UTF-8" && cd /data/Qxkj_Scripts/ && python yw_svn_zhugan.py {0}'.format(server)
            cmd = "/usr/bin/salt -E '10.0.0.59' cmd.run '{0}'".format(run_name)
            print(cmd)
            # cmd = '/usr/bin/salt -E "10.0.0.59" cmd.run "ls /data"'
            results = os.popen(cmd).readlines()
            print(results)
            # stdin, stdout, stderr = ssh_cmd.cmd_run("cd /data/Qxkj_Scripts && python test.py")
            # stdin,stdout,stderr = ssh_cmd.cmd_run(cmd)
            # print(stdout.read().decode('utf-8'))
            # print(stderr.read().decode('utf-8'))
            # print(stdin.read().decode('utf-8'))
    # print(servers,package_name,descripe_text)
    return JsonResponse(data={'results':results})

def select_server(request):
    data = {
        "tzj_game_ceshi_7":"测试服",
        "tzj_fight_server":"测试跨服",
        "tzj_public_server": "测试公共服",
        "tzj_fenzhi": "分支服",
        "tzj_game_ceshi_5": "策划服",
    }
    return JsonResponse(data=data)