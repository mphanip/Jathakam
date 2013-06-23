package mpp.jathakam.core.dc;

import mpp.jathakam.core.Raasi;

public class D12 extends BaseDivisionalChart
{
	public D12(double longitude)
	{
		super("D12", (short) 12);
		this.longitude = longitude;
	}

	@Override
	public Raasi getRaasi()
	{
		Raasi raasi = Raasi.getRaasi(longitude);
        int start  = 1;
        int raasiIndex = raasi.ordinal();

        start = raasiIndex;

        int planetPositionInRaasiDivision = getDivisionIndex();

        int dcRaasi = (start + planetPositionInRaasiDivision) % 12;
        
        if (dcRaasi == 0)
        	dcRaasi = 12;

        Raasi rtnRaasi = Raasi.getRaasi(dcRaasi);

        return rtnRaasi;
	}

}
