import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Main {
	// forward -> k�si liikkuu oikealle
	// backward -> k�si liikkuu vasemmalle
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	static RegulatedMotor lifterMotor = new EV3MediumRegulatedMotor(MotorPort.B);
	
	public static void main(String[] args) {
		String valinta = "vapaa";
		boolean lcdRefresh = false;
		rightMotor.setSpeed(300);
		leftMotor.setSpeed(300);
		lifterMotor.setSpeed(300);
		
		int x = 0;
		int y = 0;
		
		
		LCD.drawString(valinta, 0, 0);
		while (true){
			LCD.clear();
			LCD.drawString("x: " + x + ", y: " + y, 0, 0);
			LCD.refresh();
			if (valinta == "vapaa"){
				if (Button.DOWN.isDown()){
					drawLine("eteen");
				}else if (Button.UP.isDown()){
					drawLine("taakse");
				}else if (Button.RIGHT.isDown()){
					drawLine("vasen");
				}else if (Button.LEFT.isDown()){
					drawLine("oikea");
				}
//				if (Button.DOWN.isDown()){
//					y--;
//					Delay.msDelay(500);
//				}else if (Button.UP.isDown()){
//					y++;
//					Delay.msDelay(500);
//				}else if (Button.RIGHT.isDown()){
//					x++;
//					Delay.msDelay(500);
//				}else if (Button.LEFT.isDown()){
//					x--;
//					Delay.msDelay(500);
//				}
				else if (Button.ENTER.isDown()){
					ympyra((float) 3);
				}
				
//			} else if (valinta == "nelio"){
//				if (Button.ENTER.isDown()){
//					nelio(100);
//				}
//			} else if (valinta == "kolmio"){
//				if (Button.ENTER.isDown()){
//					kolmio(100);
//				}
			}
			
			if (Button.ESCAPE.isDown()){
				break;
			}
		}
		rightMotor.close();
		leftMotor.close();
		lifterMotor.close();
	}
	
	
	// piirt�� viivan valittuun suuntaan oletuspituudella
	// k�ytt�en forward, backward, flt metodeja
	static void drawLine(String dir, int lenght){ 
		rightMotor.synchronizeWith(new RegulatedMotor[] {leftMotor});
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		if (dir == "eteen"){
			rightMotor.backward();
			leftMotor.forward();
		} else if (dir == "taakse"){
			rightMotor.forward();
			leftMotor.backward();
		} else if (dir == "oikea"){
			rightMotor.forward();
			leftMotor.forward();
		} else if (dir == "vasen"){
			rightMotor.backward();
			leftMotor.backward();
		} else if (dir == "takavasen"){
			leftMotor.backward();
		}
		
		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
		
		Delay.msDelay(lenght); 
		
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		leftMotor.flt();
		rightMotor.flt();
		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
	}
	
	// piirt�� viivan valittuun suuntaan oletuspituudella
	// k�ytt�en forward, backward, flt metodeja
	static void drawLine(String dir){ 
		int lenght = 1000;
		rightMotor.synchronizeWith(new RegulatedMotor[] {leftMotor});
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		if (dir == "eteen"){
			rightMotor.backward();
			leftMotor.forward();
		} else if (dir == "taakse"){
			rightMotor.forward();
			leftMotor.backward();
		} else if (dir == "oikea"){
			rightMotor.forward();
			leftMotor.forward();
		} else if (dir == "vasen"){
			rightMotor.backward();
			leftMotor.backward();
		} else if (dir == "takavasen"){
			leftMotor.backward();
		}
		
		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
		
		Delay.msDelay(lenght); 
		
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		leftMotor.flt();
		rightMotor.flt();
		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
	}
	
	// piirt�� kolmion
	static void kolmio(int size){
		drawLine("oikea", size);
		drawLine("eteen", size);
		drawLine("takavasen", size * 2);
		Delay.msDelay(100);
	}
	
	// piirt�� neli�n eli nelj� viivaa per�kk�in
	static void nelio(int size){ 
		drawLine("vasen");
		Delay.msDelay(200); // n�it� taukoja ei tapahdu jos k�ytt�� motor.rotate
		drawLine("eteen");
		Delay.msDelay(200);
		drawLine("oikea");
		Delay.msDelay(200);
		drawLine("taakse");
		Delay.msDelay(200);
	}
	
	// yritt�� piirt�� ympyr�n
	static void ympyra(float size){
		int alpha = 0;
		float x = 0;
		float y = 0;
		
		for (alpha = 0; alpha < 360; alpha += 36){
			x = (float) (((size * Math.cos(alpha)) - size) - x);
			
			y = (float) ((size * Math.sin(alpha) - y));
			drawLineXY(x, y);
			if (Button.ESCAPE.isDown()){
				break;
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------
	
	// koordinaatteja????!?!?!?!?!!??!?
	static void drawLineXY(float x, float y){
		rightMotor.synchronizeWith(new RegulatedMotor[] {leftMotor});
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		int factor = 90;
		int intx = Math.round(x * factor);
		int inty = Math.round(y * factor);
		
		
		// hopeless
		if (x > 0 && y > 0){
			rightMotor.rotate(intx - inty);
			leftMotor.rotate(intx + inty);
		} else if (x == 0 && y > 0){
			rightMotor.rotate(-inty);
			leftMotor.rotate(inty);
		} else if (x > 0 && y == 0){
			rightMotor.rotate(intx);
			leftMotor.rotate(intx);
		} else if (x < 0 && y > 0){
			rightMotor.rotate(intx - inty);
			leftMotor.rotate(intx + inty);
		} else if (x < 0 && y == 0){
			rightMotor.rotate(intx);
			leftMotor.rotate(intx);
		} else if (x > 0 && y < 0){
			rightMotor.rotate(-inty + intx);
			leftMotor.rotate(inty + intx);
		} else if (x == 0 && y < 0){
			rightMotor.rotate(-inty);
			leftMotor.rotate(inty);
		} else if (x < 0 && y < 0){
			rightMotor.rotate(-inty + intx);
			leftMotor.rotate(inty + intx);
		}
		
		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
		rightMotor.waitComplete();
		leftMotor.waitComplete();
	}
	
	// piirt�� viivan valittuun suuntaan valitulla pituudella
	// k�ytt�en rotate
	static void drawLine2(String dir, int lenght){ 
		rightMotor.synchronizeWith(new RegulatedMotor[] {leftMotor});
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		if (dir == "eteen"){
			rightMotor.rotate(-lenght);
			leftMotor.rotate(lenght);
		} else if (dir == "taakse"){
			rightMotor.rotate(lenght);
			leftMotor.rotate(-lenght);
		} else if (dir == "oikea"){
			rightMotor.rotate(lenght);
			leftMotor.rotate(lenght);
		} else if (dir == "vasen"){
			rightMotor.rotate(-lenght);
			leftMotor.rotate(-lenght);
		} else if (dir == "takavasen"){
			leftMotor.rotate(-lenght * 2);
		}

		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
		Delay.msDelay(10); // t�m� pelasti
	}
	// piirt�� viivan valittuun suuntaan oletuspituudella
	// k�ytt�en rotate
	static void drawLine2(String dir){ 
		int lenght = 360;
		rightMotor.synchronizeWith(new RegulatedMotor[] {leftMotor});
		rightMotor.startSynchronization();
		leftMotor.startSynchronization();
		if (dir == "eteen"){
			rightMotor.rotate(-lenght);
			leftMotor.rotate(lenght);
		} else if (dir == "taakse"){
			rightMotor.rotate(lenght);
			leftMotor.rotate(-lenght);
		} else if (dir == "oikea"){
			rightMotor.rotate(lenght);
			leftMotor.rotate(lenght);
		} else if (dir == "vasen"){
			rightMotor.rotate(-lenght);
			leftMotor.rotate(-lenght);
		} else if (dir == "takavasen"){
			leftMotor.rotate(-lenght * 2);
		}

		rightMotor.endSynchronization();
		leftMotor.endSynchronization();
		Delay.msDelay(10); // t�m� pelasti
	}
}
