package com.spring5.bean;

public class Orders {

    //无参数构造
    public Orders() {
        System.out.println("第一步 执行无参数构造创建bean实例");
    }

    private String oname;

    public void setOname(String oname) {
        this.oname = oname;
        System.out.println("第二步 调用set方法设置属性值");
    }


    public void initMethod(){
        System.out.println("第三步 执行初始化方法");
    }
    public void destroyMethod(){
        System.out.println("第五步 执行销毁方法");
    }

}
