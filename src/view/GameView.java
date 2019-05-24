package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/* 
 * 
 * 
 * 
 * 
 * check 
 * check 
 * check 
 */
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controller.CommandCenter;
import exceptions.SimulationException;
import javafx.scene.layout.Border;

public class GameView extends JFrame{
	private final Starter lol = new Starter();
	private CommandCenter handler;
	private JPanel rescuePanel;
	private JPanel infoPanel;
	private JPanel unitsPanel;
	private JPanel overview;
	private JFrame welcome;
	private JButton starter = new JButton();
	private JTextArea view;
	private JTextArea info;
	private JPanel infod;
	private JTextArea log;

	public GameView(CommandCenter x) throws IOException,UnsupportedAudioFileException, LineUnavailableException, FontFormatException {
		handler = x;
		welcomeScreen();
		InputStream myStream = new BufferedInputStream(new FileInputStream("kenvector_future.ttf"));
		Font font = Font.createFont(Font.TRUETYPE_FONT,myStream);  
		Font biggerFont = font.deriveFont(Font.BOLD, 18f);
		Font small = font.deriveFont(Font.BOLD, 14f);
		JPanel back = new JPanel();
		back.setLayout(new BorderLayout());
		this.add(back, BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Rescue Simulation Game");
		this.setIconImage(ImageIO.read(new File("favicon.png")));
		setBounds(0,0,1250,900);
		
		//rescuepanel
		rescuePanel= new ImagePanel ();
		rescuePanel.setLayout(new GridLayout(10,10));
		rescuePanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
		rescuePanel.setSize(400,450);
		
		
		back.add(rescuePanel, BorderLayout.CENTER);
		
		//infopanel 
		infod = new JPanel();
		infod.setLayout(new BorderLayout());
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0,1));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		info = new MyTextArea(25,25);
		info.setSize(200, 400);
		info.setEditable(false);
		info.setAutoscrolls(true);
		info.setFont(new Font("Consolas", Font.BOLD, 14));
		info.setBackground(new Color(0,0,0,65));
		info.setForeground(Color.white);
		JPanel infoWithScroll = new JPanel(); 
		infoWithScroll.add(info);
	    JScrollPane scroll = new JScrollPane(info);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    infoWithScroll.add(scroll, BorderLayout.CENTER);
		info.setText("Information about object selected: " + "\n");
		infod.add(infoWithScroll,BorderLayout.CENTER);
		log = new MyTextArea(25,25);
		log.setSize(200, 400);
		log.setEditable(false);
		log.setText("Events that occured:" + "\n");
		log.setWrapStyleWord(isEnabled());
		log.setLineWrap(true);
		log.setFont(new Font("Consolas", Font.BOLD, 14));
		log.setBackground(new Color(0,0,0,65));
		log.setForeground(Color.white);
		String logTitle = new String("Event Log"); 
		String infoTitle = new String("Information"); 		
		log.setBorder(BorderFactory.createTitledBorder(logTitle));
		info.setBorder(BorderFactory.createTitledBorder(infoTitle));
		info.setBorder(BorderFactory.createTitledBorder("Information"));
		   ((TitledBorder) info.getBorder()).setTitleFont(small);
		   ((TitledBorder) info.getBorder()).setTitleColor(Color.CYAN);
		log.setBorder(BorderFactory.createTitledBorder("Event Log"));
			((TitledBorder) log.getBorder()).setTitleFont(small);
			((TitledBorder) log.getBorder()).setTitleColor(Color.CYAN);
		infoPanel.add(infod, BorderLayout.CENTER);
		infoPanel.add(log, BorderLayout.EAST);
		infoPanel.setSize(100,250);
		infoPanel.setBackground(new Color(0,0,0,65));
		infoPanel.setBackground(Color.BLACK);
		back.add(infoPanel, BorderLayout.EAST);
		unitsPanel = new JPanel();
		unitsPanel.setLayout(new GridLayout(5,1));
		back.add(unitsPanel, BorderLayout.WEST);
		unitsPanel.setSize(200,450);
		unitsPanel.setBackground(new Color(160,160,160));
		unitsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 20));
		overview = new JPanel();
		view = new JTextArea();
		view.setText("Number of Casualties: 0" + "\n" + "Cycle #0");
		view.setFont(biggerFont);
		view.setBackground(new Color(0,0,0,0));
		view.setForeground(Color.cyan);
		view.setEditable(false);
		view.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		overview.add(view, BorderLayout.CENTER);
		back.add(overview, BorderLayout.SOUTH);
		overview.setBackground(new Color(4, 45, 41));
		Clip clip = loadClip("battleThemeA.wav");
		clip.open();
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		this.repaint();
		this.revalidate();
		this.update(getGraphics());
	}
	public Clip loadClip( String filename )
	{
		Clip in = null;
		try
		{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename));;
			in = AudioSystem.getClip();
			in.open( audioIn );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return in;
	}
	public void open() {
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	
	

	private void welcomeScreen() throws IOException, FontFormatException{
		welcome = new JFrame();
		welcome.setTitle("Rescue Simulation Game");
		welcome.setIconImage(ImageIO.read(new File("favicon.png")));
		welcome.setSize(800,500);
		welcome.setResizable(false);
		welcome.setDefaultCloseOperation(EXIT_ON_CLOSE);;
		welcome.setVisible(true);
		JTextArea j = new MyTextArea(100,100);
		j.setBackground(new Color(1,1,1, (float) 0.01));
		j.setLineWrap(isEnabled());
		j.setText("Welcome to Rescue Simulation!" + "\n" + "\n" + 
				"In this game you control the command center that is in charge with dispatching emergency units"+ "\n" +  "available in the city."
				+ "\n" + "\n" + "your task is to respond to any and all disasters" + "\n" + "that occur during the course of the game and come out with the least amount of casualties possible. Good Luck!");
		j.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		welcome.add(j, BorderLayout.CENTER);
		welcome.setBackground(Color.orange);
		starter.setText("Click to start game");
		starter.setForeground(Color.CYAN);
		starter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		starter.setBackground(Color.DARK_GRAY);
		InputStream myStream = new BufferedInputStream(new FileInputStream("kenvector_future.ttf"));
		Font font = Font.createFont(Font.TRUETYPE_FONT,myStream);  
		Font biggerFont = font.deriveFont(Font.BOLD, 18f);
		j.setFont(biggerFont);
		starter.setFont(biggerFont);
		j.setForeground(Color.WHITE);
		welcome.add(starter, BorderLayout.SOUTH);
		starter.addActionListener(lol);
		welcome.revalidate();
		welcome.repaint();
	}

	public class ImagePanel extends JPanel{
	    private BufferedImage image;
	    public ImagePanel() {
	       try {                
	          image = ImageIO.read(new File("Ruined City Background Preview.png"));
	       } catch (IOException ex) {
				System.out.println(toString());
	       }
	    }
	    @Override
	    protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int width = image.getWidth(this);
			int height = image.getHeight(this);
			if (width > 0 && height > 0) {
				for (int x = 0; x < getWidth(); x += width) {
					for (int y = 0; y < getHeight(); y += height) {
						g.drawImage(image, x, y, width, height, this);
					}
				}
			}
		}
	}

	public class MyTextArea extends JTextArea {
		private Image img;
		public MyTextArea(int a, int b) {
			super(a,b);
			try{
				img = ImageIO.read(new File("Ruined City Background Preview.png"));
			} catch(IOException e) {
				System.out.println(e.toString());
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			int width = img.getWidth(this);
			int height = img.getHeight(this);
			if (width > 0 && height > 0) {
				for (int x = 0; x < getWidth(); x += width) {
					for (int y = 0; y < getHeight(); y += height) {
						g.drawImage(img, x, y, width, height, this);
					}
				}
			}
			super.paintComponent(g);
		}
	}
	private class Starter implements ActionListener {
		public void actionPerformed(ActionEvent x){
			try {
				start();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			welcome.setVisible(false);
			setVisible(true);
		}
		public void start() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
			welcome.setVisible(false);
			setVisible(true);
		}
	}
	public static void main(String[] args) throws IOException {
	}
	public JFrame getWelcome() {
		return welcome;
	}
	public JButton getStarter() {
		return starter;
	}
	public void load(JButton b) throws IOException {
		JLabel x = new JLabel(new ImageIcon(ImageIO.read(new File("v-truck.png"))));
		b.add(x);
	}
	public JPanel getOverview() {
		return overview;
	}
	public JTextArea getView() {
		return view;
	}
	public void updateOverview(int x, int y) {
		String s = "Number of casualties: " + x + "\n" 
				+ "Cycle#" + y;
		System.out.println(s);
		view.setText(s);
		revalidate();
		repaint();
	}
	public void addSimulated(JButton b) {
		unitsPanel.add(b);
	}
	public void createGrid(JButton b) {
		rescuePanel.add(b);
	}
	public void updateInfo(String s) {
		info.setText("Information:" + "\n" + s);
	}
	public JTextArea getInfo() {
		return info;
	}
	public void updateGrid(int x, String s) throws FontFormatException, IOException {
		InputStream myStream = new BufferedInputStream(new FileInputStream("kenvector_future.ttf"));
		Font font = Font.createFont(Font.TRUETYPE_FONT,myStream);  
		((JButton)rescuePanel.getComponent(x)).setText(s); 
		((JButton)rescuePanel.getComponent(x)).setFont(font);
		ImageIcon imageIcon = new ImageIcon("safebuilding.png"); 
		Image image = imageIcon.getImage(); 
		Image newimg = image.getScaledInstance(60, 70,  java.awt.Image.SCALE_SMOOTH); 
		imageIcon = new ImageIcon(newimg); 	
		JButton j = (JButton)rescuePanel.getComponent(x);
		j.setIcon(imageIcon);

	}
	public JPanel getInfoD() {
		return infod;
	}
	public JTextArea getLog() {
		return log;
	}
}
