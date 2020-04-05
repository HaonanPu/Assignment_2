package Assignment_2;
/*
 * Name: Haonan Pu
 * ID: 260846401
 * 
 */
public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
    
    public void dance() {
    	System.out.println("The tracks are moving!");
		for (TrainLine tl: networkLines) {
			 tl.shuffleLine();
		}
    }
    
    public void undance() {
		for (TrainLine tl: networkLines) {
			tl.sortLine();
		}
    }
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	
    	TrainLine curLine = getLineByName(startLine); //use this variable to store the current line.
    	TrainStation curStation= curLine.findStation(startStation); //use this variable to store the current station.
    	
    	int hoursCount = 0;
    	System.out.println("Ddeparting from " + startStation);
    	
    	TrainLine finishLine = getLineByName(endLine);
    	TrainStation finishStation = finishLine.findStation(endStation);

    	TrainStation previous = null;

    	while(!(curStation == finishStation)) {
    		
    		if(hoursCount == 168) {
    			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
    			return hoursCount;
    		}

			try {

				TrainStation temp = curStation;
				curStation = curLine.travelOneStation(curStation,previous);
				previous = temp;

				for (TrainLine tl:networkLines) {
					try {
						if (tl.findStation(curStation.getName()) != null) {
							if (tl != curLine) {
								System.out.println("Switching Lines from " + curLine.getName() + " to " + tl.getName());
								curLine = tl;
							}
						}
					}
					catch (StationNotFoundException ignored) {}
				}
			} catch (StationNotFoundException e) {
				System.out.println(e.toString());
				System.out.println("Jumped off after spending a full week on the train. Might as well walk. [Station not found]");
				return 168;
			}
			hoursCount++;

			if (hoursCount % 2 == 0) {
				dance();
			}
			
    		//prints an update on your current location in the network.
	    	System.out.println("Traveling on line "+ curLine.getName() + ":" + curLine.toString());
	    	System.out.println("Hour "+hoursCount + ". Current station: " + curStation.getName() + " on line " + curLine.getName());
	    	System.out.println("=============================================");

    		}
	    	
	    	System.out.println("Arrived at destination after "+ hoursCount + " hours!");
	    	return hoursCount;
    }
    
    
    //you can extend the method header if needed to include an exception. You cannot make any other change to the header.
    public TrainLine getLineByName(String lineName){
    	TrainLine result = null;
    	for (TrainLine trainLine: networkLines) {
    		if (trainLine.getName().equals(lineName)) {
    			result = trainLine;
			}
		}
    	
    	//If the result(line) does not exist, throw the error message.
    	
    	if (result == null) {
    		throw new LineNotFoundException("There is no line named "+ lineName);
		}
    	
    	return result;
    }
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i = 0; i < this.networkLines.length; i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}