package edu.uchicago.cs.java.finalproject.game.model;

public class GameRecord {
	private String[] names = new String[5];
	private long[] scores= new long[5];
	private int min;
	private int minIndex;
	public GameRecord(){
		for (int i=0;i<names.length;i++){
			names[i]="No player";
		}
		for (int i=0;i<scores.length;i++){
			scores[i]=0;
		}
	}
	
	public long getMin(){
		return scores[scores.length-1];
	}
	public String getName(int index){
		return names[index];
	}
	public long getScore(int index){
		return scores[index];
	}
	public void addTuple(char name[],long score){
		for (int i=0;i<names.length;i++){
			if (score >scores[i]){
				for (int j=names.length-1;j>i;j--){
					scores[j] = scores[j-1];
					names[j]=names[j-1];
				}
				scores[i] = score;
				names[i] = name[0]+"  "+name[1]+"  "+name[2];
				break;
			}
		}
	}
}
