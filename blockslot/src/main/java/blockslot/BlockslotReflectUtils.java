package blockslot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 反射工具类
 */

class BlockslotReflectUtils {

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

    /**
     * 根据类名、函数名及参数类型数组获取函数
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     * @throws Exception
     */
    public static Method getMethodS(Class<?> cls, String methodName, Class... parameterTypes)
            throws Exception {

        Method realMethod = cls.getDeclaredMethod(methodName, parameterTypes);

        if (realMethod == null) {
            throw new RuntimeException("there is no such method " + methodName);
        }

        /**
         *setAccessible是启用和禁用访问安全检查的开关，
         *值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查，提升反射性能
         *值为 false 则指示反射的对象应该实施 Java 语言访问检查。
         */
        realMethod.setAccessible(true);

        return realMethod;
    }

    /**
     * 根据类名和参数类型数组获取构造函数
     * @param cls
     * @param parameterTypes
     * @return
     * @throws Exception
     */
    public static Constructor getConstructor(Class<?> cls, Class... parameterTypes)
            throws Exception {

        Constructor constructor = cls.getDeclaredConstructor(parameterTypes);


        if (constructor == null) {
            String constructorInfo=cls.getCanonicalName()+"";
            for (int i=0;i<parameterTypes.length;i++){
                Class clz=parameterTypes[i];

                if (i==0){
                    constructorInfo+="(";
                }

                constructorInfo+=clz.getCanonicalName();

                if (i==parameterTypes.length-1){
                    constructorInfo+=")";
                }else {
                    constructorInfo+=", ";
                }
            }
            throw new RuntimeException("there is no such constructor:" + constructorInfo);
        }

        /**
         *setAccessible是启用和禁用访问安全检查的开关，
         *值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查，提升反射性能
         *值为 false 则指示反射的对象应该实施 Java 语言访问检查。
         */
        constructor.setAccessible(true);

        return constructor;
    }

    /**
     * 判断是否字符串相同
     * @param a
     * @param b
     * @return
     */
    private static boolean textEquals(CharSequence a, CharSequence b){
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

}
