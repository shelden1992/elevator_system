package org.almexoft;

import java.util.*;

import static org.almexoft.Message.println;

/**
 * Created by Shelupets Denys on 08.10.2020.
 */
public class Elevator {
    private final static int MAX_FLOOR;
    private final static int FIRST_FLOOR = 1;
    private static final Map<Integer, Integer> PASSENGER_ON_FLOOR = new HashMap<>();

    static {
        MAX_FLOOR = ((int) (Math.random() * 26)) + 5;
        for (int i = 1; i <= MAX_FLOOR; i++) {
            PASSENGER_ON_FLOOR.put(i, (int) (Math.random() * 9));
        }
    }

    private final int MAX_ELEVATOR_CAPACITY = 6;
    private int currentPassenger = 0;
    private int[] destinationLists = new int[MAX_FLOOR];
    private Scanner scanner = new Scanner(System.in);
    private int currentFloor = 1;
    private List<Integer> listOfFloor = new ArrayList<>();

    public static int getMaxFloor() {
        return MAX_FLOOR;
    }

    public int getMaxElevatorCapacity() {
        return MAX_ELEVATOR_CAPACITY;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int newCurrentFloor) throws IllegalArgumentException {
        if (newCurrentFloor < FIRST_FLOOR || newCurrentFloor > MAX_FLOOR)
            System.out.println("Not valid floor!");
        else
            this.currentFloor = newCurrentFloor;
    }

    public void stepDown() {
        wait(500);
        println(currentFloor + "Floor | Going Down ");
        setCurrentFloor(getCurrentFloor() - 1);
    }

    public void stepUp() {
        wait(500);
        println(currentFloor + "Floor | Going UP ");
        setCurrentFloor(getCurrentFloor() + 1);
    }

    public void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startElevator() {
        println("Highest floor " + MAX_FLOOR);

        showFloarAndPass();
        while (isPassLeafOnSomeFloor()) {
            while (PASSENGER_ON_FLOOR.get(currentFloor) == 0 && MAX_FLOOR > currentFloor + 1) {
                setCurrentFloor(currentFloor + 1);
            }

            println("Elevator come to floor " + currentFloor);
            populationElevator();
            elevatorMove();
        }
        showFloarAndPass();
    }

    private void populationElevator() {
        println("Free place in elevator " + (MAX_ELEVATOR_CAPACITY - currentPassenger));
        int comePassOnFloor = getComePassInElevator();
        for (int i = 1; i <= comePassOnFloor; i++) {
            int floor = askPassengerFloor(i);
            if (!listOfFloor.contains(floor)) listOfFloor.add(floor);
        }
        getLeftPassOnFloor(comePassOnFloor);
    }

    private boolean isPassLeafOnSomeFloor() {
        return PASSENGER_ON_FLOOR.values().stream().anyMatch(integer -> integer > 0);
    }

    private void showFloarAndPass() {
        for (Map.Entry<Integer, Integer> set : PASSENGER_ON_FLOOR.entrySet()) {
            println(set.getKey() + " floor " + set.getValue() + " pass.");
        }
    }

    private void getLeftPassOnFloor(int comePassOnElevator) {
        PASSENGER_ON_FLOOR.put(currentFloor, PASSENGER_ON_FLOOR.get(currentFloor) - comePassOnElevator);
    }

    private int getComePassInElevator() {
        if (PASSENGER_ON_FLOOR.get(currentFloor) >= MAX_ELEVATOR_CAPACITY - currentPassenger) {
            int come = MAX_ELEVATOR_CAPACITY - currentPassenger;
            println("Come in elevator pass " + come);
            return come;
        }
        if (PASSENGER_ON_FLOOR.get(currentFloor) < MAX_ELEVATOR_CAPACITY - currentPassenger) {
            println("Come in elevator pass " + PASSENGER_ON_FLOOR.get(currentFloor));
            return PASSENGER_ON_FLOOR.get(currentFloor);
        }

        return 0;
    }

    private void elevatorMove() {
        for (int i = 0; i < listOfFloor.size(); i++) {
            int shortest = getShortest();
            while (currentFloor < shortest) {
                stepUp();
                populationElevator();
            }
            while (currentFloor > shortest) {
                stepDown();
                populationElevator();
            }
            while (destinationLists[shortest - 1] > 0) {
                currentPassenger--;
                destinationLists[shortest - 1]--;
            }
            println("Yout floor " + shortest);
        }
    }

    int askPassengerFloor(int id) {
        if (currentPassenger > MAX_ELEVATOR_CAPACITY) {
            println("Sorry, max elevator capacity only " + MAX_ELEVATOR_CAPACITY);
        }
        int floor = 0;
        boolean isValid = false;
        while (!isValid) {
            println("Passenger #" + id + "please enter your floor");
            floor = scanner.nextInt();
            if (floor > MAX_FLOOR || floor < FIRST_FLOOR) {
                println("It's not correct floor. Only " + FIRST_FLOOR + "-" + MAX_FLOOR);
            } else if (floor == currentFloor) {
                println("You are already in this floor " + currentFloor);
            } else {
                destinationLists[floor - 1]++;
                currentPassenger++;
                isValid = true;
            }
        }
        return floor;
    }

    int getShortest() {
        int shortest = Math.abs(currentFloor - listOfFloor.get(0));
        int id = 0;
        for (int a = 1; a < listOfFloor.size(); a++) {
            if (shortest > Math.abs(currentFloor - listOfFloor.get(a))) {
                shortest = Math.abs(currentFloor - listOfFloor.get(a));
                id = a;
            }
        }
        shortest = listOfFloor.get(id);
        listOfFloor.set(id, 100);
        return shortest;
    }

}
