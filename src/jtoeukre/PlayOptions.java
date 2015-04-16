/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jtoeukre;

import java.util.ArrayList;

/**
 *
 * @author jeffomland
 */
public class PlayOptions {

    private int highCard, trump, leadSuit, playerTurn, cardsInTrick;
    private int trumpHasBeenPlayed = 0; //0 = false and 1 = true
    private Card card = new Card(25);
    private int cardSuitValue, cardTrickValue;
    private static int[] trickValues;

    public ArrayList<Integer> potentialPlays(int[] state) {
        //state int array code is:
        //0=trump,1=highcard,2=leadsuit,3=playerTurn,4,cardsintrick, 5 trumpHasBeenPlayed
        //player 0 cards in slots 6 to 11, player 1 in 12 to 17, player 2 in 18 to 23 and player 3 in 24 to 29
        //6*1+6 = 12 and 6*1+12 = 18 
        ArrayList<Integer> playOptions = new ArrayList();
        highCard = state[1];
        trump = state[0];
        leadSuit = state[2];
        playerTurn = state[3];
        cardsInTrick = state[4];
        trumpHasBeenPlayed = state[5];
        trickValues = card.getTrickCardValues();

        boolean higherFollow = false;
        boolean canFollow = false;
        boolean canTrump = false;
        boolean canKill = false;
        boolean trumpLed = false;

        if (cardsInTrick == 0) {
         //   System.out.println("potential plays cardsInTrick = 0");
            int j = 0;
            for (int i = (6 * playerTurn + 6); i < 6 * playerTurn + 12; i++) {
                if (state[i] != 0) {
                    playOptions.add(i);
                }
                j++;
            }
            return playOptions;
        }

        if (leadSuit == trump) {
            trumpLed = true;
        }
        //first loop hand and determine if there are cards to follow suite, or trump and if they can kill
        int j = 0;
        for (int i = (6 * playerTurn + 6); i < 6 * playerTurn + 12; i++) {
            if (state[i] != 0) {//there is a card in slot
                if (card.toSuitValue(state[i]) == leadSuit && trickValues[state[i]] < 20) { // same suit and not jick
                    canFollow = true;
                    if (trickValues[state[i]] > highCard) {
                        higherFollow = true;
                    }
                }
                if (card.toSuitValue(state[i]) == trump || trickValues[state[i]] > 19) { //if card is trump suit or jick
                    canTrump = true;
                    if (trumpLed) {  //because followSuit check excludes jick
                        canFollow = true;
                    }
                    if (trickValues[state[i]] > highCard) {
                        canKill = true;
                    }
                }
                j++;
            }
        }

        for (int i = (6 * playerTurn + 6); i < 6 * playerTurn + 12; i++) {
            cardSuitValue = card.toSuitValue(state[i]);
            cardTrickValue = trickValues[state[i]];
            //when trump is led
            if (state[i] != 0) {
                if (trumpLed) {
                    if ((cardSuitValue == leadSuit || cardTrickValue > 19) && canFollow && canKill && cardTrickValue > highCard) {
                        playOptions.add(i);
                        continue;
                    }

                    if (canFollow && !canKill && (cardSuitValue == leadSuit || cardTrickValue > 19)) {
                        playOptions.add(i);
                        continue;
                    }

                    if (!canFollow && !canKill) {
                        playOptions.add(i);
                    }
                } else {
                    //trump not led have a higher card in suit led
                    if (cardTrickValue > highCard && canFollow && higherFollow && cardSuitValue == leadSuit && cardTrickValue < 20) {
                        playOptions.add(i);
                        continue;
                    }
                    //trump not led and can follow but no cards higher
                    if (canFollow && !higherFollow && cardSuitValue == leadSuit && cardTrickValue < 20) {
                        playOptions.add(i);
                        continue;
                    }
                    //cannot follow but can trump and trump is higher
                    if (!canFollow && canKill && (cardSuitValue == trump || cardTrickValue > 19) && cardTrickValue > highCard) {
                        playOptions.add(i);
                        continue;
                    }
                    //cannot follow and cannot trump higher
                    if (!canFollow && !canKill) {
                        playOptions.add(i);
                    }
                }
            }
        }
       // System.out.println("playOptions returned for player " + state[3]);
//        for (int jj : playOptions) {
//            System.out.print(new Card(state[jj]).toStringBrief() + ",");
//        }
//        System.out.println("");
        // System.out.println("Leadsuit " + leadSuit + " Can follow is " + canFollow + " Can follow higher is " + higherFollow +" Can Kill " + canKill + " Can trump " + canTrump);
        return playOptions;
    }

}
