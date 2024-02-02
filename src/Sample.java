/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : Sample.java
*
******************************************************************/

public class Sample {

	public static void main(String args[]) {

		Magellan magellan = new Magellan(Magellan.SERIALPORT1);
		magellan.start();

		int	pos[]	= new int[3];			 	
		int	rot[]	= new int[3];
		int	button;
		
		while (true) {

			button = magellan.getButtons();
			magellan.getTranslation(pos);
			magellan.getRotation(rot);

			String buttonString = "NONE";
			if ((button & Magellan.BUTTON1) != 0)
				buttonString = "BUTTON1";
			else if ((button & Magellan.BUTTON2) != 0)
				buttonString = "BUTTON2";
			else if ((button & Magellan.BUTTON3) != 0)
				buttonString = "BUTTON3";
			else if ((button & Magellan.BUTTON4) != 0)
				buttonString = "BUTTON4";
			else if ((button & Magellan.BUTTON5) != 0)
				buttonString = "BUTTON5";
			else if ((button & Magellan.BUTTON6) != 0)
				buttonString = "BUTTON6";
			else if ((button & Magellan.BUTTON7) != 0)
				buttonString = "BUTTON7";
			else if ((button & Magellan.BUTTON8) != 0)
				buttonString = "BUTTON8";
			else if ((button & Magellan.BUTTONA) != 0) {
				//buttonString = "BUTTONA";
				break;
			}
			System.out.println("Data : " + pos[0] + ", " + pos[1] + ", " + pos[2] + ", "+ rot[0] + ", " + rot[1] + ", " + rot[2] + ", " + buttonString);
		};
		
		magellan.stop();
	}

}	

