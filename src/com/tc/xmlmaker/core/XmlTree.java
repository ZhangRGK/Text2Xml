package com.tc.xmlmaker.core;


public class XmlTree {

	private XmlNode root;

	public XmlNode getRoot() {
		return root;
	}

	public void setRoot(XmlNode root) {
		this.root = root;
	}

	public XmlTree(){
		root = null;
	}
	
	public XmlNode insert(XmlNode parent, XmlNode child){
		if(root == null){
			root = child;
		} else if(parent == null){
			//TODO should throw a exception
		} else {
			child.setParent(parent);
			parent.getChildren().add(child);
		}
		return child;
	}
	
	//遍历
	public void traverse(XmlNode node, IHandleXmlNode handler){
		handler.handle(node);
	}
}
