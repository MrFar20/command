package pers.mrwangx.tools.command.entity;

import pers.mrwangx.commons.tool.display.DisplayProcessor;
import pers.mrwangx.commons.tool.display.annotation.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 23:25
 * \* Description:
 **/
public class CmdInfo {

    @Display(value = "命令名称")
    private String cmdName;

    @Display(value = "命令描述")
    private String description;

    @Display(value = "参数输入间隔")
    private String separator;

    private List<ArgInfo> argInfos;

    public CmdInfo() {
        argInfos = new ArrayList<>();
    }

    public CmdInfo(String name, String description, String separator) {
        this();
        this.cmdName = name;
        this.description = description;
        this.separator = separator;
    }

    public static class ArgInfo implements Comparable<ArgInfo> {

        @Display("参数名")
        private String argName;
        @Display("正则表达式")
        private String regex;
        @Display("描述")
        private String description;
        @Display
        private int order;

        public ArgInfo() {
        }

        public ArgInfo(String argName, String regex, String description) {
            this.argName = argName;
            this.regex = regex;
            this.description = description;
        }

        public String getArgName() {
            return argName;
        }

        public void setArgName(String argName) {
            this.argName = argName;
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getOrder() {
            return order;
        }

        public ArgInfo setOrder(int order) {
            this.order = order;
            return this;
        }

        @Override
        public int compareTo(ArgInfo o) {
            if (this.order == o.getOrder()) {
                return 0;
            } else if (this.order < o.getOrder()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public boolean addArg(ArgInfo argInfo) {
        return this.argInfos.add(argInfo);
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Display(value = "参数个数")
    public int argNum() {
        return argInfos.size();
    }

    @Display(value = "参数信息")
    public String argInfos() {
        String lineSepartor = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator());
        this.argInfos.forEach(argInfo -> {
            builder.append("+-------------------+" + lineSepartor);
            builder.append(DisplayProcessor.toString(argInfo) + lineSepartor);
        });
        builder.append("+-------------------+");
        return builder.toString();
    }

    public List<ArgInfo> getArgInfos() {
        return argInfos;
    }

    public void setArgInfos(List<ArgInfo> argInfos) {
        this.argInfos = argInfos;
    }

    public int getArgNum() {
        return this.argInfos.size();
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
