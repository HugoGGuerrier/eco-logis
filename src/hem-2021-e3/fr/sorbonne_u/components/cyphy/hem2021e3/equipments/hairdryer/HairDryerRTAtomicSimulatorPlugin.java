package fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.HairDryerCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SetHighHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SetLowHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SwitchOffHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SwitchOnHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer.sil.HairDryerElectricitySILModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer.sil.HairDryerStateModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer.sil.HairDryerUserModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;

// -----------------------------------------------------------------------------
/**
 * The class <code>HairDryerRTAtomicSimulatorPlugin</code> defines the plug-in
 * that manages the SIL simulation inside the hair dryer component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2021-10-04</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			HairDryerRTAtomicSimulatorPlugin
extends		RTAtomicSimulatorPlugin
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** simulation architectures can have URI to name them; this is the
	 *  URI used in this example for unit tests.											*/
	public static final String		UNIT_TEST_SIM_ARCHITECTURE_URI =
														"UnitTestHairDryer";
	/** name used to pass the owner component reference as simulation
	 *  parameter.															*/
	public static final String		OWNER_REFERENCE_NAME = "HDCRN";

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// initialise the simulation parameter giving the reference to the
		// owner component before passing the parameters to the simulation
		// models
		simParams.put(OWNER_REFERENCE_NAME, this.getOwner());

		// this will pass the parameters to the simulation models that will
		// then be able to get their own parameters.
		super.setSimulationRunParameters(simParams);

		// remove the value so that the reference may not exit the context of
		// the component
		simParams.remove(OWNER_REFERENCE_NAME);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * create and set the simulation architecture internal to this component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code simArchURI != null && !simArchURIisEmpty()}
	 * pre	{@code accFactor > 0.0}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param simArchURI	URI of the simulation architecture to be created.
	 * @param accFactor				acceleration factor used in the real time simulation.
	 * @throws Exception			<i>to do</i>.
	 */
	public void			initialiseSimulationArchitecture(
		String simArchURI,
		double accFactor
		) throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor>
								atomicModelDescriptors = new HashMap<>();
		Map<String,CoupledModelDescriptor>
								coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(HairDryerUserModel.URI);
		submodels.add(HairDryerStateModel.URI);

		Map<Class<? extends EventI>,ReexportedEvent> reexported = null;
		Map<EventSource, EventSink[]> connections = null;

		atomicModelDescriptors.put(
				HairDryerUserModel.URI,
				RTAtomicModelDescriptor.create(
						HairDryerUserModel.class,
						HairDryerUserModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						accFactor));
		atomicModelDescriptors.put(
				HairDryerStateModel.URI,
				RTAtomicModelDescriptor.create(
						HairDryerStateModel.class,
						HairDryerStateModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						accFactor));

		if (simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {
			// when executed as a unit test, the simulation architecture
			// includes the hair dryer electricity model and events exported
			// by the state model are directed to the electricity model
			submodels.add(HairDryerElectricitySILModel.URI);

			atomicModelDescriptors.put(
					HairDryerElectricitySILModel.URI,
					RTAtomicHIOA_Descriptor.create(
							HairDryerElectricitySILModel.class,
							HairDryerElectricitySILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
							accFactor));

			connections = new HashMap<EventSource, EventSink[]>();
			EventSource source =
					new EventSource(HairDryerStateModel.URI,
									SwitchOnHairDryer.class);
			EventSink[] sinks =
					new EventSink[] {
							new EventSink(HairDryerElectricitySILModel.URI,
										  SwitchOnHairDryer.class)
					};
			connections.put(source, sinks);
			source = new EventSource(HairDryerStateModel.URI,
									 SwitchOffHairDryer.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricitySILModel.URI,
										  SwitchOffHairDryer.class)
					};
			connections.put(source, sinks);
			source = new EventSource(HairDryerStateModel.URI,
									 SetHighHairDryer.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricitySILModel.URI,
										  SetHighHairDryer.class)
					};
			connections.put(source, sinks);
			source = new EventSource(HairDryerStateModel.URI,
									 SetLowHairDryer.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricitySILModel.URI,
										  SetLowHairDryer.class)
					};
			connections.put(source, sinks);
		} else {
			// when *not% executed as a unit test, the simulation architecture
			// does not include the hair dryer electricity model and events
			// exported by the state model are reexported by the coupled model

			reexported =
					new HashMap<Class<? extends EventI>,ReexportedEvent>();
			reexported.put(
					SwitchOnHairDryer.class,
					new ReexportedEvent(HairDryerStateModel.URI,
										SwitchOnHairDryer.class));
			reexported.put(
					SwitchOffHairDryer.class,
					new ReexportedEvent(HairDryerStateModel.URI,
										SwitchOffHairDryer.class));
			reexported.put(
					SetHighHairDryer.class,
					new ReexportedEvent(HairDryerStateModel.URI,
										SetHighHairDryer.class));
			reexported.put(
					SetLowHairDryer.class,
					new ReexportedEvent(HairDryerStateModel.URI,
										SetLowHairDryer.class));
		}

		coupledModelDescriptors.put(
				HairDryerCoupledModel.URI,
				new RTCoupledModelDescriptor(
						HairDryerCoupledModel.class,
						HairDryerCoupledModel.URI,
						submodels,
						null,
						reexported,
						connections,
						null,
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						accFactor));

		// this sets the architecture in the plug-in for further reference
		// and use
		this.setSimulationArchitecture(
				new RTArchitecture(
						simArchURI,
						HairDryerCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS,
						accFactor));
	}
}
// -----------------------------------------------------------------------------
