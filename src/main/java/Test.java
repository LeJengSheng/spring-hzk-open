import org.springframker.AppConfigure;
import org.springframker.HzkApplicationContext;

/**
 * Created by 撸码的小孩 on 2022/8/25
 * time  16:12
 *
 *
 *
 *
 * 类加载器=====AppCationClassloader====应用类加载器====类路径下的所有jar以及class文件
 * BootStrapClassloader=======根加载器====jre/lib/rt.jar
 * ExtClassLoader=====jre/lib/ext/*.jar
 *
 *
 *
 */
public class Test {

    public static void main(String[] args) {

       //1、创建spring容器  2开始创建bean  （x） 扫描  扫描完了之后  只会创建非懒加载的单例bean
        HzkApplicationContext context = new HzkApplicationContext(AppConfigure.class);
        System.out.println(context.getBean("orderService"));
        System.out.println(context.getBean("orderService"));
        System.out.println(context.getBean("orderService"));

    }


}
