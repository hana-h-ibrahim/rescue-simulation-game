package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

public class CommandCenter implements SOSListener, ActionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private GameView game;
	private ArrayList<JButton> gameButtons;
	private ArrayList<JButton> grid;
	private JButton respond;
	private JComboBox<String> respondTo;
	private int selectedUnit;
	private ArrayList<Rescuable> rescueList;
	private JFrame responder;
	private JTextArea unit;

	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		rescueList = new ArrayList<Rescuable>();
		emergencyUnits = engine.getEmergencyUnits();
		game = new GameView(this);
		gameButtons = new ArrayList<JButton>();
		grid = new ArrayList<JButton>();
		JButton cycle = new JButton();
		cycle.setText("Advance Time");
		ImageIcon imageIcon2 = new ImageIcon("advancetime.png"); 
		Image image2 = imageIcon2.getImage(); 
		Image newimg2 = image2.getScaledInstance(250, 50, java.awt.Image.SCALE_SMOOTH); 
		cycle = new JButton(new ImageIcon(newimg2));
		cycle.setBorderPainted(false);
		cycle.setFocusPainted(false);
		cycle.addActionListener(this);
		gameButtons.add(cycle);
		game.getOverview().add(cycle, BorderLayout.EAST);

		for (int i = 0; i < 100; i++) {
			JButton button = new JButton();
			game.createGrid(button);
			button.addActionListener(this);
			button.setBorder(BorderFactory.createLineBorder(Color.black));
			button.setSize(40, 45);
			button.setOpaque(false);
			gameButtons.add(button);
			grid.add(button);
			game.createGrid(button);
			button.setOpaque(false);
			button.setContentAreaFilled(false);
		}

		InputStream myStream = new BufferedInputStream(new FileInputStream("kenvector_future.ttf"));
		Font font = Font.createFont(Font.TRUETYPE_FONT, myStream);
		Font biggerFont = font.deriveFont(Font.BOLD, 15f);
		cycle.setFont(biggerFont);
		cycle.setBackground(Color.BLACK);
		cycle.setForeground(Color.white);

		for (int i = 0; i < 5; i++) {
			JButton b = new JButton();
			b.setBackground(Color.cyan);
			Unit s = emergencyUnits.get(i);
			if (s instanceof Ambulance) {
				ImageIcon imageIcon = new ImageIcon("ambulance.png");
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
				b = new JButton(new ImageIcon(newimg));
				b.setBorderPainted(false);
				b.setFocusPainted(false);
				// b.setContentAreaFilled(false);
				b.setText("Ambulance");
				b.setFont(biggerFont);

				b.setBackground(Color.black);
				b.setVerticalTextPosition(SwingConstants.BOTTOM);
				b.setHorizontalTextPosition(SwingConstants.CENTER);
				b.setForeground(Color.white);
			}
			if (s instanceof DiseaseControlUnit) {
				ImageIcon imageIcon = new ImageIcon("v-truck.png");
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
				b = new JButton(new ImageIcon(newimg));
				b.setBorderPainted(false);
				b.setFocusPainted(false);
				// b.setContentAreaFilled(false);
				b.setText("Disease Unit");
				b.setFont(biggerFont);

				b.setBackground(Color.black);
				b.setVerticalTextPosition(SwingConstants.BOTTOM);
				b.setHorizontalTextPosition(SwingConstants.CENTER);
				b.setForeground(Color.white);

			}
			if (s instanceof Evacuator) {
				ImageIcon imageIcon = new ImageIcon("evacuator.png");
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
				b = new JButton(new ImageIcon(newimg));
				b.setBorderPainted(false);
				b.setFocusPainted(false);
				b.setText("Evacuator");
				b.setFont(biggerFont);
				b.setBackground(Color.black);
				b.setVerticalTextPosition(SwingConstants.BOTTOM);
				b.setHorizontalTextPosition(SwingConstants.CENTER);
				b.setForeground(Color.white);
			}
			if (s instanceof FireTruck) {
				ImageIcon imageIcon = new ImageIcon("firetruck.png");
				Image image = imageIcon.getImage(); 
				Image newimg = image.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
				b = new JButton(new ImageIcon(newimg));
				b.setBorderPainted(false);
				b.setFocusPainted(false);
				b.setText("Fire Truck");
				b.setFont(biggerFont);
				b.setBackground(Color.black);
				b.setVerticalTextPosition(SwingConstants.BOTTOM);
				b.setHorizontalTextPosition(SwingConstants.CENTER);
				b.setForeground(Color.white);

			}
			if (s instanceof GasControlUnit) {
				ImageIcon imageIcon = new ImageIcon("gascontrol.png");
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(120, 60, java.awt.Image.SCALE_SMOOTH);
				b = new JButton(new ImageIcon(newimg));
				b.setBorderPainted(false);
				b.setFocusPainted(false);
				b.setText("Gas Unit");
				b.setFont(biggerFont);
				b.setBackground(Color.black);
				b.setVerticalTextPosition(SwingConstants.BOTTOM);
				b.setHorizontalTextPosition(SwingConstants.CENTER);
				b.setForeground(Color.white);
			}
			b.addActionListener(this);
			b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			game.addSimulated(b);
			gameButtons.add(b);
		}
		respondTo = new JComboBox<String>();
		unit = new JTextArea();
		respond = new JButton();
		respond.setText("Respond");
		respond.addActionListener(this);
		responder = new JFrame();
		responder.setSize(400, 400);
		responder.setLayout(new BorderLayout());
		responder.add(respondTo, BorderLayout.CENTER);
		responder.add(unit, BorderLayout.EAST);
		responder.add(respond, BorderLayout.SOUTH);
		responder.setVisible(false);
		gameButtons.add(respond);
		game.revalidate();
		game.repaint();
	}

	public void actionPerformed(ActionEvent x) {
		JButton y = (JButton) x.getSource();
		int index = gameButtons.indexOf(y);
		if (index == 0) {
			try {
				engine.nextCycle();
			} catch (SimulationException e) {
				if (e instanceof CitizenAlreadyDeadException) {
					game.getLog().setText(game.getLog().getText() + "\n" + e.getMessage());
				} else if (e instanceof BuildingAlreadyCollapsedException) {
					game.getLog().setText(game.getLog().getText() + "\n" + e.getMessage());
				}
			}
			try {
				updateGrid();
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			game.getView().setText("Number of Casualties: " + engine.calculateCasualties() + "\n" + "Cycle #"
					+ engine.getCurrentCycle());
			if (engine.checkGameOver()) {
				game.setEnabled(false);
				JFrame over = new JFrame();
				over.setSize(400, 400);
				JTextArea o = new JTextArea();
				o.setText("Game Over" + "\n" + "Number of Casualties: " + engine.calculateCasualties());
				over.add(o);
				over.setVisible(true);
				over.revalidate();
				over.repaint();
			}
			game.revalidate();
			game.repaint();
			return;
		} else if (index >= 1 && index <= 100) {
			int index2 = grid.indexOf(y);
			int adx = (index2 % 10);
			int ady = (90 - (index2 - adx)) / 10;
			String s = "Empty Location";
			if (y.getText().equals("Citizen")) {
				for (int i = 0; i < visibleCitizens.size(); i++) {
					if (visibleCitizens.get(i).getLocation().getX() == adx
							&& visibleCitizens.get(i).getLocation().getY() == ady)
						s = visibleCitizens.get(i).toString();
				}
			} else if (y.getText().equals("Building")) {
				for (int i = 0; i < visibleBuildings.size(); i++) {
					if (visibleBuildings.get(i).getLocation().getX() == adx
							&& visibleBuildings.get(i).getLocation().getY() == ady)
						s = visibleBuildings.get(i).toString();
				}
			}
			game.updateInfo(s);
			responder.setVisible(false);
			responder.revalidate();
			responder.repaint();
			game.revalidate();
			game.repaint();

		} else if (index >= 101 && index <= 105) {
			String s = emergencyUnits.get(index - 101).toString();
			unit.setText("Unit information: " + "\n" + s);
			respond.setVisible(true);
			responder.setVisible(true);
			responder.revalidate();
			responder.repaint();
			game.revalidate();
			game.repaint();
			selectedUnit = index - 101;

		} else if (index == gameButtons.size() - 1) {
			Unit u = emergencyUnits.get(selectedUnit);
			Rescuable r = getRescuable(respondTo.getSelectedIndex());
			try {
				u.respond(r);
			} catch (SimulationException e) {
				if (e instanceof CannotTreatException) {
					unit.setText(unit.getText() + "\n" + e.getMessage() + "no");
				} else if (e instanceof IncompatibleTargetException) {
					unit.setText(unit.getText() + "\n" + e.getMessage() + "leh keda");
				}

			}
			String s = u.toString();
			unit.setText("Unit information: " + "\n" + s);
			game.revalidate();
			game.repaint();
		}
	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		String s = "";
		if (r instanceof ResidentialBuilding) {
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
			game.getLog().setText(game.getLog().getText() + "\n" + "Building at location(" + r.getLocation().getX()
					+ "," + r.getLocation().getY() + ") has been struck by " + r.getDisaster().toString());

			s = "Building at location (" + r.getLocation().getX() + "," + r.getLocation().getY() + ")";
		} else {
			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
			game.getLog().setText(game.getLog().getText() + "\n" + "Citizen at location(" + r.getLocation().getX() + ","
					+ r.getLocation().getY() + ") has been struck by " + r.getDisaster().toString());

			s = "Citizen with ID: " + ((Citizen) r).getNationalID() + " at location (" + r.getLocation().getX() + ","
					+ r.getLocation().getY() + ")";
		}
		rescueList.add(r);
		respondTo.addItem(s);
	}

	public static void main(String[] args) throws Exception {
		CommandCenter x = new CommandCenter();
	}

	public void updateGrid() throws FontFormatException, IOException {
		for (int i = 0; i < visibleCitizens.size(); i++) {
			int index = (90 - (visibleCitizens.get(i).getLocation().getY() * 10))
					+ visibleCitizens.get(i).getLocation().getX();
			if (visibleCitizens.get(i).getDisaster().isActive()) {
				InputStream myStream = new BufferedInputStream(new FileInputStream("kenvector_future.ttf"));
				Font font = Font.createFont(Font.TRUETYPE_FONT, myStream);
				ImageIcon imageIcon = new ImageIcon("Citizen.png");
				Image image = imageIcon.getImage();
				Image newimg = image.getScaledInstance(50, 60, java.awt.Image.SCALE_SMOOTH); 
				imageIcon = new ImageIcon(newimg); 
				grid.get(index).setIcon(imageIcon);
				grid.get(index).setText("Citizen");
				grid.get(index).setFont(font);

			}
		}
		for (int i = 0; i < visibleBuildings.size(); i++) {
			int index = 90 - (visibleBuildings.get(i).getLocation().getY() * 10)
					+ visibleBuildings.get(i).getLocation().getX();
			if (visibleBuildings.get(i).getDisaster().isActive()) {
				game.updateGrid(index, "Building");
			} else if (!visibleBuildings.get(i).getDisaster().isActive()) {
				grid.get(index).setBackground(new Color(0, 0, 0, 65));
			}
		}
		game.revalidate();
		game.repaint();
	}

	public Rescuable getRescuable(int x) {
		return rescueList.get(x);
	}

}
