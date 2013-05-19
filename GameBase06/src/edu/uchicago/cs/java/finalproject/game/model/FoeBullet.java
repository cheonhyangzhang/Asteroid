package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;


public class FoeBullet extends Sprite {

	  private final double FIRE_POWER = 20.0;

	 
	
	  public FoeBullet(UFO ufo){
		
		super();
		
		
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();
		
		pntCs.add(new Point(0,1)); //top point
		
		pntCs.add(new Point(1,0));
		pntCs.add(new Point(0,-1));
		pntCs.add(new Point(-1,0));

		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 25 );
	    setRadius(10);
	    

	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( ufo.getDeltaX() +
	               Math.cos( Game.R.nextInt(6) ) * FIRE_POWER );
	    setDeltaY( ufo.getDeltaY() +
	               Math.sin( Game.R.nextInt(6)  ) * FIRE_POWER );
	    setCenter( ufo.getCenter() );

	    //set the bullet orientation to the falcon (ship) orientation
	    setOrientation(Game.R.nextInt(720));


	}

    @Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		super.draw(g);
    	g.setColor(Color.RED);
		g.fillPolygon(getXcoords(), getYcoords(), getDegrees().length);
	}

	//override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
 		if (getExpire() == 0)
 			CommandCenter.movFoes.remove(this);
		 else 
			setExpire(getExpire() - 1);
	}

}
