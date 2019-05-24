package model.units;

import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Infection;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws SimulationException {
		if(r instanceof ResidentialBuilding)
			throw new IncompatibleTargetException(this, r, "Invalid Target: must be a Citizen");
	
		if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}
	public boolean canTreat() {
		if(((Citizen)getTarget()).getBloodLoss()>0)
			return true;
		if(((Citizen)getTarget()).getState()==CitizenState.SAFE)
			return false;
		if(getTarget().getDisaster() instanceof Infection)
			return false;
		
		return true;
	}

}
