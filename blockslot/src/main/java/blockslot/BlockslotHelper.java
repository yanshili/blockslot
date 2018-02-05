package blockslot;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import blockslot.compiler.model.FileBean;

/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 函数调用辅助类（根据tag信息调用指定函数）
 */

class BlockslotHelper {

    private Map<String, Method> mMethodCache=new HashMap<>();
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
     * 根据tag标志反射调用静态函数
     *
     * @param slotTag
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T invokeS(String slotTag, Object... parameters) {

        try {
            return ivk(slotTag, null, parameters);
        } catch (Exception e) {
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

        try {
            return ivk(slotTag, target, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param slotTag
     * @param target
     * @param parameters
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T ivk(String slotTag, Object target, Object... parameters) throws Exception{

        Method method=mMethodCache.get(slotTag);
        if (method==null){
            Class clz = mGeneratorClzCache.get(slotTag);
            if (clz==null){
                clz= Class.forName(FileBean.path +"."+slotTag.substring(0, slotTag.indexOf("#")));
                mGeneratorClzCache.put(slotTag, clz);
            }
            method=clz.getDeclaredMethod("invoke",new Class[]{String.class, Object.class,Object[].class});
            mMethodCache.put(slotTag, method);
        }
        return (T) method.invoke(null, slotTag, target, parameters);
    }



}
