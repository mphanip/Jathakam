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
public class RaasiEntityComparator
    implements Comparator<RaasiEntity>
{
    private boolean isAscending = true;

    public RaasiEntityComparator()
    {
        this(true);
    }
    
    public RaasiEntityComparator(boolean ascending)
    {
        isAscending = ascending;
    }

    @Override
    public int compare(RaasiEntity o1, RaasiEntity o2)
    {
        if (this.isAscending)
            return o1.compareTo(o2);
        else
            return o2.compareTo(o1);
    }
}
