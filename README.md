Text2Xml
========

a Text2Xml tool. danm the fuxking gt and lt and close tag.

It convert plain text to xml. 

for example:

text is

    books $$local=china  
     book  
      name @@boookkname1  
     author auttttor1  
      book  
     name @@bokkkname2  
      author aaaautor2  
  
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

#####TODO
- 用树的结构实现 √
- 用js重写
