package equipments.generator;

import equipments.crypto_miner.CryptoMiner;
import equipments.crypto_miner.CryptoMinerUnitTester;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMUnitTest
    extends AbstractCVM
{

    // ========== Constructors ==========

    public CVMUnitTest() throws Exception {}

    // ========== Class methods ==========


    @Override
    public void deploy() throws Exception {
        // Create the components to test the crypto miner
        AbstractComponent.createComponent(Generator.class.getCanonicalName(), new Object[]{});
        AbstractComponent.createComponent(GeneratorUnitTester.class.getCanonicalName(), new Object[]{});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(1000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}