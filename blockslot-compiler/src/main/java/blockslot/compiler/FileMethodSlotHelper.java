package blockslot.compiler;

import java.io.Writer;

import javax.tools.JavaFileObject;

import blockslot.compiler.model.FileBean;
import blockslot.compiler.model.MethodBean;

/**
 * 作者： mooney
 * 日期： 2018/1/25
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽代码生成器辅助类
 */

public class FileMethodSlotHelper {

    public static void generateFile(JavaFileObject jfo, String javaCodeStr) throws Exception {
        Writer writer = jfo.openWriter();
        writer.write(javaCodeStr);
        writer.flush();
        writer.close();
    }

    public static String generateJavaCode(FileBean fileBean) {

        //switch case 逻辑
        StringBuilder sbCases=new StringBuilder();
        //函数字符串
        StringBuilder sbMethods=new StringBuilder();
        for (int i=0;i<fileBean.getMethodList().size();i++){
            MethodBean methodBean=fileBean.getMethodList().get(i);
            sbMethods.append(getMethodStr(methodBean));
            sbCases.append(getSwitchCaseStr(methodBean));
        }


        StringBuilder builder=new StringBuilder();

        String packageName = fileBean.getPath();
        String clsName = fileBean.getFileName();

        builder.append( "//This codes are generated automatically. Do not modify!\n");
        builder.append( "package "+packageName+";\n\n");
        builder.append( "import blockslot.internal.BlockslotParameterUtils;\n\n");

        builder.append( "public class " + clsName + "{\n\n");

        builder.append( "    public static Object invoke(String slotTag, Object target, Object... parameters){\n\n");
        builder.append( "        switch (slotTag){\n");
        builder.append(sbCases);
        builder.append( "           default:\n");
        builder.append( "               return null;\n");
        builder.append( "        }\n\n");
        builder.append( "    }\n\n");

        builder.append(sbMethods);

        builder.append( "}");

        return builder.toString();
    }


    private static String getSwitchCaseStr(MethodBean methodBean){
        String slotTag=methodBean.getSlotTag();
        String returnClz=methodBean.getReturnClz();
        String methodName=methodBean.getMethodName();
        boolean isStatic=methodBean.isStaticMethod();

        StringBuilder sb=new StringBuilder();
        sb.append(  "            case \"" +slotTag +"\":\n");

        if (methodName.equals("<init>")){
            methodName="newInstance";
            if (returnClz.equals("void")){
                returnClz=methodBean.getClzName();
            }
            isStatic=true;

        }


        if (isStatic){
            if (returnClz.equals("void")){
                sb.append(  "                " +methodName +"(parameters);\n");
                sb.append(  "                return null;\n");
            }else {
                sb.append(  "                return " +methodName +"(parameters);\n");
            }
        }else {
            if (returnClz.equals("void")){
                sb.append(  "                " +methodName +"((" + methodBean.getClzName() + ")target, parameters);\n");
                sb.append(  "                return null;\n");
            }else {
                sb.append(  "                return " +methodName +"((" + methodBean.getClzName() + ")target, parameters);\n");
            }
        }

        return sb.toString();
    }


    private static String getMethodStr(MethodBean methodBean){
        StringBuilder sb=new StringBuilder();

        String returnClz=methodBean.getReturnClz();
        String methodName=methodBean.getMethodName();
        String[] parameters=methodBean.getParameterTypes();

        boolean isStatic=methodBean.isStaticMethod();

        if (methodName.equals("<init>")){
            methodName="newInstance";

            if (returnClz.equals("void")){
                returnClz=methodBean.getClzName();
            }

            isStatic=true;
        }

        String targetStr="";
        if (!isStatic){
            targetStr=methodBean.getClzName()+" target, ";
        }

        sb.append(  "    private static " +returnClz + " " +methodName+ "(" + targetStr+"Object... parameters){\n\n");

        String parameterTypes="";
        for (int i=0;i<methodBean.getParameterTypes().length;i++){
            if (i!=methodBean.getParameterTypes().length-1){
                parameterTypes=parameterTypes+methodBean.getParameterTypes()[i]+".class\n, ";
            }else {
                parameterTypes=parameterTypes+methodBean.getParameterTypes()[i]+".class";
            }
        }
        sb.append(  "        Class[] parameterTypes = new Class[]{"+ parameterTypes + "};\n");
        sb.append(  "        Object[] realParameters = BlockslotParameterUtils.getRealParameters(parameterTypes, parameters);\n                ");
        String varStr="";
        for (int i=0;i<parameters.length;i++){
            if (i!=parameters.length-1){
                varStr=varStr+"(" + parameters[i] + ")" + "realParameters["+i+"], ";
            }else {
                varStr=varStr+"(" + parameters[i] + ")" + "realParameters["+i+"]";
            }
        }
        if (returnClz.equals("void")){
            if (isStatic){
                sb.append("        "+methodBean.getClzName()+"."+methodName+"(" + varStr + ");\n");
            }else {
                sb.append("        target."+methodName+"(" + varStr + ");\n");
            }
        }else {
            if (methodName.equals("newInstance")){
                sb.append("        return new "+methodBean.getClzName()+"(" + varStr + ");\n");
            }else if (isStatic){
                sb.append("        return "+methodBean.getClzName()+"."+methodName+"(" + varStr + ");\n");
            }else {
                sb.append("        return target."+methodName+"(" + varStr + ");\n");
            }
        }

        sb.append(  "    }\n\n");

        return sb.toString();
    }


}
