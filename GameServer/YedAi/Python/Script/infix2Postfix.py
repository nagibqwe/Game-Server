import compiler
import struct
import sys

class INSTR:
	OP_ADD		=0
	OP_SUB		=1
	OP_MUL		=2
	OP_DIV		=3
	OP_MOD		=4
	OP_NOT		=5
	OP_AND		=6
	OP_OR 		=7
	OP_BITAND	=8
	OP_BITOR	=9
	OP_BITXOR	=10
	OP_UADD		=11
	OP_USUB		=12	
	
	CMP_EQ		=20
	CMP_NEQ		=21
	CMP_LT		=22
	CMP_GT		=23
	CMP_LTE		=24
	CMP_GTE		=25	

	FUNC		=30
	CONST		=40
	STRING		=50
	PARAM		=60

COMPARATORS = {
	'==':	INSTR.CMP_EQ,
	'!=':	INSTR.CMP_NEQ,
	'<':	INSTR.CMP_LT,
	'>':	INSTR.CMP_GT,
	'<=':	INSTR.CMP_LTE,
	'>=':	INSTR.CMP_GTE,
}

OPERATORS = {
	compiler.ast.Add:		INSTR.OP_ADD,
	compiler.ast.Sub:		INSTR.OP_SUB,
	compiler.ast.Mul:		INSTR.OP_MUL,
	compiler.ast.Div:		INSTR.OP_DIV,
	compiler.ast.Mod:		INSTR.OP_MOD,

	compiler.ast.And:		INSTR.OP_AND,
	compiler.ast.Or:		INSTR.OP_OR,

	compiler.ast.Bitand:	INSTR.OP_BITAND,
	compiler.ast.Bitor:		INSTR.OP_BITOR,
	compiler.ast.Bitxor:	INSTR.OP_BITXOR,

	compiler.ast.Not:		INSTR.OP_NOT,
	compiler.ast.UnaryAdd:	INSTR.OP_UADD,
	compiler.ast.UnarySub:	INSTR.OP_USUB,
}

class Instruction:
	OP=0
	CMP=1
	FUNC=2
	CONST=3
	STRING=4
	PARAM=5

	def __init__(self, type):
		self.type = type

	def Info(self):
		if self.type == Instruction.OP:
			print 'op',
			print self.id
		elif self.type == Instruction.CMP:
			print 'cmp', 
			print self.id
		elif self.type == Instruction.FUNC:
			print '%s(%d)' %(self.func, self.argc)
		elif self.type == Instruction.PARAM:
			print 'param ' + self.param
		elif self.type == Instruction.CONST:
			print 'const ',
			print self.const
		elif self.type == Instruction.STRING:
			print 'string '+self.string
	
	def WriteStr(self, data, string):
		strlen = len(string) + 1
		toalign = 4 - (strlen & 3)	

		data.append(struct.pack('i', strlen + toalign))
		data.append(string)
		data.append(struct.pack('b', 0))
			
		for i in range(toalign):
			data.append(struct.pack('b', 0))
				
	def Export(self, data):
		data.append(struct.pack('i', self.id))

		if self.type == Instruction.FUNC:
			data.append(struct.pack('i', 0))
			data.append(struct.pack('i', self.argc))
			self.WriteStr(data, self.func)
			
		elif self.type == Instruction.CONST:
			data.append(struct.pack('f', float(self.const)))
		elif self.type == Instruction.PARAM:
			self.WriteStr(data, self.param)
		elif self.type == Instruction.STRING:
			self.WriteStr(data, self.string)
		else :
                        pass
                        #print self
			
	@staticmethod
	def NewOp(op):
		i = Instruction(Instruction.OP)
		i.id = OPERATORS[op]
		return i
		
	@staticmethod
	def NewCmp(cmp):
		i = Instruction(Instruction.CMP)
		i.id = COMPARATORS[cmp]
		return i

	@staticmethod
	def NewFunc(func, argc):
		i = Instruction(Instruction.FUNC)
		i.id = INSTR.FUNC
		i.func = func
		i.argc = argc
		return i

	@staticmethod
	def NewConst(const):
		i = Instruction(Instruction.CONST)
		i.id = INSTR.CONST
		i.const = const
		return i
	
	@staticmethod	
	def NewString(string):
		i = Instruction(Instruction.STRING)
		i.id = INSTR.STRING
		i.string = string
		return i

	@staticmethod	
	def NewParam(param):
		i = Instruction(Instruction.PARAM)
		i.id = INSTR.PARAM
		i.param = param
		return i

def AddInstr(node, instructions):

	instr = None

	#print node.__class__,
	
	if isinstance(node, compiler.ast.Compare):
		instr = Instruction.NewCmp(node.ops[0][0])
		#print ''
	elif node.__class__ in OPERATORS:
		#print node.nodes
		#print len(node.nodes)
		
		instr = Instruction.NewOp(node.__class__)

		if node.__class__ == compiler.ast.And or node.__class__ == compiler.ast.Or:
			#print '1'
			for i in range(0, len(node.nodes)-2):
				instructions.append(instr)

		#print ''
	elif isinstance(node, compiler.ast.Name):
		#print node.name
		if node.name.lower() == 'true':
                        #print node.name
                        instr = Instruction.NewConst(1)
                elif node.name.lower() == 'false':
                        #print node.name
                        instr = Instruction.NewConst(0)
                elif node.name.startswith('cond_'):
                        instr = Instruction.NewFunc(node.node.name[5:], 0)
                else:
                        pass 
	elif isinstance(node, compiler.ast.CallFunc):

		#print ''
		#print node.asList
		#print len(node.args)
		#print node.args

		instr = Instruction.NewFunc(node.node.name, len(node.args))
	elif isinstance(node, compiler.ast.Keyword):
		#print node
		instr = Instruction.NewParam(node.name)
	elif isinstance(node, compiler.ast.Const):
		#print '\t' + str(node.value)
		if isinstance(node.value, str):
			instr = Instruction.NewString(node.value)
		elif isinstance(node.value, float) or isinstance(node.value, int) or isinstance(node.value, long):
			instr = Instruction.NewConst(node.value)
	else:
                #print node
		pass 
		#print ''
	
	if instr is not None:
		instructions.append(instr)
        #print node
	for n in node.getChildNodes():
		AddInstr(n, instructions)

def Compile(expression):
	#print expression
	astModule = compiler.parse(expression)
	#print astModule
	instructions = []
	AddInstr(astModule.node, instructions)
	
	instructions.reverse()
	
	compiled = []
	
	#print 'exp: ' + expression
	
	#print len(instructions)
	
	for i in instructions:
		#i.Info()
		i.Export(compiled)
		
	return compiled
		

if __name__=='__main__':

	if len(sys.argv) == 1:
		expression = 'RunScript(\'blabla.blabla\', 0, 3 < 1)'
	else:
		expression = sys.argv[1]
		
	#expression = 'def rand(a, b, c):\n\treturn max-min\n' + expression
	
	print expression
	
	compiled = Compile(expression)


