package pers.mrwangx.tools.command.command;

import pers.mrwangx.tools.command.annotation.Cmd;

import java.lang.reflect.Method;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 23:12
 * \* Description:
 **/
@Cmd(desc = "登录")
public class Login {

    @Cmd.Arg(desc = "登录教务在线的账号", argName = "学号", regex = "^[0-9]{10}$")
    String sno;
    @Cmd.Arg(desc = "登录教务在线的密码", argName = "密码")
    String password;

}
