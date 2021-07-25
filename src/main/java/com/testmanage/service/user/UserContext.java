package com.testmanage.service.user;

import com.testmanage.entity.MyUser;

public class UserContext {
    // 构造方法私有化
    private UserContext() {
    }

    ;

    private static final ThreadLocal<MyUser> context = new ThreadLocal<>();

    /**
     * 存放用户信息
     *
     * @param myUser
     */
    public static void set(MyUser myUser) {
        context.set(myUser);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static MyUser get() {
        return context.get();
    }

    /**
     * 清除当前线程内引用，防止内存泄漏
     */
    public static void remove() {
        context.remove();
    }
}

