
import java.util.Scanner;

/**
 A program that organizes musicians' positions for the conductor of the Band of the Hour.
 @author Julian Dallas
 */
public class BandOfTheHour {
    private static final int MAX_WEIGHT = 200;
    private static final int MIN_WEIGHT = 45;
    static int numRows;
    static int[][] positions;
    static double[] rowWeights;
    static int[] numPositions;
    static Scanner keyboard = new Scanner(System.in);

    /**
     Main method to execute the Band of the Hour program based on the user's selection of (A)dd, (R)emove, (P)rint, e(X)it.
     This part uses a case switch inside a while loop and then implements the methods based on each selection
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the Band of the Hour");
        System.out.println("--------------------------------");

        initialize();

        char choice;
        while (true) {
            do {
                System.out.print("(A)dd, (R)emove, (P)rint, e(X)it: ");
                choice = keyboard.next().toUpperCase().charAt(0);
                switch (choice) {
                    case 'A':
                        addMusician();
                        break;
                    case 'R':
                        removeMusician();
                        break;
                    case 'P':
                        displayAssignment();
                        break;
                    case 'X':
                        System.out.println("Exit");
                        return; // Exit the program
                    default:
                        System.out.println("ERROR: Invalid option, try again");
                }
            } while (choice != 'A' && choice != 'R' && choice != 'P' && choice != 'X');
        }
    } // End of main method

    /**
     Initializes the Band of the Hour program by setting up the number of rows and positions based on what the user inputs
     This will use a for-loop to iterate over the number of rows and get the number of positions in each row
     */
    static void initialize() {
        System.out.print("Please enter number of rows: ");
        numRows = validateInput(1, 10);
        positions = new int[numRows][];
        rowWeights = new double[numRows];
        numPositions = new int[numRows];

        for (int i = 0; i < numRows; i++) {
            System.out.print("Please enter number of positions in row " + (char) ('A' + i) + ": ");
            numPositions[i] = validateInput(1, 8);
            positions[i] = new int[numPositions[i]];
        }
    }

    /**
     Prints the current layout of musicians' positions.
     1) loop through each row and assign the letter label
     2) loop through each position in each row and prints the weight of the musician
     3) adjust for spacing so that the average/total weight is more consistent
     4) calculate and print total & average weights
     */
    static void displayAssignment() {
        System.out.println();
        for (int i = 0; i < numRows; i++) {
            System.out.print((char) ('A' + i) + ":");
            for (int j = 0; j < numPositions[i]; j++) {
                System.out.printf("%6.1f ", positions[i][j] / 10.0);
            }
            for (int j = numPositions[i]; j < 8; j++) {
                System.out.print("      ");
            }
            double averageWeight = numPositions[i] > 0 ? rowWeights[i] / numPositions[i] : 0;
            System.out.printf("[%6.1f, %6.1f]\n", rowWeights[i], averageWeight);
        }
        System.out.println();
    }

    /**
     Adds a musician to a specific position in a row.
     1) Get a row letter from user -- use a do while loop for a valid input
     2) Get a row position from user -- use a do while loop for a valid input
     3) Get a weight of musician from user -- use a do while loop for a valid input
     4) Update the positions array and the musician is added!
     */
    static void addMusician() {
        char rowLetter;
        int rowIdx;
        do {
            System.out.print("Please enter row letter: ");
            rowLetter = keyboard.next().toUpperCase().charAt(0);
            rowIdx = rowLetter - 'A';
            if (rowIdx < 0 || rowIdx >= numRows) {
                System.out.println("ERROR: Out of range, try again");
            }
        } while (rowIdx < 0 || rowIdx >= numRows);

        int position;
        do {
            System.out.print("Please enter position number (1 to " + numPositions[rowIdx] + "): ");
            position = keyboard.nextInt();
            if (position < 1 || position > numPositions[rowIdx]) {
                System.out.println("ERROR: Out of range, try again");
            } else if (positions[rowIdx][position - 1] != 0) {
                System.out.println("ERROR: There is already a musician there");
                return; // Return without proceeding further
            }
        } while (position < 1 || position > numPositions[rowIdx]);

        double weight;
        do {
            System.out.print("Please enter weight (45.0 to 200.0): ");
            weight = keyboard.nextDouble();
            if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
                System.out.println("ERROR: Out of range, try again");
            } else if (rowWeights[rowIdx] + weight > 100.0 * numPositions[rowIdx]) {
                System.out.println("ERROR: That would exceed the average weight limit");
                return; // Return without proceeding further
            }
        } while (weight < MIN_WEIGHT || weight > MAX_WEIGHT);

        positions[rowIdx][position - 1] = (int) (weight * 10);

        rowWeights[rowIdx] += weight;

        System.out.println("****** Musician added.");
    }

    /**
     Removes a musician from a specific position in a row.
     1) Get a row letter and make sure it is in range
     2) Get a row position and make sure the row has that position
     3) Update positions with 0.0
     4) Update the weights and the musician is removed
     */
    static void removeMusician() {
        char rowLetter;
        int rowIdx;
        do {
            System.out.print("Please enter row letter: ");
            rowLetter = keyboard.next().toUpperCase().charAt(0);
            rowIdx = rowLetter - 'A';
            if (rowIdx < 0 || rowIdx >= numRows) {
                System.out.println("ERROR: Out of range, try again");
            }
        } while (rowIdx < 0 || rowIdx >= numRows);

        int position;
        do {
            System.out.print("Please enter position number (1 to " + numPositions[rowIdx] + "): ");
            position = keyboard.nextInt();
            if (position < 1 || position > numPositions[rowIdx]) {
                System.out.println("ERROR: Out of range, try again");
            } else if (positions[rowIdx][position - 1] == 0) {
                System.out.println("ERROR: That position is vacant");
                return;
            }
        } while (position < 1 || position > numPositions[rowIdx]);

        double weightRemoved = positions[rowIdx][position - 1] / 10.0;
        positions[rowIdx][position - 1] = 0;

        rowWeights[rowIdx] -= weightRemoved;

        System.out.println("****** Musician removed.");
    }

    /**
     Let's use a validateInput method to make validating the user's input a little easier
     Use a do while loop to iterate over inputs, this makes sure that the input is greater than 45 and less than 200
     */
    static int validateInput(int min, int max) {
        int input;
        do {
            while (!keyboard.hasNextInt()) {
                System.out.print("ERROR: Invalid input, try again: ");
                keyboard.next();
            }
            input = keyboard.nextInt();
            if (input < min || input > max) {
                System.out.print("ERROR: Out of range, try again: ");
            }
        } while (input < min || input > max);
        return input;
    }

} // End of BandOfTheHour class