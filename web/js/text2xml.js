var Text2Xml = {
	parseline: function(line){
		var lineresult = "";
		//cal whitespace
		for (var i=0; i < line.length; i++) {
		  if(line[i] === ' '){
		  	lineresult = lineresult + " ";
		  }else{
		  	break;
		  }
		};
		//parse name,props,val
		var els = line.split(" ");
		var name = "", props = new Array(), val = "";
		for (var i=0; i < els.length; i++) {
		   var el = els[i];
		   if(el !== ""){
			 if(el.indexOf("$$") >= 0){ //props
			 	props.push(el.substring(2));
			 }else if(el.indexOf("@@") >= 0){ //val
		 		val = el.substring(2);
			 }else{ //name
			 	name = el;
			 }
		   }
		};
		//build node
		var xnode = "<"+name;
		for (var i=0; i < props.length; i++) {
		  var prop = props[i];
		  xnode += " ";
		  xnode += prop.split("=")[0];
		  xnode += "=\"";
		  xnode += prop.split("=")[1];
		  xnode += "\"";
		};
		xnode += ">";
		xnode += val;
		if(val!=""){
			xnode += "</"+name+">";
		}
		xnode += "\n";
		lineresult += xnode;
		return lineresult;
	},
	parse: function(text){
		var result = "";
		var lines = text.split("\n");
		for (var i=0; i < lines.length; i++) {
		  var line = lines[i];
		  result += Text2Xml.parseline(line);
		};
		//用正则补全标记
		var closetags = new Array(); //whitespacenum_tagname, e.x. 0_books,1_book  //看contans "/" 就知道这行有没有闭合 //记录last whitespacenum， 如果 cur >= last continue  , cur < last, 补一个关闭标签+"\n"
		
		return result;
	}
}
