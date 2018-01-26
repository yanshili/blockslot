package blockslot.compiler;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import blockslot.annotations.MethodSlot;


/**
 * 作者： mooney
 * 日期： 2018/1/23
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽代码生成器
 */
@AutoService(Processor.class)
public class BlockslotProcessor extends AbstractProcessor {

    private Map<String, String> fileMethodMap = new HashMap<>();

    private Messager messager;

    private FileMethodSlotProxy mMethodSlotProxy;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        mMethodSlotProxy=new FileMethodSlotProxy(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(MethodSlot.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        if (set.isEmpty()) {
            return false;
        }

        Set<? extends Element> methodElements = roundEnv.getElementsAnnotatedWith(MethodSlot.class);

        mMethodSlotProxy.process(methodElements);


        return false;
    }


}