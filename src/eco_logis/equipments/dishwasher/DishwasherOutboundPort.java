package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This class represent an outbound port for the dishwasher services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRRIER
 */
public class DishwasherOutboundPort
    extends AbstractOutboundPort
    implements DishwasherCI
{

    // ========== Constructors ==========


    /**
     * Create a new dishwasher outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherOutboundPort(ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }

    /**
     * Create a new dishwasher outbound port with its URI and its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherOutboundPort(String uri, ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see DishwasherCI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return ((DishwasherCI) getConnector()).getProgram();
    }

    /** @see DishwasherCI#getProgramDuration() */
    @Override
    public Duration getProgramDuration() throws Exception {
        return ((DishwasherCI) getConnector()).getProgramDuration();
    }

    /** @see DishwasherCI#getDeadline() */
    @Override
    public LocalTime getDeadline() throws Exception {
        return ((DishwasherCI) getConnector()).getDeadline();
    }

    /** @see DishwasherCI#getStartTime() */
    @Override
    public LocalTime getStartTime() throws Exception {
        return ((DishwasherCI) getConnector()).getStartTime();
    }

    /** @see DishwasherCI#isPlanned() */
    @Override
    public boolean isPlanned() throws Exception {
        return ((DishwasherCI) getConnector()).isPlanned();
    }

    /** @see DishwasherCI#plan(LocalTime) */
    @Override
    public boolean plan(LocalTime deadline) throws Exception {
        return ((DishwasherCI) getConnector()).plan(deadline);
    }

    /** @see DishwasherCI#plan(LocalTime, DishwasherProgram) */
    @Override
    public boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception {
        return ((DishwasherCI) getConnector()).plan(deadline, program);
    }

    /** @see DishwasherCI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        return ((DishwasherCI) getConnector()).cancel();
    }

    /** @see DishwasherCI#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        return ((DishwasherCI) getConnector()).postPone(duration);
    }

    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) getConnector()).isWashing();
    }

    /** @see DishwasherCI#startWashing() */
    @Override
    public boolean startWashing() throws Exception {
        return ((DishwasherCI) getConnector()).startWashing();
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public boolean stopWashing() throws Exception {
        return ((DishwasherCI) getConnector()).stopWashing();
    }

}
