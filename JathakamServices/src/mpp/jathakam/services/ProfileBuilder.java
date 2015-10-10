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

import mpp.jathakam.services.types.AYANAMSA;
import mpp.jathakam.services.types.HORARY_NUMBER_SET;
import mpp.jathakam.services.types.Place;

import static swisseph.SweConst.SEFLG_SPEED;
import static swisseph.SweConst.SEFLG_SIDEREAL;
import static swisseph.SweConst.SE_HSYS_PLACIDUS;

/**
 *
 * @author phani
 */
public final class ProfileBuilder
{

    protected double tjd_ut = 0.0;
    protected int horaryNumber = 0;
    protected Place place;
    protected double oneYear = Defaults.ONE_YEAR;
    protected boolean useGeocentric = true;
    protected AYANAMSA ayanamsa = AYANAMSA.KRISHNAMURTI;
    protected int ephemerisFlags = SEFLG_SPEED;
    protected int houseSystem = SE_HSYS_PLACIDUS;
    protected boolean trueNode = true;
    protected HORARY_NUMBER_SET horaryNumberGroup = HORARY_NUMBER_SET.HORARY_NONE;

    public ProfileBuilder tjd_ut(double tjd_ut)
    {
        this.tjd_ut = tjd_ut;
        return this;
    }

    public ProfileBuilder horaryNumber(int horaryNumber)
    {
        this.horaryNumber = horaryNumber;
        return this;
    }

    public ProfileBuilder place(Place place)
    {
        this.place = place;
        return this;
    }

    public ProfileBuilder oneYear(double oneYear)
    {
        this.oneYear = oneYear;
        return this;
    }

    public ProfileBuilder useGeocentric(boolean useGeocentric)
    {
        this.useGeocentric = useGeocentric;
        return this;
    }

    public ProfileBuilder ayanamsa(AYANAMSA ayanamsa)
    {
        this.ayanamsa = ayanamsa;
        return this;
    }

    public ProfileBuilder userDefinedAyanamsa(double userAyanamsaT0, double userAyanamsaInitialValue)
    {
        ayanamsa = AYANAMSA.USER_DEFINED;
        ayanamsa.setT0(userAyanamsaT0);
        ayanamsa.setInitialValue(userAyanamsaInitialValue);
        return this;
    }

    public ProfileBuilder ephemerisFlags(int ephemerisFlags)
    {
        this.ephemerisFlags = ephemerisFlags;
        return this;
    }
    
    public ProfileBuilder addEphemerisFlag(int flag)
    {
        this.ephemerisFlags |= flag;
        return this;
    }
    
    public ProfileBuilder useNirayana() {
        this.ephemerisFlags |= SEFLG_SIDEREAL;
        return this;
    }

    public ProfileBuilder houseSystem(int houseSystem)
    {
        this.houseSystem = houseSystem;
        return this;
    }

    public ProfileBuilder trueNode(boolean trueNode)
    {
        this.trueNode = trueNode;
        return this;
    }

    public ProfileBuilder horaryNumberGroup(HORARY_NUMBER_SET horaryNumberGroup)
    {
        this.horaryNumberGroup = horaryNumberGroup;
        return this;
    }
    
    public ProfileBuilder settings(ProfileSettings profileOps) {
        ayanamsa = profileOps.getAyanamsa();
        ephemerisFlags = profileOps.getEphemerisFlags();
        horaryNumberGroup = profileOps.getHoraryNumberGroup();
        houseSystem = profileOps.getHouseSystem();
        oneYear = profileOps.getOneYear();
        
        return this;
    }
    
    public Profile build() {
        return new Profile(this);
    }
}
