package Assignment_2;

import java.util.Arrays;
import java.util.Random;
/*
 * Name: Haonan Pu
 * ID: 260846401
 */

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	//This returns an integer equal to the number of stations
	//on the line.
	public int getSize() {
		TrainStation station = leftTerminus;
		//There is one station when starting from left terminal.
		//Since I added first and execute command in the while-loop, then plus 1 for the right terminal first.
		int size = 2;
		
		while (station.getRight() != null) {
			size++;
			//Keep moving right and search for available station
			station = station.getRight();
		}
		//finally return the integer/number of stations
		return size;
		
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException {
		TrainStation result = getNext(current);
		if (current.hasConnection) {
			if (! (current.getTransferStation() == previous)) {
				result = current.getTransferStation();
			}
		}

		return result;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation getNext(TrainStation station) throws StationNotFoundException {
		station = findStation(station.getName());
		//Set "station" as the unique one we got in findStation() method.
		
		//Check its status 
		//If it is not going right but at the left terminal, set its direction as right
		if (station.isLeftTerminal() && !goingRight) {
			goingRight = true;
			return station.getRight();
		}
		//However, if it is already in the terminal but still going right, set the current direction as false
		//twist its movement as set getLeft()
		if (station.isRightTerminal() &&  goingRight) {
			goingRight = false;
			return station.getLeft();
		}
		//If the train is on the line and moving right, keep its direction unchanged.
		if (goingRight) {
			return station.getRight();
		}
		
		else {
			return station.getLeft();
			
		}
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation findStation(String name) throws StationNotFoundException {
		TrainStation result = null;
		//Iterate through all stations on the line map.
		for (TrainStation ts: lineMap) {
			//If the name in the for-each loop matches the result, then assign it to the result.
			if (ts.getName().equals(name)) {
				result = ts;
			}
		}
		//If the station's value is null on the line, print out the error message.
		if (result == null) {
			throw new StationNotFoundException("Station "+ name + " is not on this line.");
		}
		//Finally return the station I found if it works.
		return result;
	}

	public void sortLine() {
		boolean swap;
		do {
			swap= false;
			TrainStation current = leftTerminus;
			while (current != null) {
				if (!(current.getRight() == null)) {
					//Use compareTo method to check alphabetical order. 
					//The result is positive if the first string is lexicographically greater than the second string
					if (current.getName().compareTo(current.getRight().getName()) > 0) {
						swap(current, current.getRight());
						swap = true;
					}
				}
				current = current.getRight();
			}

		} while (swap);
		lineMap = getLineArray();

	}
	//Here is the swap helper function.
	public void swap (TrainStation a, TrainStation b) {
		//When a is the left terminal
		if (a.isLeftTerminal()) {
			b.setNonTerminal();
			b.setLeftTerminal();//swap a to b, now b will become the left terminal
			a.setNonTerminal();
			leftTerminus = b;
		}
		
		if (b.isRightTerminal()) {
			b.setNonTerminal();
			a.setNonTerminal();
			a.setRightTerminal();//Similarly, switch a to b as the right terminal
			rightTerminus = a;
		}
		//Make a temporary station on the line for a.
		TrainStation temp_left = a.getLeft();
		
		a.setRight(b.getRight());
		
		//If the station of a's right exists, then make it as the left station of a.
		if (a.getRight() != null) {
			a.getRight().setLeft(a);
		}
		
		a.setLeft(b);
		b.setRight(a);//Swapping between station a and station b 
		b.setLeft(temp_left);
		
		//If the temporary value still exists, make it as the right station of b.
		if (temp_left != null) {
			temp_left.setRight(b);
		}
	}

	public TrainStation[] getLineArray() {
		TrainStation station = leftTerminus;
		//Set the station on the left terminal as original position.
		int size = 1;
		//A long as the next station exists, update the station which I set to the next one.
		while (station.getRight() != null) {
			size++;
			station = station.getRight();
		}
		//Here I have a array with all stations on the line.
		TrainStation[] result = new TrainStation[size];
		while (station.getLeft() != null) {
			//check each station's availability backwards.
			size--;
			result[size] = station;
			station = station.getLeft();
		}
		//the first element in the array is the left terminal.
		result[0] = leftTerminus;
		return result;
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		//LineMap is already shuffled now, we just have to update the links
		
		leftTerminus = lineMap[0];
		leftTerminus.setLeft(null);//Since it is already the terminal, set its left empty
		leftTerminus.setNonTerminal();
		leftTerminus.setLeftTerminal();//Make the left terminal true.
		leftTerminus.setRight(lineMap[1]);
		
		//The right terminal is then located on the last position of the line map array.
		rightTerminus = lineMap[lineMap.length-1];
		//There is one station right next to the end(right terminal).
		rightTerminus.setLeft(lineMap[lineMap.length-2]);
		rightTerminus.setRight(null);//Since it is already the terminal, set its right empty as well.
		rightTerminus.setNonTerminal();
		rightTerminus.setRightTerminal();
		
		//Use for-loop to rearrange the line map. Move each station one position forwards/backwards.
		for (int i = 1; i < lineMap.length-1; i++) {
			lineMap[i].setLeft(lineMap[i-1]);
			lineMap[i].setNonTerminal();
			lineMap[i].setRight(lineMap[i+1]);
		}

	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
