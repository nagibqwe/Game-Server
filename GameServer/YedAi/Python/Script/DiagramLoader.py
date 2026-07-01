import sys
import os
import re
import xml.dom.minidom
import utils
import utils_xml

import infix2Postfix

class AutomatObject:
	def __init__(self, diaFile, node):
		self.diaFile = diaFile
		self.node = node

class AutomatState(AutomatObject):
	def __init__(self, diaFile, node):
		AutomatObject.__init__(self, diaFile, node)
		
		self.id = self.diaFile.RegisterId(node.getAttribute('id'), self)
		
		self.isGroup = False
		if node.getAttribute('yfiles.foldertype') == 'group':
			self.isGroup = True

		shapeNode = utils_xml.GetFirstElement(node, 'y:Shape')
		
		self.isEllipse = False
		self.isDiamond = False

		if shapeNode:
			if shapeNode.getAttribute('type') == 'ellipse':
				self.isEllipse = True
			elif shapeNode.getAttribute('type') == 'diamond':
				self.isDiamond = True

		dataNode = utils_xml.GetFirstElement(node, 'data')
		iiiii = 1
                while dataNode != None and utils_xml.HasChild(dataNode) == False:
                        dataNode = utils_xml.GetNextElement(node, 'data',iiiii)
                        iiiii = iiiii + 1
		
		key = dataNode.getAttribute('key')
		
		self.transitions = []
		self.instructions = []
		self.name = None
		
		if self.isGroup:
			pass
		else:
			noteNode = utils_xml.GetFirstElement(dataNode, 'y:UMLNoteNode')
			if noteNode is None:
				noteNode = utils_xml.GetFirstElement(dataNode, 'y:ShapeNode')
			if noteNode is None:
				pass
			else:
				#print dataNode
				labelNode = utils_xml.GetFirstElement(noteNode, 'y:NodeLabel')
				lines = utils_xml.GetText(labelNode).split('\n')
			
				if len(lines) > 0 and lines[0].startswith('##comment'):
					pass
				else:
					for l in lines:
						if l.find('(') > 0: #is instruction
							self.instructions.append(l)
						elif self.name == None:
							self.name = l
		
		if not self.name:
			geomNode = utils_xml.GetFirstElement(dataNode, 'y:Geometry')
			if geomNode is None:
                                geomNode = utils_xml.GetFirstElement(dataNode, 'y:ShapeNode')
                                if geomNode :
                                        geomNode = utils_xml.GetFirstElement(geomNode, 'y:Geometry')
			if geomNode:
				x = geomNode.getAttribute('x')
				y = geomNode.getAttribute('y')
				
				instr = ''
					
				if len(self.instructions) > 0:
					instr = self.instructions[0]
					
				self.name = 'unnamed_%d_%s_%s_%s' %(self.id, instr, x, y)

	def Resolve(self):
		pass
	
	@staticmethod
	def SortTransition(a, b):
                if a.priority == 0 :
                        return 1
		elif a.priority < b.priority:
			return -1
		elif a.priority > b.priority:
			return 1
		else: # a.priority == b.priority:
			return 0

	def Export(self, s):
		s.writestralign(self.name)
		
		if self.isEllipse:
			s.write4u(1)
		elif self.isDiamond:
			s.write4u(2)
		else:
			s.write4u(0)

		s.write4u(len(self.instructions))
		
		for i in self.instructions:
			compiled = infix2Postfix.Compile(i)
			totalLen = 0
			for instr in compiled:
				totalLen += len(instr)

			s.write4u(totalLen)
			for instr in compiled:
				s.writeraw(instr)
				
			if DEBUG:
				s.writestralign(i)
			else:
				s.writestralign("")
				
		s.write4u(len(self.transitions))
		
		self.transitions.sort(AutomatState.SortTransition)
		#print 'transitions'
		for t in self.transitions:
			#print t.priority
			t.Export(s)
			
	def AddTransition(self, transition):
		self.transitions.append(transition)

class DiaMetaState(AutomatState):
	def __init__(self, diaFile, node):
		AutomatState.__init__(self, diaFile, node)
		self.subStates = []

	def Resolve(self):
		pass

	def AddTransition(self, transition):
		self.transitions.append(transition)

