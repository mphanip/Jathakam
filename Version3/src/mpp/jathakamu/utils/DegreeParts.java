package mpp.jathakamu.utils;

public class DegreeParts
{
    public int degree;
    public int minutes;
    public double seconds;
    
    public DegreeParts(int degree, int minutes, double seconds)
    {
        super();
        this.degree = degree;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    
    public boolean isRaasiBegining()
    {
        boolean flag = ((degree == 0.0 || degree == 30.0) && minutes == 0.0 
                && seconds == 0.0);
        
        return flag;
    }
}
