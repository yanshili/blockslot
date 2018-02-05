package blockslot.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： mooney
 * 日期： 2018/2/5
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class FileBean {

    public static final String path="blockslot.compiler.generator";

    private String fileName;

    private List<MethodBean> mMethodList;

    public FileBean(String fileName) {
        this.fileName = fileName;
        mMethodList=new ArrayList<>();
    }

    public void addMethod(MethodBean methodBean){
        mMethodList.add(methodBean);
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public List<MethodBean> getMethodList() {
        return mMethodList;
    }
}
