package AppClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jiangjingping on 2018/7/19.
 */
public class ClassLoaderTest {
    public static  void main(String[] args){
        try{
            //根据给定的类名初始化
            Class catClass = Class.forName("AppClassLoader.Cat");
            //实例化这个类
            Object obj = catClass.newInstance();
            //获得这个类的所有方法
            Method[] methods = catClass.getMethods();
            //循环查找想要的方法
            for(Method method:methods){
                if("setName".equals(method.getName())){
                    //调用这个方法，invoke第一个参数是类名，后面是方法需要的参数
                    method.invoke(obj, "Tom");
                }
            }
            // 调用指定方法
            catClass.getMethod("shout").invoke(obj, null);

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

}

class Cat {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public void shout() {
        System.out.println("My name is " + this.name + "!");
    }
}