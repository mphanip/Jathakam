/*
 * Copyright (c) 2015, phani
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpp.jathakam.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Phani
 */
public final class JathakamLogger
{

    public static final Logger LOGGER = Logger.getLogger("Jathakamu");

    static
    {
        String sLogLevel = System.getProperty("LOG_LEVEL", "INFO");
        Level logLevel = getLevel(sLogLevel);
        setLoggerSettings(LOGGER, "Jathakamu.log", logLevel);
    }

    public static void setLoggerSettings(Logger logger, String logFileName,
            Level logLevel)
    {
        try
        {
            FileHandler hand = new FileHandler(logFileName);
            SimpleFormatter formatter = new SimpleFormatter();
            hand.setFormatter(formatter);

            logger.addHandler(hand);

            if (logLevel != null)
            {
                logger.setLevel(logLevel);
                return;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JathakamLogger.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        catch (SecurityException ex)
        {
            Logger.getLogger(JathakamLogger.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private static Level getLevel(String logLevel)
    {
        if (logLevel.equalsIgnoreCase("OFF"))
        {
            return Level.OFF;
        }
        else if (logLevel.equalsIgnoreCase("SEVERE"))
        {
            return Level.SEVERE;
        }
        else if (logLevel.equalsIgnoreCase("WARNING"))
        {
            return Level.WARNING;
        }
        else if (logLevel.equalsIgnoreCase("INFO"))
        {
            return Level.FINE;
        }
        else if (logLevel.equalsIgnoreCase("FINE"))
        {
            return Level.FINE;
        }
        else if (logLevel.equalsIgnoreCase("FINER"))
        {
            return Level.FINER;
        }
        else if (logLevel.equalsIgnoreCase("FINEST"))
        {
            return Level.FINEST;
        }
        else if (logLevel.equalsIgnoreCase("ALL"))
        {
            return Level.ALL;
        }

        return Level.INFO;
    }

}
