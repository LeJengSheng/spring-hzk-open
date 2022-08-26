package org.springframker;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 撸码的小孩 on 2022/8/25
 * time  16:13
 */
public class HzkApplicationContext {


    private Class configClass;

    private HashMap<String, BeanDefinition> bdMap = new HashMap<String, BeanDefinition>();
    //单例池
    ConcurrentHashMap<String, Object> singletonObject = new ConcurrentHashMap<String, Object>();


    public HzkApplicationContext(Class configClass) {


        this.configClass = configClass;

        //1.扫描
        scan(configClass);
        //2.创建非懒加载的单例bean
        CreateNonLazySingletonBean();


    }

    private void CreateNonLazySingletonBean() {
        for (String beanName : bdMap.keySet()) {
            BeanDefinition beanDefinition = bdMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                //调用createBean()
                Object bean = createBean(beanDefinition);
                singletonObject.put(beanName, bean);
            }
        }
    }


    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String value = componentScan.value();//service.OrderService
            System.out.println(value);

            //扫描
            ClassLoader classLoader = HzkApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource("service");
            String file = resource.getFile();
            File file1 = new File(file);
            File[] files = file1.listFiles();
            for (File file2 : files) {

                try {
                    String path = file2.getAbsolutePath();
                    path = path.substring(path.indexOf("service"), path.indexOf(".class"));
                    String replacePath = path.replace("\\", ".");//service.OrderService
                    System.out.println(replacePath);//service\OrderService
                    Class<?> aClass = classLoader.loadClass(replacePath);

                    if (aClass.isAnnotationPresent(Component.class)) {


                        BeanDefinition beanDefinition = new BeanDefinition();
                        //创建bean对象了
                        Component component = aClass.getAnnotation(Component.class);
                        String beanName = component.value();


                        if (aClass.isAnnotationPresent(Lazy.class)) {
                            beanDefinition.setLazy(true);
                            //懒加载
                        }
                        if (aClass.isAnnotationPresent(Scope.class)) {
                            Scope annotation = aClass.getAnnotation(Scope.class);
                            if (annotation.value().equals("prototype")) {
                                //原型的
                                beanDefinition.setScope("prototype");
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                        } else {
                            //单例的
                            beanDefinition.setScope("singleton");
                        }

                        beanDefinition.setBeanClass(aClass);
                        bdMap.put(beanName, beanDefinition);

                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    public Object getBean(String beanName) {

        if (!bdMap.containsKey(beanName)) {
            throw new NullPointerException();
        } else {
            BeanDefinition beanDefinition = bdMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                //单例的
                Object o = singletonObject.get(beanName);
                return o;
            } else {
                //原型的时候需要去创建bean
                Object bean = createBean(beanDefinition);
                return bean;
            }


        }


    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        Constructor declaredConstructor = null;
        Object o = null;
        try {
            declaredConstructor = beanClass.getDeclaredConstructor();
            o = declaredConstructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }


}
