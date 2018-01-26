package blockslot.compiler.model;

import java.io.Serializable;

/**
 * 作者： mooney
 * 日期： 2018/1/24
 * 邮箱： shili_yan@sina.com
 * 描述： 函数信息
 */

public class MethodInfo implements Serializable{

    public static final String generateClsPackage ="blockslot.compiler.generator";

    //为null时则为类的成员函数，否则为静态函数
    private Class clz;

    private String methodName;

    private Class[] parameterTypes;

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

}
