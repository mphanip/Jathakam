/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
"use strict";

var YEAR_IN_MILLIS = 3.156e+10;// 365.2425 mean tropical days; // 365.25636 sederial days

var PLANETS = {
    1: {index: 1, name: 'Sun', vdIndex: 3, vdDuration: 6.0, shortName: 'SU', symbol: '\u2609'},
    2: {index: 2, name: 'Moon', vdIndex: 4, vdDuration: 10.0, shortName: 'MO', symbol: '\u263D'},
    3: {index: 3, name: 'Mercury', vdIndex: 9, vdDuration: 17.0, shortName: 'ME', symbol: '\u263F'},
    4: {index: 4, name: 'Venus', vdIndex: 2, vdDuration: 20.0, shortName: 'VE', symbol: '\u2640'},
    5: {index: 5, name: 'Mars', vdIndex: 5, vdDuration: 7.0, shortName: 'MA', symbol: '\u2642'},
    6: {index: 6, name: 'Jupiter', vdIndex: 7, vdDuration: 16.0, shortName: 'JU', symbol: '\u2643'},
    7: {index: 7, name: 'Saturn', vdIndex: 8, vdDuration: 19.0, shortName: 'SA', symbol: '\u2644'},
    8: {index: 8, name: 'Uranus', vdIndex: 0, vdDuration: 0, shortName: 'UR', symbol: '\u2645'},
    9: {index: 9, name: 'Neptune', vdIndex: 0, vdDuration: 0, shortName: 'NE', symbol: '\u2646'},
    10: {index: 10, name: 'Pluto', vdIndex: 0, vdDuration: 0, shortName: 'PL', symbol: '\u2647'},
    11: {index: 11, name: 'Rahu', vdIndex: 6, vdDuration: 18.0, shortName: 'RA', symbol: '\u260A'},
    12: {index: 12, name: 'Ketu', vdIndex: 1, vdDuration: 7.0, shortName: 'KE', symbol: '\u260B'}
};

var VDPLANETS = [PLANETS[12], PLANETS[4], PLANETS[1], PLANETS[2], PLANETS[5], PLANETS[11], PLANETS[6], PLANETS[7], PLANETS[3]];

var NAKSHATRA_SPAN = (13.0 * 60.0) + 20.0; // In minutes

var NAKSHATRAS = {
    1: {name: 'ASWINI', start: 0, lord: 12},
    2: {name: 'BHARANI', start: NAKSHATRA_SPAN, lord: 4},
    3: {name: 'KRITIKA', start: 2 * NAKSHATRA_SPAN, lord: 1},
    4: {name: 'ROHINI', start: 3 * NAKSHATRA_SPAN, lord: 2},
    5: {name: 'MRIGHASIRA', start: 4 * NAKSHATRA_SPAN, lord: 5},
    6: {name: 'ARUDRA', start: 5 * NAKSHATRA_SPAN, lord: 11},
    7: {name: 'PUNARVASU', start: 6 * NAKSHATRA_SPAN, lord: 6},
    8: {name: 'PUSHYAMI', start: 7 * NAKSHATRA_SPAN, lord: 7},
    9: {name: 'ASLESHA', start: 8 * NAKSHATRA_SPAN, lord: 3},
    10: {name: 'MAKHA', start: 9 * NAKSHATRA_SPAN, lord: 12},
    11: {name: 'PURVA_PHALGUNI', start: 10 * NAKSHATRA_SPAN, lord: 4}, /* Pubha */
    12: {name: 'UTTARA_PHALGUNI', start: 11 * NAKSHATRA_SPAN, lord: 1},
    13: {name: 'HASTA', start: 12 * NAKSHATRA_SPAN, lord: 2},
    14: {name: 'CHITRA', start: 13 * NAKSHATRA_SPAN, lord: 5}, /* Chitta */
    15: {name: 'SWATHI', start: 14 * NAKSHATRA_SPAN, lord: 11},
    16: {name: 'VISAKHA', start: 15 * NAKSHATRA_SPAN, lord: 6},
    17: {name: 'ANURADHA', start: 16 * NAKSHATRA_SPAN, lord: 7},
    18: {name: 'JYESHTHA', start: 17 * NAKSHATRA_SPAN, lord: 3},
    19: {name: 'MOOLA', start: 18 * NAKSHATRA_SPAN, lord: 12},
    20: {name: 'PURVA_ASHADA', start: 19 * NAKSHATRA_SPAN, lord: 4},
    21: {name: 'UTTARA_ASHADA', start: 20 * NAKSHATRA_SPAN, lord: 1},
    22: {name: 'SHRAVANA', start: 21 * NAKSHATRA_SPAN, lord: 2},
    23: {name: 'DHANISHTA', start: 22 * NAKSHATRA_SPAN, lord: 5},
    24: {name: 'SHATABHISHA', start: 23 * NAKSHATRA_SPAN, lord: 11},
    25: {name: 'PURVA_BHADRA', start: 24 * NAKSHATRA_SPAN, lord: 6},
    26: {name: 'UTTARA_BHADRA', start: 25 * NAKSHATRA_SPAN, lord: 7},
    27: {name: 'REVATHI', start: 26 * NAKSHATRA_SPAN, lord: 3}
};

