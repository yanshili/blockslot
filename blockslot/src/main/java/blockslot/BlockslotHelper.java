package blockslot;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import blockslot.compiler.model.MethodInfo;

/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 函数调用辅助类（根据tag信息调用指定函数）
 */

class BlockslotHelper {

    private Map<String, Method> mMethodCache=new HashMap<>();
    private Map<String, MethodInfo> mMethodInfoCache=new HashMap<>();
    private Map<String, Class> mGeneratorClzCache =new HashMap<>();


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
            Constructor constructor=BlockslotReflectUtils.getConstructor(clz, parameterTypes);
            return (T)constructor.newInstance(parameters);
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
        Class[] parameterTypes = methodInfo.getParameterTypes();

        try {
            return (T) getMethod(null, slotTag, methodInfo)
                    .invoke(BlockslotReflectUtils.getRealParameters(parameterTypes,parameters));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
        Class[] parameterTypes = methodInfo.getParameterTypes();

        try {
            return (T) getMethod(target, slotTag, methodInfo)
                    .invoke(target,BlockslotReflectUtils.getRealParameters(parameterTypes, parameters));
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
        slotTag=slotTag.replace(" ","");

        try {
            MethodInfo methodInfo=mMethodInfoCache.get(slotTag);
            if (methodInfo==null){
                Class clz = mGeneratorClzCache.get(slotTag);
                if (clz==null){
                    clz= Class.forName(MethodInfo.generateClsPackage +"."+slotTag.substring(0, slotTag.indexOf("#")));
                    mGeneratorClzCache.put(slotTag, clz);
                }

                methodInfo= (MethodInfo) clz
                        .getDeclaredMethod("getMethodInfo",  new Class[]{String.class})
                        .invoke(slotTag);

                mMethodInfoCache.put(slotTag, methodInfo);
            }
            return methodInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 获取指定方法
     * @param target
     * @param slotTag
     * @param methodInfo
     * @return
     * @throws Exception
     */
    private Method getMethod(Object target, String slotTag, MethodInfo methodInfo) throws Exception{
        slotTag=slotTag.replace(" ","");

        Method method=mMethodCache.get(slotTag);
        if (method==null){

            Class clz = methodInfo.getClz();
            String methodName = methodInfo.getMethodName();
            Class[] parameterTypes = methodInfo.getParameterTypes();

            if (target==null){
                method=BlockslotReflectUtils.getMethodS(clz,methodName,parameterTypes);
            }else {
                Class<?> cls = target.getClass();
                method=BlockslotReflectUtils.getMethodS(cls,methodName,parameterTypes);
            }

            mMethodCache.put(slotTag, method);
        }

        return method;
    }

}
