package blockslot.compiler.model;

/**
 * 作者： mooney
 * 日期： 2018/2/5
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class MethodBean {

    public MethodBean(String clzName, String slotTag, String returnClz, String methodName
            , String[] parameterTypes, boolean staticMethod) {
        this.clzName = clzName;
        this.slotTag = slotTag;
        this.returnClz = returnClz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.staticMethod = staticMethod;
    }

    //插槽所在类全名
    private String clzName;

    //插槽标志
    private String slotTag;

    //返回类型
    private String returnClz;

    //方法名
    private String methodName;

    //参数类型列表
    private String[] parameterTypes;

    private boolean staticMethod;

    public String getClzName() {
        return clzName;
    }

    public String getSlotTag() {
        return slotTag;
    }

    public String getReturnClz() {
        return returnClz;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public boolean isStaticMethod() {
        return staticMethod;
    }
}
