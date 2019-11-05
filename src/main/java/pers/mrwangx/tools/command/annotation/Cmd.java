package pers.mrwangx.tools.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 18:58
 * \* Description:
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {

    String name() default "";

    String desc() default "";

    String separator() default " ";

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Arg {

        String regex() default ".*";

        String argName() default "";

        String desc() default "";

        int order() default 0;
    }



}
