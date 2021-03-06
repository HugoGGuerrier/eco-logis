package fr.sorbonne_u.components.cyphy.hem2021e2.equipments.meter.mil;

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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.hem2021e2.HEM_ReportI;
import fr.sorbonne_u.components.cyphy.hem2021e2.utils.Electricity;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

// -----------------------------------------------------------------------------
/**
 * The class <code>ElectricMeterElectricityModel</code> defines the simulation
 * model for the electric meter electricity consumption.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This model is an HIOA model that imports variables, hence shows how this kind
 * of models are programmed.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code STEP > 0.0}
 * </pre>
 * 
 * <p>Created on : 2021-09-24</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ElectricMeterElectricityModel
extends		AtomicHIOA
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/** URI for a model; works when only one instance is created.			*/
	public static final String		URI = ElectricMeterElectricityModel.class.
																getSimpleName();
	/** tension of electric circuit for appliances in volts.			 	*/
	public static final double		TENSION = 220.0;

	/** current intensity of the heater in amperes.							*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>			currentHeaterIntensity;
	/** current intensity of the heater in amperes.							*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>			currentHairDryerIntensity;

	/** evaluation step for the equation (assumed in seconds).				*/
	protected static final double	STEP = 0.1;
	/** evaluation step as a duration, including the time unit.				*/
	protected final Duration		evaluationStep;

	/** current total intensity of the house in amperes.					*/
	@InternalVariable(type = Double.class)
	protected final Value<Double>	currentIntensity =
											new Value<Double>(this, 0.0);
	/** current total consumption of the house in kwh.						*/
	@InternalVariable(type = Double.class)
	protected final Value<Double>	currentConsumption =
											new Value<Double>(this, 0.0);

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create an <code>ElectricMeterElectricityModel</code> instance.
	 * 
	 * <pre>
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code simulationEngine == null || simulationEngine instanceof HIOA_AtomicEngine}
	 * post	{@code getURI() != null}
	 * post	{@code uri != null implies this.getURI().equals(uri)}
	 * post	{@code getSimulatedTimeUnit().equals(simulatedTimeUnit)}
	 * post	{@code simulationEngine != null implies getSimulationEngine().equals(simulationEngine)}
	 * post	{@code !isDebugModeOn()}
	 * </pre>
	 *
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 * @throws Exception		<i>to do</i>.
	 */
	public				ElectricMeterElectricityModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
		this.evaluationStep = new Duration(STEP, this.getSimulatedTimeUnit());
		this.setLogger(new StandardLogger());
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * update the total electricity consumption in kwh given the current
	 * intensity has been constant for the duration {@code d}.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code d != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param d	duration for which the intensity has been maintained.
	 */
	protected void		updateConsumption(Duration d)
	{
		this.currentConsumption.v +=
				Electricity.computeConsumption(
									d, TENSION*this.currentIntensity.v);
		this.currentConsumption.time =
				this.currentConsumption.time.add(d);
	}

	/**
	 * compute the current total intensity.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected void		computeTotalIntensity()
	{
		// simple sum of all incoming intensities
		this.currentIntensity.v =
				this.currentHairDryerIntensity.v +
				this.currentHeaterIntensity.v;
		this.currentIntensity.time = this.getCurrentStateTime();

		// Tracing
		StringBuffer message = new StringBuffer("current total consumption: ");
		message.append(this.currentIntensity.v);
		message.append(" at ");
		message.append(this.getCurrentStateTime());
		message.append('\n');
		this.logMessage(message.toString());
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		super.initialiseVariables(startTime);

		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		this.computeTotalIntensity();
		this.currentConsumption.v = 0.0;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI>	output()
	{
		// The model does not export any event.
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		// trigger a new internal transition at each evaluation step duration
		return this.evaluationStep;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		// update the current consumption since the last consumption update.
		// must be done before recomputing the instantaneous intensity.
		this.updateConsumption(elapsedTime);
		// recompute the current total intensity
		this.computeTotalIntensity();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.updateConsumption(endTime.subtract(this.currentConsumption.time));

		this.logMessage("simulation ends.\n");
		super.endSimulation(endTime);
	}

	// -------------------------------------------------------------------------
	// Optional DEVS simulation protocol: simulation report
	// -------------------------------------------------------------------------

	/**
	 * The class <code>ElectricMeterElectricityReport</code> implements the
	 * simulation report for the <code>ElectricMeterElectricityModel</code>.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p><strong>Invariant</strong></p>
	 * 
	 * <pre>
	 * invariant	true
	 * </pre>
	 * 
	 * <p>Created on : 2021-10-01</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public static class		ElectricMeterElectricityReport
	implements	SimulationReportI, HEM_ReportI
	{
		private static final long serialVersionUID = 1L;
		protected String	modelURI;
		protected double	totalConsumption; // in kwh

		public			ElectricMeterElectricityReport(
			String modelURI,
			double totalConsumption
			)
		{
			super();
			this.modelURI = modelURI;
			this.totalConsumption = totalConsumption;
		}

		@Override
		public String	getModelURI()
		{
			return this.modelURI;
		}

		@Override
		public String	printout(String indent)
		{
			StringBuffer ret = new StringBuffer(indent);
			ret.append("---\n");
			ret.append(indent);
			ret.append('|');
			ret.append(this.modelURI);
			ret.append(" report\n");
			ret.append(indent);
			ret.append('|');
			ret.append("total consumption in kwh = ");
			ret.append(this.totalConsumption);
			ret.append(".\n");
			ret.append(indent);
			ret.append("---\n");
			return ret.toString();
		}		
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#getFinalReport()
	 */
	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new ElectricMeterElectricityReport(URI,
												  this.currentConsumption.v);
	}
}
// -----------------------------------------------------------------------------
