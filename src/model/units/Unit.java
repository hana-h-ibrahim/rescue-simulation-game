package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws SimulationException {
		
		 if(this instanceof FireUnit || this instanceof PoliceUnit ) {
			if(r instanceof Citizen)
				throw new IncompatibleTargetException(this, r, "Invalid Target: must be a ResidentialBuilding");
		}
		

		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		//if(!canTreat())
		//	throw new CannotTreatException(this, r, "This unit cannot treat the current target as it is safe.");

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) throws SimulationException {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());
		if(!canTreat())
			throw new CannotTreatException(this, r, "This unit cannot treat the current target.");

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	public boolean canTreat() {
		if(this instanceof MedicalUnit) {
			if(this instanceof Ambulance)
				return ((Ambulance)this).canTreat();
			else
				return ((DiseaseControlUnit)this).canTreat();
		}
		else if(this instanceof FireUnit) {
			if (this instanceof FireTruck) {
				if(((ResidentialBuilding)target).getFireDamage()==0) {
					return false;
					}
				else if(target.getDisaster() instanceof GasLeak)
					return false;
			}
			else if(this instanceof GasControlUnit) {
				if(((ResidentialBuilding)target).getGasLevel()==0)
				return false;
				else if(target.getDisaster() instanceof Fire)
					return false;
			}
		}
		else if(this instanceof PoliceUnit) {
			if(((ResidentialBuilding)target).getFoundationDamage()>0)
				return true;
				else if(((ResidentialBuilding)target).getGasLevel()==0 && ((ResidentialBuilding)target).getFireDamage()==0)
				return false;
			else if(!(target.getDisaster() instanceof Collapse))
				return false;
		}
		return true;
	}
	public String toString() {
		String target = "none";
		if(this instanceof MedicalUnit && target!=null) {
			 target = "Citizen at location (" + getLocation().getX()+ ","+ getLocation().getY() + ")";
		}
		else if(this instanceof FireUnit || this instanceof Evacuator) {
			target = "Building at location ( " + getLocation().getX()+ ","+ getLocation().getY() + ")";
		}
		String s = "UnitID: " + unitID + "\n" 
				+ "State: " + state + "\n"
				+ "Address: " + getLocation().getX()+ ","+ getLocation().getY() + "\n"
				+ "Target " + target + "\n"
				+ "Distance to target: " + distanceToTarget + "\n"
				+ "Steps per Cycle: " + stepsPerCycle + "\n";
		if(this instanceof Evacuator) {
			s = s + "Max Capacity: " + ((Evacuator)this).getMaxCapacity() + "\n"
					+ "Passengers: " + ((Evacuator)this).getPassengers().size() + "\n"
					+ "Distance to base: " + ((Evacuator)this).getDistanceToBase();
			for(int i =0;i<((Evacuator)this).getPassengers().size();i++) {
				s = s + "\n" + ((Evacuator)this).getPassengers().get(i).toString();
			}
			 
		}
		return s;
	}
}
