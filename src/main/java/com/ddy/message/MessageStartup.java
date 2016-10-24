/**
 *
 */
package com.ddy.message;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.PropertyConfigurator;

import com.ddy.message.core.ContextHolder;

/**
 * @author wgm
 */
public class MessageStartup {

    private static volatile boolean running = true;

    public static void main(String[] args) {
    	Properties props = new Properties();
		try {
			props.load(MessageStartup.class.getResourceAsStream("/log/log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		} 

		PropertyConfigurator.configure(props);
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/spring-*.xml");
            ContextHolder.setCtx(context);
            context.start();
        } catch (Throwable e) {
        	e.printStackTrace();
        }
        try {
            synchronized (MessageStartup.class) {
                while (running) {
                    MessageStartup.class.wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }
}