class AutomatTransition(AutomatObject):
	def __init__(self, diaFile, node):
		AutomatObject.__init__(self, diaFile, node)

		self.source = node.getAttribute('source')
		self.target = node.getAttribute('target')

		dataNode 		= utils_xml.GetFirstElement(node, 'data')
		
		allEdgeLabel = node.getElementsByTagName('y:EdgeLabel')
		
		#print dataNode
		
		edgeLabelNode = None
		
		if len(allEdgeLabel) > 1:
			print '*' * 80
			idx = 0
			for l in allEdgeLabel:
				print 'label %d -> %s' %(idx, utils_xml.GetText(l))
				idx += 1
				
			print '*' * 80
			
			#raise Exception('no more than one label per edge')
			
			edgeLabelNode	= allEdgeLabel[0] # utils_xml.GetFirstElement(dataNode, 'y:EdgeLabel')
			
		elif len(allEdgeLabel) == 1:
			edgeLabelNode	= allEdgeLabel[0]
		
		#if edgeLabelNode == None:
		#	print 'None'
		#else:
		#	print self.condition
		self.condition = utils_xml.GetText(edgeLabelNode)		

		self.priority = 0

		if self.condition:
			#strip comments
			condition = ''
			for l in self.condition.split('\n'):	
				if not l.strip().startswith('#'):
					condition += l + ' '
				
			self.condition = condition #self.condition.replace('\n', ' ')
			
			if self.condition.find('[') >= 0:

				g = re.match('\[([+-]?[0-9]+)\](.*)', self.condition)
				
				if g:
					self.priority = int(g.group(1))
					self.condition = g.group(2).strip()

		if self.condition:
			self.condition = self.condition.strip()
			if self.condition == '':
                                self.condition = '1'
		else:
			self.condition = '1'
			
		#print '*'+self.condition
			
		#print self.priority
		#print self.condition

	def Export(self, s):
		s.write4u(self.toObject.id)

		compiled = infix2Postfix.Compile(self.condition)
		totalLen = 0
		
		for c in compiled:
			totalLen += len(c)

		#s.write4u(len(compiled))
		s.write4u(totalLen)

		for c in compiled:
			s.writeraw(c)
			
		if DEBUG:
			s.writestralign(self.condition)
		else:
			s.writestralign("")
			
	def Resolve(self):
		self.fromObject	= self.diaFile.GetObjectFromId(self.source)
		self.toObject	= self.diaFile.GetObjectFromId(self.target)

		self.fromObject.AddTransition(self)

class DiaFile:
	def __init__(self, filename):
		self.filename = filename
		self.dom = xml.dom.minidom.parse(filename)
		self.root = self.dom.documentElement
		
		self.states 	= []
		self.metastates = []
		self.transitions= []

		self.idToObject	= {}
		
		graph = utils_xml.GetFirstElement(self.root, 'graph')
		
		for node in utils_xml.GetChildElementNodes(graph):
			self.ParseObject(node)

		self.ResolveAll()

	def RegisterId(self, id, object):
		self.idToObject[id] = object
		return len(self.idToObject.keys()) - 1

	def ResolveAll(self):
		for t in self.transitions:
			t.Resolve()
		for s in self.states:
			s.Resolve()

	def GetObjectFromId(self, id):
		return self.idToObject.get(id, None)

	def ParseObject(self, node):
		type = node.nodeName

		if type == 'node':
			if node.getAttribute('yfiles.foldertype') == 'group':
				print 'groups are not supported yet'
			else:
				ret = AutomatState(self, node)
				if ret.name is None:
					pass
				else:
					self.states.append(ret)
		elif type == 'edge':
			self.transitions.append(AutomatTransition(self, node))

			
	def Export(self, file):
		s = utils.Stream()
		
		s.write4u(0) 			#version
		s.write4u(0x0000fffe)	#checkEndian
		fname, fextension=os.path.splitext(os.path.basename(self.filename))
		s.writestralign(fname)
 		#s.writestralign(os.path.basename(self.filename))
		
		s.write4u(len(self.states))
		
		for state in self.states:
			state.Export(s)

		s.WriteBinary(file)

def main(argv):
	infile = argv[1]
	outfile= argv[2]
	
	global DEBUG
	if len(argv) > 3 and argv[3] == 'debug':
		DEBUG = 1
	else:
		DEBUG = 0
	DEBUG = 1 #temp
	DiaFile(infile).Export(outfile)
	

if __name__ == "__main__":
	sys.exit(main(sys.argv))
