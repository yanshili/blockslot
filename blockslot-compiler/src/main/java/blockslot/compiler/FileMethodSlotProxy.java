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
import blockslot.compiler.model.FileBean;
import blockslot.compiler.model.MethodBean;

/**
 * 作者： mooney
 * 日期： 2018/1/26
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class FileMethodSlotProxy extends AbstractSlotProxy{

    private Map<String, FileBean> fileMethodMap = new HashMap<>();

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
            String javaFileName= slotTag.substring(0,slotTag.indexOf("#"));

            /**
             * 函数名称
             */
            String methodName = element.getSimpleName().toString();

            /**
             * 函数返回类型
             */
            String returnType=executableElement.getReturnType().toString();

            /**
             * 获取类名
             */
            TypeElement classElement = (TypeElement) executableElement.getEnclosingElement();
            String clzFullName = classElement.getQualifiedName().toString();

            /**
             * 获取参数类型列表字符串
             */
            List<? extends VariableElement> list = executableElement.getParameters();
            String[] parameterArray=new String[list.size()];
            for (int i = 0; i < list.size(); i++) {

                VariableElement variableElement = list.get(i);

                //参数类型
                TypeMirror methodParameterType = variableElement.asType();

                String parameterType = methodParameterType.toString();

                parameterArray[i]=parameterType;

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

            FileBean fileBean=fileMethodMap.get(javaFileName);
            if (fileBean==null){
                fileBean=new FileBean(javaFileName);
                fileMethodMap.put(javaFileName, fileBean);
            }
            MethodBean methodBean=new MethodBean(clzFullName, slotTag,returnType, methodName
                    , parameterArray, staticMethod);
            fileBean.getMethodList().add(methodBean);

            log("slotTag==" + slotTag
                    + "\njavaFileName==" + javaFileName
                    + "\nreturnType=="+returnType
                    + "\nmethod==" + clzFullName + "#" + methodName + "(" + parameterArray.toString() + ")");
        }

        Iterator<Map.Entry<String,FileBean>> iterator= fileMethodMap.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String,FileBean> entry=iterator.next();
            String fileName=entry.getKey();
            FileBean fileBean=entry.getValue();

            String javaStr = FileMethodSlotHelper.generateJavaCode(fileBean);
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        fileBean.getPath()+"."+fileName);

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
