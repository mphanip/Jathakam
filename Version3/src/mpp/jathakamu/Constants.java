package mpp.jathakamu;

import swisseph.SweConst;

public final class Constants
{
    public final static String[] PLANET_NAMES =
    {
        "Sun", "Moon", "Mercury", "Venus", "Mars", "Jupiter", "Saturn",
        "Uranus", "Neptune", "Pluto",
        "Rahu",
        "Ketu",
        "Fortuna"
    };
    
    public final static String[] PLANET_SHORT_NAMES =
    {
        "SUN", "MOO", "MER", "VEN", "MAR", "JUP", "SAT",
        "URA", "NEP", "PLU",
        "RAH",
        "KET",
        "FOR"
    };
    
    public final static String[] PLANET_2_LETTER_NAMES =
    {
        "SU", "MO", "ME", "VE", "MA", "JU", "SA",
        "UR", "NE", "PL",
        "RA",
        "KE",
        "FO"
    };
    
    public final static int FORTUNA = 12;
    
    public static final int []INT_AYANAMSA = {
        SweConst.SE_SIDM_FAGAN_BRADLEY,
        SweConst.SE_SIDM_LAHIRI,
        SweConst.SE_SIDM_DELUCE,
        SweConst.SE_SIDM_RAMAN,
        SweConst.SE_SIDM_USHASHASHI,
        SweConst.SE_SIDM_KRISHNAMURTI,
        SweConst.SE_SIDM_DJWHAL_KHUL,
        SweConst.SE_SIDM_YUKTESHWAR,
        SweConst.SE_SIDM_JN_BHASIN,
        SweConst.SE_SIDM_BABYL_KUGLER1,
        SweConst.SE_SIDM_BABYL_KUGLER2,
        SweConst.SE_SIDM_BABYL_KUGLER3,
        SweConst.SE_SIDM_BABYL_HUBER,
        SweConst.SE_SIDM_BABYL_ETPSC,
        SweConst.SE_SIDM_ALDEBARAN_15TAU,
        SweConst.SE_SIDM_HIPPARCHOS,
        SweConst.SE_SIDM_SASSANIAN,
        SweConst.SE_SIDM_GALCENT_0SAG,
        SweConst.SE_SIDM_J2000,
        SweConst.SE_SIDM_J1900,
        SweConst.SE_SIDM_B1950,
        SweConst.SE_SIDM_USER
    };
    
    public static final String []AYANAMSA = {
        "Fagan Bradley",
        "Lahiri",
        "Deluce",
        "Raman",
        "Usha Shashi",
        "Prof. K Krishna Murti",
        "Djwhal Khul",
        "Yukteshwar",
        "J N Bhasin",
        "Babyl Kugler1",
        "Babyl Kugler2",
        "Babyl Kugler3",
        "Babyl Huber",
        "Babyl ETPSC",
        "Aldebaran 15TAU",
        "Hipparchos",
        "Sassanian",
        "Galcent 0SAG",
        "J2000",
        "J1900",
        "B1950",
        "User Defined"
    };
    
    public final static int HSYS_PLACIDUS = (int) 'P';

    public final static int HSYS_KOCH = (int) 'K';

    public final static int HSYS_PORPHYRIUS = (int) 'O';

    public final static int HSYS_REGIOMONTANUS = (int) 'R';

    public final static int HSYS_CAMPANUS = (int) 'C';

    public final static int HSYS_CUSP_1_ASC = (int) 'A';

    public final static int HSYS_ASC_IN_MIDDLE_OF_HOUSE_1 = (int) 'V';

    public final static int HSYS_MERIDIAN_HOUSES = (int) 'X';

    public final static int HSYS_AZIMUTHAL = (int) 'H';

    public final static int HSYS_POLICH = (int) 'T';

    public final static int HSYS_ALCABITIUS = (int) 'B';

    // Stat extent in minutes
    public final static int STAR_EXTENT_IN_MINUTES = 800;
    
    public static enum SIGNIFICATOR_LEVELS {LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6};
    
    /**
     * GOOD - TRINE (120), BIQUINTILE (144), SEXTILE (60), QUINTILE(72), TRIDECILE(108)
     * 
     * MODERATELY_GOOD - DECILE SEMI-QUINTILE(36)
     * 
     * SLIGHTLY_GOOD - SEMI-SEXTILE(30), VIGINTILE(18), QUINDECILE(24), 54, 162
     * 
     * BAD - QUINCUNX (150), SQUARE(90), 
     * 
     * MODERATELY_BAD - SESQUIQUADRATE (135), SEMI-ANGULAR(45)
     * 
     * SLIGHTLY_BAD -
     * 
     * All multiples of 22.5 degrees are considered evil(BAD) i.e. 22.5, 45, 67.5,
     * 90, 112.5, 135, 157.5, OPPOSITION (180), 202.5, 225, 247.5, 270, 292.5, 315, 337.5,
     * 
     * 
     */
    public static enum ASPECT_EFFECT
    {
        NONE, GOOD, MODERATELY_GOOD, SLIGHTLY_GOOD, CONJUNCTION, BAD,
        MODERATELY_BAD, SLIGHTLY_BAD
    };
    
    public static enum HORARY_NUMBER_SET
    {
        HORARY_NONE, HORARY_249, HORARY_2193
    };
}
