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
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

//Class for the puzzle
class Game
{
	int choose; //what number will be next put into a box
	int numbers[][] = new int[9][9]; //the master matrix for the sudoku

	//Graphic elements
	Button chooseNum[] = new Button[9]; //top row
	Button squares[][] = new Button[9][9]; //sudoku board
	Line borders[][] = new Line[2][4]; 
	Button reset = new Button();
	Button solver = new Button();
	Label warn = new Label();

	//Draws the game on the canvas 
	void drawGrid(Group root, int width,int offset)
	{	
		//Formats top row of buttons
		for(int i =0;i<9;i++){
			chooseNum[i] = new Button();
			root.getChildren().add(chooseNum[i]);
			chooseNum[i].relocate((i+1)*offset,offset/2);
			chooseNum[i].setPrefWidth(offset);
			chooseNum[i].setPrefHeight(offset);
			chooseNum[i].setText(Integer.toString(i+1));
			
		}
		
		//Formats sudoku puzzle
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++)
			{
				squares[i][j] = new Button();
				root.getChildren().add(squares[i][j]);
				squares[i][j].relocate((i+1)*offset,(j+2)*offset);
				squares[i][j].setPrefWidth(offset);
				squares[i][j].setPrefHeight(offset);
				squares[i][j].setStyle("-fx-background-color: #ffffff; -fx-border-width: 1; -fx-border-color: #000000;");
			}
		}
		
		//Draws vertical borders between 3x3s and outside
		for(int j=0;j<4;j++)
		{ 
			int i=0;
			borders[i][j] = new Line();
			root.getChildren().add(borders[i][j]);
			borders[i][j].setStartX(offset);
			borders[i][j].setStartY(3*offset*j+2*offset);
			borders[i][j].setEndX(offset*10);
			borders[i][j].setEndY(3*offset*j+2*offset);
			borders[i][j].setStrokeWidth(3);
			borders[i][j].setStrokeType(StrokeType.OUTSIDE);
		}
		
		//Draws horizontal borders betwen 3x3s and outside
		for(int j=0;j<4;j++)
		{ 
			int i=1;
			borders[i][j] = new Line();
			root.getChildren().add(borders[i][j]);
			borders[i][j].setStartX(3*offset*j+offset);
			borders[i][j].setStartY(2*offset);
			borders[i][j].setEndX(3*offset*j+offset);
			borders[i][j].setEndY(11*offset);
			borders[i][j].setStrokeWidth(3);
			borders[i][j].setStrokeType(StrokeType.OUTSIDE);
		}
		
		//Add the various buttons
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
		
		root.getChildren().add(warn);
		warn.relocate(offset,0);
		warn.setText("Input the sudoku using the buttons or keys to select numbers");		
	}
	
	//Puts the choose number into the sudoku
	void addNum(int x,int y)
	{
		numbers[x][y] = choose;
		if(choose != 0) squares[x][y].setText(Integer.toString(choose));
	}
	
	//Puts the array into the graphics
	void updateButton()
	{
		//fills the button text with the string
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	squares[i][j].setText(Integer.toString(numbers[i][j]));
	}
	
	//Clears the board and array
	void resetButtons()
	{
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++){
			squares[i][j].setText("");
			numbers[i][j] = 0;
		}
		warn.setText("Input the sudoku using the buttons to select numbers");
	}
	
	//Check if a number works in the array
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
	
	//Finds the next open position in the array; returns true if at the end of array
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
	
	//This is the method that calls itself for the recursion
	boolean recurse(int newNum[][])
	{
		int num[][] = new int[9][9];
		int pos[] = new int[2];
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	num[i][j]= newNum[i][j];
		
		//This is true when at the end of array, causes it to bubble back up
 		if(increment(pos,num)){
			
			for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	numbers[i][j]= num[i][j];
			return true;			
		} 
		
		//For loop tries number and then calls itself if it works
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
	
	//Fills the buttons
	void setText()
	{
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)	squares[i][j].setText(Integer.toString(numbers[i][j]));				
	}
	
	//Checks to see if it is a valid sudoku
	boolean checkwork(int num[][])
	{
		//tries every number in array
		for(int i = 0; i< 9;i++) for(int j = 0;j<9;j++)
		{
			if(num[i][j]!=0)
			{
				for(int k =0;k<9;k++)
				{
					if(num[i][j]==num[k][j] && i != k) return false;
					if(num[i][j]==num[i][k] && j != k) return false;
				}
				int sectx = i/3;
				int secty = j/3;
				for(int k=sectx*3;k<sectx*3+3;k++) for(int l=secty*3;l<secty*3+3;l++){
					if(num[i][j]==num[k][l] && j != l && i != k) return false;
				} 				
			}

		}
		return true;		
	}
	
	//gets called when the solve button is clicked 
	void solve()
	{
		if(checkwork(numbers))
		{
			warn.setText("Solving");
			recurse(numbers);
			setText();
			warn.setText("Solved");
		}
		else warn.setText("Sudoku not valid");
	}
}


//Class where main is
public class sudoku2 extends Application
{
	public void start(Stage stage) 
	{
		//scales the height and width appropriately
		int width =500;
		int height = (int)((float)width*(15f/11f));
		
		//Creates an instance of class
		Game game = new Game();
		
		//Javafx window stuff
		Group root = new Group();
		stage.setTitle( "Sudoku Solver" );
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(width,height);
		root.getChildren().add( canvas);
		
		//Draw the grid
		game.drawGrid(root,width,width/11);
		
		//Sets event for choose buttons
 		for(int i =0; i<9;i++){
			final int a = i;
			game.chooseNum[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.choose = a+1;
				}
			});
		}
		
		//sets events for grid
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
		
		//reset event
		game.reset.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.resetButtons();
				}
		});
		
		//solve event
		game.solver.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					game.solve();
				}
		});
		
		//key events for choose num
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override public void handle(KeyEvent ke) {
					String str = ke.getText();
					int value = (int)(str.charAt(0))-48;
					game.choose = value;
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