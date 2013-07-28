package com.tc.xmlmaker.core;


//用树的数据结构来重写
public class Text2XmlParser {

	
	private int lineindex=0;
	private String text;
	private String[] lines;
	private int[] linelvs;

	private Text2XmlParser(){
		lineindex = 0;
	}
	
	public static Text2XmlParser getInstance(String text){
		Text2XmlParser parser = new Text2XmlParser();
		parser.setText(text);
		return parser;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	//核心方法，完成从文本到xml格式的转化
	public String parse(){
		final StringBuffer result = new StringBuffer(text.length()*2); //乘以多少可以再看
		XmlTree xmltree = new XmlTree();
		
		lines = text.split("\n");
		
		//初始化每行的lv
		linelvs = new int[lines.length+1]; //+1是为了应对最后一行
		int i=0;
		for( ;i<lines.length; i++){
			linelvs[i] = getLineLevel(lines[i]);
		}
		linelvs[i] = -1;
		
		//1. 插出一棵树
		XmlNode parent = null;
		for(i=0; i<lines.length; i++){
			String line = lines[i];
			int curlv = linelvs[i];
			if(parent!=null){
				int parentlv = parent.getHeight();
				if(curlv == parentlv){
					parent = parent.getParent();
				}
				if(curlv < parentlv){
					//上溯到正确的树高度
					while(curlv - parentlv != 1){
						parent = parent.getParent();
						parentlv = parent.getHeight();
					}
				}
			}
			XmlNode newnode = XmlNode.buildXmlNode(parent, line); //只buildpro和content
			parent = xmltree.insert(parent, newnode);
		}
		//2. 遍历一棵树
		xmltree.traverse(xmltree.getRoot(), new IHandleXmlNode() {
			@Override
			public void handle(XmlNode node) {
				//输出head
				result.append(node.getIndentation()); //适当的退格
				result.append(node.wrapTagStart());
				//输出children
				if(node.getContent()!=null){
					result.append(node.getContent());
				}else{
					result.append("\n");
					for(XmlNode child: node.getChildren()){
						handle(child);
					}
					result.append(node.getIndentation()); //适当的退格
				}
				//输出tail
				result.append(node.wrapTagEnd());
				result.append("\n");
			}
		});
		return result.toString();
	}
	
	//return 从字符串第一个字符开始 连续为空格的数量
	public int getLineLevel(String line){
		int lv = 0;
		for(int i=0; i<line.length(); i++){
			char c = line.charAt(i);
			if(Character.isWhitespace(c)){
				lv = lv +1;
			}else{
				break;
			}
		}
		return lv;
	}
	
	//简单单元测试
	public static void main(String[] args) {
		System.out.println(Text2XmlParser.getInstance("books $$local=china $$version=1.1\n book\n  name @@boookkname1\n  author @@auttttor1\n book\n  name @@bokkkname2\n  author @@aaaautor2").parse());
	}

}
