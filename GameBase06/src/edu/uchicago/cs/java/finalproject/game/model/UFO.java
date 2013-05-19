package edu.uchicago.cs.java.finalproject.game.model;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import edu.uchicago.cs.java.finalproject.controller.Game;

public class UFO extends Sprite {

	
	private int nSpin;
	
	//radius of a large asteroid
	private final int RAD = 100;
	
	//nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris
	public UFO(){
		
		//call Sprite constructor
		super();
		
		
			
		//random delta-x
		int nDX = Game.R.nextInt(10);
		if(nDX %2 ==0)
			nDX = -nDX;
		setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(10);
		if(nDY %2 ==0)
			nDY = -nDY;
		setDeltaY(nDY);
			
		
		setRadius(RAD);
		
	}
	

	//overridden
	public void move(){
		super.move();
		if (Game.nTick% (Game.R.nextInt(50)+1) == 0){
			CommandCenter.movFoes.add(new FoeBullet(this));
		}
		//an asteroid spins, so you need to adjust the orientation at each move()
		setOrientation(getOrientation() + getSpin());
		
	}

	public int getSpin() {
		return this.nSpin;
	}
	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}	


	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Image imgNewShip = null;
		if (imgNewShip==null){
			try {
				imgNewShip = ImageIO.read(new File("src/edu/uchicago/cs/java/finalproject/images/ufo.png"));
			} catch (IOException e) {
			e.printStackTrace();
				System.out.println("cannot read");
			}
		}
		g.drawImage(imgNewShip,getCenter().x-50,getCenter().y-35,100,70,null);
	}
	  

}
