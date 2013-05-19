package edu.uchicago.cs.java.finalproject.game.model;
import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class NewSuperBombFloater extends Sprite{

		private int nSpin;

		public NewSuperBombFloater() {
			super();

			setExpire(250);
			setRadius(50);

			int nX = Game.R.nextInt(10);
			int nY = Game.R.nextInt(10);
			int nS = Game.R.nextInt(5);
			
			//set random DeltaX
			if (nX % 2 == 0)
				setDeltaX(nX);
			else
				setDeltaX(-nX);

			//set rnadom DeltaY
			if (nY % 2 == 0)
				setDeltaY(nY);
			else
				setDeltaY(-nY);
			
			//set random spin
			if (nS % 2 == 0)
				setSpin(nS);
			else
				setSpin(-nS);

			//random point on the screen
			setCenter(new Point(Game.R.nextInt(Game.DIM.width),
					Game.R.nextInt(Game.DIM.height)));

			//random orientation 
			 setOrientation(Game.R.nextInt(360));

		}

		public void move() {
			super.move();

			setOrientation(getOrientation() + getSpin());

		}

		public int getSpin() {
			return this.nSpin;
		}

		public void setSpin(int nSpin) {
			this.nSpin = nSpin;
		}

		//override the expire method - once an object expires, then remove it from the arrayList.
		@Override
		public void expire() {
			if (getExpire() == 0)
				CommandCenter.movFloaters.remove(this);
			else
				setExpire(getExpire() - 1);
		}

		@Override
		public void draw(Graphics g) {
			Image imgNewShip = null;
			if (imgNewShip==null){
				try {
					imgNewShip = ImageIO.read(new File("src/edu/uchicago/cs/java/finalproject/images/newSuperBombFloater.png"));
				} catch (IOException e) {
				e.printStackTrace();
					System.out.println("cannot read");
				}
			}
			g.drawImage(imgNewShip,getCenter().x-25,getCenter().y-25,50,50,null);
		}

	}


