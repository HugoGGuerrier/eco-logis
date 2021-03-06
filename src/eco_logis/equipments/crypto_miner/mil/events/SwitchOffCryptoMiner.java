package eco_logis.equipments.crypto_miner.mil.events;

import eco_logis.equipments.crypto_miner.mil.CryptoMinerElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represent a switching off event for the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOffCryptoMiner
    extends AbstractCryptoMinerEvent
{

    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public SwitchOffCryptoMiner(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching off has the lowest priority
        return false;
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof CryptoMinerElectricityModel;
        CryptoMinerElectricityModel m = (CryptoMinerElectricityModel) model;
        if(m.isOn()) {
            m.setMining(false);
            m.setOn(false);
            m.setHasChanged(true);
        }
    }

}
