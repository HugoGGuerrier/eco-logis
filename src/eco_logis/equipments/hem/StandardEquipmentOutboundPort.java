package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.StandardEquipmentControlCI;

/**
 * This class represents an outbound port for a standard equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class StandardEquipmentOutboundPort
    extends AbstractOutboundPort
    implements StandardEquipmentControlCI
{

    // ========== Constructors ==========

    /**
     * Create a new standard equipment outbound port with the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public StandardEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(StandardEquipmentControlCI.class, owner);
    }

    /**
     * Create a new standard equipment outbound port with the URI and the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public StandardEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, StandardEquipmentControlCI.class, owner);
    }

    /**
     * Create a new standard equipment outbound port with the implemented interface and the owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param implInterface The implemented interface
     * @param owner The port owner
     * @throws Exception TODO
     */
    protected StandardEquipmentOutboundPort(Class<? extends RequiredCI> implInterface, ComponentI owner) throws Exception {
        super(implInterface, owner);
    }

    /**
     * Create a new standard equipment outbound port with the URI, the implemented interface and the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param implInterface The implemented interface
     * @param owner The port owner
     * @throws Exception TODO
     */
    protected StandardEquipmentOutboundPort(String uri, Class<? extends RequiredCI> implInterface, ComponentI owner) throws Exception {
        super(uri, implInterface, owner);
    }


    // ========== Override methods ==========


    /** @see StandardEquipmentControlCI#on() */
    @Override
    public boolean on() throws Exception {
        return ((StandardEquipmentControlCI) getConnector()).on();
    }

    /** @see StandardEquipmentControlCI#switchOn() */
    @Override
    public boolean switchOn() throws Exception {
        assert !on() : new PreconditionException("switchOn() -> !on()");
        boolean res = ((StandardEquipmentControlCI) getConnector()).switchOn();
        assert on() : new PostconditionException("switchOn() -> on()");
        return res;
    }

    /** @see StandardEquipmentControlCI#switchOff() */
    @Override
    public boolean switchOff() throws Exception {
        assert on() : new PreconditionException("switchOff() -> on()");
        boolean res = ((StandardEquipmentControlCI) getConnector()).switchOff();
        assert !on() : new PostconditionException("switchOff() -> !on()");
        return res;
    }

}
