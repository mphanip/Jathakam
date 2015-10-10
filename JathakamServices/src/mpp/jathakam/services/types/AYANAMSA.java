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
package mpp.jathakam.services.types;

import swisseph.SweConst;
import swisseph.SweDate;

/**
 *
 * @author phani
 */
public enum AYANAMSA
{

    FAGAN_BRADLEY(SweConst.SE_SIDM_FAGAN_BRADLEY),
    LAHIRI(SweConst.SE_SIDM_LAHIRI),
    DELUCE(SweConst.SE_SIDM_DELUCE),
    RAMAN(SweConst.SE_SIDM_RAMAN),
    USHASHASHI(SweConst.SE_SIDM_USHASHASHI),
    KRISHNAMURTI(SweConst.SE_SIDM_KRISHNAMURTI),
    DJWHAL_KHUL(SweConst.SE_SIDM_DJWHAL_KHUL),
    YUKTESHWAR(SweConst.SE_SIDM_YUKTESHWAR),
    JN_BHASIN(SweConst.SE_SIDM_JN_BHASIN),
    BABYL_KUGLER1(SweConst.SE_SIDM_BABYL_KUGLER1),
    BABYL_KUGLER2(SweConst.SE_SIDM_BABYL_KUGLER2),
    BABYL_KUGLER3(SweConst.SE_SIDM_BABYL_KUGLER3),
    BABYL_HUBER(SweConst.SE_SIDM_BABYL_HUBER),
    BABYL_ETPSC(SweConst.SE_SIDM_BABYL_ETPSC),
    ALDEBARAN_15TAU(SweConst.SE_SIDM_ALDEBARAN_15TAU),
    HIPPARCHOS(SweConst.SE_SIDM_HIPPARCHOS),
    SASSANIAN(SweConst.SE_SIDM_SASSANIAN),
    GALCENT_0SAG(SweConst.SE_SIDM_GALCENT_0SAG),
    J2000(SweConst.SE_SIDM_J2000),
    J1900(SweConst.SE_SIDM_J1900),
    B1950(SweConst.SE_SIDM_B1950),
    SURYASIDDHANTA(SweConst.SE_SIDM_SURYASIDDHANTA),
    SURYASIDDHANTA_MSUN(SweConst.SE_SIDM_SURYASIDDHANTA_MSUN),
    ARYABHATA(SweConst.SE_SIDM_ARYABHATA),
    ARYABHATA_MSUN(SweConst.SE_SIDM_ARYABHATA_MSUN),
    SS_REVATI(SweConst.SE_SIDM_SS_REVATI),
    SS_CITRA(SweConst.SE_SIDM_SS_CITRA),
    TRUE_CITRA(SweConst.SE_SIDM_TRUE_CITRA),
    TRUE_REVATI(SweConst.SE_SIDM_TRUE_REVATI),
    USER_DEFINED(SweConst.SE_SIDM_USER),
    OLD_KRISHNAMURTI(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.362416666666665D),
    NEW_KRISHNAMURTI(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.37045D /* 22.371027777777776D <- this is the actual value */);

    private final int swissEphAyanamsa;
    private double t0;
    private double initialValue;

    private AYANAMSA(int swissEphAyanamsa)
    {
        this.swissEphAyanamsa = swissEphAyanamsa;
    }
    
    private AYANAMSA(double t, double val) {
        swissEphAyanamsa = SweConst.SE_SIDM_USER;
        t0 = t;
        initialValue = val;
    }

    public int getValue()
    {
        return swissEphAyanamsa;
    }
    
    public double getInitialValue() {
        return initialValue;
    }
    
    public double getT0() {
        return t0;
    }

    public void setT0(double t0)
    {
        this.t0 = t0;
    }

    public void setInitialValue(double initialValue)
    {
        this.initialValue = initialValue;
    }
    
    public boolean isUserDefined() {
        return swissEphAyanamsa == SweConst.SE_SIDM_USER;
    }
}
