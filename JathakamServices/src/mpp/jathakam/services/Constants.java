/*
 * Copyright (c) 2015, phani
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpp.jathakam.services;
import java.util.Arrays;
import java.util.List;
import swisseph.SweConst;

/**
 *
 * @author Phani
 */
public final class Constants
{
    public final static long HOURS_IN_MILLS = 60 * 60 * 1000;
    
    public final static long MINS_IN_MILLIS = 60 * 1000;

    public final static double HOURS_IN_MILLS_D = HOURS_IN_MILLS;
    
    public final static double SEC_IN_NANO_D = Math.pow(10.0, 9.0);
    
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
    
    public final static List<String> PLANET_NAMES = Arrays.asList("Sun", "Moon", "Mercury", "Venus", "Mars",
            "Jupiter", "Saturn",
            "Uranus", "Neptune", "Pluto",
            "Rahu",
            "Ketu",
            "Fortuna");

    /*
     * To use for above defined index
     */
    public final static int FORTUNA_INDEX = PLANET_NAMES.indexOf("Fortuna");
    
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
}

