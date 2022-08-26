package org.springframker;

/**
 * Created by 撸码的小孩 on 2022/8/25
 * time  16:57
 */
public class BeanDefinition {



    private Class BeanClass;
    private String scope;
    private boolean lazy;

    public Class getBeanClass() {
        return BeanClass;
    }

    public void setBeanClass(Class beanClass) {
        BeanClass = beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
}
