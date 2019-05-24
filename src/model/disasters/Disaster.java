package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.SimulationException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active;
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	}
	public void strike() throws SimulationException{
		if(this instanceof Injury || this instanceof Infection) {
			if(((Citizen)target).getState()==CitizenState.DECEASED)
				throw new CitizenAlreadyDeadException(this, "A planned disaster failed to strike the citizen as the citizen is alrady dead.");
		}
		else if(this instanceof Fire || this instanceof GasLeak || this instanceof Collapse) {
			if(((ResidentialBuilding)target).getStructuralIntegrity()==0)
				throw new BuildingAlreadyCollapsedException(this, "A planned disaster failed to strike the building as the building is already collapsed.");
		}
			
			target.struckBy(this);
			active=true;
		
	}
	public String toString() {
		String s ="";
		if(this instanceof Fire)
			s = "Fire";
		else if(this instanceof GasLeak)
			s = "GasLeak";
		else if(this instanceof Infection)
			s = "Infection";
		else if(this instanceof Injury)
			s = "Injury";
		else if(this instanceof Collapse)
			s = "Collapse";
		return s;
	}
	
}
