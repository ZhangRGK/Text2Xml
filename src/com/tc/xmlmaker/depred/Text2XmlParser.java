package com.tc.xmlmaker.depred;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Text2XmlParser {

	public static int HasValue = 1;
	public static int NoValue = 0;
	
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
		StringBuffer result = new StringBuffer(text.length()*2); //乘以多少可以再看
		lines = text.split("\n");
		
		//初始化每行的lv
		linelvs = new int[lines.length+1]; //+1是为了应对最后一行
		int i=0;
		for( ;i<lines.length; i++){
			linelvs[i] = getLineLevel(lines[i]);
		}
		linelvs[i] = -1;
		
		//逐行解析
		while(lineindex < lines.length){
			parseLine(result);
			lineindex ++;
		}
		return result.toString();
	}
	
	public void parseLine(StringBuffer result){
		String line = lines[lineindex];
		int curlv = linelvs[lineindex];
		int nextlv = linelvs[lineindex+1];
		//
		String[] words = line.substring(curlv).split(" "); //避免字符串开头空格的干扰
		//head
		String tagname = words[0];
		//middle
		List<Map<String, String>> properties = getProperties(words);
		String content = getContent(words);
		//
		result.append(getIndentation(curlv)); //适当的退格
		result.append(wrapTagStart(tagname, properties));
		//有嵌套的情况 这种情况没有content
		if(nextlv > curlv){
			//继续处理下一行
			result.append("\n");
			lineindex++;
			parseLine(result);
			result.append(getIndentation(curlv)); //适当的退格
		}else{ //无递归 有内容则输出
			if(content != null){
				result.append(content);
			}
		}
		result.append(wrapTagEnd(tagname));
		result.append("\n");
		//有平级的情况
		nextlv = linelvs[lineindex+1];
		if(nextlv == curlv){
			lineindex++;
			parseLine(result);
		}
	}
	
	
	public String wrapTagStart(String tagname, List<Map<String, String>> properties){
		StringBuffer wraped = new StringBuffer(tagname.length() + properties.size()*2);
		wraped.append("<").append(tagname);
		for(Map<String, String> map: properties){
			wraped.append(" ").append(map.get("key")).append("=")
				  .append("\"").append(map.get("value")).append("\"");
		}
		wraped.append(">");
		return wraped.toString();
	}
	public String wrapTagEnd(String tagname){
		return "</" + tagname + ">";
	}
	
	//得到缩进
	public String getIndentation(int lv){
		String indent = "";
		for(int i=0; i<lv; i++){
			indent = indent + " ";
		}
		return indent;
	}
	
	public String getContent(String[] words){
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
	
	public List<Map<String, String>> getProperties(String[] words){
		List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
		for(int i=1; i<words.length; i++){
			String w = words[i];
			if(w.startsWith("$$")){
				String k = w.substring(2, w.indexOf("=")); //要求property里只有一个等号
				String v = w.substring(w.indexOf("=")+1);
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", k);
				map.put("value", v);
				listmap.add(map);
			}
		}
		return listmap;
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
