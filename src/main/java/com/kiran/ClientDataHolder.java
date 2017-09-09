package com.kiran;

import org.springframework.context.ApplicationContext;

/**
 * @author Kiran
 * @since 9/9/17
 */
public class ClientDataHolder {
    private static ApplicationContext appCtx;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (appCtx == null) {
            appCtx = applicationContext;
        } else {
            throw new RuntimeException("Illegal attempt to change Spring application context.");
        }
    }
}
