# blockslot
模块插槽
* 动机： 开发插槽框架主要是因为经常会遇到项目中的业务不能彻底解耦，而各个业务需要分开开发（即各个业务模块均可独立运行），除了基础业务外其他业务之间无相互依赖关系。
* 描述： 模块插槽（用于模块间的解耦，类似于积木的正反面，每个模块均可通过{@link blockslot.annotations.MethodSlot}对外提供插槽位，同时可以通过{@link Blockslot}工具连接到slotTag对应的插槽位）
* 原理： 用注解标志指定插槽并生成代码缓存插槽信息（暂时只标注函数），最后通过{@link Blockslot}工具根据插槽信息利用反射连接插槽
* 注意： 混淆时添加-keep class blockslot.** {*;}
* 使用： compile 'me.mooney:blockslot:0.2.0'
