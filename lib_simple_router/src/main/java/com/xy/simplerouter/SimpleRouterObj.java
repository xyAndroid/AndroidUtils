package com.xy.simplerouter;

import android.content.Context;
import androidx.annotation.IntDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 路由对象
 *
 * @author kingpang
 * @date 2018/9/21.
 */
public class SimpleRouterObj {

    //region 【类型定义】

    public static final int UNKNOWN = -1;
    public static final int ACTIVITY = 0;
    public static final int FRAGMENT = 1;
    public static final int STATIC_METHOD = 2;
    //public static final int GENERAL_METHOD = 3;

    @IntDef({UNKNOWN, ACTIVITY, FRAGMENT, STATIC_METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SimpleRouterRegisterType {
    }

    public Class _classObj = null;

    @SimpleRouterRegisterType
    public int _type = UNKNOWN;

    //endregion

    //region 【内部函数】

    public Object getSupportInstance() throws IllegalAccessException, InstantiationException {
        if (_type == STATIC_METHOD) {
            return null;
        }
        return _classObj.newInstance();
    }

    public boolean isSupportMethod() {
        if (_type == STATIC_METHOD) {
            return true;
        }
        return false;
    }

    //endregion

    //region 【静态函数】

    public static SimpleRouterObj Create(Class<?> cls, @SimpleRouterRegisterType int nType) {
        SimpleRouterObj objNew = new SimpleRouterObj();
        objNew._classObj = cls;
        objNew._type = nType;

        return objNew;
    }

    // Java的封装类和基本数据类型之间不能识别，需要手动配置，今后可能需要拓展补充
    public static Class<?> paramObj2ClassType(Object obj) {
        Class<?> retType = obj.getClass();

        if (obj instanceof Integer) {
            retType = int.class;
        } else if (obj instanceof Boolean) {
            retType = boolean.class;
        } else if (obj instanceof Long) {
            retType = long.class;
        } else if (obj instanceof Double) {
            retType = double.class;
        } else if (obj instanceof Float) {
            retType = float.class;
        } else if (obj instanceof Byte) {
            retType = byte.class;
        } else if (obj instanceof Character) {
            retType = char.class;
        } else if (obj instanceof Short) {
            retType = short.class;
        } else if (obj instanceof CommLibCallback) {
            retType = CommLibCallback.class;
        } else if (obj instanceof Context) {
            retType = Context.class;
        }

        return retType;
    }

    //endregion

}
