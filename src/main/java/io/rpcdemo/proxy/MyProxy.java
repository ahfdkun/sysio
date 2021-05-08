package io.rpcdemo.proxy;

import io.rpcdemo.rpc.Dispatcher;
import io.rpcdemo.rpc.protocol.MyContent;
import io.rpcdemo.rpc.transport.ClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;


public class MyProxy {

    public static <T>T proxyGet(Class<T>  interfaceInfo){
        //实现各个版本的动态代理。。。。

        ClassLoader loader = interfaceInfo.getClassLoader();
        Class<?>[] methodInfo = {interfaceInfo};


        Dispatcher dis = Dispatcher.getDis();
        return (T) Proxy.newProxyInstance(loader, methodInfo, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Object res=null;
                Object o = dis.get(interfaceInfo.getName());
                if(o== null){
                    //就要走rpc
                    String name = interfaceInfo.getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    MyContent content = new MyContent();
                    content.setArgs(args);
                    content.setName(name);
                    content.setMethodName(methodName);
                    content.setParameterTypes(parameterTypes);

                    /**
                     * 1,缺失了注册发现，zk
                     * 2,第一层负载面向的provider
                     * 3，consumer  线程池  面向 service；并发就有木桶，倾斜
                     * serviceA
                     *      ipA:port
                     *          socket1
                     *          socket2
                     *      ipB:port
                     */
                    CompletableFuture resF = ClientFactory.transport(content);

                    res =  resF.get();//阻塞的

                }else{
                    //就是local
                    //插入一些插件的机会，做一些扩展
                    System.out.println("lcoal FC....");
                    Class<?> clazz = o.getClass();
                    try {
                        Method m = clazz.getMethod(method.getName(), method.getParameterTypes());
                        res = m.invoke(o, args);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
                return  res;


            }
        });


    }


}