var RAASI_SPAN = 30.0 * 60.0; // In minutes

var RAASIS = {
    1: {name: 'Aries', start: 0, lord: 5},
    2: {name: 'Taurus', start: RAASI_SPAN, lord: 4},
    3: {name: 'Gemini', start: 2 * RAASI_SPAN, lord: 3},
    4: {name: 'Cancer', start: 3 * RAASI_SPAN, lord: 2},
    5: {name: 'Leo', start: 4 * RAASI_SPAN, lord: 1},
    6: {name: 'Virgo', start: 5 * RAASI_SPAN, lord: 3},
    7: {name: 'Libra', start: 6 * RAASI_SPAN, lord: 4},
    8: {name: 'Scorpio', start: 7 * RAASI_SPAN, lord: 5},
    9: {name: 'Sagittarius', start: 8 * RAASI_SPAN, lord: 6},
    10: {name: 'Capricorn', start: 9 * RAASI_SPAN, lord: 7},
    11: {name: 'Aquarius', start: 10 * RAASI_SPAN, lord: 7},
    12: {name: 'Pisces', start: 11 * RAASI_SPAN, lord: 6}
};

var PLANET_SIGNS = ["\u2609", // Sun
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
];

var CUSP_SIGNS = ["I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"];

function degree(a) {
    if (a === 'undefined')
        return "";

    // returns a in degrees as a string degrees:minutes
    a = Math.abs(a);
    var ar = Math.round(a * 60) / 60;
    var deg = Math.abs(ar);
    var min = Math.round(60.0 * (deg - Math.floor(deg)));
    if (min >= 60) {
        deg += 1;
        min = 0;
    }
    var anglestr = "";

    deg = deg % 30;

//    anglestr += ((Math.floor(deg) < 100) ? "0" : "");
    anglestr += ((Math.floor(deg) < 10) ? "0" : "") + Math.floor(deg);
    anglestr += ((min < 10) ? ":0" : ":") + (min);
    return anglestr;
}

function objectSign(objectType) {
    if (objectType < 20) {
        return PLANET_SIGNS[objectType];
    }

    return CUSP_SIGNS[objectType - 20];
}

function degnorm(x) {
    var y = Math.abs(x % 360.0);
    if (y < 1e-13) {
        y = 0;
    }
    if (y < 0.0) {
        y += 360.0;
    }
    return y;
}

var Range = function (lower, upper) {
    return {
        lower: lower,
        upper: upper,
        inRange: function (val) {
            return (val >= lower && val <= upper);
        },
        toString: function () {
            return "[" + lower + "-" + upper + "]";
        }
    };
};

/*
 * Get next vimshottari dasa planet
 */
function nextVdPlanet(planet) {
    var vdi = planet.vdIndex;

    if (vdi === 9)
        return VDPLANETS[0];

    return VDPLANETS[vdi];
}

// To run the java script using node.
if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {
    module.exports = {
        PLANETS: PLANETS,
        NAKSHATRAS: NAKSHATRAS,
        RAASIS: RAASIS,
        degnorm: degnorm,
        Range: Range,
        nextVdPlanet:nextVdPlanet
    };
}

function cell1(nodes) {
    caller(nodes);
}

function caller(nodes) {
//    console.log("nodes = " + nodes);

    var parentId = nodes[0].parentElement.getAttribute("id");
//    console.log("parentId = " + parentId);
    var parentElem = $("#" + parentId + " span").eq(0);
    var planetName = parentElem.text();
    console.log("planetName = " + planetName);

    if (planetName === PLANET_SIGNS[3] || planetName === PLANET_SIGNS[4] || planetName === PLANET_SIGNS[6]) {
        parentElem.css({'font-size': '30px'});
    }
}
