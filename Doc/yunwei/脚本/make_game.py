#!/usr/local/env/python
# encoding:utf-8
# decript: reconsitution make_game.sh
# date:20200409
#auther:changshao

import os, sys, time, datetime, shutil
import svn.remote
import svn.local
import tarfile


def get_svn_path():
    print("""
            "请输入分支："
            "主干：main"
            "版署：bs"
            "分支：fz"

        """)
    svn_path = input("请输入选项: ")
    path_info = {
        "main": "http://10.0.1.151/svn/MMORPG/TianZhiJin/trunk/Server",
        "bs": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.1.0.0_banshu_2020_01_15/Server",
        #"fz": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.0.0_0416_2020.04.10_Main/trunk/Server"
        "fz": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.1.0_0701_2020.06.19_Main/trunk/Server"
    }
    return path_info[svn_path],svn_path

def get_gm_svn_path(svn_path):
    path_info = {
        "main": "http://10.0.1.151/svn/MMORPG/TianZhiJin/trunk/Tool/TzjBackend",
        "bs": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.1.0.0_banshu_2020_01_15/Tool/TzjBackend",
        #"fz": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.0.0_0416_2020.04.10_Main/trunk/Tool/TzjBackend"
        "fz": "http://10.0.1.151/svn/MMORPG/TianZhiJin/branches/1.2.1.0_0701_2020.06.19_Main/trunk/Tool/TzjBackend"
    }
    return path_info[svn_path],svn_path

def get_zone():
    print("""
    	    "请输入控制命令："
    	    "国内：China"
    	    "日本：Japan"
    	""")
    zone = input("请输入选项: ")
    return zone

def get_app():
    print("""
        	    "请输入控制命令："
        	    "所有：all"
        	    "游戏服：gs"
        	    "公共服：ps"
        	    "公共服：as"
                    "GM后台: gm"
        	""")
    app_names = {
        "all": ["GameServer","AgentServer","PublicServer"],
        "gs":  ["GameServer"],
        "ps": ["PublicServer"],
        "as": ["AgentServer"],
        "gm" : ["TzjBackend"],
    }
    app_name = input("请输入选项: ")
    return app_names[app_name]

def update_or_hotupdate():
    update_select = input("请输入update或者hot:")
    return update_select

def check_ops(path_info,zone,app_names):
    print("请确认你准备打包{0},语种{1}的{2}包".format(path_info,zone,app_names))
    choice = input("确认请输入Y/y,否则请输入N/n:")
    if choice == "Y" or choice == "y":
        return True
    else:
        return False

def input_file_name():
    file_names = input("请输入热更需要的配置文件名,多个文件使用,分割:")
    filenames = file_names.split(',')
    return filenames

def cp_package(package,app_names):
    dir_name = '/data/mhfwz/server/CN/stable/android_cn/'
    shutil.copy(package,dir_name)
    os.chdir(dir_name)
    for app_name in app_names:
        if os.path.exists(app_name.lower()):
            shutil.rmtree(app_name.lower())
    cmds = ["tar xf tzj*.tar.gz","rm -rf *.tar.gz","mv tzj*/* .","rm -rf tzj*"]
    for cmd in cmds:
        os.system(cmd)

