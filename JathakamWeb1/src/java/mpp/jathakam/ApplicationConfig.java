/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author phani
 */
@javax.ws.rs.ApplicationPath("ws")
public class ApplicationConfig extends Application
{

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(
            Set<Class<?>> resources)
    {
        resources.add(mpp.jathakam.JathakamService.class);
    }
    
}
