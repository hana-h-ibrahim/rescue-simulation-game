package model.disasters;

import exceptions.SimulationException;
import model.people.Citizen;


public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {
		super(startCycle, target);
	}
@Override
public void strike() throws SimulationException
{
	super.strike();
	Citizen target = (Citizen)getTarget();
	target.setToxicity(target.getToxicity()+25);
	//super.strike();
}
	@Override
	public void cycleStep() {
		Citizen target = (Citizen)getTarget();
		target.setToxicity(target.getToxicity()+15);
		
	}

}
