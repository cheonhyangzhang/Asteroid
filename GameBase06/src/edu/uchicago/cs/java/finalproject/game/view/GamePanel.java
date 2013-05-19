package edu.uchicago.cs.java.finalproject.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;


import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.game.model.CommandCenter;
import edu.uchicago.cs.java.finalproject.game.model.Falcon;
import edu.uchicago.cs.java.finalproject.game.model.Movable;
import edu.uchicago.cs.java.finalproject.sounds.Sound;


 public class GamePanel extends Panel {
	
	// ==============================================================
	// FIELDS 
	// ============================================================== 
	 
	// The following "off" vars are used for the off-screen double-bufferred image. 
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	Image imgBack = null;
	
	
	private GameFrame gmf;
	private Font fnt = new Font("SansSerif", Font.BOLD, 12);
	private Font fntMid = new Font("SansSerif", Font.BOLD, 20);
	private Font fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
	private Font fntBigNomarl = new Font("SansSerif", Font.PLAIN , 36);
	private Font fntSuperBig = new Font("SansSerif", Font.PLAIN +Font.ITALIC, 130);
	private FontMetrics fmt; 
	private int nFontWidth;
	private int nFontHeight;
	private String strDisplay = "";
	private static final int FONT_SPACE = 20;

	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public GamePanel(Dimension dim){
	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		
		gmf.setSize(dim);
		gmf.setTitle("Asteriod");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	
	private void drawScore(Graphics g) {
		if (CommandCenter.isPlaying())
		{
		g.setColor(Color.white);
		g.setFont(fnt);
		g.drawString("SCORE :  " + CommandCenter.getScore(), nFontWidth, nFontHeight + FONT_SPACE);
		}
	}
	private void drawWeapon(Graphics g) {
		if (CommandCenter.isPlaying() && !CommandCenter.isPaused())
		{
			g.setColor(Color.white);
			g.setFont(fntMid);
			if (CommandCenter.getnWeaponTyple()==CommandCenter.nNormal){
				g.drawString("Weapon: "+"Normal", nFontWidth+(int)Game.DIM.width-230, nFontHeight+80);
			}
			else if (CommandCenter.getnWeaponTyple()==CommandCenter.nShortGun){
				g.drawString("Weapon: "+"ShortGun", nFontWidth+(int)Game.DIM.width-260, nFontHeight+80);
				
			}
		}
	}
	private void drawTips(Graphics g) {
		if (CommandCenter.isPlaying()  && !CommandCenter.isPaused())
		{
		g.setColor(Color.white);
		g.setFont(fnt);
		g.drawString("Press ESC to pause and see the instructions for game", nFontWidth + 350, nFontHeight );
		}
	}
	private void drawLevel(Graphics g) {
		if (CommandCenter.isPlaying())
		{
		g.setColor(Color.white);
		g.setFont(fnt);
		g.drawString("Level :  " + CommandCenter.getLevel(), nFontWidth, nFontHeight);
		}
	}

	private void drawNumberSuperBomb(Graphics g){
		if (CommandCenter.isPlaying() && !CommandCenter.isPaused())
		{
		g.setColor(Color.white);
		g.setFont(fntMid);
		g.drawString("SuperBomb: " + CommandCenter.getnNumSuperBomb(), nFontWidth+(int)Game.DIM.width-200, nFontHeight+50);
		}
	}
	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);

			grpOff = imgOff.getGraphics();
		}
		if (imgBack==null){
			try {
				imgBack = ImageIO.read(new File("src/edu/uchicago/cs/java/finalproject/images/background.jpg"));
			} catch (IOException e) {
			e.printStackTrace();
			}
		}

		grpOff.drawImage(imgBack, 0,0,Game.DIM.width, Game.DIM.height,null);
		/**
		 * This is place to change the background
		 */
		// Fill in background with black.
