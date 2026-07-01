from django.db import models
from django.contrib.auth.models import  User
import django.utils.timezone as timezone
from datetime import  datetime

# Create your models here.


class AssetStatus(models.Model):
    status_name = models.CharField(max_length=8, verbose_name='服务器状态')

    def __str__(self):
        return self.status_name

class ProjectAsset(models.Model):
    project_name = models.CharField(max_length=8, verbose_name="项目名称")
    project_short_name = models.CharField(max_length=8, verbose_name="项目简称")

    def __str__(self):
        return self.project_name

class MachineRoom(models.Model):
    machine_name = models.CharField(max_length=8, verbose_name="机房名称")
    machine_address = models.CharField(max_length=128,verbose_name='机房地址')
    machine_user = models.CharField(max_length=8,verbose_name='机房联系人')
    machine_mail = models.EmailField()

    def __str__(self):
        return self.machine_name

class Region(models.Model):
    region_name = models.CharField(max_length=8, verbose_name="渠道名称")
    region_short_name = models.CharField(max_length=8, verbose_name="渠道简称")

    def __str__(self):
        return self.region_name

class Assets(models.Model):
    assets_type_choices = (
        ('server', u'服务器'),
        ('database', u'数据库'),
    )

    assets_type = models.CharField(choices=assets_type_choices, max_length=128, default='server', verbose_name='资产类型')
    hostname = models.CharField(max_length=128,verbose_name="服务器名称")
    wip = models.GenericIPAddressField(verbose_name="外网IP")
    lip = models.GenericIPAddressField(verbose_name='内网IP')
    status = models.ForeignKey(AssetStatus,on_delete=models.DO_NOTHING)
    os = models.CharField(max_length=128,verbose_name="服务器操作系统")
    cup = models.CharField(max_length=8,verbose_name="服务器CPU")
    memory = models.CharField(max_length=8,verbose_name="服务器内存")
    disk = models.CharField(max_length=8,verbose_name="服务器磁盘")
    project = models.ForeignKey(ProjectAsset,on_delete=models.CASCADE)
    buy_time = models.DateField(blank=True, null=True, verbose_name='购买时间')
    expire_date = models.DateField(u'过保修期', null=True, blank=True)
    machineroom = models.ForeignKey(MachineRoom,on_delete=models.CASCADE)

# class ServerInfo(models.Model):
#     asset = models.ForeignKey(Assets,on_delete=models.CASCADE)
#     region = models.ForeignKey(Region,on_delete=models.CASCADE)
#     servername = models.CharField(max_length=32,verbose_name='区服名称')
