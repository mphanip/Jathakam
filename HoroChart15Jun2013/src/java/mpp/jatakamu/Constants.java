
package mpp.jatakamu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phani Pramod M
 */
public final class Constants
{
    public static final long L_MINS_IN_MILLIS = 60 * 1000;
    public static final double D_HOURS_IN_MILLS = 60.0 * 60.0 * 1000.0;
    public static final long L_HOURS_IN_MILLS = 60 * 60 * 1000;
    public static final String[] PLANET_NAME = {"KE", "VE", "SU", "MO", "MA",
        "RA", "JU", "SA", "ME"};
    public static enum PLANETS {KETU, VENUS, SUN, MOON, MARS, RAHU, JUPITER,
                               SATURN, MERCURY, URANUS, NETPUTE, PLUTO};
    public static enum CUSP_AND_PLANTS {I, II, III, IV, V, VI, VII, VIII, IX, X,
                                    XI, XII,
                                    KETU, VENUS, SUN, MOON, MARS, RAHU, JUPITER,
                                    SATURN, MERCURY, URANUS, NETPUTE, PLUTO};
    public static enum ENTITY_TYPE {CUSP, PLANET};
    public static final String[] PLANET_SIGN = {"\u260B", "\u2640", "\u2609",
        "\u263d", "\u2642", "\u260A", "\u2643", "\u2644", "\u263f", "\u2645",
        "\u2646", "\u2647" };
    public static final List<String> PLANET_NAMES_LIST = new ArrayList<>(9);

    static
    {
        PLANET_NAMES_LIST.add("KETU");
        PLANET_NAMES_LIST.add("VENUS");
        PLANET_NAMES_LIST.add("SUN");
        PLANET_NAMES_LIST.add("MOON");
        PLANET_NAMES_LIST.add("MARS");
        PLANET_NAMES_LIST.add("RAHU");
        PLANET_NAMES_LIST.add("JUPITER");
        PLANET_NAMES_LIST.add("SATURN");
        PLANET_NAMES_LIST.add("MERCURY");
    }
}
