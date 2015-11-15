/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
"use strict";

//var calcModule = angular.module("calc", []);
//calcModule.factory('lords', function(){
//    
//});

// TODO: Covert the below code to angular service.

// to run using nodejs.
if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {
    var globs = require('./globals');
    var Range = globs.Range;
    var degnorm = globs.degnorm;
    var nextVdPlanet = globs.nextVdPlanet;
    var PLANETS = globs.PLANETS;
    var RAASIS = globs.RAASIS;
    var NAKSHATRAS = globs.NAKSHATRAS;
}

var LordNode = function (planet /* as Number */, range /* as Range */) {
    this.planet = planet;
    this.range = range;

    // 'planet' sub lord for the 'longitude', in the 'range' (units: minutes)
    function sublord(longitude /* logitude for the sub lord */) {
        var startDegInMins = this.range.lower;
        var endDegInMins = this.range.upper;
        var totalSpan = endDegInMins - startDegInMins;
        var nextPlanet = PLANETS[this.planet]; // Dasa start with itself KE-KE-KE
        var newStartDeg = startDegInMins;
        var newEndDeg = startDegInMins;
        
//        console.log("startDegInMins = " + startDegInMins);
//        console.log("endDegInMins = " + endDegInMins);
//        console.log("totalSpan = " + totalSpan);

        do {
            var nextPlanetDuration = nextPlanet.vdDuration;
            var subSpan = totalSpan * nextPlanetDuration/120;
            newEndDeg += subSpan;
            
//            console.log("nextPlanet = " + nextPlanet.name);
//            console.log("nextPlanetDuration = " + nextPlanetDuration + "\nnewEndDeg = " + newEndDeg);

            if (newEndDeg > longitude) {
                return new LordNode(nextPlanet.index, new Range(newStartDeg, newEndDeg));
            }
            newStartDeg = newEndDeg;

            nextPlanet = nextVdPlanet(nextPlanet);
        } while (this.planet !== nextPlanet.index);

        return new LordNode(this.planet, new Range(0, 0));
    }

    return {
        planet: this.planet, // Number
        range: this.range, // Range

        sublord: sublord,

        toString: function () {
            return "[" + PLANETS[planet].name + ", " + range + ']';
        }
    };
};

/* longitude in degrees */
var Calc = function (pLongitude /* as Number */) {
    //console.log("longitude = " + pLongitude);
    var longitude = degnorm(pLongitude) ;
    var longitudeInMins = ((longitude === 0) ? 1e-16 : longitude) * 60.0;
    //console.log("degnorm longitude = " + longitude + ", longitudeInMins = " + longitudeInMins);
        
    function raasi() {
        var raasiIndex = Math.floor(longitudeInMins / (30.0*60.0));
        var raasi = RAASIS[raasiIndex+1];
        
        return raasi;
    }
    
    function raasiLord() {
        var r = raasi();
        var planetIndex = r.lord;
        var raasiSpan = RAASIS[2].start;
        
        return new LordNode(planetIndex, new Range(r.start, r.start+raasiSpan));
    }
    
    function star() {
        var starIndex = Math.ceil(longitudeInMins / 800.0);
        var nak = NAKSHATRAS[starIndex];
        
        return nak;
    }

    /* return start lord (int) */
    function starLord() {
        var nak = star();
        var planetIndex = nak.lord;
        
        return new LordNode(planetIndex, new Range(nak.start, nak.start+800));
    }
    
    /* Number of sub lords to calculate, default is 2 (Sub-Sub Lord) */
    function lords(subLordLevels) {
        if (subLordLevels === undefined || subLordLevels < 2) {
            subLordLevels = 2;
        }
        
        var sublords = [];
        
        // Get Raasi lord
        var rl = raasiLord();
        
        // Get the star lord
        var nl = starLord();
        
        sublords.push(rl);
        sublords.push(nl);
        
        var  sl = nl;

        for (var index = 0; index < subLordLevels; index++) {
            sl = sl.sublord(longitudeInMins);
            sublords.push(sl);
        }
        
        return sublords;
    }

    return {
        star: star,
        raasi: raasi,
        lords: lords
    };
};

