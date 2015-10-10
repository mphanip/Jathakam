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
package mpp.jathakam.ephimeris;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import mpp.jathakam.services.JathakamLogger;

/**
 *
 * @author phani
 */
public class EphemerisResources
{
    private final static List<String> SWISSS_EPHEMERIS_FILES = Arrays.asList("seas_18.se1",
            "semo_18.se1", "sepl_18.se1", "fixstars.cat", "sefstars.txt", "seorbel.txt");
    private final static String SWISSS_EPHEMERIS_FOLDER_NAME = "emphemerisFiles";
    public final static Path SWISSS_EPHEMERIS_PATH = Paths.get(SWISSS_EPHEMERIS_FOLDER_NAME).toAbsolutePath();
    
    static {
        setupEphemerisFiles();
    }
    
    private static void setupEphemerisFiles()
    {
        File swephDir = SWISSS_EPHEMERIS_PATH.toFile();
        
        if (swephDir.exists()) {
            return;
        }
        
        try
        {
            Files.createDirectories(SWISSS_EPHEMERIS_PATH);
        }
        catch (IOException ex)
        {
            JathakamLogger.LOGGER.log(Level.SEVERE, null, ex);
        }
        
        SWISSS_EPHEMERIS_FILES.parallelStream().forEach(resourceName -> {
            System.out.println("Copying the resource '" + resourceName + "'...");
            try (InputStream stream = EphemerisResources.class.getResourceAsStream(resourceName)) {
                if (stream != null) {
                    int readBytes;
                    byte[] buffer = new byte[100 * 1024];
                    String resFileName = Paths.get(SWISSS_EPHEMERIS_FOLDER_NAME, resourceName).toString();

                    try (OutputStream resStreamOut = new FileOutputStream(resFileName))
                    {
                        while ((readBytes = stream.read(buffer)) > 0)
                        {
                            resStreamOut.write(buffer, 0, readBytes);
                        }
                        resStreamOut.flush();
                    }
                }
            }
            catch (Exception ex)
            {
                JathakamLogger.LOGGER.throwing("EphemerisResource", "getResource", ex);
            }
        });
    }
    
    public static String getEphemerisPath() {
        return SWISSS_EPHEMERIS_PATH.toString();
    }

    public static void main(String[] args)
    {
//        EphemerisResources.setupEphemerisFiles();
        System.out.println("File Name = " + SWISSS_EPHEMERIS_PATH);
    }
}
