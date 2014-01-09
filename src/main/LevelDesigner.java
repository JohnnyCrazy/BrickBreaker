package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import powerups.Powerup;
import bricks.Brick;

//JFrame Fenster zum Level erstellen
//Implementiert noch einen MouseListener, damit man Mouse Klicks abfangen kann
public class LevelDesigner extends JFrame implements MouseListener{

	//Gibt eine Warnung in Eclipse aus, wird bei mehreren JFrames benötigt (soweit ich weiß)
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	Level level;
	JPanel center;
	
	JList<String> brickTool;
	JList<String> powerupTool;
	
	JButton saveButton;
	JButton instructions;
	
	//Starten des LevelDesigners aus einem anderen Thread
	//Damit wird der 1. Thread (Spiel) nicht überlastet.
	public static void start(final int x,final int y) 
	{
		new Thread(new Runnable() 
		{	
			@Override
			public void run()
			{
				new LevelDesigner(x, y);
			}
		}).start();
	}
	public LevelDesigner(int x,int y) {
		
		
		//Das hier wurde größtenteils vom JFrame Designer für Eclipse erstellt,
		setBounds(new Rectangle(0, 0, 40, 0));
		setType(Type.POPUP);
		setTitle("BrickBreaker - Level Designer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(x,y, 916, 730);
		contentPane = new JPanel();
		contentPane.setBounds(new Rectangle(0, 0, 40, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(50, 10));
		contentPane.add(panel, BorderLayout.WEST);
		
		//Button zum anzeigen der Anleitung
		instructions = new JButton("Anleitung");
		instructions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Zeigt die Hilfe in einem Popup
				JOptionPane.showMessageDialog(null, "Folgende Sachen kannst du im LevelEditor machen: \n\n" +
						"Neues Level erstellen:\n" +
						"- Klicke auf den Button 'Level erstellen' und gib den Namen ein (Ohne Dateiendung)\n" +
						"- Wähle links einen Brick-Type und rechts ein Powerup (Beschreibung im Hauptmenü unter 'Anleitung'\n" +
						"- Oben im jeweiligen Kästchen steht der Brick-Type, unten das Powerup\n" +
						"- Klicke 'Speichern', damit das Level gesichert wird\n\n\n" +
						"Vorhandenes Level bearbeiten:\n" +
						"- Wähle ein Level aus der Drop-Down-Liste\n" +
						"- Wähle links einen Brick-Type und rechts ein Powerup (Beschreibung im Hauptmenü unter 'Anleitung'\n" +
						"- Oben im jeweiligen Kästchen steht der Brick-Type, unten das Powerup\n" +
						"- Klicke 'Speichern', damit das Level gesichert wird\n\n\n" +
						"","Anleitung",JOptionPane.INFORMATION_MESSAGE );
			}
		});
		
		//JLabel und JList, JList zum Auswählen der Bricks + Powerups
		JLabel lblNewLabel = new JLabel("Brick:");
		brickTool = new JList<String>(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"1HIT", "2HIT", "3HIT","NONE","SOLID"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		//Man kann nur ein Brick auswählen
		brickTool.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		
		//Layout Sachen, wurden vom Designer erstellt
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
				.addComponent(brickTool, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
				.addComponent(instructions)
				.addGap(30)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(brickTool)
					.addPreferredGap(ComponentPlacement.RELATED, 547, Short.MAX_VALUE)
					.addComponent(instructions)
		));
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblWhleDasLevel = new JLabel("Wähle das Level aus:");
		panel_1.add(lblWhleDasLevel);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("");
		for(String name : LevelManager.getInstance().getLevelList())
			comboBox.addItem(name);
		comboBox.addItemListener(new ItemListener() {
			
			//Level auswahl
			@Override
			public void itemStateChanged(ItemEvent ie) 
			{
				String item =(String) ie.getItem();
				if(item.equalsIgnoreCase("") || ie.getStateChange() == ItemEvent.DESELECTED)
					return;
				level = LevelManager.getInstance().getLevel(item);
				
				//Falls das Level nicht bestehen sollte
				if(level == null)
					level = LevelManager.getInstance().createLevel(item);
				
				ArrayList<Brick> bricks = level.getBricks();
				
				
				//Setze die Felder
				for(int i = 0;i < bricks.size();i++)
				{
					JLabel label = (JLabel) center.getComponents()[i];
					label.setEnabled(true);
					//HTML damit man ein Zeilenumbruch machen kann
					label.setText("<HTML>" + bricks.get(i).getName() +"<BR>" + bricks.get(i).getpowerup().getName() + "</HTML>");
				}
				saveButton.setEnabled(true);
			}
		});
		panel_1.add(comboBox);
		
