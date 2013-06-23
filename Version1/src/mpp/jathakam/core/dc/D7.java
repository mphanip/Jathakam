package mpp.jathakam.core.dc;

import mpp.jathakam.core.Raasi;

public class D7 extends BaseDivisionalChart
{

	public D7(double longitude)
	{
		super("D7", (short) 7);
		this.longitude = longitude;
	}

	@Override
	public Raasi getRaasi()
	{
		Raasi raasi = Raasi.getRaasi(longitude);
        int start  = 1;
        int raasiIndex = raasi.ordinal();

        switch (raasiIndex)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
            	start = raasiIndex;
                break;
            case 2:
            case 4:
            case 6:
            case 8:
            case 10:
            case 12:
            	start = raasiIndex + 6;
                break;
        }

        int planetPositionInRaasiDivision = getDivisionIndex();

        int dcRaasi = (start + planetPositionInRaasiDivision) % 12;
        
        if (dcRaasi == 0)
        	dcRaasi = 12;

        Raasi rtnRaasi = Raasi.getRaasi(dcRaasi);

        return rtnRaasi;
	}

}
