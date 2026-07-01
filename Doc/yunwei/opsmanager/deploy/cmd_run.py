import  paramiko

class Sshcmd():
    def __init__(self):
        self.ssh = paramiko.SSHClient()
        self.ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    def connect(self,hostname,port,username,password):
        self.ssh.connect(hostname=hostname,port=port,username=username,password=password)

    def cmd_run(self,cmd):
        stdin,stdout,stderr = self.ssh.exec_command(cmd)
        print(11111111111)
        return stdin,stdout,stderr
    def colse(self):
        self.ssh.close()


# ssh = paramiko.SSHClient()
#
# ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
# # 连接服务器
# ssh.connect(hostname='10.0.0.59', port=22, username='root', password='jjker1314')
# # 执行命令
# stdin, stdout, stderr = ssh.exec_command('')
# # 获取命令结果
# result = stdout.read()
# # 关闭连接
# ssh.close()