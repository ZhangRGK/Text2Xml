Text2Xml
========

a Text2Xml tool. danm the fuxking gt and lt and close tag.

It convert plain text to xml. 

for example:

text is

books $$local=china  
&nbsp;book  
&nbsp;&nbsp;name @@boookkname1  
&nbsp;&nbsp;author auttttor1  
&nbsp;book  
&nbsp;&nbsp;name @@bokkkname2  
&nbsp;&nbsp;author aaaautor2  
  
then xml will be


<books local="china">
  <book>
    <name>boookkname1</name>
    <author>auttttor1</author>
  </book>
  <book>
    <name>bokkkname2</name>
    <author>aaaautor2</author>
  </book>
</books>

is it simple?
