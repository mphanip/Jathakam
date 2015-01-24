/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.beans;

import java.io.Serializable;
import mpp.jathakamu.types.HouseEntity;

/**
 *
 * @author phani
 */
public interface HouseInfo extends Serializable
{

    /**
     * House information
     * 
     * @param housePosition
     * @return 
     */
    HouseEntity[] getHouseInfo(int housePosition);
    
    /**
     * To clear information
     */
    void clearInformation();
    
    /**
     * Add information to be displayed
     * 
     * @param info 
     */
    void addInformation(String info);
    
    /**
     * The information will be display in the center panel
     * 
     * @return 
     */
    String[] getInformation();
}
