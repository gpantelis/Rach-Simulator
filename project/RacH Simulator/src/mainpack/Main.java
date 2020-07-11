package mainpack;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

	private static int slots = 1;
	static Random rnd = new Random();
	
	public static void main(String[] args) {
		

		Enode enode = new Enode();
		int simulation_time = 2;
		int number_of_ue = 27;
		
		
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		
		    executorService.scheduleAtFixedRate(new Runnable() {
		    	int slot_id = 0;
		    	
		    	@SuppressWarnings("unused")
				Ue[] ue_to_rpc = null;
		    	
		        @Override
		        public void run() {
		        	
					if(slots >= simulation_time) {
					  executorService.shutdown();  
				    }
					//the number of the users that are trying to connect to the Enode
					int number_ue = rnd.nextInt(number_of_ue) + 1;
					ue_to_rpc = slot(number_ue,enode,slot_id,ue_to_rpc);
					slot_id++;
		        }
		        
		    }, 0, 5, TimeUnit.SECONDS);


        //printArray(enode.ue_assigned);
        
       // printArray(enode.ue_unassigned);

	}
	
	
	
	private static Ue[] slot(int number_ue, Enode enode,int slot_id, Ue[] ue_to_rpc) {
			
			System.out.println("----------------------New slot with id " + slot_id + "--------------------------");      	
			System.out.println("Number of Ue trying to connect " + number_ue);   
			slots++;
			
			//the UE list that want to connect to the Enode 
			List<Ue> users = new ArrayList<>(); 
			
			//Create list of users
			for (int i = 0; i < number_ue; i++) {
				//create UE with random preamble selection (1-64)
				users.add(new Ue(rnd.nextInt(63) + 1));
			}
			
			if(ue_to_rpc != null) {
				
				List<Integer> collide = collision_detection_msg2(users,ue_to_rpc);
				System.out.println("---------From previews-----------");
				//System.out.println(ue_to_rpc.length);
	            for (Ue ue:ue_to_rpc ) {
	            	if(ue != null) {
		            	System.out.println("--------------------");
		            	System.out.println(ue.id);
		            	System.out.println(ue.preamble);
		            	System.out.println("--------------------");
	            	}

	            }
	            System.out.println("---------End of previews-----------");
	            
	    		System.out.println("Collisions in msg2: ");
	    		System.out.println(collide.remove(collide.size() - 1));
	    		
	    		
	    		System.out.println("---------Id that collide-----------");
	           	for (Integer i:collide) {
            		System.out.println(i);
            	}
	           	System.out.println("-----------------------------------");
	    		//move ue that not collide to the connected UE to eNode
	           	
	           	System.out.println("---------------EKKATHARISI----------------");
	            for (Ue ue:ue_to_rpc ) {
	
	            	if(ue != null) {
	            		System.out.println("We examine the Ue with id " + ue.getid());
		            	if(!collide.contains(ue.id)) {
		            		
		            		enode.connected_ue.add(ue);   
		            		System.out.println("Ue with id " + ue.getid() + " connected!");
		            	} else {
		            		enode.ue_unassigned.add(ue);
		            		System.out.println("Ue with id " + ue.getid() + " collide!");
		            	}
		            	      
	            	}
   	
	            }
	            System.out.println("------------TELOS EKKATHARISIS-----------");
	    		
			}
		
			
    		//Here we "connect the UE" with the Enode 
    		// if another UE has already assigned to the Enode under the specific 
    		// preamble , we put the UE to the list with the unassigned UE 
            for (Ue ue:users ) {
            	
            	
            	if(ue_to_rpc != null) {
            		
            		List<Integer> collide = collision_detection_msg2(users,ue_to_rpc);
            		collide.remove(collide.size() - 1);
            		
                	if(enode.ue_assigned[ue.preamble] == null && !collide.contains(ue.id)) {
                		enode.ue_assigned[ue.preamble] = ue;
                	} else {
                		enode.ue_unassigned.add(ue);
                	}
                	
            	} else {
                	if(enode.ue_assigned[ue.preamble] == null) {
                		enode.ue_assigned[ue.preamble] = ue;
                	} else {
                		enode.ue_unassigned.add(ue);
                	}
            	}

            	
            }
            
 
            System.out.println("--------Assigned-----------");
            printArray(enode.ue_assigned);
            System.out.println("===========================");
            System.out.println("--------Unassigned---------");
            for (Ue ue:enode.ue_unassigned) {
            	System.out.println(ue.getid() + " --> " + ue.getPreamble());
            }
            System.out.println("===========================");
            System.out.println("--------Connected---------");
            for (Ue ue:enode.connected_ue) {
            	System.out.println(ue.getid() + " --> " + ue.getPreamble());
            }
            //send message 2 to all UE that are assigned successfully to the eNode
            enode.sendMessage2();
            
            //clear unassigned
            enode.clearUe();
            
            System.out.println("-------------------------------------------------"); 
            
            return enode.getUeArray();
	}
	
	//return the number of collisions as the last integer and the previews are the id 
	//of the Ue that collide
	public static List<Integer> collision_detection_msg2(List<Ue> ue1,Ue[] ue2) {
		int coll = 0;
		List<Integer> ue_id_collide = new ArrayList<Integer>();
		for (Ue a:ue1 ) {
        	if(a !=null) {
                for (Ue b:ue2 ) {
                	if(b != null) {
                    	if(a.getPreamble() == b.getPreamble()) {
                    		coll++;
                    		ue_id_collide.add(a.id);
                    		ue_id_collide.add(b.id);
                    		
                    	}
                	}

                }
        	}

        }
		ue_id_collide.add(coll);
		
		return ue_id_collide;
	}
	
	public static void printArray(Ue array[]) {
		System.out.println("id " + " --> " + " signature");
		 for (Ue ue:array ) {
			 if(ue != null) {
				 System.out.println(ue.getid() + " --> " + ue.getPreamble());
				
			 }
			 
		 }
	}

}