var VdNode = function(planet, startDate, endDate) {
    this.planet = planet;
    this.startDate = startDate;
    this.endDate = endDate;

    function subDasa() {
        var subDasaList = [];
        var dasaStartDate = this.startDate.getTime();
        var dasaEndDate;
        var dasaSpan = this.endDate.getTime() - this.startDate.getTime();
        var currentPlanet = this.planet;
        var i;

        for (i = 0; i < 9; i++) {
            dasaEndDate = dasaStartDate + (dasaSpan * currentPlanet.vdDuration) / 120.0;
            var vdn = new VdNode(currentPlanet, new Date(dasaStartDate), new Date(dasaEndDate));
            subDasaList.push(vdn);
            dasaStartDate = dasaEndDate;
            currentPlanet = nextVdPlanet(currentPlanet);
        }

        return subDasaList;
    }

    return {
        planet: this.planet,
        startDate: this.startDate,
        endDate: this.endDate,
        subDasa: subDasa,
        toString: function() {
            return this.startDate + " - " + this.planet.name + "\t- " + this.endDate;
        }
    }
}

 /*
  * eventDateTime, either birth time or horary chart time in milliseconds. type: Number
  * longitude, of the planet. type: Number
  *
  * Perform vimshottari dasa calculations
  */
var VdCalc = function (eventDateTime, longitude) {
    var yearInMillis = 3.156e+10;// 365.2425 mean tropical days; // 365.25636 sederial days
    var calc = new Calc(longitude);
    var lordsList = calc.lords(6);
    var mahaDasaLord = lordsList[1];
    var longitudeInMins = longitude * 60.0;

    function mahadasa() {
        var mdList = [];
        var mahaDasaPlanet = PLANETS[mahaDasaLord.planet];
        var remainingMahaDasa = ((mahaDasaLord.range.upper - longitudeInMins) * mahaDasaPlanet.vdDuration * yearInMillis)/800;
        var mahadasaEndDate = eventDateTime + remainingMahaDasa;
        var mahaDasaStartDate = mahadasaEndDate - mahaDasaPlanet.vdDuration * yearInMillis;
        var vdNode = new VdNode(mahaDasaPlanet, new Date(mahaDasaStartDate), new Date(mahadasaEndDate));
        mdList.push(vdNode);
        var i = 0;
        var nextMahaDasaPlanet = mahaDasaPlanet;

        for (; i < 8; i++) {
            mahaDasaStartDate = mahadasaEndDate;
            nextMahaDasaPlanet = nextVdPlanet(nextMahaDasaPlanet);
            mahadasaEndDate = mahaDasaStartDate + nextMahaDasaPlanet.vdDuration * yearInMillis;
            vdNode = new VdNode(nextMahaDasaPlanet, new Date(mahaDasaStartDate), new Date(mahadasaEndDate));
            mdList.push(vdNode);

            nextMahaDasaPlanet = nextMahaDasaPlanet;
        }

        return mdList;
    }

    return {
        mahadasa: mahadasa
    }
};

// Testing
var longitude = 10*30.0 + 24 + (37.0/60.0) + (38.0/(60.0*60.0)) + (0.0/(60.0*60.0*60.0));
//var calc = new Calc(longitude);
//var lordsList = calc.lords(4);
//console.log("longitude = " + longitude);
//console.log("Raasi = " + calc.raasi().name);
//console.log("Nakshatram = " + calc.star().name);
//console.log("Lords = " + lordsList);

//var dob = new Date(); //new Date(1978, 11, 7, 12, 15, 0, 0);
//var i = 0;
//var vdc = new VdCalc(dob.getTime(), 207.64856604634355);
//var mdList = vdc.mahadasa();
//
//console.log("----------Mahadasa------------");
//for (i = 0;i < mdList.length; i++) {
//    console.log(mdList[i].toString());
//}
//
//console.log("----------Bhukti------------");
//var bdList = mdList[0].subDasa();
//for (i = 0;i < bdList.length; i++) {
//    console.log(bdList[i].toString());
//}
//
//console.log("----------Pratyantara------------");
//var pdList = bdList[0].subDasa();
//for (i = 0;i < pdList.length; i++) {
//    console.log(pdList[i].toString());
//}
//
//console.log("----------Bhukti 2------------");
//bdList = mdList[1].subDasa();
//for (i = 0;i < bdList.length; i++) {
//    console.log(bdList[i].toString());
//}
//
//console.log("----------Pratyantara 2------------");
//pdList = bdList[1].subDasa();
//for (i = 0;i < pdList.length; i++) {
//    console.log(pdList[i].toString());
//}
