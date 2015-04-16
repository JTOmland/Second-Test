/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jtoeukre;

/**
 *
 * @author Jeff Omland
 */
//This class represents a single playing card
//This class is imutable.  Once an objectis constructed
//there are no seeter (mutator) functions to change it's state
//Deck values 1-24 with suit ordered clubs, diamonds, hearts and spades
//face values from 9-14 (9-10, J, Q, K, A)
//Suit values 0-3 (c,d,h,s)
public class Card {

    //suit value constants:
    public static final int CLUBS = 0;
    public static final int DIAMONDS = 1;
    public static final int HEARTS = 2;
    public static final int SPADES = 3;

    //Suit name constants:
    private final String[] suitNames = new String[]{"clubs", "diamonds", "hearts", "spades"};

    //Face value constants:
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;
    public static final int ACE = 14;

    //Face name constatns:
    private final String[] faceNames = new String[]{"9", "10", "Jack", "Queen", "King", "Ace"};

    //Card values:
    private final int deckValue; //1-24(c,d,h,s)
    private final int faceValue; //9-14 (9,10, J, Q, K, A)
    private final int suitValue; //0-3 (c,d,h,s)
    private static int [] trickValue = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//initial state zeros
    //  private final int trumpValue; //(9-A 9 to 13, Jick 14 and Jack 15)


    public Card(int deckValue) {
       // System.out.println("deckvalue " + deckValue);
        //Guard against invalid state
        if ((deckValue < 0) || (deckValue > 25)) {
            
            throw new IllegalArgumentException();
        }
        this.deckValue = deckValue;
        faceValue = toFaceValue(deckValue);
        suitValue = toSuitValue(deckValue);
        // trumpValue = suitValue;
    }

    public Card(int faceValue, int suitValue) {
        if ((faceValue < 2) || (suitValue > 3)) {
            throw new IllegalArgumentException();

        }

        this.faceValue = faceValue;
        this.suitValue = suitValue;
        this.deckValue = toDeckValue(faceValue, suitValue);
      // this.trumpValue = suitValue;

    }

    public boolean equals(Object other) {
        if (!(other instanceof Card)) {
            return false;
        }
        Card cardOther = (Card) other;
        if (deckValue != cardOther.deckValue) {
            return false;
        }
        return true;
    }

    public static int toFaceValue(int deckValue) {
        int faceValue = (deckValue % 6) + 8;
        if (faceValue == 8) {
            faceValue = 14;
        }
        return faceValue;
    }

    public static int toDeckValue(int faceValue, int suitValue) {
        //Guard against invalid state
        if ((faceValue < 9) || (faceValue > 14)) {
            throw new IllegalArgumentException();
        }
        if ((suitValue < 0) || (suitValue > 3)) {
            throw new IllegalArgumentException();
        }

        int deckValue = (6 * (suitValue)) + faceValue - 8;
        return deckValue;
    }

    public int toSuitValue(int deckValue) {
        int suitValue = deckValue / 6;
        // System.out.println("DeckValue " + deckValue + " dv/13 " + deckValue/13);
        if (deckValue % 6 == 0) {
            suitValue--;
        }
        return suitValue;
    }

    public String toString() {
        String string = getFaceName() + " Of " + getSuitName();
        return string;
    }

    public String toStringBrief() {
        String faceBrief;
        if (faceValue <= 10) {
            faceBrief = getFaceName();
        } else {
            faceBrief = getFaceName().substring(0, 1);
        }
        String suitBrief = getSuitName().substring(0, 1);
        String brief = faceBrief + suitBrief;
        return brief;
    }

    public int getDeckValue() {
        return deckValue;
    }

    public String getFaceName() {
        return faceNames[faceValue - 9];
    }

    public int getFaceValue() {
        return faceValue;
    }

    public int getSuitValue() {
        return suitValue;
    }

    private String getSuitName() {
        //System.out.println("suitValue " + suitValue);
        return suitNames[suitValue];
    }
    
    public int getTrickValue(int deckValue){
        return trickValue[deckValue];
    }
    
    public int [] getTrickCardValues(){
        return trickValue;
    }
   
    
    public void setTrickValue(int leadSuit, int trump){
        //1-6 clubs, 7-12 diamonds, 13-18 hearts, 19 to 24 spades 
        int startValue = 9;
        for(int i = (leadSuit*6) + 1; i<(leadSuit*6 + 7); i++){
            trickValue[i] = startValue;
            startValue ++;
        }
        
         int bowerSuit = 0;
        switch (trump) {
            case 0:
                bowerSuit = 3;
                break;
            case 1:
                bowerSuit = 2;
                break;
            case 2:
                bowerSuit = 1;
                break;
            case 3:
                bowerSuit = 0;
                break;
        }
            //6*1+3 = 9 6=9,7=10 9 = jack
            trickValue[trump*6+3] = 21; //Jack of trump
            trickValue[bowerSuit*6+3] = 20; //Jick of trump
            trickValue[trump*6+6] = 19; //ace of trump
            trickValue[trump*6+5] = 18; //king of trump
            trickValue[trump*6+4] = 17; //queen of trump
            trickValue[trump*6+2] = 16; //10 of trump
            trickValue[trump*6+1] = 15; //9 of trump
    }

    public int getTrumpValue(int trumpSuit, boolean trumpHasBeenPlayed) {
        int trumpValue = this.faceValue;
        if (trumpHasBeenPlayed) {
            trumpValue = 0;
        }
        int bowerSuit = 0;
        switch (trumpSuit) {
            case 0:
                bowerSuit = 3;
                break;
            case 1:
                bowerSuit = 2;
                break;
            case 2:
                bowerSuit = 1;
                break;
            case 3:
                bowerSuit = 0;
                break;
        }
        if (toFaceValue(this.deckValue) == 11 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 21; //Jack of trump
        }
        if (toFaceValue(this.deckValue) == 11 && toSuitValue(this.deckValue) == bowerSuit) {
            trumpValue = 20; //Jick of trump
        }
        if (toFaceValue(this.deckValue) == 14 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 19; //ace of trump
        }
        if (toFaceValue(this.deckValue) == 13 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 18; //king of trump
        }
        if (toFaceValue(this.deckValue) == 12 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 17; //queen of trump
        }
        if (toFaceValue(this.deckValue) == 10 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 16; //10 of trump
        }
        if (toFaceValue(this.deckValue) == 9 && toSuitValue(this.deckValue) == trumpSuit) {
            trumpValue = 15; //9 of trump
        }
        return trumpValue;
    }
}
