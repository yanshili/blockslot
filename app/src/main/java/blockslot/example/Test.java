package blockslot.example;

import blockslot.annotations.MethodSlot;

/**
 * 作者： mooney
 * 日期： 2018/1/26
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class Test {

    private int times;

    @MethodSlot("test#constructor")
    public Test(int times) {
        this.times = times;
    }

    @MethodSlot("test#getTimes")
    public int getTimes(){
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName()+":times=="+times;
    }
}
