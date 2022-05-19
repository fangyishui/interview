package com.spring5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class JDKProxy {

    public static void main(String[] args) {


        //创建接口实现类代理对象
        Class[] interfases = {UserDao.class};

        //1.InvocationHandler内部类的实现方式
//        Object o = Proxy.newProxyInstance(JDKProxy.class.getClassLoader(), interfases, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                return null;
//            }
//        });

//        2.InvocationHandler外部实现
        UserDao userDao = new UserDaoImpl();
        UserDao dao =
                (UserDao)Proxy.newProxyInstance(JDKProxy.class.getClassLoader(),interfases,new UserDaoProxy(userDao));

        int add = dao.add(2, 3);
        System.out.println("相加的结果："+add);
        System.out.println("=============================================");
        String update = dao.update("30");
        System.out.println("更新的结果："+update);
    }
}

//创建代理对象代码
class UserDaoProxy implements InvocationHandler{

    //通过构造方法，将需要代理的对象传递进来
    //    有参构造传递
    //可以直接 传递 UserDaoImpl对象，但为了通用，使用Object
    private Object object;
    public UserDaoProxy(Object object){
        this.object = object;
    }

    //增强的代理逻辑
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //可以根据获取的对象，代码名称判断 是否增强

        //方法之前
        System.out.println("方法之前执行。。。。。"    + method.getName() + "  传递的参数："+ Arrays.toString(args));

        //执行方法-被增强的方法
        Object res = method.invoke(object, args);

        //方法之后
        System.out.println("方法之后执行1。。。。。"    + method.getName() + "  传递的参数："+ Arrays.toString(args));
        System.out.println("方法之后执行2。。。。。"    + method.getName() + "  返回值："+ res);
        System.out.println("方法之后执行3。。。。。"    + method.getName() + "  增强的对象："+ object);
        return res;
    }
}