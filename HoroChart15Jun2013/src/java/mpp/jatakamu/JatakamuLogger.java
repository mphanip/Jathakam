/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpp.jatakamu;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Phani Pramod M
 */
public final class JatakamuLogger
{
    public static final Logger LOGGER = Logger.getLogger("mpp.jatakamu");
    
    static
    {
//        System.out.println("Initializing Logger...");
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
//        System.out.println("Logger Initialized...");

    }
    
    /**
     *
     * @param mesg
     */
    public static void log(String mesg)
    {
        log(Level.INFO, mesg);
    }
    
    /**
     *
     * @param level
     * @param mesg
     */
    public static void log(Level level, String mesg)
    {
        String[] clsMethodName = getClassMethodName();
//        LOGGER.logp(level, clsMethodName[0], clsMethodName[1], mesg);
        System.out.printf("[%s][%s][%s]-[%s]", (new Date()).toString(),
            clsMethodName[0], clsMethodName[1], mesg);
    }
    
    private static String[] getClassMethodName()
    {
        String[] names = {"", ""};
        
        StackTraceElement[] stack = new Throwable().getStackTrace();
        int i = 0;
        for (StackTraceElement elem : stack)
        {
            String className = elem.getClassName();
            if (className.equals(JatakamuLogger.class.getName()))
            {
                i++;
                continue;
            }

            names[0] = stack[i].getClassName();
            // Just in case, as per JVM spec getClassName should not be null.
            if (names[0] == null)
            {
                names[0] = "";
            }

            // Just in case, as per JVM spec getMethodName should not be null.
            names[1] = stack[i].getMethodName();
            if (names[1] == null)
            {
                names[1] = "";
            }
            return names;
        }
        
        return names;
    }
}
