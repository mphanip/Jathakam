/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpp.jatakamu.utils;

/**
 *
 * @author Phani Pramod M
 */
public final class MathUtils
{
    public static boolean hasNoFractionPart(Double value)
    {
        long longValue = value.longValue();
        double diff = value - longValue;
        boolean rtnValue = (diff == 0);

        return rtnValue;
    }
}
