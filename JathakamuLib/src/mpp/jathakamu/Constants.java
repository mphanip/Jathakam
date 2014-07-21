/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import swisseph.SweConst;

/**
 *
 * @author Phani
 */
public final class Constants
{
    public final static long MILLS_IN_HOURS = 60 * 60 * 1000;
    
    public final static double MILLS_IN_HOURS_D = MILLS_IN_HOURS;
    
    public final static long SECONDS_IN_HOURS = 60 * 60;
    
    public final static double SECONDS_IN_HOURS_D = 60 * 60;
    
    public final static double MINUTES_IN_HOURS = 60;
    
    public final static double MINUTES_IN_HOURS_D = MINUTES_IN_HOURS;
    
    public final static long MILLIS_IN_MINUTES = 60 * 1000;
    
    public final static long MILLIS_IN_SECONDS = 1000;
    
    public final static long MILLIS_IN_SECONDS_D = MILLIS_IN_SECONDS;

    public final static double NANOS_IN_SECONDS_D = Math.pow(10.0, 9.0);
    
    public final static long NANOS_IN_SECONDS = 1000000000L;
    
    public final static long NANOS_IN_MILLIS = 1000000L;
    
    public final static double NANOS_IN_MILLIS_D = NANOS_IN_MILLIS;
    
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
    
    public final static List<String> PLANET_NAMES_LIST = new ArrayList<>();
    public static final String DEG_SEP = "-";
    public static final String DATE_SEP = "/";
    public static final String TIME_SEP = ":";
    public static final NumberFormat DEG_NUM_FORMAT = NumberFormat.getNumberInstance();
    public static final NumberFormat NUM_FORMAT = NumberFormat.getNumberInstance();
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final NumberFormat MIN_NUM_FORMAT = NumberFormat.getNumberInstance();
    public static final String DATE_FORMATT_LONG = "EEE, dd/MM/yyyy HH:mm:ss";
    
    static 
    {
        PLANET_NAMES_LIST.addAll(Arrays.asList(PLANET_NAMES));

        NUM_FORMAT.setGroupingUsed(false);
        NUM_FORMAT.setMaximumFractionDigits(2);
        NUM_FORMAT.setMinimumFractionDigits(2);
        NUM_FORMAT.setMinimumIntegerDigits(2);

        DEG_NUM_FORMAT.setGroupingUsed(false);
        DEG_NUM_FORMAT.setMaximumFractionDigits(0);
        DEG_NUM_FORMAT.setMinimumFractionDigits(0);
        DEG_NUM_FORMAT.setMinimumIntegerDigits(3);

        MIN_NUM_FORMAT.setGroupingUsed(false);
        MIN_NUM_FORMAT.setMaximumFractionDigits(0);
        MIN_NUM_FORMAT.setMinimumFractionDigits(0);
        MIN_NUM_FORMAT.setMinimumIntegerDigits(2);
    }

    /*
     * To use for above defined index
     */
    public final static int FORTUNA_INDEX = PLANET_NAMES_LIST.indexOf("Fortuna");
    
//    public static enum PLANETS
//    {
//
//        KETU, VENUS, SUN, MOON, MARS, RAHU, JUPITER,
//        SATURN, MERCURY, URANUS, NETPUTE, PLUTO
//    };
    
    public static enum CUSP_AND_PLANETS
    {
        I, II, III, IV, V, VI, VII, VIII, IX, X,
        XI, XII,
        SUN, MOON, MERCURY, VENUS, MARS, JUPITER, SATURN,
        URANUS, NETPUTE, PLUTO, RAHU, KETU
    };
    
    public static enum ENTITY_TYPE {CUSP, PLANET};
    
    public static final String[] PLANET_SIGN =
    {
        "\u2609", // Sun
        "\u263d", // Moon
        "\u263f", // Mercury
        "\u2640", // Venus
        "\u2642", // Mars
        "\u2643", // Jupiter
        "\u2644", // Saturn
        "\u2645", // Uranus
        "\u2646", // Neptune
        "\u2647", // Pluto
        "\u260A", // Rahu
        "\u260B"  // Ketu
    };
    
    public static final int[] INT_AYANAMSA =
    {
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

    public static final String[] AYANAMSA =
    {
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

    /**
     * Star extent in minutes
     */ 
    public final static int STAR_EXTENT_IN_MINUTES = 800;

    public static enum SIGNIFICATOR_LEVELS
    {

        LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6
    };

    /**
     * GOOD - TRINE (120), BIQUINTILE (144), SEXTILE (60), QUINTILE(72),
     * TRIDECILE(108)
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
     * All multiples of 22.5 degrees are considered evil(BAD) i.e. 22.5, 45,
     * 67.5,
     * 90, 112.5, 135, 157.5, OPPOSITION (180), 202.5, 225, 247.5, 270, 292.5,
     * 315, 337.5,
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
    
    public static String DEFAULT_TIME_ZONE = "Asia/Calcutta";
    
}
