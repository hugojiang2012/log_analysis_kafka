package xmlparsing;

/**
 * Created by jiangjingping on 2018/7/20.
 */
public class classstruct {
    private  String classname;
    private  int classid;
    private  int elementnum;

    //初始化类表
    public  void class_tbl_initialize(String classname, int classid, int elementnum){
        this.classname = classname;
        this.classid = classid;
        this.elementnum =elementnum;
    }

    //获取类名
    public String getClassname(){
        return classname;
    }
    public void  setClassname(String classname){
        this.classname = classname;
    }

    //获取类id
    public int getClassid(){
        return classid;
    }
    public void setClassid(int classid){
        this.classid = classid;
    }

    //获取类元素个数
    public  int  getElementnum(){
        return elementnum;
    }
    public void setElementnum(int elementnum){
        this.elementnum = elementnum;
    }


}
