/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpp.jathakamu.types;

import java.util.Comparator;

/**
 *
 * @author Phani Pramod M
 */
public class HouseEntityComparator
    implements Comparator<HouseEntity>
{
    private boolean isAscending = true;

    public HouseEntityComparator()
    {
        this(true);
    }
    
    public HouseEntityComparator(boolean ascending)
    {
        isAscending = ascending;
    }

    @Override
    public int compare(HouseEntity o1, HouseEntity o2)
    {
        if (this.isAscending)
            return o1.compareTo(o2);
        else
            return o2.compareTo(o1);
    }
}