class Make_Game():
    ##初始化
    def __init__(self):
        self.SVNROMATEPATH,self.area = get_svn_path() #获取SVN地址
        self.zone = get_zone() #获取区域九  零一起 玩 www.9 0175.c  om
        self.app_names = get_app() #获取构建应用
        if self.app_names[0] == 'TzjBackend':
            self.update_select = 'update' #重写构建模式
            self.SVNROMATEPATH,self.area = get_gm_svn_path(self.area) #重写svn地址
            self.ROOTPATH = os.getcwd() #获取当前目录
            self.SVNWORKPATH = os.path.join(self.ROOTPATH,'{0}_{1}'.format(self.area,'gm'))
            os.makedirs(self.SVNWORKPATH) if not os.path.exists(self.SVNWORKPATH) else print("GM目录已经存在") #创建GMcheckout目录
        else:
            self.update_select = update_or_hotupdate()
            self.ROOTPATH = os.getcwd() #获取当前目录
            self.SVNWORKPATH = os.path.join(self.ROOTPATH, self.area)
        #self.update_select = update_or_hotupdate()

        self.USERNAME = 'luowei'
        self.PASSWORD = 'yZ1r67T7'
        if self.update_select == "update":
            self.Version = "update-0.0.1"
        elif self.update_select == 'hot':
            self.Version = "hotupdate-0.0.1"
        self.PACKAGE_TIME = datetime.datetime.now().strftime('%Y-%M-%d_%H:%M:%S')

        self.COUNT = len(open('dabao.txt', 'r').readlines())
        PACKAGE_NAME1 = 'tzj-GM-' + self.Version + '-' + datetime.datetime.now().strftime('%Y-%M-%d') + '-v' + str(self.COUNT+1)
        PACKAGE_NAME2 = 'tzj-gameserver-' + self.Version + '-' + datetime.datetime.now().strftime('%Y-%M-%d') + '-v' + str(self.COUNT+1)
        if self.app_names[0] == 'TzjBackend':
            self.PACKAGE_NAME = PACKAGE_NAME1
        else:
            self.PACKAGE_NAME = PACKAGE_NAME2
        os.system('echo "打包时间:{0} {1}" >> dabao.txt'.format(self.PACKAGE_TIME,self.PACKAGE_NAME))
        self.md5value = 0

    def get_svn_code(self):
        print("=" * 10 + '开始从SVN拉取代码' + '=' * 10)
        svn.remote.RemoteClient(self.SVNROMATEPATH, username=self.USERNAME, password=self.PASSWORD).checkout(
            self.SVNWORKPATH)
        print("=" * 10 + 'SVN checkout Success!!!' + '=' * 10)

    def build_default_svn_code(self):
        BUILD_LIST = ['GameCore', "GameCfg", "Message"]
        for b_list in BUILD_LIST:
            os.chdir(os.path.join(self.SVNWORKPATH, b_list))
            os.system('/usr/local/ant/bin/ant -f build-ant.xml')

    def build_svn_code(self,BUILD_DIR):
        print("=" * 10 + "开始编译{0}".format(BUILD_DIR) + "=" * 10)
        os.chdir(os.path.join(self.SVNWORKPATH, BUILD_DIR))
        os.system('/usr/local/ant/bin/ant -f build-ant.xml')
        print("=" * 10 + "编译{0}完成".format(BUILD_DIR) + "=" * 10)

    def mv_build_code(self,BUILD_DIR):
        os.chdir(self.SVNWORKPATH)
        cmds_select = {
            "GameServer" : "mkdir -pv ../{1}/gameserver && cp -ra {0}/gameserver/* ../{1}/gameserver/".format(os.path.join(self.SVNWORKPATH, BUILD_DIR), self.PACKAGE_NAME),
            "AgentServer" : "mkdir -pv ../{1}/agentserver && cp -ra {0}/agentserver/* ../{1}/agentserver/".format(os.path.join(self.SVNWORKPATH, BUILD_DIR), self.PACKAGE_NAME),
            "PublicServer": "mkdir -pv ../{1}/publicserver && cp -ra {0}/publicserver/* ../{1}/publicserver/".format(os.path.join(self.SVNWORKPATH, BUILD_DIR),self.PACKAGE_NAME),
        }
        os.system(cmds_select[BUILD_DIR])

    def tar_code(self):
        print("=" * 10 + "开始打包" + "=" * 10)
        os.chdir(self.ROOTPATH) #/data/opsmanger
        if self.app_names[0] == 'TzjBackend':
            gm_dir = os.path.join(self.SVNWORKPATH,'target')
            os.chdir(gm_dir)
            tar_filename = os.path.join(gm_dir,"TzjBackend")
        else:
            tar_filename = os.path.join(self.ROOTPATH,self.PACKAGE_NAME)
        try:
            with tarfile.open('{0}.tar.gz'.format(self.PACKAGE_NAME), "w:gz") as tar:
                tar.add(tar_filename, arcname=os.path.basename(tar_filename))
            return True
        except Exception as e:
            print(e)
            return False
        print("=" * 10 + "打包完成" + "=" * 10)

    def make_same_game_package(self):
        if self.tar_code():
            os.chdir(self.ROOTPATH)
            if os.path.exists(os.path.join(self.ROOTPATH,"server.tar.gz")):
                os.remove(os.path.join(self.ROOTPATH,"server.tar.gz"))
            shutil.copy('{0}.tar.gz'.format(self.PACKAGE_NAME),)

    def make_gm(self):
        os.chdir(self.ROOTPATH)
        svn_path = self.SVNROMATEPATH
        result = check_ops(svn_path, self.zone, self.app_names)
        if result:
            self.get_svn_code()
            os.chdir(self.SVNWORKPATH)
            print("=" * 10 + "开始编译{0}".format('GM') + "=" * 10)
            os.system("/usr/local/maven/bin/mvn clean package")
            print("=" * 10 + "编译{0}完成".format("GM") + "=" * 10)
            self.tar_code()

        else:
            print("退出打包GM程序")
            sys.exit(1)

    def update(self,zone,app_names):
        svn_path = self.SVNROMATEPATH
        result = check_ops(svn_path, zone, app_names)
        if result:
            self.get_svn_code()
            os.chdir(self.SVNWORKPATH)
            # os.mkdir(self.PACKAGE_NAME)
            self.build_default_svn_code()
            for app_name in app_names:
                self.build_svn_code(app_name)
                self.mv_build_code(app_name)
            self.tar_code()

        else:
            print("即将退出打包程序....")
            time.sleep(3)
            sys.exit(1)

    def hotupdate(self,app_names):
        os.chdir(self.ROOTPATH)
        filenames = input_file_name()
        self.get_svn_code()
        for filename in filenames:
            if filename.startswith("Cfg"):
                Cfg_dir = os.path.join(self.SVNWORKPATH, 'GameCfg/script/config/')
                package_dir = os.path.join(os.path.join(self.PACKAGE_NAME, app_names[0].lower()), "script/config/")
                os.chdir(self.ROOTPATH)
                if not os.path.exists(package_dir):
                    os.makedirs(package_dir)
                os.chdir(Cfg_dir)
                src_filename_one = os.listdir('.')
                if filename in src_filename_one:
                    cmd = 'cp -ra {0} {1}'.format(filename,os.path.join(self.ROOTPATH,'{0}'.format(package_dir)))
                    os.system(cmd)
                    # shutil.copy(filename,os.path.join(self.ROOTPATH,'{0}'.format(package_dir)))
            elif filename.startswith("common"):
                Cfg_dir = os.path.join(self.SVNWORKPATH, '{0}/script/'.format(app_names[0]))
                package_dir = os.path.join(os.path.join(self.PACKAGE_NAME, app_names[0].lower()), "script/")
                if not os.path.exists(package_dir):
                    os.makedirs(package_dir)
                os.chdir(Cfg_dir)
                first_dir_name, file_name = filename.rsplit('/',1)
                base_dir = os.path.join(os.path.join(self.ROOTPATH,package_dir),first_dir_name)
                if not os.path.exists(base_dir):
                    os.makedirs(base_dir)
                shutil.copy(os.path.join(Cfg_dir,filename),base_dir)
            else:
                print("热更新打包出错,程序即将退出!!!")
                sys.exit(1)

    def rm_package_dir(self):
        if os.path.exists(self.PACKAGE_NAME):
            shutil.rmtree(self.PACKAGE_NAME)
        os.chdir(self.ROOTPATH)
        if not os.path.exists("package"):
            os.mkdir('package')
        shutil.move("{0}.tar.gz".format(self.PACKAGE_NAME),"package/")

    def run(self):
        if self.update_select == "update":
            self.make_gm() if self.app_names[0] == "TzjBackend" else self.update(self.zone,self.app_names)
        elif self.update_select == "hot":
            self.hotupdate(self.app_names)
            self.tar_code()
        else:
            print("参数错误，请检查!!!")
            sys.exit(1)
        if self.app_names[0] != 'TzjBackend':
            package = "{0}/package/{1}.tar.gz".format(self.ROOTPATH,self.PACKAGE_NAME)
            self.rm_package_dir()
            cp_package(package,self.app_names)
        else:
            gm_dir = os.path.join(self.SVNWORKPATH,'target')
            os.chdir(gm_dir)
            try:
                shutil.copy("{0}.tar.gz".format(self.PACKAGE_NAME),os.path.join(self.ROOTPATH,'package'))
            except Exception as e:
                print(e)



if __name__ == "__main__":
    make_game = Make_Game()
    make_game.run()
