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

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import mpp.jathakam.services.types.AYANAMSA;
import mpp.jathakam.services.types.HORARY_NUMBER_SET;

import static swisseph.SweConst.SEFLG_SIDEREAL;
import static swisseph.SweConst.SEFLG_SPEED;
import static swisseph.SweConst.SE_HSYS_PLACIDUS;
//import static swisseph.SweConst.SE_SIDM_KRISHNAMURTI;
import static swisseph.SweConst.SE_SIDM_USER;

/**
 * These setting are per instance (may be user). These settings are intended to use
 * with each instance of SwissEph
 *
 * @author Phani
 */
@XmlRootElement(name="settings")
public final class ProfileSettings implements Serializable
{
    public final static ProfileSettings DEFAULT = new ProfileSettings();
    
    static {
        DEFAULT.setKPNewAynamsa();
        DEFAULT.setUseGeocentric(false);
    }
    
    @XmlAttribute(name = "year")
    private double oneYear = 365.2425D;

    @XmlAttribute(name = "gc")
    private boolean useGeocentric = true;

    /**
     * Should be one of the constant swisseph.SweConst.SE_SIDM_*. Default is
     * SE_SIDM_KRISHNAMURTI
     */
    @XmlAttribute(name = "ay")
    private AYANAMSA ayanamsa = AYANAMSA.KRISHNAMURTI;

    /**
     * Ephemeris flags to use. Default is set to get retrograde planets
     */
    @XmlAttribute(name = "seflags")
    private int ephemerisFlags = SEFLG_SPEED;

    /**
     * Default is Placidus system. This will not change mostly, but just in case
     * if someone needs this value changed.
     */
    @XmlAttribute(name = "hs")
    private int houseSystem = SE_HSYS_PLACIDUS;

    /**
     * Value of nodes (Rahu and Ketu) computation, required for Swiss Ephemeris.
     */
    @XmlAttribute(name = "tn")
    private boolean trueNode = true;

    /**
     * Used for Horary charts. Default NONE means it is Natal chart.
     */
    private transient HORARY_NUMBER_SET horaryNumberGroup = HORARY_NUMBER_SET.HORARY_NONE;

    public ProfileSettings()
    {
        super();
    }

    public boolean deductAyanamsa()
    {
        return ((ephemerisFlags & SEFLG_SIDEREAL) == 0);
    }

    /**
     * set KP Old Ayanamsa. Swiss Ephemeris does not have this option.
     */
    public void setKPOldAynamsa()
    {
        ayanamsa = AYANAMSA.OLD_KRISHNAMURTI;
    }

    /**
     * Looks like there is some difference in swiss ephemeris calculation and
     * this one.
     */
    public void setKPNewAynamsa()
    {
        ayanamsa = AYANAMSA.NEW_KRISHNAMURTI;
    }

    protected void setAynamsa(AYANAMSA ayan)
    {
        this.ayanamsa = ayan;
    }

    protected void setAynamsa(double t0, double initialValue)
    {
        ayanamsa = AYANAMSA.USER_DEFINED;
        ayanamsa.setT0(t0);
        ayanamsa.setInitialValue(initialValue);
    }
    
    public AYANAMSA getAyanamsa() {
        return ayanamsa;
    }

    public double[] getUserDefinedAynamsa()
    {
        return new double[]
        {
            SE_SIDM_USER, ayanamsa.getT0(), ayanamsa.getInitialValue()
        };
    }

    public double getOneYear()
    {
        return oneYear;
    }

    protected void setOneYear(double oneYear)
    {
        this.oneYear = oneYear;
    }

    public boolean isUseGeocentric()
    {
        return useGeocentric;
    }

    protected void setUseGeocentric(boolean useGeocentric)
    {
        this.useGeocentric = useGeocentric;
    }

    public int getEphemerisFlags()
    {
        return ephemerisFlags;
    }

    protected void setEphemerisFlags(int ephemerisFlags)
    {
        this.ephemerisFlags = ephemerisFlags;
    }

    public int getHouseSystem()
    {
        return houseSystem;
    }

    protected void setHouseSystem(int houseSystem)
    {
        this.houseSystem = houseSystem;
    }

    public boolean isTrueNode()
    {
        return trueNode;
    }

    protected void setTrueNode(boolean trueNode)
    {
        this.trueNode = trueNode;
    }

    public HORARY_NUMBER_SET getHoraryNumberGroup()
    {
        return horaryNumberGroup;
    }

    protected void setHoraryNumberGroup(
            HORARY_NUMBER_SET horaryNumberGroup)
    {
        this.horaryNumberGroup = horaryNumberGroup;
    }
}
