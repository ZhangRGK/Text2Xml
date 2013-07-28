package com.tc.xmlmaker.core;

import java.util.ArrayList;
import java.util.List;

public class XmlNode{
	
	private List<XmlNodeProperty> properties;
	private XmlNode parent;
	private List<XmlNode> children;
	private String tagname;
	private String content;
	private int height;
	
	public static XmlNode buildXmlNode(XmlNode parent, String line){
		int curlv = parent == null? 0:parent.getHeight()+1;
		XmlNode newnode = new XmlNode(curlv);
		String[] words = line.substring(curlv).split(" "); //避免字符串开头空格的干扰
		//head
		newnode.setParent(parent);
		newnode.setTagname(words[0]);
		newnode.setProperties(newnode.buildProperties(words));
		newnode.setContent(newnode.buildContent(words));
		return newnode;
	}
	
	public XmlNode(){
		properties = new ArrayList<XmlNodeProperty>();
		parent = new XmlNode();
		children = new ArrayList<XmlNode>();
		content = null;
		height = 0;
	}
	
	public XmlNode(int height){
		properties = new ArrayList<XmlNodeProperty>();
		children = new ArrayList<XmlNode>();
		content = null;
		this.height = height;
	}
	
	//
	public String wrapTagStart(){
		StringBuffer wraped = new StringBuffer(tagname.length() + properties.size()*2);
		wraped.append("<").append(this.getTagname());
		for(XmlNodeProperty prop: this.getProperties()){
			wraped.append(" ").append(prop.getKey()).append("=")
				  .append("\"").append(prop.getValue()).append("\"");
		}
		wraped.append(">");
		return wraped.toString();
	}
	public String wrapTagEnd(){
		return "</" + this.getTagname() + ">";
	}
	//得到缩进
	public String getIndentation(){
		String indent = "";
		for(int i=0; i<this.getHeight(); i++){
			indent = indent + " ";
		}
		return indent;
	}

	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public XmlNode getParent() {
		return parent;
	}
	public void setParent(XmlNode parent) {
		this.parent = parent;
	}

	public List<XmlNodeProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<XmlNodeProperty> properties) {
		this.properties = properties;
	}
	public List<XmlNodeProperty> buildProperties(String[] words){
		List<XmlNodeProperty> prolist = new ArrayList<XmlNodeProperty>();
		for(int i=1; i<words.length; i++){
			String w = words[i];
			if(w.startsWith("$$")){
				String k = w.substring(2, w.indexOf("=")); //要求property里只有一个等号
				String v = w.substring(w.indexOf("=")+1);
				XmlNodeProperty pro = new XmlNodeProperty(k, v);
				prolist.add(pro);
			}
		}
		return prolist;
	}
	public String buildContent(String[] words){
		String content = null;
		for(int i=1; i<words.length; i++){
			String w = words[i];
			if(w.startsWith("@@")){
				content = w.substring(2);
				break;  //如果出现多个 content，只管第一个
			}
		}
		return content;
	}
	
	
	public List<XmlNode> getChildren() {
		return children;
	}

	public void setChildren(List<XmlNode> children) {
		this.children = children;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
