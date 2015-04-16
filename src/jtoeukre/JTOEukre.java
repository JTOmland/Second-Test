/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jtoeukre;

/**
 *
 * @author jeffomland
 */
public class JTOEukre {

    public static void main(String[] args) {
        int[] initialState = new int[42];

         //state int array code is:
        //0=trump,1=highcard,2=leadsuit,3=playerTurn,4=cardsintrick, 5 trumpHasBeenPlayed
        //player 0 cards in slots 6 to 11, player 1 in 12 to 17, player 2 in 18 to 23 and player 3 in 24 to 29
        //trick cards slots 30 , 31, 32, 33
        //player scores 34 = east,35 = south,36 west,27 north Maybe used
        //Deck values 1-24 with suit ordered clubs, diamonds, hearts and spades
        //face values from 9-14 (9-10, J, Q, K, A)
        //Suit values 0-3 (c,d,h,s)
        initialState[0] = 0;
        initialState[1] = 0;
        initialState[2] = 0;
        initialState[3] = 0;
        initialState[4] = 0;
        initialState[5] = 0;
        
        
        initialState[12] = 4;
        initialState[13] = 6;
        initialState[14] = 24;//24
        initialState[15] = 17;
        initialState[16] = 5;
        initialState[17] = 1;
        
        initialState[18] = 22;
        initialState[19] = 18;
        initialState[20] = 7;//7
        initialState[21] = 14;
        initialState[22] = 9;
        initialState[23] = 16;
        
        initialState[6] = 11;
        initialState[7] = 3;
        initialState[8] = 20;//20
        initialState[9] = 21;
        initialState[10] = 10;
        initialState[11] = 23;
                
        initialState[24] = 8;
        initialState[25] = 12;
        initialState[26] = 13;//13
        initialState[27] = 2;
        initialState[28] = 15;
        initialState[29] = 15;
        
        
        initialState[34] = 0;//player 0 score
        initialState[35] = 0;//player 1 trickscore
        initialState[36] = 0;//player 2 trickscore
        initialState[37] = 0;//player 3 trickscore
        
        
        initialState[38] = 0;//sets trick number to one
        initialState[40] = 0; //totalCards played
        initialState[39] = 0; //sets node to zero
        
        int maxP = Integer.MAX_VALUE;
        int minP = Integer.MAX_VALUE;
        
        NewSearch paranoid = new NewSearch();
        
        int score;
        long startTime = System.currentTimeMillis();
        score = paranoid.FastSearch(initialState, maxP, minP);
        long endTime = System.currentTimeMillis();
        System.out.println("time " + (endTime - startTime));
        System.out.println("Score " + score);
        System.out.println("Nodes " + paranoid.node);
        
//        Card card = new Card(25);
//        card.setTrickValue(3, 1);
//        
//        for(int i = 1; i<25; i++){
//            Card tempCard = new Card(i);
//            System.out.println("i = " + i + " card "  +tempCard.toString() + " faceValue " + tempCard.getFaceValue() + " tickValue " + card.getTrickValue(i));
//        }
        

    }

}
