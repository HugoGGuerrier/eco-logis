package fr.sorbonne_u.components.cyphy.hem2021e3.equipments.meter;

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

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SetHighHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SetLowHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SwitchOffHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.hairdryer.mil.events.SwitchOnHairDryer;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.heater.mil.events.DoNotHeat;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.heater.mil.events.Heat;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.heater.mil.events.SwitchOffHeater;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.heater.mil.events.SwitchOnHeater;
import fr.sorbonne_u.components.cyphy.hem2021e2.equipments.meter.mil.ElectricMeterElectricityModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer.HairDryerRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.hairdryer.sil.HairDryerElectricitySILModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.heater.ThermostatedHeaterRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.heater.sil.HeaterElectricitySILModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.meter.sil.ElectricMeterCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.meter.sil.ElectricMeterElectricitySILModel;

// -----------------------------------------------------------------------------
/**
 * The class <code>ElectricMeterRTAtomicSimulatorPlugin</code> implements
 * the simulation plug-in for the <code>ElectricMeter</code> component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2021-10-07</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ElectricMeterRTAtomicSimulatorPlugin
extends		RTAtomicSimulatorPlugin
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** simulation architectures can have URI to name them; this is the
	 *  URI used in this example for unit tests.							*/
	public static final String		UNIT_TEST_SIM_ARCHITECTURE_URI =
															"UnitTestMeter";
	/** name used to pass the owner component reference as simulation
	 *  parameter.															*/
	public static final String		METER_REFERENCE_NAME = "MCRN";

	// Hair dryer and heater electricity models to be co-instantiated with the
	// electric meter electricity model to enable the sharing of continuous
	// variables between them, the electricity consumption of the two
	// appliances.
	// Nota: these static variables try to abstract the electric meter
	// implementation from the implementation of the appliances simulators.
	// Indeed, this solution still names explicitly the classes that implements
	// the models of the appliances, introducing a coupling between the present
	// class and the two model classes. A better solution should be implemented,
	// but for ALASCA, this way of doing things will be fine (and simple).

	/** URI of the hairdryer electricity model.								*/
	protected static final String	HAIR_DRYER_ELECTRICITY_MODEL_URI =
											HairDryerElectricitySILModel.URI;
	/** class implementing the hairdryer electricity model.					*/
	protected static final Class<HairDryerElectricitySILModel>
									HAIR_DRYER_ELECTRICITY_MODEL_CLASS =
											HairDryerElectricitySILModel.class;
	/** URI of the heater electricity model.								*/
	protected static final String	HEATER_ELECTRICITY_MODEL_URI =
											HeaterElectricitySILModel.URI;
	/** class implementing the heater electricity model.					*/
	protected static final Class<HeaterElectricitySILModel>
									HEATER_ELECTRICITY_MODEL_CLASS =
											HeaterElectricitySILModel.class;

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
		// models; because each model has been defined to retrieve the
		// reference to its owner component using its own parameter name,
		// we must pass the reference under each different name
		simParams.put(METER_REFERENCE_NAME, this.getOwner());
		simParams.put(ThermostatedHeaterRTAtomicSimulatorPlugin.
														OWNER_REFERENCE_NAME,
					  this.getOwner());
		simParams.put(HairDryerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME,
					  this.getOwner());

		// this will pass the parameters to the simulation models that will
		// then be able to get their own parameters
		super.setSimulationRunParameters(simParams);

		// remove the value so that the reference may not exit the context of
		// the component
		simParams.remove(METER_REFERENCE_NAME);
		simParams.remove(ThermostatedHeaterRTAtomicSimulatorPlugin.
														OWNER_REFERENCE_NAME);
		simParams.remove(HairDryerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
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
	 * @param accFactor		acceleration factor used in the real time simulation.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			initialiseSimulationArchitecture(
		String simArchURI,
		double accFactor
		) throws Exception
	{
		// For the project, the coupled model created for the electric meter
		// will include all of the models simulating the electricity consumption
		// and production for appliances and production units.
		// At this point, this only includes the electricity consumption models
		// of the hair dryer and the heater.

		Map<String,AbstractAtomicModelDescriptor>
									atomicModelDescriptors = new HashMap<>();
		Map<String,CoupledModelDescriptor>
									coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(HAIR_DRYER_ELECTRICITY_MODEL_URI);
		submodels.add(HEATER_ELECTRICITY_MODEL_URI);
		submodels.add(ElectricMeterElectricitySILModel.URI);

		atomicModelDescriptors.put(
				HAIR_DRYER_ELECTRICITY_MODEL_URI,
				RTAtomicHIOA_Descriptor.create(
						HAIR_DRYER_ELECTRICITY_MODEL_CLASS,
						HAIR_DRYER_ELECTRICITY_MODEL_URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						accFactor));
		atomicModelDescriptors.put(
				HEATER_ELECTRICITY_MODEL_URI,
				RTAtomicHIOA_Descriptor.create(
						HEATER_ELECTRICITY_MODEL_CLASS,
						HEATER_ELECTRICITY_MODEL_URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						accFactor));
		atomicModelDescriptors.put(
				ElectricMeterElectricitySILModel.URI,
				RTAtomicHIOA_Descriptor.create(
						ElectricMeterElectricitySILModel.class,
						ElectricMeterElectricitySILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						accFactor));

		Map<Class<? extends EventI>, EventSink[]> imported = null;

		if (!simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {
			// if not executed as a unit test, the events concerning the
			// heater and the hair dryer electricity consumption models will
			// be imported from the corresponding components simulation models

			imported = new HashMap<>();
			imported.put(
					SwitchOnHeater.class,
					new EventSink[] {
							new EventSink(HEATER_ELECTRICITY_MODEL_URI,
										  SwitchOnHeater.class)
					});
			imported.put(
					SwitchOffHeater.class,
					new EventSink[] {
							new EventSink(HEATER_ELECTRICITY_MODEL_URI,
										  SwitchOffHeater.class)
					});
			imported.put(
					Heat.class,
					new EventSink[] {
							new EventSink(HEATER_ELECTRICITY_MODEL_URI,
										  Heat.class)
					});
			imported.put(
					DoNotHeat.class,
					new EventSink[] {
							new EventSink(HEATER_ELECTRICITY_MODEL_URI,
										  DoNotHeat.class)
					});

			imported.put(
					SwitchOnHairDryer.class,
					new EventSink[] {
							new EventSink(HAIR_DRYER_ELECTRICITY_MODEL_URI,
										  SwitchOnHairDryer.class)
					});
			imported.put(
					SwitchOffHairDryer.class,
					new EventSink[] {
							new EventSink(HAIR_DRYER_ELECTRICITY_MODEL_URI,
										  SwitchOffHairDryer.class)
					});
			imported.put(
					SetHighHairDryer.class,
					new EventSink[] {
							new EventSink(HAIR_DRYER_ELECTRICITY_MODEL_URI,
										  SetHighHairDryer.class)
					});
			imported.put(
					SetLowHairDryer.class,
					new EventSink[] {
							new EventSink(HAIR_DRYER_ELECTRICITY_MODEL_URI,
										  SetLowHairDryer.class)
					});
		}

		// variable bindings between exporting and importing models
		Map<VariableSource,VariableSink[]> bindings =
								new HashMap<VariableSource,VariableSink[]>();

		// bindings between hair dryer and heater models to the electric
		// meter model
		bindings.put(
				new VariableSource("currentIntensity",
								   Double.class,
								   HAIR_DRYER_ELECTRICITY_MODEL_URI),
				new VariableSink[] {
						new VariableSink("currentHairDryerIntensity",
										 Double.class,
										 ElectricMeterElectricityModel.URI)
				});
		bindings.put(
				new VariableSource("currentIntensity",
								   Double.class,
								   HEATER_ELECTRICITY_MODEL_URI),
				new VariableSink[] {
						new VariableSink("currentHeaterIntensity",
										 Double.class,
										 ElectricMeterElectricityModel.URI)
				});

		// coupled model descriptor: an HIOA requires a
		// RTCoupledHIOA_Descriptor
		coupledModelDescriptors.put(
				ElectricMeterCoupledModel.URI,
				new RTCoupledHIOA_Descriptor(
						ElectricMeterCoupledModel.class,
						ElectricMeterCoupledModel.URI,
						submodels,
						imported,
						null,
						null,
						null,
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						null,
						null,
						bindings,
						accFactor));

		// this sets the architecture in the plug-in for further reference
		// and use
		this.setSimulationArchitecture(
				new RTArchitecture(
						simArchURI,
						ElectricMeterCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS,
						accFactor));
	}
}
// -----------------------------------------------------------------------------
