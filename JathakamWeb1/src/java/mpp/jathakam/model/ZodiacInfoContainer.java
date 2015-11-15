/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam.model;

import java.util.*;

/**
 *
 * @author phani
 */
public class ZodiacInfoContainer
{
    List<ZodiacInfo> infoList = new ArrayList<ZodiacInfo>();

    public ZodiacInfoContainer()
    {
        super();
    }
    
    public void add(ZodiacInfo zi) {
        infoList.add(zi);
    }
    
    public void remove(ZodiacInfo zi) {
        infoList.remove(zi);
    }
    
    public void clear() {
        infoList.clear();
    }
    
    public List<ZodiacInfo> getList() {
        return new ArrayList(infoList);
    }
}
