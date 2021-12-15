package eco_logis.equipments.crypto_miner.sil;

import eco_logis.equipments.crypto_miner.CryptoMinerImplementationI;
import eco_logis.equipments.crypto_miner.CryptoMinerInboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.exceptions.PreconditionException;

public class CryptoMiner
    extends AbstractCyPhyComponent
    implements CryptoMinerImplementationI
{

    // ========== Macros ==========


    /** URI of the crypto miner inbound port */
    public static final String INBOUND_PORT_URI = "CRYPTO-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** If the crypto miner is currently on */
    private boolean isOn;

    /** If the miner is currently mining crypto-currency */
    private boolean isMining;

    /** The inbound port */
    private CryptoMinerInboundPort cmip;


    // ========== Constructors ==========


    /**
     * Create a new crypto miner
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post	true
     * </pre>
     *
     *
     * @throws Exception TODO
     */
    protected CryptoMiner() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(int, int)
     *
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String cryptoMinerInboundPortURI) throws Exception {
        super(1, 0);
        initialise(cryptoMinerInboundPortURI);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(String, int, int)
     *
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String reflectionInboundPortURI, String cryptoMinerInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(cryptoMinerInboundPortURI);
    }


    // ========== Class methods ==========


    /**
     * Initialise the newly created crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post {@code !isMining}
     * post	{@code cmip.isPublished()}
     * </pre>
     *
     * @param cryptoMinerInboundPortURI The crypto miner inbound port URI
     * @throws Exception TODO
     */
    protected void initialise(String cryptoMinerInboundPortURI) throws Exception {
        // Assert the URI consistence
        assert cryptoMinerInboundPortURI != null : new PreconditionException(
                "cryptoMinerInboundPortURI != null");
        assert !cryptoMinerInboundPortURI.isEmpty() : new PreconditionException(
                "!cryptoMinerInboundPortURI.isEmpty()");

        // Initialise the component
        isOn = false;
        isMining = false;

        // Create the inbound port
        cmip = new CryptoMinerInboundPort(cryptoMinerInboundPortURI, this);
        cmip.publishPort();

        // Create the trace
        if(eco_logis.equipments.crypto_miner.CryptoMiner.VERBOSE) {
            tracer.get().setTitle("Crypto miner component");
            tracer.get().setRelativePosition(0, 0);
            toggleTracing();
        }
    }


    // ========== Override methods ==========


    /** @see CryptoMinerImplementationI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner get on : " + isOn);
        }

        return isOn;
    }

    /** @see CryptoMinerImplementationI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner power on");
        }

        assert !isOn : new PreconditionException("powerOn() -> !isOn()");

        isOn = true;
    }

    /** @see CryptoMinerImplementationI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner power off");
        }

        assert isOn : new PreconditionException("powerOff() -> isOn()");

        isOn = false;
        isMining = false;
    }

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        if(eco_logis.equipments.crypto_miner.CryptoMiner.VERBOSE) {
            logMessage("Crypto miner get mining : " + isMining);
        }

        return isMining;
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        if(eco_logis.equipments.crypto_miner.CryptoMiner.VERBOSE) {
            logMessage("Start the crypto mining");
        }

        assert isOn : new PreconditionException("startMiner() -> isOn()");
        assert !isMining : new PreconditionException("startMiner() -> !isMining()");

        isMining = true;
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        if(eco_logis.equipments.crypto_miner.CryptoMiner.VERBOSE) {
            logMessage("Stop the crypto mining");
        }

        assert isOn : new PreconditionException("stopMiner() -> isOn()");
        assert isMining : new PreconditionException("stopMiner() -> isMining()");;

        isMining = false;
    }

}
