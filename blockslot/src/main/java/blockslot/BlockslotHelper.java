package blockslot;


import blockslot.compiler.model.MethodInfo;

/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 函数调用辅助类（根据tag信息调用指定函数）
 */

class BlockslotHelper {


    private BlockslotHelper() {
    }

    public static BlockslotHelper getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static BlockslotHelper instance = new BlockslotHelper();
    }

    /**
     * 根据tag标志反射调用构造函数
     *
     * @param slotTag
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T newInstance(String slotTag, Object... parameters) {

        MethodInfo methodInfo = getMethodInfoWithSlotTag(slotTag);
        Class clz = methodInfo.getClz();
        Class[] parameterTypes = methodInfo.getParameterTypes();

        try {
            return (T) BlockslotReflectUtils.newInstance(clz, parameterTypes, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据tag标志反射调用静态函数
     *
     * @param slotTag
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T invokeS(String slotTag, Object... parameters) {

        MethodInfo methodInfo = getMethodInfoWithSlotTag(slotTag);
        Class clz = methodInfo.getClz();
        String methodName = methodInfo.getMethodName();
        Class[] parameterTypes = methodInfo.getParameterTypes();

        return (T) invokeS(clz, methodName, parameterTypes, parameters);
    }

    /**
     * 根据tag标志反射调用普通函数
     *
     * @param target
     * @param slotTag
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T invoke(Object target, String slotTag, Object... parameters) {

        MethodInfo methodInfo = getMethodInfoWithSlotTag(slotTag);
        String methodName = methodInfo.getMethodName();
        Class[] parameterTypes = methodInfo.getParameterTypes();

        return (T) invoke(target, methodName, parameterTypes, parameters);
    }


    /**
     * 反射调用普通函数
     * @param target
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @param <T>
     * @return
     */
    private <T> T invoke(Object target, String methodName, Class[] parameterTypes
            , Object... parameters) {
        try {
            return (T) BlockslotReflectUtils.invoke(target, methodName, parameterTypes, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反射调用静态函数
     * @param clz
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @param <T>
     * @return
     */
    private <T> T invokeS(Class<?> clz, String methodName, Class[] parameterTypes
            , Object... parameters) {
        try {
            return (T) BlockslotReflectUtils.invokeS(clz, methodName, parameterTypes, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据插槽标志获取对应的函数信息
     * @param slotTag
     * @return
     */
    private MethodInfo getMethodInfoWithSlotTag(String slotTag){

        try {
            slotTag=slotTag.replace(" ","");
            Class clz= Class.forName(MethodInfo.generateClsPackage +"."+slotTag.substring(0, slotTag.indexOf("#")));
            return BlockslotReflectUtils.invokeS(clz, "getMethodInfo", new Class[]{String.class},slotTag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
