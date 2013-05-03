package mpp.jathakamu;

public class Jyothishyam
{
    public static Horoscope getNatalHoroscope()
    {
        return new NatalHoroscope();
    }
    
    public static Horoscope getHoraryHoroscope()
    {
        return new HoraryHoroscope();
    }
}
