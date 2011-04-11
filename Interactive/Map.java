package Interactive;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import Frame.GridPanel;

public class Map {

	private String currMap;
	private Image middleEarth;
	private Image grassMap;
	private Image sandMap;
	private Image waterMap;
	private Image moonMap;
	
	public Map(){
		currMap = "Default";
		grassMap = new ImageIcon(getClass().getResource("maps/grass.jpg")).getImage();
		sandMap = new ImageIcon(getClass().getResource("maps/sand.jpg")).getImage();
		waterMap = new ImageIcon(getClass().getResource("maps/water.jpg")).getImage();
		moonMap = new ImageIcon(getClass().getResource("maps/moon.jpg")).getImage();
		middleEarth = new ImageIcon(getClass().getResource("maps/middle-earth-map.jpg")).getImage();
	}
	
	public void setCurrMap(String s){
		currMap = s;
	}
	
	public void paint(Graphics g){
		if(currMap.equals("Default"))
			g.drawRect(0, 0, GridPanel.WIDTH, GridPanel.HEIGHT);
		else if(currMap.equals("Grass"))
			g.drawImage(grassMap, 0, 0, GridPanel.WIDTH, GridPanel.HEIGHT, null);
		else if(currMap.equals("Sand"))
			g.drawImage(sandMap, 0, 0, GridPanel.WIDTH, GridPanel.HEIGHT, null);
		else if(currMap.equals("Water"))
			g.drawImage(waterMap, 0, 0, GridPanel.WIDTH, GridPanel.HEIGHT, null);
		else if(currMap.equals("Moon"))
			g.drawImage(moonMap, 0, 0, GridPanel.WIDTH, GridPanel.HEIGHT, null);
		else if(currMap.equals("Middle Earth"))
			g.drawImage(middleEarth, 0, 0, GridPanel.WIDTH, GridPanel.HEIGHT, null);
	}
}
