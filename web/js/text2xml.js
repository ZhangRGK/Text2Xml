var Text2Xml = {
	gettagname: function(l){
		var tagname = "";
		var ltrim = $.trim(l);
		if(ltrim.indexOf(" ") > -1 ){
			tagname = ltrim.substring(1, ltrim.indexOf(" "));
		}else{
			tagname = ltrim.substring(1, ltrim.indexOf(">"));
		}
		return tagname
	},
	gettaglv: function(l){
		var lv = 0;
		for (var i=0; i < l.length; i++) {
		  if(" "===l[i]){ lv++; }
		  else{ break; }
		};
		return lv;
	},
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
		//去掉最后一个换行
		result = result.substring(0, result.length-1);
		//用正则补全标记
		var closetags = new Array(); //whitespacenum_tagname, e.x. 0_books,1_book  //看contans "/" 就知道这行有没有闭合 //记录last whitespacenum， 如果 cur >= last continue  , cur < last, 补一个关闭标签+"\n"
		var eachline = result.split("\n");
		var finalresult = "";
		for (var j=0; j < eachline.length; j++) {
		  var l = eachline[j];
		  //处理同级标签的闭合问题
		  var lllv = Text2Xml.gettaglv(l);
		  var lastcloselv = closetags.length>0? closetags[closetags.length-1].split("_")[0] : undefined ;
		  if( lastcloselv !== undefined && lllv == lastcloselv){
		  	var close = closetags.pop();
			var llv = close.split("_")[0];
			var tag = close.split("_")[1];
			if(llv > 0){
				for (var k=0; k < llv; k++) {
				  finalresult += " ";
				};
			}
			finalresult += (tag + "\n") ;
		  }
		  finalresult += (l + "\n");
		  if(l.indexOf("/")>-1){ continue; }
		  var ltagname = Text2Xml.gettagname(l);
		  var llv = Text2Xml.gettaglv(l);
		  closetags.push(llv+"_</"+ltagname+">");
		};
		while(closetags.length>0){
			var close = closetags.pop();
			var llv = close.split("_")[0];
			var tag = close.split("_")[1];
			if(llv > 0){
				for (var t=0; t < llv; t++) {
				  finalresult += " ";
				};
			}
			finalresult += (tag + "\n") ;
		}
		return finalresult;
	}
}
