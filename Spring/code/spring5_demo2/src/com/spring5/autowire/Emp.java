package com.spring5.autowire;

public class Emp {

    //自动装配是 查找的属性名：dept
    private Dept dept;

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "dept=" + dept +
                '}';
    }


    public void test(){
        System.out.println(dept);
    }
}
