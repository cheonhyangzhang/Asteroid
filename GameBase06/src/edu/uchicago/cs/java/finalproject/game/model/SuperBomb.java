package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class SuperBomb extends Sprite {

	  private final double FIRE_POWER = 35.0;
	  private final int RADIUS_INCREAMENT = 10;
	 
	
	  public SuperBomb(Falcon fal){
		
		super();
		
		
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();
		
		pntCs.add(new Point(1,1)); //top point
		
		pntCs.add(new Point(1,-1));
		pntCs.add(new Point(-1,-1));
		pntCs.add(new Point(-1,1));

		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 50 );
	    setRadius(6);
	    

	    setCenter( fal.getCenter() );



	}
	  

  @Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
	  g.drawOval(getCenter().x-getRadius(),getCenter().y-getRadius() , getRadius()*2, getRadius()*2);
	}


	//override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
		if (getExpire() == 0)
			CommandCenter.movFriends.remove(this);
		 else 
			setExpire(getExpire() - 1);
			setRadius(getRadius()+ RADIUS_INCREAMENT);
			ArrayList<Point> pntCs = new ArrayList<Point>();
			int nTmp = getRadius();
			pntCs.add(new Point(nTmp,nTmp)); //top point
			
			pntCs.add(new Point(nTmp,-nTmp));
			pntCs.add(new Point(-nTmp,-nTmp));
			pntCs.add(new Point(-nTmp,nTmp));

			assignPolarPoints(pntCs);
	}

}
