package eco_logis.equipments.crypto_miner.mil.events;

import eco_logis.equipments.crypto_miner.mil.CryptoMinerElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents a switch on event on the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOnCryptoMiner
    extends AbstractCryptoMinerEvent
{

    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public SwitchOnCryptoMiner(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching on has priority over all others events
        return true;
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof CryptoMinerElectricityModel;
        CryptoMinerElectricityModel m = (CryptoMinerElectricityModel) model;
        if(!m.isOn()) {
            m.setOn(true);
            m.setHasChanged(true);
        }
    }

}
