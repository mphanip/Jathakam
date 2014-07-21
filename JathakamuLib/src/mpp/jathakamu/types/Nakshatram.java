/*
 * Yet to decide on the license
 *
 */
package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static mpp.jathakamu.Constants.STAR_EXTENT_IN_MINUTES;

/**
 *
 * @author phani
 */
public enum Nakshatram implements Serializable
{
    NONE(-1),
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

    private static Map<Range, Nakshatram> extents = null;
    private static Set<Range> extentsKeySet = null;

    private int start = 0;
    private int end = 0;

    private Nakshatram(int start)
    {
        if (start < 0)
        {
            end = start;
        }
        else
        {
            this.start = start;
            end = start + STAR_EXTENT_IN_MINUTES;
        }
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
        return new Range.Builder().values(start(), end()).build();
    }

    public synchronized static Nakshatram getStar(double planetLongitude)
    {
        if (extents == null)
        {
            extents = new HashMap<>();

            for (Nakshatram star : Nakshatram.values())
            {
                if (star.equals(NONE))
                {
                    continue;
                }
                extents.put(star.getExtent(), star);
            }
            extentsKeySet = extents.keySet();
        }

        Stream<Range> rtnRange = extentsKeySet.parallelStream()
                .filter(range -> range.inRange((planetLongitude == 360.0) ? 0.0 : planetLongitude))
                .limit(1);
        
        Optional<Range> keyRange = rtnRange.findFirst();
        
        if (!keyRange.isPresent())
        {
            return NONE;
        }
        
        Range key = keyRange.get();
        
        Nakshatram star = extents.getOrDefault(key, NONE);

        return star;
    }
}
