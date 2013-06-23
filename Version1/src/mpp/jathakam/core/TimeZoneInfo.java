/*
 *
 *
 */
package mpp.jathakam.core;

import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author phani
 */
public class TimeZoneInfo
{

    private final static Map<String, Double> mTimeInfoToUTC = new ConcurrentSkipListMap<String, Double>();

    static
    {
        mTimeInfoToUTC.put("ACDT-Australian Central Daylight Time", 10.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("ACST-Australian Central Standard Time", 09.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("ACT-ASEAN Common Time", 08.0);
        mTimeInfoToUTC.put("ADT-Atlantic Daylight Time", -03.0);
        mTimeInfoToUTC.put("AEDT-Australian Eastern Daylight Time", 11.0);
        mTimeInfoToUTC.put("AEST-Australian Eastern Standard Time", 10.0);
        mTimeInfoToUTC.put("AFT-Afghanistan Time", 04.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("AKDT-Alaska Daylight Time", -08.0);
        mTimeInfoToUTC.put("AKST-Alaska Standard Time", -09.0);
        mTimeInfoToUTC.put("AMST-Armenia Summer Time", 05.0);
        mTimeInfoToUTC.put("AMT-Armenia Time", 04.0);
        mTimeInfoToUTC.put("ART-Argentina Time", -03.0);
        mTimeInfoToUTC.put("AST-Arab Standard Time (Kuwait, Riyadh)", 03.0);
        mTimeInfoToUTC.put("AST-Arabian Standard Time (Abu Dhabi, Muscat)", 04.0);
        mTimeInfoToUTC.put("AST-Arabic Standard Time (Baghdad)", 03.0);
        mTimeInfoToUTC.put("AST-Atlantic Standard Time", -04.0);
        mTimeInfoToUTC.put("AWDT-Australian Western Daylight Time", 09.0);
        mTimeInfoToUTC.put("AWST-Australian Western Standard Time", 08.0);
        mTimeInfoToUTC.put("AZOST-Azores Standard Time", -01.0);
        mTimeInfoToUTC.put("AZT-Azerbaijan Time", 04.0);
        mTimeInfoToUTC.put("BDT-Brunei Time", 08.0);
        mTimeInfoToUTC.put("BIOT-British Indian Ocean Time", 06.0);
        mTimeInfoToUTC.put("BIT-Baker Island Time", -12.0);
        mTimeInfoToUTC.put("BOT-Bolivia Time", -04.0);
        mTimeInfoToUTC.put("BRT-Brasilia Time", -03.0);
        mTimeInfoToUTC.put("BST-Bangladesh Standard Time", 06.0);
        mTimeInfoToUTC.put("BST-British Summer Time (British Standard Time from Feb 1968 to Oct 1971)", 01.0);
        mTimeInfoToUTC.put("BTT-Bhutan Time", 06.0);
        mTimeInfoToUTC.put("CAT-Central Africa Time", 02.0);
        mTimeInfoToUTC.put("CCT-Cocos Islands Time", 06.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("CDT-Central Daylight Time (North America)", -05.0);
        mTimeInfoToUTC.put("CEDT-Central European Daylight Time", 02.0);
        mTimeInfoToUTC.put("CEST-Central European Summer Time (Cf. HAEC)", 02.0);
        mTimeInfoToUTC.put("CET-Central European Time", 01.0);
        mTimeInfoToUTC.put("CHADT-Chatham Daylight Time", 13.0 + 45.0 / 60.0);
        mTimeInfoToUTC.put("CHAST-Chatham Standard Time", 12.0 + 45.0 / 60.0);
        mTimeInfoToUTC.put("CIST-Clipperton Island Standard Time", -08.0);
        mTimeInfoToUTC.put("CKT-Cook Island Time", -10.0);
        mTimeInfoToUTC.put("CLST-Chile Summer Time", -03.0);
        mTimeInfoToUTC.put("CLT-Chile Standard Time", -04.0);
        mTimeInfoToUTC.put("COST-Colombia Summer Time", -04.0);
        mTimeInfoToUTC.put("COT-Colombia Time", -05.0);
        mTimeInfoToUTC.put("CST-Central Standard Time (North America)", -06.0);
        mTimeInfoToUTC.put("CST-China Standard Time", 08.0);
        mTimeInfoToUTC.put("CST-Central Standard Time (Australia)", 09.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("CT-China Time", 08.0);
        mTimeInfoToUTC.put("CVT-Cape Verde Time", -01.0);
        mTimeInfoToUTC.put("CXT-Christmas Island Time", 07.0);
        mTimeInfoToUTC.put("CHST-Chamorro Standard Time", 10.0);
        mTimeInfoToUTC.put("DFT-AIX specific equivalent of Central European Time", 01.0);
        mTimeInfoToUTC.put("EAST-Easter Island Standard Time", -06.0);
        mTimeInfoToUTC.put("EAT-East Africa Time", 03.0);
        mTimeInfoToUTC.put("ECT-Eastern Caribbean Time (does not recognise DST)", -04.0);
        mTimeInfoToUTC.put("ECT-Ecuador Time", -05.0);
        mTimeInfoToUTC.put("EDT-Eastern Daylight Time (North America)", -04.0);
        mTimeInfoToUTC.put("EEDT-Eastern European Daylight Time", 03.0);
        mTimeInfoToUTC.put("EEST-Eastern European Summer Time", 03.0);
        mTimeInfoToUTC.put("EET-Eastern European Time", 02.0);
        mTimeInfoToUTC.put("EST-Eastern Standard Time (North America)", -05.0);
        mTimeInfoToUTC.put("FJT-Fiji Time", 12.0);
        mTimeInfoToUTC.put("FKST-Falkland Islands Summer Time", -03.0);
        mTimeInfoToUTC.put("FKT-Falkland Islands Time", -04.0);
        mTimeInfoToUTC.put("GALT-Galapagos Time", -06.0);
        mTimeInfoToUTC.put("GET-Georgia Standard Time", 04.0);
        mTimeInfoToUTC.put("GFT-French Guiana Time", -03.0);
        mTimeInfoToUTC.put("GILT-Gilbert Island Time", 12.0);
        mTimeInfoToUTC.put("GIT-Gambier Island Time", -09.0);
        mTimeInfoToUTC.put("GMT-Greenwich Mean Time", 0.0);
        mTimeInfoToUTC.put("GST-South Georgia and the South Sandwich Islands", -02.0);
        mTimeInfoToUTC.put("GST-Gulf Standard Time", 04.0);
        mTimeInfoToUTC.put("GYT-Guyana Time", -04.0);
        mTimeInfoToUTC.put("HADT-Hawaii-Aleutian Daylight Time", -09.0);
        mTimeInfoToUTC.put("HAEC-Heure Avancée d'Europe Centrale francised name for CEST", 02.0);
        mTimeInfoToUTC.put("HAST-Hawaii-Aleutian Standard Time", -10.0);
        mTimeInfoToUTC.put("HKT-Hong Kong Time", 08.0);
        mTimeInfoToUTC.put("HMT-Heard and McDonald Islands Time", 05.0);
        mTimeInfoToUTC.put("HST-Hawaii Standard Time", -10.0);
        mTimeInfoToUTC.put("ICT-Indochina Time", 07.0);
        mTimeInfoToUTC.put("IDT-Israeli Daylight Time", 03.0);
        mTimeInfoToUTC.put("IRKT-Irkutsk Time", 08.0);
        mTimeInfoToUTC.put("IRST-Iran Standard Time", 03.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("IST-Indian Standard Time", 05.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("IST-Irish Summer Time", 01.0);
        mTimeInfoToUTC.put("IST-Israel Standard Time", 02.0);
        mTimeInfoToUTC.put("JST-Japan Standard Time", 09.0);
        mTimeInfoToUTC.put("KRAT-Krasnoyarsk Time", 07.0);
        mTimeInfoToUTC.put("KST-Korea Standard Time", 09.0);
        mTimeInfoToUTC.put("LHST-Lord Howe Standard Time", 10.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("LINT-Line Islands Time", 14.0);
        mTimeInfoToUTC.put("MAGT-Magadan Time", 11.0);
        mTimeInfoToUTC.put("MDT-Mountain Daylight Time (North America)", -06.0);
        mTimeInfoToUTC.put("MET-Middle European Time Same zone as CET", 01.0);
        mTimeInfoToUTC.put("MEST-Middle European Saving Time Same zone as CEST", 02.0);
        mTimeInfoToUTC.put("MIT-Marquesas Islands Time", -(9.0 + 30.0 / 60.0));
        mTimeInfoToUTC.put("MSD-Moscow Summer Time", 04.0);
        mTimeInfoToUTC.put("MSK-Moscow Standard Time", 03.0);
        mTimeInfoToUTC.put("MST-Malaysian Standard Time", 08.0);
        mTimeInfoToUTC.put("MST-Mountain Standard Time (North America)", -07.0);
        mTimeInfoToUTC.put("MST-Myanmar Standard Time", 06.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("MUT-Mauritius Time", 04.0);
        mTimeInfoToUTC.put("MYT-Malaysia Time", 08.0);
        mTimeInfoToUTC.put("NDT-Newfoundland Daylight Time", -(02.0 + 30.0 / 60.0));
        mTimeInfoToUTC.put("NFT-Norfolk Time[1]", 11.0 + 30.0 / 60.0);
        mTimeInfoToUTC.put("NPT-Nepal Time", 05.0 + 45.0 / 60.0);
        mTimeInfoToUTC.put("NST-Newfoundland Standard Time", 03 + 30.0 / 60.0);
        mTimeInfoToUTC.put("NT-Newfoundland Time", 03 + 30.0 / 60.0);
        mTimeInfoToUTC.put("NZDT-New Zealand Daylight Time", 13.0);
        mTimeInfoToUTC.put("NZST-New Zealand Standard Time", 12.0);
        mTimeInfoToUTC.put("OMST-Omsk Time", 06.0);
        mTimeInfoToUTC.put("PDT-Pacific Daylight Time (North America)", -07.0);
        mTimeInfoToUTC.put("PETT-Kamchatka Time", 12.0);
        mTimeInfoToUTC.put("PHOT-Phoenix Island Time", 13.0);
        mTimeInfoToUTC.put("PKT-Pakistan Standard Time", 05.0);
        mTimeInfoToUTC.put("PST-Pacific Standard Time (North America)", -08.0);
        mTimeInfoToUTC.put("PST-Philippine Standard Time", 08.0);
        mTimeInfoToUTC.put("RET-Réunion Time", 04.0);
        mTimeInfoToUTC.put("SAMT-Samara Time", 04.0);
        mTimeInfoToUTC.put("SAST-South African Standard Time", 02.0);
        mTimeInfoToUTC.put("SBT-Solomon Islands Time", 11.0);
        mTimeInfoToUTC.put("SCT-Seychelles Time", 04.0);
        mTimeInfoToUTC.put("SGT-Singapore Time", 08.0);
        mTimeInfoToUTC.put("SLT-Sri Lanka Time", 05.0 + 30.0);
        mTimeInfoToUTC.put("SST-Samoa Standard Time", -11.0);
        mTimeInfoToUTC.put("SST-Singapore Standard Time", 08.0);
        mTimeInfoToUTC.put("TAHT-Tahiti Time", -10.0);
        mTimeInfoToUTC.put("THA-Thailand Standard Time", 07.0);
        mTimeInfoToUTC.put("UTC-Coordinated Universal Time", 0.0);
        mTimeInfoToUTC.put("UYST-Uruguay Summer Time", -02.0);
        mTimeInfoToUTC.put("UYT-Uruguay Standard Time", -03.0);
        mTimeInfoToUTC.put("VET-Venezuelan Standard Time", 04 + 30.0 / 60.0);
        mTimeInfoToUTC.put("VLAT-Vladivostok Time", 10.0);
        mTimeInfoToUTC.put("WAT-West Africa Time", 01.0);
        mTimeInfoToUTC.put("WEDT-Western European Daylight Time", 01.0);
        mTimeInfoToUTC.put("WEST-Western European Summer Time", 01.0);
        mTimeInfoToUTC.put("WET-Western European Time", 0.0);
        mTimeInfoToUTC.put("WST-Western Standard Time", 08.0);
        mTimeInfoToUTC.put("YAKT-Yakutsk Time", 09.0);
        mTimeInfoToUTC.put("YEKT-Yekaterinburg Time", 05.0);
    }

    public static Set<String> getAllDisplayNames()
    {
        return mTimeInfoToUTC.keySet();
    }

    public static double getOffset(String displayName)
    {
        return mTimeInfoToUTC.get(displayName);
    }

    public static double getStdLongitude(String displayName)
    {
        double offset = getOffset(displayName);
        double utcInMinutes = offset * 60.0;
        return utcInMinutes / 4.0;
    }

    public static String getJavaStringId(String displayName)
    {
        double offset = getOffset(displayName);
        String []ids = TimeZone.getAvailableIDs((int)(offset * 60D * 60D * 1000D));
        return ids[0];
    }
}
