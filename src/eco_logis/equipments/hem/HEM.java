package equipments.hem;

import equipments.crypto_miner.CryptoMiner;
import equipments.oven.Oven;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * The class <code>HEM</code> implements the basis for a household energy
 * management component.
 *
 * <p>Created on : 2021-10-17</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class HEM
    extends AbstractComponent
{

    // ========== Attributes ==========


    private SuspensionEquipmentOutboundPort cryptoMinerOP;
    private StandardEquipmentOutboundPort ovenOP;


    // ========== Constructors ==========


    /**
     * Create a new HEM component
     */
    protected HEM() {
        super(1, 0);

        this.tracer.get().setTitle("Home Energy Manager component");
        this.tracer.get().setRelativePosition(2, 2);
        this.toggleTracing();
    }


    // ========== Lifecycle methods ==========


    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        try {
            // Create the crypto miner outbound port and connect it to the component
            cryptoMinerOP = new SuspensionEquipmentOutboundPort(this);
            cryptoMinerOP.publishPort();
            doPortConnection(
                    cryptoMinerOP.getPortURI(),
                    CryptoMiner.INBOUND_PORT_URI,
                    CryptoMinerConnector.class.getCanonicalName());

            // Create the oven outbound port and connect it to the component
            ovenOP = new StandardEquipmentOutboundPort(this);
            ovenOP.publishPort();
            doPortConnection(
                    ovenOP.getPortURI(),
                    Oven.INBOUND_PORT_URI,
                    OvenConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    @Override
    public synchronized void execute() throws Exception {
        // First scenario
        traceMessage("User powers on the crypto miner : " + cryptoMinerOP.switchOn());
        traceMessage("User starts mining crypto-currency : " + cryptoMinerOP.resume());
        traceMessage("User powers on its oven : " + ovenOP.switchOn());
        traceMessage("Is the crypto miner on : " + cryptoMinerOP.on());
        traceMessage("Is the crypto miner suspended : " + cryptoMinerOP.suspended());
        traceMessage("HEM suspends the crypto miner : " + cryptoMinerOP.suspend());
        traceMessage("User power of the oven : " + ovenOP.switchOff());
        traceMessage("Is the oven on : " + ovenOP.on());
        traceMessage("HEM resumes the crypto miner : " + cryptoMinerOP.resume());
        traceMessage("User power off the crypto miner : " + cryptoMinerOP.switchOff());
    }

    @Override
    public synchronized void finalise() throws Exception {
        doPortDisconnection(cryptoMinerOP.getPortURI());
        doPortDisconnection(ovenOP.getPortURI());
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            cryptoMinerOP.unpublishPort();
            ovenOP.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

}
