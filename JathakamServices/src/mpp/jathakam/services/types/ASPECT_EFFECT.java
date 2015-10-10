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
 * @author phani
 */
public enum ASPECT_EFFECT
{
    NONE, GOOD, MODERATELY_GOOD, SLIGHTLY_GOOD, CONJUNCTION, BAD,
        MODERATELY_BAD, SLIGHTLY_BAD
}
