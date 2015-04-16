/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jtoeukre;

import java.util.ArrayList;
import java.util.Arrays;

class Evaluation {

    private ArrayList<Integer> scores = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0));
    private int score;
    private int highScore;
    Card card = new Card(25);
    private int trickWinningPlayer;

    //state int array code is:
    //0=trump,1=highcard,2=leadsuit,3=playerTurn,4=cardsintrick, 5 trumpHasBeenPlayed
    //player 0 cards in slots 6 to 11, player 1 in 12 to 17, player 2 in 18 to 23 and player 3 in 24 to 29
    //trick cards slots 30 , 31, 32, 33
    //player scores 34 = east,35 = south,36 west,27 north Maybe used
    //slot 37 total cards played slot 38 tricknumber
    public int getEvaluation(int[] state) {
        card.setTrickValue(state[2], state[0]);
//        System.out.println("&&&&&&&&&&&&&&&&&&Trick Print&&&&&&&&&&&&&&");
//
//        for (int i = 30; i < 34; i++) {
//            System.out.print(new Card(state[i]).toStringBrief() + ",");
//        }
//        System.out.println("\n");

        for (int i = 30; i < 34; i++) {
            if (state[i] != 0) {
                int cardTrickValue = card.getTrickValue(state[i]);
                if (cardTrickValue > highScore) {
                    highScore = cardTrickValue;
                    trickWinningPlayer = i - 30; //should be player 0 - 3;
                }
            }
            // state[i] = 0;//remove card from trick slot
        }
        //System.out.println("TrickWinner " + trickWinningPlayer + " trickNumber " + state[38]);
        state[34 + trickWinningPlayer] = state[34 + trickWinningPlayer] + 1;
        score = state[34] - state[35] - state[36] - state[37];
        highScore = 0;  //reset highscore
        state[3] = trickWinningPlayer;//set turn
        state[1] = 0; //remove highcard
        state[4] = 0;  //reset cards in trick
        for (int i = 30; i < 34; i++) {//remove cards from trickslots
            state[i] = 0;
        }
       // System.out.println("Score " + score);
        return score;
    }

    void resetScores() {

        scores.set(0, 0);
        scores.set(1, 0);
        scores.set(2, 0);
        scores.set(3, 0);

    }

}