		JLabel lblOder = new JLabel("oder");
		panel_1.add(lblOder);
		
		JButton btnErstelleEinNeues = new JButton("erstelle ein Neues");
		btnErstelleEinNeues.addActionListener(new ActionListener() {
			
			//Wenn der Button geklickt wurde:
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				String name = JOptionPane.showInputDialog(null, "Gib den Namen deines neuen Levels ein:", "LevelDesigner - Wähle den Namen", JOptionPane.INFORMATION_MESSAGE);
				if(name == null || name == "")
					return;
				while(name.contains(":"))
				{
					name = JOptionPane.showInputDialog(null, "Gib den Namen deines neuen Levels ein (Keine Doppelpunkte):", "LevelDesigner - Wähle den Namen", JOptionPane.INFORMATION_MESSAGE);
					if(name == null || name == "")
						return;
				}
				//Füge zur Combobox hinzu und selecte es
				comboBox.addItem(name);
				comboBox.setSelectedItem(name);
			}
		});
		panel_1.add(btnErstelleEinNeues);
		
		JPanel panel_2 = new JPanel();
		panel_2.setMinimumSize(new Dimension(50, 10));
		contentPane.add(panel_2, BorderLayout.EAST);
		
		JLabel lblPowerup = new JLabel("Powerup:");
		
		powerupTool = new JList<String>(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"NONE", "BallGrow", "Bomb","BallShrink","PlayerShrink","PlayerGrow","PlayerSwitch"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		powerupTool.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		saveButton = new JButton("Speichern");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Component[] labels = center.getComponents();
				ArrayList<Brick> bricks = new ArrayList<>();
				ArrayList<Powerup> powerups = new ArrayList<>();
				
				for(int i = 0;i < labels.length;i++)
				{
					JLabel label = (JLabel) labels[i];
					
					//Replace und Split, wegen HTML etc
					String brick = label.getText().split("<BR>")[0].replace("<HTML>", "");
					bricks.add(LevelManager.getInstance().getBlockForName(brick));
					
					//Replace und Split, wegen HTML etc
					String powerup = label.getText().split("<BR>")[1].replace("</HTML>", "");
					powerups.add(LevelManager.getInstance().getPowerupForName(powerup));
				}
				level.setupBricks(bricks,powerups);
				LevelManager.getInstance().saveLevel(level);
				JOptionPane.showMessageDialog(null, "Level '" + level.getName() + "' erfolgreich gespeichert!");
			}
		});
		//erstmal auf false setzen, da ja am Anfang kein Level ausgewählt ist
		saveButton.setEnabled(false);
		
		
		//Design etc
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(lblPowerup, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
				.addComponent(powerupTool, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(saveButton)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPowerup)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(powerupTool)
					.addPreferredGap(ComponentPlacement.RELATED, 547, Short.MAX_VALUE)
					.addComponent(saveButton))
		);
		panel_2.setLayout(gl_panel_2);
		center = new JPanel();
		contentPane.add(center, BorderLayout.CENTER);
		center.setLayout(new GridLayout(9, 10, 0, 0));
		
		
		JLabel insert;
		//Erzeugung aller JLables aka Bricks, und den Mouselistener hinzufügen
		for (int i = 0; i < 90; ++i)
		{
			insert = new JLabel("-");
			insert.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			insert.addMouseListener(this);
			insert.setEnabled(false);
			insert.setHorizontalAlignment(SwingConstants.CENTER);
			center.add(insert);
		}
		setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//Wenn ein Brick im Feld angeklickt wurde
	@Override
	public void mousePressed(MouseEvent me) 
	{
		//Falls kein Brick/Powerup ausgewählt wurde
		if(brickTool.getSelectedValue() == null || powerupTool.getSelectedValue() == null || level == null)
			return;
		String brickname = brickTool.getSelectedValue();
		String powerupname = powerupTool.getSelectedValue();
		
		JLabel label = (JLabel) me.getSource();
		
		String old = label.getText().split("<BR>")[1].replace("</HTML>", "");
		label.setText("<HTML>" + brickname + "<BR>" + old + "</HTML>");
			
		old = label.getText().split("<BR>")[0].replace("<HTML>", "");
		label.setText("<HTML>" + old + "<BR>" + powerupname + "</HTML>");
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
