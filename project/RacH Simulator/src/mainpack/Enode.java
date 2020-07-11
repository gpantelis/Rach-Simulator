package mainpack;

import java.util.ArrayList;
import java.util.List;

//Class that represent a Enode

public class Enode {
	
	//the number of all prembles (default 64)
	public static int preamble_number = 64;
	
	//the number of preambles that used for contention based
	public static int contention_based = 64;
	
	//this is an array that stores the User Equipment(UE) that
	//are assigned to a preamble (NO Collision) 
	public Ue[] ue_assigned = new Ue[64];
	
	//this is an array that stores the User Equipment(UE) that
	//are not assigned to a preamble (Collision) 
	public List<Ue> ue_unassigned = new ArrayList<>(); 
	

	
	public List<Ue> connected_ue = new ArrayList<>(); 
	
	public Enode() {
		System.out.println("Enode deployed...");
	}

	public void setUe(int i, Ue ue) {
		this.ue_assigned[i-1] = ue;
	}
	
	public Ue[] getUeArray() {
		System.out.println("inside getUearray");
		Ue[] temp = ue_assigned;
		ue_assigned = new Ue[64];
		return temp;
	}
	
	//this method illustrates the message2(TA,PUSCH resources,bck) that are sent
	//to the UE
	//We assume that the 
	public void sendMessage2() {
		System.out.println("inside sendMessage2 START...");
		int preamble = 0;
		int counter = 0;
		for (Ue ue:ue_assigned ) {
			//System.out.println("FOR..." + counter);
			
			if(ue != null) {
				//System.out.println("In position " + Integer.toString(counter) + " we set preamble " + Integer.toString(preamble));
				ue.setBCK("1");
				
				//pusch is the uplink channel that is devided into 64 preambles so the eNode gives one preamble to every UE.
				//we assume that 64 preambles are enough to cover the UE's
				ue.setPSUCH(preamble);
				preamble++;
				ue.setTA(0.0);
			} else {
				//System.out.println("In position " + Integer.toString(counter) + " is null");
			}
			counter++;

		}
		System.out.println("inside sendMessage2 END");
	}
		
	
	public void clearUe() {
		System.out.println("inside clearUe");
		
		//ue_assigned = new Ue[64];
		ue_unassigned.clear();
		System.out.println("ue_assigned and ue_unassigned are initilazed to null");
	}
}
