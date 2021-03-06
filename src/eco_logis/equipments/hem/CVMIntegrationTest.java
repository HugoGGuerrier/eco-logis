package eco_logis.equipments.hem;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.dishwasher.Dishwasher;
import eco_logis.equipments.electric_meter.ElectricMeter;
import eco_logis.equipments.generator.Generator;
import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.wind_turbine.WindTurbine;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMIntegrationTest
    extends AbstractCVM
{

    // ========== Constructors ==========


    public CVMIntegrationTest() throws Exception {}


    // ========== Lifecycle ==========


    /** @see AbstractCVM#deploy() */
    @Override
    public void deploy() throws Exception {
        // Create the crypto miner
        AbstractComponent.createComponent(
                CryptoMiner.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the dishwasher
        AbstractComponent.createComponent(
                Dishwasher.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the generator
        AbstractComponent.createComponent(
                Generator.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the oven
        AbstractComponent.createComponent(
                Oven.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the power bank
        AbstractComponent.createComponent(
                PowerBank.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the wind turbine
        AbstractComponent.createComponent(
                WindTurbine.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the electric meter
        AbstractComponent.createComponent(
                ElectricMeter.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the HEM
        AbstractComponent.createComponent(
                HEM.class.getCanonicalName(),
                new Object[]{}
        );

        // Super deploy
        super.deploy();
    }


    // ========== Class methods ==========


    public static void main(String[] args) {
        try {
            CVMIntegrationTest cvm = new CVMIntegrationTest();
            cvm.startStandardLifeCycle(15000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
