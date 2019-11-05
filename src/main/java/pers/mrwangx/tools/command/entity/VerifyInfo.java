package pers.mrwangx.tools.command.entity;

import pers.mrwangx.commons.tool.display.annotation.Display;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 13:56
 * \* Description:
 **/
public class VerifyInfo {

    @Display("命令名称")
    private String cmdName;
    @Display("是否成功")
    private boolean flag;
    @Display("验证结果信息")
    private String message;
    private String[] argValues;


    public VerifyInfo() {
    }

    public VerifyInfo(String cmdName, boolean flag, String message, String[] values) {
        this.cmdName = cmdName;
        this.flag = flag;
        this.message = message;
        this.argValues = values;
    }

    @Display("参数值")
    public String argValues() {
        if (argValues == null) {
            return null;
        }
        String str = "";
        for (String v : argValues) {
            str += v + ",";
        }
        return str;
    }

    public boolean isFlag() {
        return flag;
    }

    public VerifyInfo setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public VerifyInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public String[] getArgValues() {
        return argValues;
    }

    public String getArgValue(int index) {
        return argValues[index];
    }

    public VerifyInfo setArgValues(String[] argValues) {
        this.argValues = argValues;
        return this;
    }

    public String getCmdName() {
        return cmdName;
    }

    public VerifyInfo setCmdName(String cmdName) {
        this.cmdName = cmdName;
        return this;
    }
}
