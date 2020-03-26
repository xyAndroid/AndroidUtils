package com.xy.simplerouter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexFile;

/*************************************************
 * Copyright (C), 2017-2021, Bailun Tech. Co., Ltd.
 * File name: SimpleRouter.java
 * Author: kingpang   Version: v1.0   Date: 2017/9/29
 * Description: 简单的跨组件跳转功能，如果将来有复杂的，就再引入新的源码
 *              1、跨组件间的Activity直接跳转（类似startActivity）
 *              2、跨组件间的Fragment的使用
 *              3、跨组件间的Method的使用
 * History:
 *          kingpang 2017/9/29 v1.0 Create
 *          kingpang 2018/9/21 v1.1 Update
 *************************************************/
public class SimpleRouter {

    private static SimpleRouter m_Instance = null;
    private static Map<String, SimpleRouterObj> m_shareViewMap = null;
    private static final String REGULAR = "com.stl";
    private static Application mApplication;

    public static SimpleRouter getInstance() {
        if (null == m_Instance) {
            synchronized (SimpleRouter.class) {
                if (null == m_Instance) {
                    m_Instance = new SimpleRouter();
                }
            }
        }

        return m_Instance;
    }

    public void init(Application app) {
        mApplication = app;
        if (null == m_shareViewMap) {
            m_shareViewMap = new HashMap<>();
        }

        try {
            for (String strClassName : SimpleRouterMembers.list) {
                Class<?> vClass = Class.forName(strClassName);
                if (vClass != null) {
                    if (vClass.isAnnotationPresent(SimpleRouterClassRegister.class)) {
                        m_shareViewMap.put(vClass.getAnnotation(SimpleRouterClassRegister.class).key(),
                                SimpleRouterObj.Create(vClass, vClass.getAnnotation(SimpleRouterClassRegister.class).type()));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init2(Application app) {
        mApplication = app;
        if (null == m_shareViewMap) {
            m_shareViewMap = new HashMap<>();
        }

        try {
            DexFile df = new DexFile(mApplication.getPackageResourcePath());
            Enumeration<String> n = df.entries();
            while (n.hasMoreElements()) {
                String strClassName = n.nextElement();

                //Log.e("@@@", " --> " + strClassName);

                //加上判断，将代码放入标准位置，这样可以大大减少运算时间
                if (strClassName.contains(REGULAR)) {
                    Class<?> vClass = Class.forName(strClassName);
                    if (vClass != null) {
                        if (vClass.isAnnotationPresent(SimpleRouterClassRegister.class)) {
                            m_shareViewMap.put(vClass.getAnnotation(SimpleRouterClassRegister.class).key(),
                                    SimpleRouterObj.Create(vClass, vClass.getAnnotation(SimpleRouterClassRegister.class).type()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 通过GET_ACTIVITIES来找，只能找到Activity，Fragment暂时没有找到合适的方法
        //        try {
        //            PackageInfo pi = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_ ACTIVITIES);
        //            if (pi != null) {
        //                for (ActivityInfo info : pi.activities) {
        //                    Class<?> vClass = Class.forName(info.name);
        //                    if (vClass != null) {
        //                        if (vClass.isAnnotationPresent(SimpleRouterRegister.class)) {
        //                            m_shareViewMap.put(vClass.getAnnotation(SimpleRouterRegister.class).key(), vClass);
        //                        }
        //                    }
        //                }
        //            }
        //        } catch (PackageManager.NameNotFoundException e) {
        //            e.printStackTrace();
        //        } catch (ClassNotFoundException e) {
        //            e.printStackTrace();
        //        }
    }

    private SimpleRouterObj getSimpleRouterObj(String strKey) {
        if (null != m_shareViewMap) {
            return m_shareViewMap.get(strKey);
        }

        return null;
    }


    public void startActivity(String key) {
        startActivity(key, null);
    }

    public void startActivity(String key, Bundle bundle) {
        startActivity(key, bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public void startActivity(String key, Bundle bundle, int flags) {
        SimpleRouterObj curSimpleRouterObj = getSimpleRouterObj(key);

        if (curSimpleRouterObj != null && curSimpleRouterObj._type == SimpleRouterObj.ACTIVITY) {
            Intent intent = new Intent(mApplication, curSimpleRouterObj._classObj);
            if (bundle != null) {
                intent.putExtras(bundle);
            }

            intent.addFlags(flags);
            mApplication.startActivity(intent);
            return;
        }

        Toast.makeText(mApplication, String.format("Can not find ACTIVITY class to open\n * key=%s * \n make sure add @SimpleRouterRegister in class file or disable Instant Run", key), Toast.LENGTH_LONG).show();
    }

    public Fragment getFragment(String key) {
        return getFragment(key, null);
    }

    public Fragment getFragment(String key, Bundle bundle) {
        Fragment retFragment = null;
        SimpleRouterObj curSimpleRouterObj = getSimpleRouterObj(key);
        if (curSimpleRouterObj != null && curSimpleRouterObj._type == SimpleRouterObj.FRAGMENT) {
            try {
                retFragment = (Fragment) curSimpleRouterObj.getSupportInstance();
                if (null != bundle) {
                    retFragment.setArguments(bundle);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (null == retFragment) {
            Toast.makeText(mApplication, String.format("Can not find FRAGMENT class to open\n * key=%s * \n make sure add @SimpleRouterRegister in class file or disable Instant Run", key), Toast.LENGTH_LONG).show();
        }

        return retFragment;
    }

    public <T> T doMethod(String key, String method, Object... args) {
        String strCurMsg = null;
        SimpleRouterObj curSimpleRouterObj = getSimpleRouterObj(key);
        if (curSimpleRouterObj != null && curSimpleRouterObj.isSupportMethod()) {
            try {
                Class<?>[] parameterTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    parameterTypes[i] = SimpleRouterObj.paramObj2ClassType(args[i]);
                }

                Method curMethod = curSimpleRouterObj._classObj.getMethod(method, parameterTypes);
                return (T) curMethod.invoke(curSimpleRouterObj.getSupportInstance(), args);

            } catch (InstantiationException e) {
                strCurMsg = "[Instantiation]" + e.getMessage();
            } catch (IllegalAccessException e) {
                strCurMsg = "[Illegal Access]" + e.getMessage();
            } catch (InvocationTargetException e) {
                strCurMsg = "[Invocation Target]" + e.getMessage(); // 函数体内部异常捕获
            } catch (NoSuchMethodException e) {
                strCurMsg = "[No Such Method]" + e.getMessage();
            }
        } else {
            strCurMsg = "class is not exist or type is not METHOD";
        }

        if (null != strCurMsg) {
            Toast.makeText(mApplication, String.format("Call other module method failed.\n * key=%s * \n method=%s \n error message:%s", key, method, strCurMsg), Toast.LENGTH_LONG).show();
        }

        return null;
    }
}
