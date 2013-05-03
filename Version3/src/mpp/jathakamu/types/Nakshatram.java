/*
 *
 *
 */
package mpp.jathakamu.types;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static mpp.jathakamu.Constants.STAR_EXTENT_IN_MINUTES;

/**
 *
 * @author phani
 */
public enum Nakshatram
{
    ASWINI(0),
    BHARANI(ASWINI.end),
    KRITIKA(BHARANI.end),
    ROHINI(KRITIKA.end),
    MRIGHASIRA(ROHINI.end),
    ARUDRA(MRIGHASIRA.end),
    PUNARVASU(ARUDRA.end),
    PUSHYAMI(PUNARVASU.end),
    ASLESHA(PUSHYAMI.end),
    MAKHA(ASLESHA.end),
    PURVA_PHALGUNI(MAKHA.end), /* Pubha */
    UTTARA_PHALGUNI(PURVA_PHALGUNI.end),
    HASTA(UTTARA_PHALGUNI.end),
    CHITRA(HASTA.end), /* Chitta */
    SWATHI(CHITRA.end),
    VISAKHA(SWATHI.end),
    ANURADHA(VISAKHA.end),
    JYESHTHA(ANURADHA.end),
    MOOLA(JYESHTHA.end),
    PURVA_ASHADA(MOOLA.end),
    UTTARA_ASHADA(PURVA_ASHADA.end),
    SHRAVANA(UTTARA_ASHADA.end),
    DHANISHTA(SHRAVANA.end),
    SHATABHISHA(DHANISHTA.end),
    PURVA_BHADRA(SHATABHISHA.end),
    UTTARA_BHADRA(PURVA_BHADRA.end),
    REVATHI(UTTARA_BHADRA.end);

    private static NavigableMap<Range, Nakshatram> extents = null;

    private int start = 0;
    private int end = 0;

    private Nakshatram(int start)
    {
        this.start = start;
        end = start + STAR_EXTENT_IN_MINUTES;
    }

    public double start()
    {
        return ((double)start)/60D;
    }

    public double end()
    {
        return ((double)end)/60D;
    }

    public Range getExtent()
    {
        return new Range(start(), end());
    }

    public synchronized static Nakshatram getStar(double planetLongitude)
    {
        if (extents == null)
        {
            extents = new ConcurrentSkipListMap<Range, Nakshatram>();

            for (Nakshatram star : Nakshatram.values())
            {
                extents.put(star.getExtent(), star);
            }
        }

        Map.Entry<Range, Nakshatram> entry = extents.ceilingEntry(new Range(0, planetLongitude));

        return entry.getValue();
    }
}
