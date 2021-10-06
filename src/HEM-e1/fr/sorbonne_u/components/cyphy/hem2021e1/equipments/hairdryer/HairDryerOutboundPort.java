package fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a basic
// household management systems as an example of a cyber-physical system.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

// -----------------------------------------------------------------------------

/**
 * The class <code>HairDryerOutboundPort</code> implements an outbound port for
 * the <code>HairDryerCI</code> component interface.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 *
 * <p>Created on : 2021-09-09</p>
 *
 * @author    <a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class HairDryerOutboundPort
        extends AbstractOutboundPort
        implements HairDryerCI {

    private static final long serialVersionUID = 1L;

    public HairDryerOutboundPort(ComponentI owner)
            throws Exception {
        super(HairDryerCI.class, owner);
    }

    public HairDryerOutboundPort(String uri, ComponentI owner)
            throws Exception {
        super(uri, HairDryerCI.class, owner);
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#getState()
     */
    @Override
    public HairDryerState getState() throws Exception {
        return ((HairDryerCI) this.getConnector()).getState();
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#getMode()
     */
    @Override
    public HairDryerMode getMode() throws Exception {
        return ((HairDryerCI) this.getConnector()).getMode();
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#turnOn()
     */
    @Override
    public void turnOn() throws Exception {
        ((HairDryerCI) this.getConnector()).turnOn();
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#turnOff()
     */
    @Override
    public void turnOff() throws Exception {
        ((HairDryerCI) this.getConnector()).turnOff();
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#setHigh()
     */
    @Override
    public void setHigh() throws Exception {
        ((HairDryerCI) this.getConnector()).setHigh();
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI#setLow()
     */
    @Override
    public void setLow() throws Exception {
        ((HairDryerCI) this.getConnector()).setLow();
    }
}
// -----------------------------------------------------------------------------
