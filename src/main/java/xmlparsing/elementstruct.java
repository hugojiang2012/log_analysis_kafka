package xmlparsing;

/**
 * Created by jiangjingping on 2018/7/20.
 * 元素的结构定义
 */
public class elementstruct {
    private String elementname;
    private String elementtype;
    private int elementid;
    private int elementlength;

    //初始化字段表
    public  void element_tbl_initialize(String elementname,String elementtype, int elementid, int elementlength ){
        this.elementname = elementname;
        this.elementtype = elementtype;
        this.elementid = elementid;
        this.elementlength = elementlength;

    }
    //获取字段名
    public String getElementname(){
        return elementname;
    }
    public void setElementname(String elementname){
        this.elementname = elementname;
    }

    //获取字段Id
    public int getElementid(){
        return  elementid;
    }
    public void setElementid(int elementid){
        this.elementid = elementid;
    }

    //获取字段长度
    public int getElementlength(){
        return elementlength;
    }
    public void setElementlength(int elementlength){
        this. elementlength = elementlength;
    }

}
