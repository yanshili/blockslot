package blockslot.internal;

/**
 * 作者： mooney
 * 日期： 2018/2/5
 * 邮箱： shili_yan@sina.com
 * 描述： 默认参数处理工具类
 */

public class BlockslotParameterUtils {

    /**
     * 处理参数（设置默认参数为null）
     * @param parameterTypes
     * @param parameters
     * @return
     */
    public static Object[] getRealParameters(Class[] parameterTypes, Object... parameters){
        Object[] realParameters=new Object[parameterTypes.length];
        int validIndex=0;
        for (int i=0;i<parameterTypes.length;i++){

            Class clz=parameterTypes[i];

            realParameters[i]=getDefaultInstance(clz);

            if (parameters!=null&&validIndex<parameters.length){
                Object instance=parameters[validIndex];
                if (matchClass(clz, instance)){
                    realParameters[i]=instance;
                    validIndex++;

                }
            }

        }
        return realParameters;
    }

    /**
     *  获取指定数据类型的默认值
     *
     * @param clz
     * @return
     */
    private static Object getDefaultInstance(Class clz){
        if (clz==byte.class){
            return (byte)0;
        }else if (clz==short.class){
            return (short)0;
        }else if (clz==int.class){
            return 0;
        }else if (clz==long.class){
            return 0L;
        }else if (clz==float.class){
            return 0.F;
        }else if (clz==double.class){
            return 0.D;
        }else if (clz==char.class){
            return (char)0;
        }else if (clz==boolean.class){
            return false;
        }else {
            return null;
        }
    }

    /**
     * 判断{@param object}是否为{@param clz}类的实例
     *
     * @param clz
     * @param object
     * @return
     */
    private static boolean matchClass(Class clz, Object object){

        if (clz==byte.class){
            return Byte.class.isInstance(object);
        }else if (clz==short.class){
            return Short.class.isInstance(object);
        }else if (clz==int.class){
            return Integer.class.isInstance(object);
        }else if (clz==long.class){
            return Long.class.isInstance(object);
        }else if (clz==float.class){
            return Float.class.isInstance(object);
        }else if (clz==double.class){
            return Double.class.isInstance(object);
        }else if (clz==char.class){
            return Character.class.isInstance(object);
        }else if (clz==boolean.class){
            return Boolean.class.isInstance(object);
        }else {
            return clz.isInstance(object);
        }

    }


}