//		grpOff.setColor(Color.black);
//		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

		drawScore(grpOff);
		drawLevel(grpOff);
		drawWeapon(grpOff);
		drawTips(grpOff);
		
		if (!CommandCenter.isPlaying() && !CommandCenter.isEnding()) {
			displayTextOnScreen(grpOff); //The welcome screen
		} else if (CommandCenter.isPaused()) {
			//pause string
			displayPauseTextOnScreen(grpOff);
		} else if (CommandCenter.isEnding()){
			displayEndTextOnScreen(grpOff); // Scoring diplay
			
		}
		
		//playing and not paused!
		else {
			
			//draw them in decreasing level of importance
			//friends will be on top layer and debris on the bottom
			iterateMovables(grpOff, 
					   CommandCenter.movDebris,
			           CommandCenter.movFloaters, 
			           CommandCenter.movFoes,
			           CommandCenter.movFriends);
			
			
			drawNumberShipsLeft(grpOff);
			drawNumberSuperBomb(grpOff);
			if (CommandCenter.isGameOver()) {
				CommandCenter.setPlaying(false);
				CommandCenter.setEnding(true);
				CommandCenter.setMuted(true);
				//bPlaying = false;
			}
		}
		//draw the double-Buffered Image to the graphics context of the panel
		

		g.drawImage(imgOff, 0, 0, this);
		
	
//		
	} 


	
	//for each movable array, process it.
	private void iterateMovables(Graphics g, CopyOnWriteArrayList<Movable>...movMovz){
		
		for (CopyOnWriteArrayList<Movable> movMovs : movMovz) {
			for (Movable mov : movMovs) {

				mov.move();
				mov.draw(g);
				mov.fadeInOut();
				mov.expire();
			}
		}
		
	}
	

	// Draw the number of falcons left on the bottom-right of the screen. 
	private void drawNumberShipsLeft(Graphics g) {
		Falcon fal = CommandCenter.getFalcon();
		double[] dLens = fal.getLengths();
		int nLen = fal.getDegrees().length;
		Point[] pntMs = new Point[nLen];
		int[] nXs = new int[nLen];
		int[] nYs = new int[nLen];
	
		//convert to cartesean points
		for (int nC = 0; nC < nLen; nC++) {
			pntMs[nC] = new Point((int) (10 * dLens[nC] * Math.sin(Math
					.toRadians(90) + fal.getDegrees()[nC])),
					(int) (10 * dLens[nC] * Math.cos(Math.toRadians(90)
							+ fal.getDegrees()[nC])));
		}
		
		//set the color to white
		g.setColor(Color.white);
		//for each falcon left (not including the one that is playing)
		for (int nD = 1; nD < CommandCenter.getNumFalcons(); nD++) {
			//create x and y values for the objects to the bottom right using cartesean points again
			for (int nC = 0; nC < fal.getDegrees().length; nC++) {
				nXs[nC] = pntMs[nC].x + Game.DIM.width - (20 * nD);
				nYs[nC] = pntMs[nC].y + Game.DIM.height - 40;
			}
			g.drawPolygon(nXs, nYs, nLen);
		} 
	}
	
	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		nFontWidth = fmt.getMaxAdvance();
		nFontHeight = fmt.getHeight();
		g.setFont(fntBig);					// set font info
	}
	private void displayPauseTextOnScreen(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(fntBigNomarl);
		fmt = g.getFontMetrics();
		strDisplay = "Game Paused";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		g.setFont(fnt);
		fmt = g.getFontMetrics();
		strDisplay = "Press ESC to resume";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + 30);
		strDisplay = "Press SPACE to fire, the effect may depend on what weapon you are currently using";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + 30*2);
		strDisplay = "Press UP to thrust Press LEFT and RIGHT to turn";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 30*3);
		strDisplay = "Press m to mute";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 30*4);
		strDisplay = "Press r to use SuperBomb";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 30*5);
		strDisplay = "Things power up: You could have NewShip, SuperBomb and ShortGun";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 30*6);
		strDisplay = "";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 30*7);
		
	}
	private void displayTextOnScreen(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.setFont(fntSuperBig);
		fmt = g.getFontMetrics();
		strDisplay = "Asteroid";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 );
				
		g.setFont(fntBigNomarl);
		fmt = g.getFontMetrics();
		strDisplay = "GLORY BOARD";
		grpOff.drawString(strDisplay,
		(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 +130);
		for (int i =0;i<5;i++){
			strDisplay = (i+1)+":"+CommandCenter.gameRecord.getName(i)+"  "+CommandCenter.gameRecord.getScore(i);
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
							+ nFontHeight + 40*(i+4));
		}
		g.setFont(fnt);
		fmt = g.getFontMetrics();	
		strDisplay = "Press ENTER to start or ESC to exit";
		grpOff.drawString(strDisplay,
		(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 +40*10);
	

	}
