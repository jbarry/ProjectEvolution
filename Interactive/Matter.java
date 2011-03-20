package Interactive;

import Frame.Coordinate;
import Frame.GridPanel;

public abstract class Matter {
	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected int scnRng;
	public static int width = 5;
	public static int height = 5;
	
	public Matter() {
		
	}
	
	public Matter(double aMxHlth) {
		hlth = mxHlth = aMxHlth;
		scnRng = 10;
	}
	
	public Matter(double aMxHlth, int anId) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = 10;
	}
	
	public Matter(double aMxHlth, int anId, int aScnRng) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = aScnRng;
	}
	
	public void deplete(double val) {
		if (hlth - val < 0) hlth = 0;
		else hlth-=val;
	}
	
	 /**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public double numSurroundingObjects(int scanRange){
		double numObj = 0.0;
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].snd == 'f' 
						|| GridPanel.locationMap[i][j].snd == 'o'){
						numObj++;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		//make sure that scanning object was not included in scan.
		if(numObj >= width*height){
			numObj -= width*height; 
		}
		//return a normalized value. Will count "partially" discovered organisms
		//as a whole number, does not include "wrapped" scan.
		return Math.ceil(numObj/(width*height));
	}
}
