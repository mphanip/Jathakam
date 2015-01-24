/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.listener;

import java.util.logging.Level;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import mpp.jathakamu.view.ViewLogger;

/**
 *
 * @author Phani
 */
public class JathakamuPhaseListener
    implements PhaseListener
{
    @Override
    public void beforePhase(PhaseEvent pe)
    {
        ViewLogger.LOGGER.log(Level.FINE, "Before Phase {0}", pe.getPhaseId());
    }

    @Override
    public void afterPhase(PhaseEvent pe)
    {
        ViewLogger.LOGGER.log(Level.FINE, "After Phase {0}", pe.getPhaseId());
    }

    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }
}
