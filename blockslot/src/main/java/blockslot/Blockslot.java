package blockslot;

/**
 * 作者： mooney
 * 日期： 2018/1/25
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽（用于模块间的解耦，类似于积木的正反面，每个模块均可通过
 *       {@link blockslot.annotations.MethodSlot}对外提供插槽位，
 *       同时可以通过{@link Blockslot}工具连接到slotTag对应的插槽位）
 * 动机： 开发插槽框架主要是因为经常会遇到项目中的业务不能彻底解耦，而各个业务需要分开开发（即各个
 *       业务模块均可独立运行），除了基础业务外其他业务之间无相互依赖关系。
 * 原理： 用注解标志指定插槽并生成代码缓存插槽信息（暂时只标注函数），最后通过{@link Blockslot}工
 *       具根据插槽信息利用反射连接插槽
 * 注意： 混淆时添加-keep class blockslot.** {*;}
 */

public class Blockslot {

    /**
     * 连接{@param slotTag}对应的插槽位（调用静态函数）
     *
     * @param slotTag       插槽标志（每个插槽对应的标志都不能相同，格式(module#slotTag)）
     * @param parameters    参数列表（可以少传参数，但类型不能传错，少传的参数会根据基本数据类型设置默认参数）
     * @param <T>           返回的数据类型
     * @return
     */
    public static  <T> T invokeS(String slotTag, Object... parameters) {

        return BlockslotHelper.getInstance().invokeS(slotTag, parameters);

    }

    /**
     * 连接{@param slotTag}对应的插槽位（调用普通函数）
     *
     * @param target        指定插槽所在类的实例
     * @param slotTag       插槽标志（每个插槽对应的标志都不能相同，格式(module#slotTag)）
     * @param parameters    参数列表（可以少传参数，但类型不能传错，少传的参数会根据基本数据类型设置默认参数）
     * @param <T>           返回的数据类型
     * @return
     */
    public static  <T> T invoke(Object target, String slotTag, Object... parameters) {

        return BlockslotHelper.getInstance().invoke(target, slotTag, parameters);

    }

    /**
     * 连接{@param slotTag}对应的插槽位（调用构造函数）
     *
     * @param slotTag       插槽标志（每个插槽对应的标志都不能相同，格式(module#slotTag)）
     * @param parameters    参数列表（可以少传参数，但类型不能传错，少传的参数会根据基本数据类型设置默认参数）
     * @param <T>           返回的数据类型
     * @return
     */
    public static  <T> T newInstance(String slotTag, Object... parameters) {

        return BlockslotHelper.getInstance().invokeS(slotTag, parameters);

    }
}