private void displayEndTextOnScreen(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(fnt);
		fmt = g.getFontMetrics();
		Game.clpMusicBackground.stop();
		// no chance to get into leader board
		if (CommandCenter.getScore() <= CommandCenter.gameRecord.getMin()){
			
			strDisplay = "Your score is too low to enter the Glory Board";
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
			strDisplay = "Please press enter to go on";
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + 40);
		}
		else{
			fmt = g.getFontMetrics();
			strDisplay = "Conguatulations! You are in the Glory Board";
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
			strDisplay = "Use arrow key to edit your name";
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + 40*1);
			strDisplay = "UP and DOWN to change character, LEFT and RIGHT to switch position";
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + 40*2);
			g.setFont(fntBigNomarl);
			strDisplay = "Your score " + CommandCenter.getScore();
			grpOff.drawString(strDisplay,
			(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 - 80, Game.DIM.height / 4 + 40*5);
			int nCharacterSpace = 60;
			if (CommandCenter.nPlayerNameIndex == 0){
				if (Game.nTick % 30 <15 || CommandCenter.isKeyPressed()){
					strDisplay = CommandCenter.cPlayerName[0]+"" ;
					grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 -nCharacterSpace, Game.DIM.height / 4 + 40*8);
				}
				strDisplay = CommandCenter.cPlayerName[1]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 40*8);
				strDisplay = CommandCenter.cPlayerName[2]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 +nCharacterSpace, Game.DIM.height / 4 + 40*8);
		
			}
			if  (CommandCenter.nPlayerNameIndex == 1){
				if (Game.nTick % 30 <15){					
					strDisplay = CommandCenter.cPlayerName[1]+"" ;
					grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 40*8);
				}
				strDisplay = CommandCenter.cPlayerName[0]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 -nCharacterSpace, Game.DIM.height / 4 + 40*8);
				strDisplay = CommandCenter.cPlayerName[2]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 +nCharacterSpace, Game.DIM.height / 4 + 40*8);

			}
			if  (CommandCenter.nPlayerNameIndex == 2){
				if (Game.nTick % 30 <15){
					strDisplay = CommandCenter.cPlayerName[2]+"" ;
					grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 +nCharacterSpace, Game.DIM.height / 4 + 40*8);
				}
				strDisplay = CommandCenter.cPlayerName[0]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2  -nCharacterSpace, Game.DIM.height / 4 + 40*8);
				strDisplay = CommandCenter.cPlayerName[1]+"" ;
				grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2 , Game.DIM.height / 4 + 40*8);

			}
			
		}
		

	}
	
//	// This method draws some text to the middle of the screen before/after a game
//	private void displayTextOnScreen() {
//
//		strDisplay = "GAME OVER";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
//
//		strDisplay = "use the arrow keys to turn and thrust";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 40);
//
//		strDisplay = "use the space bar to fire";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 80);
//
//		strDisplay = "'S' to Start";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 120);
//
//		strDisplay = "'P' to Pause";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 160);
//
//		strDisplay = "'Q' to Quit";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 200);
//		strDisplay = "left pinkie on 'A' for Shield";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 240);
//
//		strDisplay = "left index finger on 'F' for Guided Missile";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 280);
//
//		strDisplay = "'Numeric-Enter' for Hyperspace";
//		grpOff.drawString(strDisplay,
//				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
//						+ nFontHeight + 320);
//	}
	
	public GameFrame getFrm() {return this.gmf;}
	public void setFrm(GameFrame frm) {this.gmf = frm;}	
}