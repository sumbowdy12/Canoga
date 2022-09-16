package Canoga;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
Canoga Game
Author: Boris Le
Date: 05/11/2022
Description: Game that simulates canoga. Can play 1-4 players.
 */


// Canoga Class that implement event listeners and JFrame
public class CanogaMain extends JFrame implements ActionListener{

    //Height and Width of window
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    //Game loop variable
    public static boolean gameOver = false;

    //Declare various GUI layout features such as button, radio buttons and labels
    public static JButton[] inputButtons = new JButton[9];
    public static JRadioButton rollOne = new JRadioButton("Roll one");
    public static JRadioButton rollTwo = new JRadioButton("Roll two");
    public static JLabel textBox = new JLabel();
    public static JLabel textBox2 = new JLabel();

    //Dice attributes
    public static int diceRoll;
    public static boolean twoDice = true;

    //Arrays to work with box numbers
    public static boolean availableNums[];
    public static boolean cloneNums[];

    //Sum of values to be submitted by user combination
    public static int submit = 0;

    //Attributes to keep track of scores, players and turn
    public static int[] playerScores;
    public static int playerTurn = 0;
    public static int players;

    //Main
    public static void main(String[] args) throws IOException {

        //Initialize gui and set visible
        CanogaMain gui = new CanogaMain();
        gui.setVisible(true);

        //Ask user for number of players and set game to that player count
        String[] choices = {"1","2","3","4"};
        String input = JOptionPane.showInputDialog(gui, "Please enter number of players:", "Canoga", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]).toString();
        players = Integer.parseInt(input);
        playerScores = new int[players];

