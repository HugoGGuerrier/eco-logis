package equipments.power_bank;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMUnitTest
    extends AbstractCVM {

    // ========== Constructors ==========

    public CVMUnitTest() throws Exception {}

    // ========== Class methods ==========

    @Override
    public void deploy() throws Exception {
        // Create the components to test the power bank
        AbstractComponent.createComponent(PowerBank.class.getCanonicalName(), new Object[]{});
        AbstractComponent.createComponent(PowerBankUnitTester.class.getCanonicalName(), new Object[]{});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            equipments.power_bank.CVMUnitTest cvm = new equipments.power_bank.CVMUnitTest();
            cvm.startStandardLifeCycle(1000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}