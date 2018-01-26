package blockslot.compiler;

import java.io.Writer;

import javax.tools.JavaFileObject;

/**
 * 作者： mooney
 * 日期： 2018/1/25
 * 邮箱： shili_yan@sina.com
 * 描述： 模块插槽代码生成器辅助类
 */

public class GeneratorBlockslotFileHelper {

    public static void generateFile(JavaFileObject jfo, String javaCodeStr) throws Exception {
        Writer writer = jfo.openWriter();
        writer.write(javaCodeStr);
        writer.flush();
        writer.close();
    }

    public static StringBuilder generateJavaCode(String clsFullName, String methodPutStr) {

        String packageName = clsFullName.substring(0, clsFullName.lastIndexOf("."));
        String clsName = clsFullName.substring(clsFullName.lastIndexOf(".") + 1);
        StringBuilder builder = new StringBuilder();

        builder.append("//This codes are generated automatically. Do not modify!\n");
        builder.append("package "+packageName+";\n\n");

        builder.append("import java.util.HashMap;\n");
        builder.append("import java.util.Map;\n");
        builder.append("import blockslot.internal.model.MethodInfo;\n\n");


        builder.append("public class " + clsName + "{\n\n");


        builder.append("    public static MethodInfo getMethodInfo(String tag){\n\n" +
                "        Map<String, MethodInfo> map=new HashMap<>();\n\n" +
                methodPutStr +
                "        return map.get(tag);\n" +
                "    }\n\n");

        builder.append("    private static MethodInfo produceMethodInfo(String methodName, Class encloseClz\n" +
                "            , Class... parameterTypes) {\n\n" +
                "        MethodInfo methodInfo = new MethodInfo();\n" +
                "        methodInfo.setMethodName(methodName);\n" +
                "        methodInfo.setClz(encloseClz);\n" +
                "        methodInfo.setParameterTypes(parameterTypes);\n" +
                "        \n"+
                "        return methodInfo;\n" +
                "    }\n\n");

        builder.append("}");

        return builder;
    }

    public static String generatePutMethod(String tag
            , String methodName
            , String encloseClzName  //函数所在类的名称
            , String parameterTypesNamesStr) {

        StringBuilder builder = new StringBuilder();

        // map.put("tag", produceMethodInfo("tag", target.class, int.class));
        builder.append("        map.put(\"" +tag
                + "\", produceMethodInfo(\"" + methodName
                + "\", " + encloseClzName
                + parameterTypesNamesStr + "));\n\n");
        return builder.toString();
    }

}
