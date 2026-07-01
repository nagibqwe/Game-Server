import sys
import os
import shutil
import filecmp

import utils

def DelDirTree(dirName):
	for shortname in os.listdir(dirName):
		fullentryname = os.path.join(dirName, shortname)
		if os.path.isdir(fullentryname):
			DelDirTree(dirName)
		else:
			os.remove(fullentryname)
	os.rmdir(dirName)

def CleanEntries(root_tgt, root_src):
	for shortname in os.listdir(root_tgt):
		if(shortname.find("dress") != -1):
			aaa = 0
		fullentryname = os.path.join(root_tgt, shortname)
		fullsrcname = os.path.join(root_src, shortname)
		if os.path.isdir(fullentryname):
			if os.path.abspath(fullentryname).find(".svn") != -1:
				continue
			if  os.path.isdir(fullsrcname):
				CleanEntries(fullentryname, fullsrcname)
			else:
				cmd = 'rd /s /q '+'"%s"'%fullentryname
				print cmd
				g_delDir.append(cmd)
				
		else:
			if not os.path.isfile(fullsrcname):
				cmd = 'del /q '+'"%s"'%fullentryname
				print cmd
				g_delFile.append(cmd)
	
			

def EnsurePath(fullname):
	if os.path.isdir(fullname):
		return

	idx = fullname.rfind('\\')
	if idx < 0:
		return
			
	path = fullname[0:idx]
	
	if os.path.isdir(path):
		return
	
	try:
		os.mkdir(path)
	except Exception, e:
		EnsurePath(path)
		os.mkdir(path)



def main(argv):
	if len(argv) != 3: 
		print 'usage: %s <tgt_dir> <src_dir>' %(argv[0])
		return 1
	global g_delDir, g_delFile
	g_delDir=[]
	g_delFile=[]
	CleanEntries(argv[1], argv[2])
	for cmd in g_delDir:
		os.system(cmd)

	for cmd in g_delFile:
		os.system(cmd)
		
		
	

if __name__=='__main__':
	sys.exit(main(sys.argv))
