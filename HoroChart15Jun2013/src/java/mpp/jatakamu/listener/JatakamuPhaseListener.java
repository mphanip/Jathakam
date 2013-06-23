/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpp.jatakamu.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import mpp.jatakamu.JatakamuLogger;

/**
 *
 * @author Phani Pramod M
 */
public class JatakamuPhaseListener
    implements PhaseListener
{
    private static final long serialVersionUID = 1L;

    @Override
    public void afterPhase(PhaseEvent pe)
    {
        JatakamuLogger.log("---------------  After Phase " + pe.getPhaseId() + "---------------");
    }

    @Override
    public void beforePhase(PhaseEvent pe)
    {
        JatakamuLogger.log("===============  Befor Phase " + pe.getPhaseId() + "===============");
    }

    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }
}
