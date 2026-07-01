#!/usr/bin/env python

import paramiko

  
def sshclient_execmd(hostname, port, username, password, execmd):  
    paramiko.util.log_to_file("paramiko.log")  
      
    s = paramiko.SSHClient()  
    s.set_missing_host_key_policy(paramiko.AutoAddPolicy())  
      
    s.connect(hostname=hostname, port=port, username=username, password=password)  
    stdin, stdout, stderr = s.exec_command (execmd)  
    stdin.write("Y")  # Generally speaking, the first connection, need a simple interaction.  
    print(stdout.read())
    s.close()  
      
      
      
def main(cmd):  
      
    hostname = '10.0.0.160'  
    port = 22  
    username = 'root'  
    password = 'jjker1314'  
    execmd =  cmd  
      
    sshclient_execmd(hostname, port, username, password, execmd)  
      
      
if __name__ == "__main__":  
    import sys
    build_name = sys.argv[1] 
    #step1: 压缩文件
    cmd = 'cd /var/lib/jenkins/workspace/{0}/GameServer && tar zcvf gameserver.tar.gz gameserver'.format(build_name)
    main(cmd)
    #step2: 传输文件
    cmd = 'cd /var/lib/jenkins/workspace/{0}/GameServer && scp gameserver.tar.gz root@10.0.0.59:/data/'
    main(cmd)  
