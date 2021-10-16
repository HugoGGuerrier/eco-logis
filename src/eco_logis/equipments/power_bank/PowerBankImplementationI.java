package equipments.power_bank;

/**
 * The interface <code>PowerBankImplementationI</code> defines the signatures
 * of services implemented by the power bank component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-16</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface PowerBankImplementationI {

    /**
     * Start charging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code !isCharging()}
     * post	{@code isCharging()}
     * </pre>
     *
     * @throws Exception
     */
    void startCharging() throws Exception;

    /**
     * Stop charging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code isCharging()}
     * post	{@code !isCharging()}
     * </pre>
     *
     * @throws Exception
     */
    void stopCharging() throws Exception;

    /**
     * Get the current charging state of the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return true if the power bank is charging, false otherwise
     * @throws Exception
     */
    boolean isCharging() throws Exception;

    /**
     * Start discharging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code !isDischarging()}
     * pre	{@code !isCharging()}
     * post	{@code isDischarging()}
     * </pre>
     *
     * @throws Exception
     */
    void startDischarging() throws Exception;

    /**
     * Stop discharging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code isDischarging()}
     * pre	{@code !isCharging()}
     * post	{@code !isDischarging()}
     * </pre>
     *
     * @throws Exception
     */
    void stopDischarging() throws Exception;

    /**
     * Get the current discharging state of the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return true if the power bank is charging, false otherwise
     * @throws Exception
     */
    boolean isDischarging() throws Exception;

    /**
     * Get the power bank percentage of energy stocked (battery level)
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return the battery level (percentage)
     * @throws Exception
     */
    double getBatteryLevel() throws Exception;


}
