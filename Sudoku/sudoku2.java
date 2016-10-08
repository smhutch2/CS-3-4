/*
This program opens a new window with a sudoku game.
*/
import java.lang.*;
import java.io.*;
import java.util.*;

//Javafx imports
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.canvas.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.*;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.scene.text.*;
import javafx.scene.control.Button;

//draws the game
class Game
{
	int choose;
	int numbers[][] = new int[9][9];
	Button chooseNum[] = new Button[9];
	Button squares[][] = new Button[9][9] ;
	Button reset = new Button();
	Button solver = new Button();
	int runs=0;

	//Draws the game on the canvas 
	void drawGrid(Group root, int width,int offset)
	{	
		
		for(int i =0;i<9;i++){
			chooseNum[i] = new Button();
			root.getChildren().add(chooseNum[i]);
			chooseNum[i].relocate((i+1)*offset,offset/2);
			chooseNum[i].setPrefWidth(offset);
			chooseNum[i].setPrefHeight(offset);
			chooseNum[i].setText(Integer.toString(i+1));
		}
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++)
			{
				squares[i][j] = new Button();
				root.getChildren().add(squares[i][j]);
				squares[i][j].relocate((i+1)*offset,(j+2)*offset);
				squares[i][j].setPrefWidth(offset);
				squares[i][j].setPrefHeight(offset);
			}
		}
		root.getChildren().add(reset);
		reset.relocate(offset, (int)(11.5*(float)offset));
		reset.setPrefWidth(offset*9);
		reset.setPrefHeight(offset);
		reset.setText("Reset");
		
		root.getChildren().add(solver);
		solver.relocate(offset, (int)(13*(float)offset));
		solver.setPrefWidth(offset*9);
		solver.setPrefHeight(offset);
		solver.setText("Solve");		
	}
	
	void addNum(int x,int y)
	{
		numbers[x][y] = choose;
		if(choose != 0) squares[x][y].setText(Integer.toString(choose));
	}
	
	void updateButton()
	{
		//fills the button text with the string
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	squares[i][j].setText(Integer.toString(numbers[i][j]));
	}
	
	void resetButtons()
	{
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++){
			squares[i][j].setText("");
			numbers[i][j] = 0;
		}
	}
	
	boolean check(int num,int x,int y,int numb[][])
	{

		boolean work = true;
		for(int i =0;i<9;i++)
		{
			if(num==numb[i][y]) work = false;
			if(num==numb[x][i]) work = false;
		}
		int sectx = x/3;
		int secty = y/3;
		for(int i=sectx*3;i<sectx*3+3;i++) for(int j=secty*3;j<secty*3+3;j++) if(num==numb[i][j]) work = false;
		return work;
	}
	
	boolean increment(int pos[], int num[][])
	{
		if(pos[1]>=8){
			pos[0]++;
			pos[1]=0;
		}
	 	for(int i=0;i<9;i++) for(int j=0;j<9;j++)
		{
			if(num[i][j]==0)
			{
				pos[0]=i;
				pos[1]=j;
				return false;
			}
		}
		return true;
 
	}
	
	boolean recurse(int newNum[][])
	{
		int num[][] = new int[9][9];
		int pos[] = new int[2];
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	num[i][j]= newNum[i][j];
		

 		if(increment(pos,num)){
			
			for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	numbers[i][j]= num[i][j];
			return true;			
		} 
		
		for(int i = 0; i< 9;i++) 
		{
			if(check(i+1,pos[0],pos[1],num)){
				num[pos[0]][pos[1]] = i+1;
				if(recurse(num)){
					return true;
				}
			}
		}
		return false;
	}
	
	void setText()
	{
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	squares[i][j].setText(Integer.toString(numbers[i][j]));				
	}
	
	void solve()
	{
		recurse(numbers);
		setText();
	
	}
}

public class sudoku2 extends Application
{
	public void start(Stage stage) 
	{
		int width =500;
		int height = (int)((float)width*(15f/11f));
		
		Game game = new Game();
		
		Group root = new Group();
		stage.setTitle( "Sudoku Solver" );
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(width,height);
		root.getChildren().add( canvas);
		
		game.drawGrid(root,width,width/11);
		
 		for(int i =0; i<9;i++){
			final int a = i;
			game.chooseNum[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.choose = a+1;
				}
			});
		}
		for(int i =0;i<9;i++) for(int j=0;j<9;j++)
		{
			final int a =i;
			final int b = j;
			game.squares[i][j].setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.addNum(a,b);
				}
			});
		} 
				
		game.reset.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.resetButtons();
				}
		});

		game.solver.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.solve();
				}
		});
		
		stage.show();
	}
}


/*
game.reset.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.resetButtons();
				}
		});
*/