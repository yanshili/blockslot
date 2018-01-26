package blockslot.compiler;

import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * 作者： mooney
 * 日期： 2018/1/26
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public abstract class AbstractSlotProxy {

    private Messager messager;

    protected ProcessingEnvironment processingEnv;

    public AbstractSlotProxy(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;

        messager = processingEnv.getMessager();
    }


    public abstract void process(Set<? extends Element> elements);


    /************************************编译时日志函数*****************************************/
    protected void log(String str) {
        l(Diagnostic.Kind.NOTE, str);
    }

    protected void logE(String str) {
        l(Diagnostic.Kind.ERROR, str);
    }

    protected void logW(String str) {
        l(Diagnostic.Kind.WARNING, str);
    }

    private void l(Diagnostic.Kind kind, String str){

        str=getClass().getName()+"-->"+str;
        //输出日志
        messager.printMessage(kind, str);
    }
}
