package xmlparsing;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import constant.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import constant.Constants;



/**
 * Created by jiangjingping on 2018/7/20.
 * 解析业务定制规则的xml
 */
public class parserXml {

    //创建字段存储信息列表
    List<elementstruct> elementinformation = new ArrayList<elementstruct>();
    //创建一个类id和类里面元素具体信息的hashMap
    Map <Integer, ArrayList> elementinfomap = new HashMap <Integer, ArrayList>();

    //创建一个类id和类内容对应的hashMap
    Map<Integer, classstruct> classinfomap = new HashMap<Integer,classstruct>();


    // 1.创建一个DocumentBuiderFactory的对象
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    //2.创建一个DocumentBuilder的对象
    try{
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse("  ");

        NodeList classnameList = document.getElementsByTagName(Constants.ClASS_NAME);
        for(int i = 0; i< classnameList.getLength(); i++){
            Node classiteam = classnameList.item(i);
            //获取class节点的所有属性集合
            NamedNodeMap attrs = classiteam.getAttributes();
            //遍历class的属性
            classstruct classinformation = new classstruct();
            for(int j = 0; j < attrs.getLength(); j++){
                //通过item(index)方法获取book节点的某一个属性
                Node attr = attrs.item(j);
                //获取属性名
                System.out.print("节点的名称" + attr.getNodeName());
                //获取属性值
                System.out.println("节点的值" + attr.getNodeValue());

                //获取classname
                if (attr.getNodeName().equals(Constants.ClASS_NAME)) {
                    classinformation.setClassname(attr.getNodeValue());
                }

                //获取classid
                else if(attr.getNodeName().equals(Constants.CLASS_ID)) {
                    classinformation.setClassid(Integer.valueOf(attr.getNodeValue()));
                }

                //获取elem_num
                else if(attr.getNodeName().equals(Constants.ELEMENT_NUM)) {
                    classinformation.setElementnum(Integer.valueOf(attr.getNodeValue()));
                }

            }
            classinfomap.put(classinformation.getClassid(), classinformation);

            //解析classname节点的子节点
            NodeList childNodes = classiteam.getChildNodes();
            for()
            //获取class xml 子节点element的值

            //获取class xml 子节点rule的值




            //解析classname下面的子节点



        }


    }catch(){

    }


}


