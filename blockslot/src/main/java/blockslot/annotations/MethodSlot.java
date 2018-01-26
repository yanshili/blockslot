package blockslot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者： mooney
 * 日期： 2018/1/24
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽注解（指定插槽标志）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MethodSlot {
    //格式(module#slotTag)
    //只能含有一个字符“#”，并且不可放在首部或尾部
    String value();
}
