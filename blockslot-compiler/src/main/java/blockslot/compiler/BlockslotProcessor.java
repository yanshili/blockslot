package blockslot.compiler;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import blockslot.annotations.MethodSlot;
import blockslot.compiler.model.MethodInfo;


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


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
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

        log("set.size==" + set.size());
        if (set.isEmpty()) {
            return false;
        }

        Set<? extends Element> providers = roundEnv.getElementsAnnotatedWith(MethodSlot.class);

        for (Element element : providers) {

            ExecutableElement executableElement = (ExecutableElement) element;

            /**
             * 函数标志
             */
            String slotTag =  element.getAnnotation(MethodSlot.class).value().replace(" ","");

            verifySlotTag(slotTag);

            /**
             * 生成的Java类名
             */
            String javaFileName= MethodInfo.generateClsPackage +"." + slotTag.substring(0,slotTag.indexOf("#"));

            /**
             * 函数名称
             */
            String methodName = element.getSimpleName().toString();

            /**
             * 获取类名
             */
            TypeElement classElement = (TypeElement) executableElement.getEnclosingElement();
            String clzFullName = classElement.getQualifiedName().toString();

            /**
             * 获取参数类型列表字符串
             */
            StringBuilder parameterStr = new StringBuilder();
            List<? extends VariableElement> list = executableElement.getParameters();
            for (int i = 0; i < list.size(); i++) {

                VariableElement variableElement = list.get(i);

                //参数类型
                TypeMirror methodParameterType = variableElement.asType();

                String parameterType = methodParameterType.toString();


                if (i == 0) {
                    parameterStr.append(", ");
                }

                parameterStr.append(parameterType+".class");

                if (i != list.size() - 1) {
                    parameterStr.append(", ");
                }
            }

            /**
             * 是否为静态函数
             */
            boolean staticMethod=false;
            Iterator<Modifier> modifierIterator=element.getModifiers().iterator();
            while (modifierIterator.hasNext()){
                if (modifierIterator.next()==Modifier.STATIC){
                    staticMethod=true;
                    break;
                }
            }

            /**
             * 如果为静态函数，则设置类全名，否则不设置类名
             */
            String encloseClzName=null;
            if (staticMethod){
                encloseClzName=clzFullName+".class";
            }

            /**
             * 缓存getMethodInfo方法字符串
             */
            String methodPutStr= fileMethodMap.get(javaFileName);
            if (methodPutStr==null){
                methodPutStr="";
            }
            methodPutStr=methodPutStr
                    + GeneratorBlockslotFileHelper
                    .generatePutMethod(slotTag, methodName, encloseClzName, parameterStr.toString());
            fileMethodMap.put(javaFileName, methodPutStr);

            log("BlockslotProcessor " + "slotTag==" + slotTag
                    + "   javaFileName==" + javaFileName
                    + "\nmethod==" + clzFullName + "#" + methodName + "(" + parameterStr + ")");
        }

        Iterator<Map.Entry<String,String>> iterator= fileMethodMap.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            String fileName=entry.getKey();
            String methodPutStr=entry.getValue();

            String javaStr = GeneratorBlockslotFileHelper
                    .generateJavaCode(fileName, methodPutStr).toString();
            try {

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        fileName);

                GeneratorBlockslotFileHelper.generateFile(jfo, javaStr);
            } catch (Exception e) {
                logE(e.toString());
            }
        }

        return false;
    }


    private void verifySlotTag(String slotTag){
        int indexF=slotTag.indexOf("#");
        int indexL=slotTag.lastIndexOf("#");
        int index=-1;
        if (indexF==indexL){
            index=indexF;
        }

        if (index<1||index>=slotTag.length()-1){
            logE("**************************************************"
                    + "\nThe slot tag \"" +slotTag+"\""+ " is illegal!!!"
                    + "\nthe slot tag must contain only one character # "
                    + "\ncharacter # and can not be placed on the head and tail of the slot tag"
                    + "\n**************************************************");
        }
    }


    /************************************编译时日志函数*****************************************/
    private void log(String str) {

        //输出日志
        messager.printMessage(Diagnostic.Kind.NOTE, str);

    }

    private void logE(String str) {

        //输出日志
        messager.printMessage(Diagnostic.Kind.ERROR, str);

    }

    private void logW(String str) {

        //输出日志
        messager.printMessage(Diagnostic.Kind.WARNING, str);

    }

}