        //Main game loop
        while(!gameOver){

            //Create array of boolean variables representing boxes and enable all boxes (flip them up)
            availableNums = new boolean[9];
            Arrays.fill(availableNums, true);
            setEnabled(availableNums);
            cloneNums = availableNums.clone();

            //Initial die roll and declare player turn
            diceRoll = rollDice();
            textBox.setText("It is now player " + (playerTurn+1) + "'s turn. You rolled a " + diceRoll + ".");
            textBox2.setText("Player " + (playerTurn+1));

            //Turn loop for a player that exits once there are no moves
            while(hasMove(availableNums, 9, diceRoll)){

                //Enable rolling one die if conditions are met
                if (canThrowOne(availableNums)){
                    rollOne.setEnabled(true);
                } else {
                    rollOne.setEnabled(false);
                    rollTwo.setSelected(true);
                    twoDice = true;
                }
            }

            //Add player score to array
            playerScores[playerTurn] = calculateScore(availableNums);

            //Check if canoga is achieved and end game
            if (playerScores[playerTurn] == 45){
                int b = JOptionPane.showConfirmDialog(gui, "Player " + (playerTurn+1) + " wins with Canoga! \n " +
                        "Play again?");
                if(b==JOptionPane.YES_OPTION){
                    playerTurn = 0;
                    continue;
                } else {
                    System.exit(4);
                }
            }

            //Increment to next player turn
            playerTurn += 1;

            //If all players have played
            if (playerTurn == players){

                //Create message string of scores
                String message = "";
                for (int i = 0; i < playerScores.length; i++){
                    message += "Player " + (i+1) + " score: " + playerScores[i] + "\n";
                }

                //Find winner
                int winner = findLowestIndex(playerScores) + 1;

                //Dialog box presenting scores and asking to play again
                int a = JOptionPane.showConfirmDialog(gui, "Game Over! \n" + message + "Winner is player " +
                        winner + "\n" + "Play again?");

                //Play again if yes and exit window if no.
                if(a==JOptionPane.YES_OPTION){
                    playerTurn = 0;
                } else {
                    System.exit(4);
                }
            }
        }
    }

    //Canoga constructor that creates window
    public CanogaMain() throws IOException {
        super();
        setSize(WIDTH, HEIGHT);
        setTitle("Canoga");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayBoard();

    }

    //Event listeners for GUI components
    public void actionPerformed(ActionEvent e){

        String buttonString = e.getActionCommand();

        //If submit button is clicked
        if(buttonString.equals("Submit")){

            //If user did not submit valid combination, reset boxes
            if (submit != diceRoll){
                textBox.setText("Not a valid move. Your roll is: " + diceRoll);
                System.arraycopy(availableNums, 0, cloneNums, 0, cloneNums.length);
                submit = 0;
                setEnabled(availableNums);
            }

            //If valid submission, flip boxes down and reroll
            else {
                diceRoll = rollDice();
                System.arraycopy(cloneNums, 0, availableNums, 0, availableNums.length);
                textBox.setText("You rolled a " + diceRoll + ".");
                submit = 0;
            }
        }

        //If user chooses to roll one, set die roll to one
        if(buttonString.equals("Roll one")){
            twoDice = false;
        }
        //If user chooses to roll tow, set die roll to two
        if(buttonString.equals("Roll two")){
            twoDice = true;
        }

        //If box 1 is clicked disable button and add to submit
        if(buttonString.equals("1")){
            submit += 1;
            cloneNums[0] = false;
            inputButtons[0].setEnabled(false);
        }

        //If box 2 is clicked disable button and add to submit
        if(buttonString.equals("2")){
            submit += 2;
            cloneNums[1] = false;
            inputButtons[1].setEnabled(false);
        }

        //If box 3 is clicked disable button and add to submit
        if(buttonString.equals("3")){
            submit += 3;
            cloneNums[2] = false;
            inputButtons[2].setEnabled(false);
        }

        //If box 4 is clicked disable button and add to submit
        if(buttonString.equals("4")){
            submit += 4;
            cloneNums[3] = false;
            inputButtons[3].setEnabled(false);
        }

        //If box 5 is clicked disable button and add to submit
        if(buttonString.equals("5")){
            submit += 5;
            cloneNums[4] = false;
            inputButtons[4].setEnabled(false);
        }

        //If box 6 is clicked disable button and add to submit
        if(buttonString.equals("6")){
            submit += 6;
            cloneNums[5] = false;
            inputButtons[5].setEnabled(false);
        }

        //If box 7 is clicked disable button and add to submit
        if(buttonString.equals("7")){
            submit += 7;
            cloneNums[6] = false;
            inputButtons[6].setEnabled(false);
        }

        //If box 8 is clicked disable button and add to submit
        if(buttonString.equals("8")){
            submit += 8;
            cloneNums[7] = false;
            inputButtons[7].setEnabled(false);
        }

        //If box 9 is clicked disable button and add to submit
        if(buttonString.equals("9")){
            submit += 9;
            cloneNums[8] = false;
            inputButtons[8].setEnabled(false);
        }
    }

    //Function to initialize all gui components
    public void displayBoard() throws IOException {

        //Give window a border layout
        setLayout(new BorderLayout());

        //JPanel for south side that consists of an initial grid of two rows and 1 column, whereas the top row
        // has a grid of two columns and the bottom has a grid of 2 rows and 9 columns.
        JPanel gridSouth = new JPanel(new GridLayout(2,9));
        JPanel gridSouth2 = new JPanel(new GridLayout(2,1));
        JPanel textBoxs = new JPanel(new GridLayout(1,2));

        //Textbox that indicate player turn and rolls
        textBoxs.add(textBox);
        textBoxs.add(textBox2);


        //Create a radio button group for rolling one and two
        ButtonGroup rolls = new ButtonGroup();
        rollOne.addActionListener(this);
        rollOne.setEnabled(false);
        rollTwo.addActionListener(this);
        rollTwo.setSelected(true);
        rolls.add(rollOne);
        rolls.add(rollTwo);
        gridSouth.add(rollOne);
        gridSouth.add(rollTwo);

        //Create empty labels for better layout design
        for (int i = 2; i < inputButtons.length-1; i++){
            gridSouth.add(new JLabel(""));
        }

        //Create submit button and add event listener
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        gridSouth.add(submitButton);

        //Create number boxes
        for (int i = 0; i < inputButtons.length; i++){
            inputButtons[i] = new JButton(String.valueOf(i+1));
            inputButtons[i].addActionListener(this);
            gridSouth.add(inputButtons[i]);
        }

        //Add the textboxs and grid to the parent grid
        gridSouth2.add(textBoxs);
        gridSouth2.add(gridSouth);
        add(gridSouth2, BorderLayout.SOUTH);

        //Add center board with an image
        BufferedImage board = ImageIO.read(new File("canogaBoard.jpeg"));
        JLabel picLabel = new JLabel(new ImageIcon(board));
        JPanel centerBoard = new JPanel();
        centerBoard.add(picLabel);
        add(centerBoard, BorderLayout.CENTER);

    }

    //Dice roll function that can roll either two or one die
    public static int rollDice(){
        if (twoDice){
            return ((int)(Math.random()*6+1) + (int)(Math.random()*6+1));
        }
        return (int)(Math.random()*6+1);
    }

    //Set all relevant boxes enabled/facing up
    public static void setEnabled(boolean[] arr){
        for (int i = 0; i < arr.length; i ++){
            if (arr[i]) {
                inputButtons[i].setEnabled(true);
            }
        }
    }

    //Check to see if player is allowed to throw one dice
    public static boolean canThrowOne(boolean[] numbersLeft) {
        return !numbersLeft[6] & !numbersLeft[7] & !numbersLeft[8];
    }

    //Calculate the score given an array of what boxes are up or down
    public static int calculateScore(boolean[] arr) {
        int score = 0;
        for (int i = 0; i < arr.length; i++)
        {
            if (!arr[i])
            {
                score += (i + 1);	// add 1 because tiles are numbered 1-9

            }
        }
        return score;
    }

    //Find lowest index of an array
    public static int findLowestIndex(int[] arr){
        int lowest = 46;
        int index = 0;
        for (int i = 0; i< arr.length; i++){
            if (lowest > arr[i]){
                lowest = arr[i];
                index = i;
            }
        }
        return index;
    }

    //Function to check whether a player has a move or not
    public static boolean hasMove(boolean[] validNumbers, int numNumbers, int total){
        int value[] = new int[numNumbers]; 			// numeric values of the remaining tiles
        int nRemaining = 0;			// how many tiles are left
        boolean validMove = false;		// the result variable

        //Create array of numbers still facing up
        for (int i = 0; i < numNumbers; i++)
        {
            if (validNumbers[i])
            {
                value[nRemaining] = i + 1;	// add 1 because tiles are numbered 1-9
                nRemaining++;
            }
        }

        //Create a list of all combinations that can be made into the sum
        List<List<Integer>> combinations = combinationSum2(value, total);

        //If list is not empty, then player can still move
        if (!combinations.isEmpty()){
            validMove = true;
        }

        return validMove;
    }

    // Code to create a list of all combinations of a given array.
    // Retrieved from: https://leetcode.com/problems/combination-sum-ii/discuss/16861/Java-solution-using-dfs-easy-understand
    public static List<List<Integer>> combinationSum2(int[] cand, int target) {
        Arrays.sort(cand);
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        List<Integer> path = new ArrayList<Integer>();
        dfs_com(cand, 0, target, path, res);
        return res;
    }
    public static void dfs_com(int[] cand, int cur, int target, List<Integer> path, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList(path));
            return ;
        }
        if (target < 0) return;
        for (int i = cur; i < cand.length; i++){
            if (i > cur && cand[i] == cand[i-1]) continue;
            path.add(path.size(), cand[i]);
            dfs_com(cand, i+1, target - cand[i], path, res);
            path.remove(path.size()-1);
        }
    }
}
