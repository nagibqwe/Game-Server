import xml.dom.minidom

def GetText(node):
	if node:
		for n in node.childNodes:
			if n.nodeType == xml.dom.Node.TEXT_NODE:
				return n.nodeValue
			
	return ''

def GetFirstElement(root, nodeName):
	n = root.getElementsByTagName(nodeName)
	if len(n) > 0:
		return n[0]
	else:
		return None

def GetNextElement(root, nodeName,i):
	n = root.getElementsByTagName(nodeName)
	if len(n) > i:
		return n[i]
	else:
		return None

def HasChild(node):
	if node:
		if len(node.childNodes) > 0:
                        return True
			
	return False


def GetChildElementNodes(root, name=None) :

	for node in root.childNodes :
		if node.nodeType != xml.dom.Node.ELEMENT_NODE :
			continue
		if name is None or name == node.nodeName:
			yield node
