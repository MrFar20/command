package pers.mrwangx.tools.command;

import pers.mrwangx.commons.tool.display.DisplayProcessor;
import pers.mrwangx.commons.tool.display.annotation.Display;
import pers.mrwangx.commons.tool.reflect.ReflectUtil;
import pers.mrwangx.tools.command.annotation.Cmd;
import pers.mrwangx.tools.command.entity.CmdInfo;
import pers.mrwangx.tools.command.entity.VerifyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 18:57
 * \* Description:
 **/
public class CommandProcesser {

    private Map<String, CmdInfo> cmdInfoMap;
    private Object targetObj;
    private Map<String, Method> processors;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CommandProcesser processer = new CommandProcesser("pers.mrwangx.tools.command.command");
        processer.initProcessors(processer);
        String in = input.nextLine();
        processer.process(in);
    }

    public CommandProcesser(String packageName) {
        this.cmdInfoMap = loadCommands(packageName);
    }

    public CommandProcesser(String packageName, Object targetObj) {
        this.cmdInfoMap = loadCommands(packageName);
        this.initProcessors(targetObj);
    }

    public void initProcessors(Object targetObj) {
        this.targetObj = targetObj;
        processors = loadProcessors(targetObj);
    }

    public CmdInfo getCmdInfoFromInput(String input) {
        String[] values = input.split(" ");
        CmdInfo cmdInfo = cmdInfoMap.get(values[0]);
        return cmdInfo;
    }

    public CmdInfo getCmdInfo(String cmd) {
        return cmdInfoMap.get(cmd);
    }

    public void process(String input) {
        VerifyInfo verifyInfo = this.verifyInput(input);
        if (verifyInfo.isFlag()) {
            Method m = processors.get(verifyInfo.getCmdName());
            if (m == null) {
                System.out.println("没有相应的处理方法");
                return;
            }
            try {
                m.invoke(targetObj, verifyInfo.getArgValues());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(verifyInfo.getMessage());
        }
    }

    @Display("命令")
    public String cmdInfos() {
        String lineSeparator = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        builder.append(lineSeparator);
        cmdInfoMap.forEach((k, v) -> {
            builder.append(lineSeparator + DisplayProcessor.toString(v) + lineSeparator);
        });
        return builder.toString();
    }


    public VerifyInfo verifyInput(String input) {
        CmdInfo cmdInfo = getCmdInfoFromInput(input);
        if (cmdInfo == null) {
            return new VerifyInfo().setFlag(false).setMessage("没有此命令");
        }
        return verifyCommand(input, cmdInfo);
    }


    /**
     * 验证命令
     *
     * @param input
     * @param cmdInfo
     * @return
     */
    public static VerifyInfo verifyCommand(String input, CmdInfo cmdInfo) {
        VerifyInfo verifyInfo = new VerifyInfo();
        verifyInfo.setCmdName(cmdInfo.getCmdName());
        if (cmdInfo == null) {
            return verifyInfo.setFlag(false).setMessage("命令信息不能为空");
        }

        String[] values = input.split(" ");
        String[] argValues = new String[values.length - 1];
        for (int i = 0; i < argValues.length; i++) {
            argValues[i] = values[i + 1];
        }
        verifyInfo.setArgValues(argValues);

        if (values.length - 1 != cmdInfo.argNum()) {
            return verifyInfo.setFlag(false).setMessage("参数个数不正确");
        }
        List<CmdInfo.ArgInfo> argInfos = cmdInfo.getArgInfos();
        for (int i = 0; i < argValues.length; i++) {
            CmdInfo.ArgInfo argInfo = argInfos.get(i);
            if (!verifyArg(argValues[i], argInfo)) { //验证正则表达式
                return verifyInfo.setFlag(false).setMessage("参数[" + argInfo.getArgName() + "]错误");
            }
        }
        return verifyInfo.setFlag(true).setMessage("参数正确");
    }


    /**
     * 验证参数
     *
     * @param arg
     * @param argInfo
     * @return
     */
    public static boolean verifyArg(String arg, CmdInfo.ArgInfo argInfo) {
        return arg.matches(argInfo.getRegex());
    }

    /**
     * 获取命令
     *
     * @param packageName
     * @return
     */
    public static Map<String, CmdInfo> loadCommands(String packageName) {
        Map<String, CmdInfo> cmdInfoMap = new HashMap<>();
        Set<Class> classes = ReflectUtil.scanClasses(packageName);
        classes.forEach(clazz -> {
            if (clazz.isAnnotationPresent(Cmd.class)) {
                Cmd cmd = (Cmd) clazz.getAnnotation(Cmd.class);
                CmdInfo cmdInfo = new CmdInfo();
                //设置命令名
                if (cmd.name().equals("")) {
                    cmdInfo.setCmdName(clazz.getSimpleName().toLowerCase());
                } else {
                    cmdInfo.setCmdName(cmd.name());
                }

                //获取参数
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Cmd.Arg.class)) {
                        CmdInfo.ArgInfo argInfo = new CmdInfo.ArgInfo();
                        Cmd.Arg arg = field.getAnnotation(Cmd.Arg.class);
                        //设置参数名
                        if (arg.argName().equals("")) {
                            argInfo.setArgName(field.getName());
                        } else {
                            argInfo.setArgName(arg.argName());
                        }
                        argInfo.setRegex(arg.regex());
                        argInfo.setDescription(arg.desc());
                        cmdInfo.addArg(argInfo);
                    }
                }
                Collections.sort(cmdInfo.getArgInfos());
                cmdInfo.setDescription(cmd.desc());
                cmdInfo.setSeparator(cmd.separator());
                cmdInfoMap.put(cmdInfo.getCmdName(), cmdInfo);
            }
        });
        return cmdInfoMap;
    }


    public static Map<String, Method> loadProcessors(Object targetObj) {
        Map<String, Method> processors = new HashMap<>();
        if (targetObj != null) {
            Class clazz = targetObj.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(Cmd.Processor.class)) {
                    Cmd.Processor processor = m.getAnnotation(Cmd.Processor.class);
                    String value = processor.value();
                    String name = "";
                    if (value.equals("")) {
                        name = m.getName();
                    } else {
                        name = value;
                    }
                    m.setAccessible(true);
                    processors.put(name, m);
                }
            }
        }
        return processors;
    }

}
