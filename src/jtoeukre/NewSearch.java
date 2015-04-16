/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jtoeukre;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class NewSearch {

    private PlayOptions playOptions = new PlayOptions();
    private Evaluation evaluation = new Evaluation();
    // private ArrayList<Integer> potentialPlays = new ArrayList();
    private int rootCard;
    private Map<Integer, Integer> rootCardToScore = new LinkedHashMap();
    //private Map<int[],ArrayList<Integer> node = new LinkedHashMap();
    private ArrayList<Integer> scores = new ArrayList();//max score for players
    private final int maxSum = 6; //max score for sum of all players scores
    private final int maxp = 6; //max score for a player
    private Stack stateStack = new Stack();
    private Stack scoreStack = new Stack();
    private int currentBest;
    private Card card = new Card(25);
    private ArrayList<int[]> backupStates = new ArrayList();
    public static final String ANSI_RED = "\u001B[31m";
    public int node = 0;
    private int depth = 4;
    private boolean prune = true;

    NewSearch() {

    }

    public int FastSearch(int[] state, int maxP, int minP) {
        //state int array code is:
        //0=trump,1=highcard,2=leadsuit,3=playerTurn,4=cardsintrick, 5 trumpHasBeenPlayed
        //player 0 cards in slots 6 to 11, player 1 in 12 to 17, player 2 in 18 to 23 and player 3 in 24 to 29
        //trick cards slots 30 , 31, 32, 33
        //player scores 34 = east,35 = south,36 west,37 north Maybe used
        //6*1+6 = 12 and 6*1+12 = 18 
        //TODO keep track of highCard in playOptions
        //TODO need to add a slot for leadcard or all cards played
        //TODO need to add a slot for playerTurn calling FastSearch the first time

        ArrayList<Integer> potentialPlays = new ArrayList();
        potentialPlays = playOptions.potentialPlays(state);//playOptions returns the slot of the card (not the card)

        if (potentialPlays.isEmpty() || state[38] == depth) {// terminal leaf reached
            // System.out.println("********Terminal Node **********");
            //evaluation.resetScores();
            //state[39] = node++;
            // backupStates.add(createStateCopy(state));
            //if (state[4] == 4) {//terminal defined as end of trick
            //   System.out.println("Return end of trick for player " + state[3]);
            //  currentBest = evaluation.getEvaluation(state);//sets scores changes playerTurn resets highcard slot
            //  }
            return currentBest;
        }

        if (state[3] == 0) {//Max node defined as east but needs to be defined as original player
            currentBest = Integer.MIN_VALUE;
            // wps = Integer.MIN_VALUE;
            for (int i : potentialPlays) {
                node++;
                state[39] = node;
                Card cardPlay1 = new Card(state[i]);
                //System.out.println(ANSI_RED + "MAX node card " + state[i] + " card " + cardPlay1.toString());
                // System.out.println(ANSI_RED + node + " MAX node card " + cardPlay1.toString());

                backupStates.add(createStateCopy(state));
                playCard(state, i);//remember that i is the slot of card not the card number
                currentBest = Math.max(currentBest, FastSearch(state, maxP, minP));
                //undo play
                for (int jj = 0; jj < state.length; jj++) {
                    state[jj] = backupStates.get(backupStates.size() - 1)[jj];
                }
                backupStates.remove(backupStates.size() - 1);
                //maxP = depth - (state[35] + state[36] + state[37]);
                maxP = state[34] + (depth -state[38]) - (state[38] - state[34]);
              //  System.out.println("maxP " + maxP + " score " + currentBest);
                if (prune == true) {
                    if (currentBest >= maxP) {
                     //   System.out.println("**************** Pruning ***************");
                        return currentBest;
                    } else {
                        maxP = currentBest;
                    }
                }
                //        System.out.println("Undid node " + state[39]);
            }
        } else {// MIN node
            currentBest = Integer.MAX_VALUE;
            //bps = Integer.MAX_VALUE;
            for (int i : potentialPlays) {
                node++;
                state[39] = node;
                Card cardPlay = new Card(state[i]);
//                
                //System.out.println("int i slot in potentialplays = " + i);
                //System.out.println("min node card " + state[i] + " card " + cardPlay.toString());
                // System.out.println(node + "  min node card " + state[i] + " card " + cardPlay.toString());
                //System.out.println("Number of cards in trick before playCard " + state[4]);
                backupStates.add(createStateCopy(state));
                //stateStack.push(createStateCopy(state));//backupstate into a stack
                playCard(state, i);//remember that i is the slot of card not the card number
                currentBest = Math.min(currentBest, FastSearch(state, maxP, minP));

                //undo play
                for (int jj = 0; jj < state.length; jj++) {
                    state[jj] = backupStates.get(backupStates.size() - 1)[jj];
                }
                //         System.out.println("Undid node " + state[39]);
                backupStates.remove(backupStates.size() - 1);
               // System.out.println("sum of state total ticks " + (state[34] + state[35] + state[36] + state[37]) + " slot 38 " + state[38]);
                minP = state[34] - (depth -state[38]) - (state[38] - state[34]);
//                System.out.println("minP " + minP + " score " + currentBest);
//                for (int kk = 34; kk < 38; kk++) {
//                    System.out.println(kk + " " + state[kk]);
//                }

                if (prune == true) {
                    if (currentBest <= minP) {
                       // System.out.println("**************** Pruning ***************");
                        return currentBest;
                    } else {
                        minP = currentBest;
                    }
                }
            }
        }
        // System.out.println("Return end of for player " + state[3] + " card " + state[state[3]]);
        return currentBest;
    }

    private int[] createStateCopy(int[] state) {
        int[] backUpState = new int[state.length];

        for (int i = 0; i < state.length; i++) {
            // System.out.println("backup " + i + " state value " + state[i]);
            backUpState[i] = state[i];
        }

        return backUpState;
    }

    private int[] playCard(int[] state, int cardSlot) {
        //0=trump,1=highcard,2=leadsuit,3=playerTurn,4=cardsintrick, 5 trumpHasBeenPlayed
        //player 0 cards in slots 6 to 11, player 1 in 12 to 17, player 2 in 18 to 23 and player 3 in 24 to 29
        //trick cards slots 30 , 31, 32, 33
        //slot 37 total cards played slot 38 tricknumber
        //slot 39 is node

        //In order to make a play.  Must add card to play slot, remove card from player slot, increase cardsintrick count
        //index playerturn slot, leadsuit if first card, trumphasbeen played if trump, and high card
        state[40] = state[40] + 1;//increase total cards played by one
        if (card.toSuitValue(state[cardSlot]) == state[0]) {//check if trump has been played
            state[5] = 1;
        }
        Card tempCard = new Card(state[cardSlot]);
        if (state[4] == 0) { //if first card in trick set leadSuit
            state[2] = tempCard.getSuitValue();
            // state[4] = 1;
            state[1] = state[cardSlot];
            card.setTrickValue(state[2], state[0]);//sets card trick values based on trump and leadsuit
        }

        if (tempCard.getTrickValue(state[cardSlot]) > tempCard.getTrickValue(state[1])) { //check highcard
            state[1] = state[cardSlot];
        }
        state[30 + state[3]] = state[cardSlot]; //play card into trick slot. Trick slots by player not by card played
        state[cardSlot] = 0; //remove card from hand
        state[4] = state[4] + 1;//increase cards in trick

        state[3] = state[3] + 1;//change playerturn
        if (state[3] > 3) { //if playerturn greater than 3 then go to player 0;
            state[3] = 0;
        }
        if (state[4] == 4) {
            state[38] = state[38] + 1;  //increase trick number
            currentBest = evaluation.getEvaluation(state);//sets scores changes playerTurn resets highcard slot
        }

        return state;
    }

}
