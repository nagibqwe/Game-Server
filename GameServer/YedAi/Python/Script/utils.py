import os
import struct
import filecmp
import glob 

EOL='\n'

def mkdirs(dirname):	
	if not os.path.exists(dirname):
		os.makedirs(dirname)
		
def align4(val):
	val = (val + 3) & ~3
	return val

class Stream:
	INDENT = '\t'

	def __init__(self, eol=EOL, endian='little'):
		self.tokens = []
		self.indent=''
		if endian == 'little':
			self.endian = '<'
		else:
			self.endian = '>'
		self.eol = eol
		
		self.strings = []
		
	def DumpToStream(self, stream):
		for token in self.tokens:
			stream.write(token)
		
	def IncIndent(self):
		self.indent += Stream.INDENT

	def DecIndent(self):
		if len(self.indent) > 0:
   			self.indent = self.indent[1:]

	def writeraw(self, text):
		self.tokens.append(text)
        
	def write(self, text):
		if len(self.indent) > 0:
		   self.tokens.append(self.indent)

		self.tokens.append(text)

	def writeln(self, text=''):
		if len(self.indent) > 0:  
			self.tokens.append(self.indent)
		self.tokens.append(text)
		self.tokens.append(EOL)
		
	def write1(self, val):
		if val < -128:
			val = -128
		if val > 127:
			val = 127
		self.tokens.append(struct.pack(self.endian+'b', val))

	def write1u(self, val):
		if val < 0:
			val = 0
		elif val > 255:
			val = 255
		self.tokens.append(struct.pack(self.endian+'B', val))

	def write2(self, val):
		self.tokens.append(struct.pack(self.endian+'h', val))

	def write2u(self, val):
		if val < 0:
			self.write2(val)
		else:
			self.tokens.append(struct.pack(self.endian+'H', val))
	
	def write4(self, val):
		self.tokens.append(struct.pack(self.endian+'i', val))

	def write4u(self, val):
		if val < 0:
			self.write4(val)
		else:
			self.tokens.append(struct.pack(self.endian+'I', val))
		
	def writestr2(self, string):
		self.write4u(len(self.strings))
		self.strings.append((string, len(self.tokens)-1))

	def resolvestrings(self):
		totalLength = 0

		for t in self.tokens:
			totalLength += len(t)
		
		toalign = align4(totalLength) - totalLength
		
		for i in range(toalign):
			self.write1u(0)
			
		totalLength += toalign

		offset = totalLength
		for string,idx in self.strings:
			self.tokens[idx] = struct.pack(self.endian+'i', offset + 4)
			strlen = len(string)
			self.write4u(strlen)
			self.writeraw(string)
	
			toalign = 4 - (strlen & 3)
			
			for i in range(toalign):
				self.write1u(0)
			
			offset += 4 + strlen + toalign

	def writestralign(self, string):
		length = len(string)+1
		toalign = 4 - (length & 3)	
		
		self.write4u(length + toalign)
		self.writeraw(string)
		self.write1u(0)
		
		for i in range(toalign): 
			self.write1u(0)
			
	def writestr(self, string):
		length = len(string)
		self.write4u(length + 1)
		self.writeraw(string)
		self.write1u(0)
		#toalign = 4 - (length & 3)	
		#for i in range(toalign): self.write1u(0)

	def WriteBinary(self, dst):
		f = open(dst, 'wb')
		for t in self.tokens:
			f.write(t)
		f.close()

def WriteIfDifferent(realFilename, data, eol=EOL):
	tmpFilename = realFilename + '.tmp'

	if isinstance(data, list):
		f=open(tmpFilename, 'w')
		for l in data:
			f.write(l+eol)
		f.close()
	elif isinstance(data, str):
		f=open(tmpFilename, 'w')
		f.write(data)
		f.close()
	elif isinstance(data, Stream):
		f=open(tmpFilename, 'w')
		for t in data.tokens:
			f.write(t)
		f.close()
	else:
		raise Exception('unknown data type')

	if os.path.isfile(realFilename):
		if filecmp.cmp(tmpFilename, realFilename): #they are the same
			os.remove(tmpFilename)
			return False
		else:
			os.remove(realFilename)
			os.rename(tmpFilename, realFilename)
	else:
		os.rename(tmpFilename, realFilename)

	return True


def MakeStringProgFriendly(s):
	string = ''
	if s[0].isdigit() or s[0]=='-' or s[0] == '+':
		string += '_'
	else:
		string += s[0]
	for c in s[1:]:
		if not c.isalnum():
			string += '_'
		else:
			string += c
	return string
	
def FindMaxLength(strings):
	maxLen = 0
	for s in strings:
		l = len(s) 
		if l > maxLen:
			maxLen = l
			
	return maxLen
	
def GeneratePythonIncludeAllFile( input_directory, out_filename, header_prefix, file_type):
	
	CPP_TYPE=".h" 
	PY_TYPE=".py"
	if not (file_type == CPP_TYPE or file_type == PY_TYPE):
		print "error GeneratePythonIncludeAllFile need " + CPP_TYPE + " or " + PY_TYPE + " file_type, given " + str(file_type)
		return 
	
	if not os.path.isdir( input_directory ):
		print "error : " + input_directory + " is not a directory"
		return
				
	(dir, ppName) = os.path.split(out_filename)					
	ppName = ppName.upper()
	ppName = ppName.replace(".", "_")
	
	header	= Stream()
			
	#print " input_directory " + input_directory
	search_value = input_directory + "/" + header_prefix + "*" + file_type
	#print "search value " + search_value
	filenames = glob.glob(search_value)
	for pathname in filenames:		
		(dirname, filename) = os.path.split(pathname)
		if file_type == CPP_TYPE:
			header.writeln("#include \"" + filename + "\"")		
		elif file_type == PY_TYPE:
			filename = filename[0: len(filename)- len(PY_TYPE)]	#substring without the .py extansion.
			header.writeln("from " + filename + " import *")

	WriteIfDifferent(out_filename, header)
