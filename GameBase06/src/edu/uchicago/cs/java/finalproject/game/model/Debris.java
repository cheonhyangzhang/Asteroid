package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import edu.uchicago.cs.java.finalproject.controller.Game;

public class Debris extends Sprite{
	
	private int nSpin;
	private final int EXPIRE = 5;
	private final int DEBRIS_SIZE = 5;
	public Debris (Asteroid astExploded){

		//call Sprite constructor
		super();
		
		int  nSizeNew =	astExploded.getSize() + 1;
		
		
		//the spin will be either plus or minus 0-9
		int nSpin = Game.R.nextInt(10);
		if(nSpin %2 ==0)
			nSpin = -nSpin;
		setSpin(nSpin);
			
		//random delta-x
		int nDX = Game.R.nextInt(10 + nSizeNew*2);
		if(nDX %2 ==0)
			nDX = -nDX;
		setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(10+ nSizeNew*2);
		if(nDY %2 ==0)
			nDY = -nDY;
		setDeltaY(nDY);
			
		assignRandomShape();
		setExpire(EXPIRE);
		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively

		setRadius(DEBRIS_SIZE);
		setCenter(astExploded.getCenter());
	}
	public Debris (UFO astExploded){

		//call Sprite constructor
		super();
		int  nSizeNew = 1;
		
		//the spin will be either plus or minus 0-9
		int nSpin = Game.R.nextInt(10);
		if(nSpin %2 ==0)
			nSpin = -nSpin;
		setSpin(nSpin);
			
		//random delta-x
		int nDX = Game.R.nextInt(10 + nSizeNew*2);
		if(nDX %2 ==0)
			nDX = -nDX;
		setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(10+ nSizeNew*2);
		if(nDY %2 ==0)
			nDY = -nDY;
		setDeltaY(nDY);
			
		assignRandomShape();
		setExpire(EXPIRE);
		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively

		setRadius(DEBRIS_SIZE);
		setCenter(astExploded.getCenter());
	}
	
	
	public int getSize(){
		
		int nReturn = 0;
		
		switch (getRadius()) {
			case 100:
				nReturn= 0;
				break;
			case 50:
				nReturn= 1;
				break;
			case 25:
				nReturn= 2;
				break;
		}
		return nReturn;
		
	}

	public int getSpin() {
		return this.nSpin;
	}
	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}
	
	
	public void move(){
		super.move();
		
		setOrientation(getOrientation() + getSpin());
	
		
	}
	
	public void assignRandomShape ()
	  {
	    int nSide = Game.R.nextInt( 7 ) + 7;
	    int nSidesTemp = nSide;

	    int[] nSides = new int[nSide];
	    for ( int nC = 0; nC < nSides.length; nC++ )
	    {
	      int n = nC * 48 / nSides.length - 4 + Game.R.nextInt( 8 );
	      if ( n >= 48 || n < 0 )
	      {
	        n = 0;
	        nSidesTemp--;
	      }
	      nSides[nC] = n;
	    }

	    Arrays.sort( nSides );

	    double[]  dDegrees = new double[nSidesTemp];
	    for ( int nC = 0; nC <dDegrees.length; nC++ )
	    {
	    	dDegrees[nC] = nSides[nC] * Math.PI / 24 + Math.PI / 2;
	    }
	   setDegrees( dDegrees);
	   
		double[] dLengths = new double[dDegrees.length];
			for (int nC = 0; nC < dDegrees.length; nC++) {
				if(nC %3 == 0)
				    dLengths[nC] = 1 - Game.R.nextInt(40)/100.0;
				else
					dLengths[nC] = 1;
			}
		setLengths(dLengths);

	  }
		//override the expire method - once an object expires, then remove it from the arrayList. 
		public void expire(){
	 		if (getExpire() == 0)
	 			CommandCenter.movDebris.remove(this);
			 else 
				setExpire(getExpire() - 1);
		}

}
