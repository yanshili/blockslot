package blockslot.compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import blockslot.annotations.MethodSlot;
import blockslot.compiler.model.MethodInfo;

/**
 * 作者： mooney
 * 日期： 2018/1/26
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class FileMethodSlotProxy extends AbstractSlotProxy{

    private Map<String, String> fileMethodMap = new HashMap<>();

    public FileMethodSlotProxy(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public void process(Set<? extends Element> elements) {
        for (Element element : elements) {


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
             * 如果为静态函数或构造函数，则设置类全名，否则不设置类名
             */
            String encloseClzName=null;
            if (staticMethod||methodName.equals("<init>")){
                encloseClzName=clzFullName+".class";
            }

            /**
             * 如果为构造函数，则不设置方法名
             */
            if (methodName.equals("<init>")){
                methodName=null;
            }

            /**
             * 缓存getMethodInfo方法字符串
             */
            String methodPutStr= fileMethodMap.get(javaFileName);
            if (methodPutStr==null){
                methodPutStr="";
            }
            methodPutStr=methodPutStr
                    + FileMethodSlotHelper
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

            String javaStr = FileMethodSlotHelper
                    .generateJavaCode(fileName, methodPutStr).toString();
            try {

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        fileName);

                FileMethodSlotHelper.generateFile(jfo, javaStr);
            } catch (Exception e) {
                logE(e.toString());
            }
        }

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


}
