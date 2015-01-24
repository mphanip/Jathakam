/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import static mpp.jathakamu.JathakamLogger.LOGGER;

/**
 *
 * @author phani
 */
public final class Range
    implements Serializable
{

    public final static Range EMPTY = new Range(0, 0);

    private final Number min;
    private final Number max;

    private Range(Number min, Number max)
    {
        this.min = min;
        this.max = max;
    }

    private Range(Builder builder)
    {
        this.min = builder.min;
        this.max = builder.max;
    }

    public static class Builder
    {

        private Number min = 0.0;
        private Number max = 0.0;

        public Range.Builder values(Number min, Number max)
        {
            this.min = min;
            this.max = max;
            return this;
        }

        public Range.Builder min(Number min)
        {
            this.min = min;
            return this;
        }

        public Range.Builder max(Number max)
        {
            this.max = max;
            return this;
        }

        public Range build()
        {
            return new Range(this);
        }
    }

    public Number getMin()
    {
        return min;
    }

    public Number getMax()
    {
        return max;
    }
    
    public boolean inRange(Number num)
    {
       double minValue = min.doubleValue();
       double maxValue = max.doubleValue();
       double givenValue = num.doubleValue();
       
       LOGGER.log(Level.FINEST, "Range = [{0}-{1}], check number = {2}", new Object[]{minValue, maxValue, givenValue});
       
       boolean rtnValue = (givenValue >= minValue && givenValue < maxValue);
       
       return rtnValue;
    }
    
//    public Comparator getComparator(Range r1, Range r2)
//    {
//        Comparator comp = (Object o1, Object o2) ->
//        {
//            if (!(o1 instanceof Range) || !(o2 instanceof Range))
//            {
//                return 0;
//            }
//            
//            double value = ((Range) o1).min.doubleValue() - ((Range) o2).min.doubleValue();
//            return Double.valueOf(value).intValue();
//        };
//        
//        return comp;
//    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.min);
        hash = 89 * hash + Objects.hashCode(this.max);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        final Range other = (Range) obj;
        
        boolean flag = (this.min == other.min && this.max == other.max);
        
        return flag;
    }

    @Override
    public String toString()
    {
        return "[" + min + ".." + max + "]";
    }

}
