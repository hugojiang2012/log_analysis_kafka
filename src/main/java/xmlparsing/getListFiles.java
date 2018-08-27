package xmlparsing;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jiangjingping on 2018/7/20.
 * 读取指定路径目录下的文件
 * @param in ; 文件所在的目录
 */
public class getListFiles {

    public static ArrayList<File> getListFiles(Object filepath){
        File directory = null;
        if(filepath instanceof  File){
            directory = (File) filepath;
        }
        else{
            directory = new File(filepath.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if(directory.isFile()){
            files.add(directory);
            return  files;
        }
        else if (directory.isDirectory()){
            File[] fileArr = directory.listFiles();
            for(int i = 0 ; i <  fileArr.length; i++){
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;

    }
}
