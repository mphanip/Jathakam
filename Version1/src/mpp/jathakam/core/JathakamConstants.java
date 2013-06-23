/*
 *
 *
 */
package mpp.jathakam.core;

/**
 *
 * @author phani
 */
public interface JathakamConstants
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
    
    public static enum AYANAMSA
    {

        SE_SIDM_FAGAN_BRADLEY,
        SE_SIDM_LAHIRI,
        SE_SIDM_DELUCE,
        SE_SIDM_RAMAN,
        SE_SIDM_USHASHASHI,
        SE_SIDM_KRISHNAMURTI,
        SE_SIDM_DJWHAL_KHUL,
        SE_SIDM_YUKTESHWAR,
        SE_SIDM_JN_BHASIN,
        SE_SIDM_BABYL_KUGLER1,
        SE_SIDM_BABYL_KUGLER2,
        SE_SIDM_BABYL_KUGLER3,
        SE_SIDM_BABYL_HUBER,
        SE_SIDM_BABYL_ETPSC,
        SE_SIDM_ALDEBARAN_15TAU,
        SE_SIDM_HIPPARCHOS,
        SE_SIDM_SASSANIAN,
        SE_SIDM_GALCENT_0SAG,
        SE_SIDM_J2000,
        SE_SIDM_J1900,
        SE_SIDM_B1950,
        SE_SIDM_USER;

        public int getOrdinal()
        {
            if (ordinal() == SE_SIDM_USER.ordinal())
            {
                return 255;
            }

            return ordinal();
        }
    };
    /*
     * use JPL ephemeris
     */
    public final static int SEFLG_JPLEPH = 1;

    /*
     * use SWISSEPH ephemeris
     */
    public final static int SEFLG_SWIEPH = 2;

    /*
     * use Moshier ephemeris
     */
    public final static int SEFLG_MOSEPH = 4;

    /*
     * return heliocentric position
     */
    public final static int SEFLG_HELCTR = 8;

    /*
     * return true positions, not apparent
     */
    public final static int SEFLG_TRUEPOS = 16;

    /*
     * no precession, i.e. give J2000 equinox
     */
    public final static int SEFLG_J2000 = 32;

    /*
     * no nutation, i.e. mean equinox of date
     */
    public final static int SEFLG_NONUT = 64;
// Commenting out below since we should not use it.
    /*
     * speed from 3 positions (do not use it, SEFLG_SPEED is faster and more
     * precise.)
     */
    //public final static int SEFLG_SPEED3 = 128;
    /*
     * high precision speed
     */
    public final static int SEFLG_SPEED = 256;

    /*
     * turn off gravitational deflection
     */
    public final static int SEFLG_NOGDEFL = 512;

    /*
     * turn off 'annual' aberration of light
     */
    public final static int SEFLG_NOABERR = 1024;

    /*
     * equatorial positions are wanted
     */
    public final static int SEFLG_EQUATORIAL = (2 * 1024);

    /*
     * cartesian, not polar, coordinates
     */
    public final static int SEFLG_XYZ = (4 * 1024);

    /*
     * coordinates in radians, not degrees
     */
    public final static int SEFLG_RADIANS = (8 * 1024);

    /*
     * barycentric positions
     */
    public final static int SEFLG_BARYCTR = (16 * 1024);

    /*
     * topocentric positions
     */
    public final static int SEFLG_TOPOCTR = (32 * 1024);

    /*
     * sidereal positions
     */
    public final static int SEFLG_SIDEREAL = (64 * 1024);

    /*
     * ICRS (DE406 reference frame)
     */
    public final static int SEFLG_ICRS = (128 * 1024);

    public final static char SE_HSYS_PLACIDUS = 'P';
    public final static char SE_HSYS_KOCH = 'K';
    public final static char SE_HSYS_PORPHYRIUS = 'O';
    public final static char SE_HSYS_REGIOMONTANUS = 'R';
    public final static char SE_HSYS_CAMPANUS = 'C';

    /*
     * cusp 1 is Ascendant
     */
    public final static char SE_HSYS_EQUAL = 'E';

    /*
     * Asc. in middle of house 1
     */
    public final static char SE_HSYS_VEHLOW = 'V';
    public final static char SE_HSYS_WHOLE_SIGN = 'W';
    public final static char SE_HSYS_AXIAL_ROTATION_SYSTEM = 'X';
    public final static char SE_HSYS_AZIMUTHAL_OR_HORIZONTAL_SYSTEM = 'H';
    public final static char SE_HSYS_POLICH_OR_PAGE_OR_TOPOCENTRIC_SYSTEM = 'T';
    public final static char SE_HSYS_ALCABITUS = 'B';
    public final static char SE_HSYS_MORINUS = 'M';
    public final static char SE_HSYS_KRUSINSKI_PISA = 'U';
    public final static char SE_HSYS_GAUQUELIN_SECTORS = 'G';

    public final static int STAR_EXTENT = 800;
}
