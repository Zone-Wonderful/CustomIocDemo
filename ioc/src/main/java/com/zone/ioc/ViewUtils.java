package com.zone.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Date: 2017/4/1.
 * @Author: Zone-Wonderful
 * @Description:
 */
public class ViewUtils {


    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);

    }

    /**
     * 兼容上面的方法
     *
     * @param finder
     * @param object 反射需要执行的类
     */
    public static void inject(ViewFinder finder, Object object) {
        injectFiled(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 注入属性
     *
     * @param finder
     * @param object
     */
    private static void injectFiled(ViewFinder finder, Object object) {
        //1.获取类里面的所有属性
        Class<?> clazz = object.getClass();
        //获取所有的属性，包括公有地和私有的
        Field[] fields = clazz.getDeclaredFields();
        //2.获取viewById里面的value值
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //获取注解里面的Id值  ---> R.id.tv
                int viewId = viewById.value();
                //3.findViewById找到View
                View view = finder.findViewById(viewId);
                if (view != null) {
                    //能够注入所有修饰符
                    field.setAccessible(true);
                    //4.非空的情况下，动态的注入找到的View
                    try {
                        field.set(object, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注入事件
     *
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        //1.获取类里面的所有的方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //2.获取onClick里面的value值
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    //3.findViewById找到View
                    View view = finder.findViewById(viewId);

                    //检测网络
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;
                    if (view != null) {
                        //4. setOnClickListener
                        view.setOnClickListener(new DeclaredOnClickListener(method, object, isCheckNet));
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Object mObject;
        private Method mMethod;
        private boolean mIsCheckNet;

        public DeclaredOnClickListener(Method method, Object object, boolean isCheckNet) {
            this.mObject = object;
            this.mMethod = method;
            this.mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View view) {
            //判断需不需要检测网络
            if (mIsCheckNet) {
                //判断一下当前网络
                if (!networkAvailable(view.getContext())) {
                    Toast.makeText(view.getContext(), "亲，您的网络不给力", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            //点击或调用该方法
            try {
                //所有方法都可以，包括私有修饰符的
                mMethod.setAccessible(true);
                //5.反射执行方法
                mMethod.invoke(mObject, view);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 检测网络的方法
     *
     * @param context
     * @return
     */
    private static boolean networkAvailable(Context context) {
        //得到链接管理器的对象
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activieNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (activieNetworkInfo != null && activieNetworkInfo.isConnected()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
