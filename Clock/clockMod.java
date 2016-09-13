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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


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
	static String zone;
	
	Watch(int width)
	{
		updateWidth(width);
		updateTime();
	}
	
	Watch(){
		
	}
	
	void updateTime()
	{
		TimeZone tz = TimeZone.getTimeZone(zone);
		Calendar cal = Calendar.getInstance(tz);
		time[2] = cal.get(Calendar.SECOND); 
		time[1] = cal.get(Calendar.MINUTE)*60+time[2];
		time[0] = cal.get(Calendar.HOUR)*3600+time[1];
		System.out.println(cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+" "+zone);
	}
	
	void updateWidth(float width)
	{
		radius = (int)(width * .4);
		length[0] = (int)(radius*0.6);
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
		y[index] = -1*(int)((float)length[index]*Math.cos(angle[index]));
		return y[index];		
	}
	
	void putAngle(int index)
	{
		if(index==0) angle[index] = ((double)time[index]/43200f)*2f*Math.PI;//+Math.PI*0.25;
		if(index==1) angle[index] = ((double)time[index]/3600f)*2f*Math.PI;//+Math.PI*0.25;
		if(index==2) angle[index] = ((double)time[index]/60f)*2f*Math.PI;//+Math.PI*0.25;
	} 
} 

class Face extends Watch
{
	//int handWidth[] = new handWidth[3];
	Ellipse ellipse = new Ellipse();
	Circle center = new Circle();
	Line lines[] = new Line[3];
	Line hashes[] = new Line[60];
	
	
	Face(int width)
	{
		updateWidth(width);
		//updateTime();
	}
	void drawClock(Group root, int width,int offset)
	{	
		for(int i = 0;i<3;i++)	lines[i] = new Line();
		for(int i = 0;i<60;i++) hashes[i] = new Line();
		
		ellipse.setFill(Color.WHITE);
		ellipse.setStroke(Color.BLACK);
		
		lines[2].setStroke(Color.RED);
	
		updateSize(offset);
		root.getChildren().addAll(ellipse,center);
		for(Line line: lines) root.getChildren().add(line);
		for(Line hash: hashes) root.getChildren().add(hash);
		center.toFront();
	}
	
	void updateClock(int width,int offset)
	{
		updateTime();
		updateWidth(width);
		
		//updateSize(offset);
		
		for(int i = 0;i<3;i++){
			lines[i].setEndX(ox + getX(i)+offset);
			lines[i].setEndY(oy + getY(i));
			//System.out.print(i+" time: "+watch.time[i]+" x: "+watch.angle[i]/*" x: "+watch.getX(i)+" y: "+watch.getY(i)*/);
		}			
	}
	
	void updateSize(int offset)
	{
		center.setRadius(radius/25);
		center.setCenterX(ox+offset);
		center.setCenterY(oy);
		
		ellipse.setRadiusX(radius);
		ellipse.setRadiusY(radius);
		ellipse.setCenterX(ox+offset);
		ellipse.setCenterY(oy);

		lines[0].setStrokeWidth(radius/30);
		lines[1].setStrokeWidth(radius/30);
		lines[2].setStrokeWidth(radius/80);		
		for(int i = 0;i<3;i++){
			lines[i].setStartX(ox+offset);
			lines[i].setStartY(oy);
		}
		
		double theta = 0;
		double bratio = .85;
		double lratio = .9;
		double ratio = lratio;
		double stroke = radius/80;
		for(int i = 0;i<60;i++)
		{
			theta+=(2*Math.PI)/60;
			if((i+1)%5 == 0){
				ratio=bratio;
				stroke=stroke*2;
			}				
			else{
				ratio = lratio;
				stroke=radius/80;
			} 
			hashes[i].setStrokeWidth(stroke);
			hashes[i].setStartX(((double)radius*ratio*Math.sin(theta))+ox+offset);
			hashes[i].setStartY(((double)radius*ratio*Math.cos(theta))+oy);
			hashes[i].setEndX(((double)radius*Math.sin(theta))+ox+offset);
			hashes[i].setEndY(((double)radius*Math.cos(theta))+oy);
		}
	}
}

public class clockMod extends Application
{
	public void start(Stage stage) 
	{
		String tzone[] = TimeZone.getAvailableIDs();
		for(int i =0;i<tzone.length;i++) System.out.println(tzone[i]+"*");
		
		
		int width =500;
		int height = width;
		
		Face watch[] = new Face[5];
		
		
		Group root = new Group();		
		stage.setTitle( "Clock" );
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(width,height);
		
		root.getChildren().add( canvas);
		
		for(int i = 0;i<5;i++){
			watch[i] = new Face(width/5);
			watch[i].drawClock(root,width/5,i*width/5);
		} 
		
		watch[0].zone = "PST";
		watch[1].zone = "MST";
		watch[2].zone = "CST";
		watch[3].zone = "EST";
		watch[4].zone = "GMT";
				
		new AnimationTimer()
		{	
			long last = 0;
			public void handle(long currentNanoTime)
			{
				
				if(currentNanoTime-last >= 100000)
				{
					for(int i = 0;i<5;i++) watch[i].updateClock((int)scene.getWidth()/5,i*(int)scene.getWidth()/5);
					last = currentNanoTime;				
				}
				
			}
		}.start();
		
/* 		Calendar.SECOND.addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				System.out.println("second");
			}
		}); */
		
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				for(int i = 0;i<5;i++) watch[i].updateSize(i*(int)scene.getWidth()/5);
			}
		});
		
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				//if(scene.getHeight()!=scene.getWidth()) scene.setHeight(scene.getWidth());
			}
		});
		
		stage.show();
	}
}