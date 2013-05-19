package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Point;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;


public class ShortGun extends Sprite {

	  private final double FIRE_POWER = 35.0;

	 
	
	  public ShortGun(Falcon fal){
		
		super();
		
		
	}

    //override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
 		if (getExpire() == 0)
 			CommandCenter.movFriends.remove(this);
		 else 
			setExpire(getExpire() - 1);
	}

}
