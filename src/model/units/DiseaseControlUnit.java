package model.units;

import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}

	public void respond(Rescuable r) throws SimulationException {
		if(r instanceof ResidentialBuilding)
			throw new IncompatibleTargetException(this, r, "Invalid Target: must be a Citizen");
	
		if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}
	public boolean canTreat() {
		if(((Citizen)getTarget()).getToxicity()>0)
			return true;
		if(((Citizen)getTarget()).getState()==CitizenState.SAFE)
			return false;
		if(getTarget().getDisaster() instanceof Injury)
			return false;
		
		return true;
	}

}
