package eco_logis.equipments.power_bank;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredInterfaces(required = {PowerBankCI.class})
public class PowerBankUnitTester
    extends AbstractComponent {

    // ========== Attributes ==========

    private String powerBankInboundPortUri;
    private PowerBankOutboundPort powerBankOutboundPort;

    // ========== Constructors ==========

    protected PowerBankUnitTester() throws Exception {
        this(PowerBank.INBOUND_PORT_URI);
    }

    protected PowerBankUnitTester(String powerBankInboundPortURI) throws Exception {
        super(1, 0);
        initialise(powerBankInboundPortURI);
    }

    protected PowerBankUnitTester(String reflectionInboundPortURI, String powerBankInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(powerBankInboundPortURI);
    }

    // ========== Class methods ==========

    protected void initialise(String powerBankInboundPortUri) throws Exception {
        // Set the inbound port uri
        this.powerBankInboundPortUri = powerBankInboundPortUri;

        // Create the outbound port
        powerBankOutboundPort = new PowerBankOutboundPort(this);
        powerBankOutboundPort.publishPort();

        // Trace the execution
        tracer.get().setTitle("Power bank tester component");
        tracer.get().setRelativePosition(0, 2);
        toggleTracing();
    }

    // ========== Test methods ==========


    protected void testIsCharging() {
        logMessage("Test isCharging()...");
        try {
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.CHARGING);
            assertSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.STANDBY);
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testIsDischarging() {
        logMessage("Test isDischarging()...");
        try {
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.DISCHARGING);
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }


    protected void testStartStopCharging() {
        logMessage("Test startCharging() and stopCharging()...");
        try {
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.CHARGING);
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.DISCHARGING);
            powerBankOutboundPort.startCharging();
            assertSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.CHARGING);
            powerBankOutboundPort.standBy();
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.CHARGING);
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("...Done!");
    }

    protected void testStartStopDischarging() {
        logMessage("Test startDischarging() and stopDischarging()...");
        try {
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.CHARGING);
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.DISCHARGING);
            powerBankOutboundPort.startDischarging();
            assertSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.DISCHARGING);
            powerBankOutboundPort.standBy();
            assertNotSame(powerBankOutboundPort.getCurrentState(), PowerBank.State.DISCHARGING);
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("...Done!");
    }

    protected void runAllTests() {
        logMessage("Starting power bank test suite...");
        testIsCharging();
        testIsDischarging();
        testStartStopCharging();
        testStartStopDischarging();
        logMessage("All tests passed !");
    }

    // ========== Lifecycle methods ==========


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        // Connect the outbound port to the inbound port
        try {
            doPortConnection(
                    powerBankOutboundPort.getPortURI(),
                    powerBankInboundPortUri,
                    PowerBankTestConnector.class.getCanonicalName()
            );
        } catch(Exception e) {
            throw new ComponentStartException(e);
        }
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        // Run all the tests
        runAllTests();

        super.execute();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#finalise() */
    @Override
    public synchronized void finalise() throws Exception {
        // Disconnect the ports
        doPortDisconnection(powerBankOutboundPort.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            powerBankOutboundPort.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }


}
