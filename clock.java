import java.lang.*;
import java.io.*;
import java.util.*;
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

class Watch
{
	//for each array index: 0-hour, 1-minute, 2-seconds
	int time[] = new int[3];
	int x[] = new int[3];
	int y[] = new int[3];
	double angle[] = new double[3];
	int length[] = new int[3];
	float radius;
	int ox;
	int oy;
	
	Watch(int width)
	{
		updateWidth(width);
		updateTime();
	}
	
	void updateTime()
	{
		Calendar cal = Calendar.getInstance();
		time[0] = cal.get(10)*3600+time[1];
		time[1] = cal.get(12)*60+time[2];
		time[2] = cal.get(13); 
	}
	
	void updateWidth(float width)
	{
		radius = (int)(width * .4);
		length[0] = (int)(radius*0.5);
		length[1] = (int)(radius*0.8);
		length[2] = (int)(radius*0.8);
		ox = (int)(width/2);
		oy = (int)(width/2);
	}
	
 	int getX(int index)
	{
		putAngle(index);
		x[index] = (int)((float)length[index]*Math.sin(angle[index]));
		return x[index];
	}
	
	int getY(int index)
	{
		putAngle(index);
		y[index] = (int)((float)length[index]*Math.cos(angle[index]))*-1;
		return y[index];		
	}
	
	void putAngle(int index)
	{
		if(index==0)	angle[index] = ((double)time[index]/43200f)*2f*Math.PI;//+Math.PI*0.25;
		if(index==1) angle[index] = ((double)time[index]/3600f)*2f*Math.PI;//+Math.PI*0.25;
		if(index==2) angle[index] = ((double)time[index]/60f)*2f*Math.PI;//+Math.PI*0.25;
	} 
} 


public class clock extends Application
{
	public void start(Stage stage) 
	{
		int width =500;
		int height = width;
		
		Watch watch = new Watch(width);
		Ellipse ellipse = new Ellipse(watch.ox,watch.oy,watch.radius,watch.radius);
		ellipse.setFill(Color.WHITE);
		ellipse.setStroke(Color.BLACK);
		
		Line lines[] = new Line[3];
		for(int i = 0;i<3;i++){
			lines[i] = new Line();
			lines[i].setStartX(watch.ox);
			lines[i].setStartY(watch.oy);
		}
		lines[0].setStrokeWidth(5);
		lines[1].setStrokeWidth(3);
		lines[2].setStrokeWidth(1);
		lines[2].setStroke(Color.RED);
				
		Circle center = new Circle(watch.ox,watch.oy,watch.radius/25);		
		
				
		Group root = new Group();		
		stage.setTitle( "Clock" );
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(width,height);
		
		root.getChildren().addAll( canvas, ellipse,center);
		for(Line line: lines) root.getChildren().add(line);
		center.toFront();
	
		new AnimationTimer()
		{	
			long last = 0;
			public void handle(long currentNanoTime)
			{
				if(currentNanoTime-last >= 100000)
				{
					watch.updateTime();
					watch.updateWidth((int)scene.getWidth());
					
					for(int i = 0;i<3;i++){
						lines[i].setEndX(watch.ox + watch.getX(i));
						lines[i].setEndY(watch.oy + watch.getY(i));
						//System.out.print(i+" time: "+watch.time[i]+" x: "+watch.angle[i]/*" x: "+watch.getX(i)+" y: "+watch.getY(i)*/);
					}	
			//		System.out.println();
					last = currentNanoTime;				
				}
				
			}
		}.start();
		
		
		stage.show();
	}
}