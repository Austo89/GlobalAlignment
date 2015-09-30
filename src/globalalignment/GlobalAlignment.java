/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalalignment;
import java.util.Random;

/**
 *
 * @author Austin Arden
 */
public class GlobalAlignment {
    String input1,input2,output1,output2;
    int match, mismatch, indel;
    
    //contructor
    public GlobalAlignment (String input1, String input2, int match, int mismatch, int indel){
        this.input1 = input1;
        this.input2 = input2;
        this.match = match;
        this.mismatch = mismatch;
        this.indel = indel;
        output1 = "";
        output2 = "";
    }
    
    public int alignThis(){
        //create the matrix
        int[][] scores = new int[input1.length()+1][input2.length()+1];
        int[][] direction = new int[input1.length()+1][input2.length()+1];
        scores[0][0] = 0;
        //fill first column
        for (int i = 1; i < scores.length; i++){
            scores[i][0] = scores[i-1][0] + indel;
            direction[i][0] = 1;
        }
        //fill first row
        for (int i = 1; i < scores[0].length;i++){
            scores[0][i] = scores[0][i-1] + indel;
            direction[0][i] = -1;
        }
        
        for(int i = 1; i < scores.length;i++){
            for(int j= 1; j < scores[0].length;j++){
                boolean matchflag = false;
                if (input1.charAt(i-1) == input2.charAt(j-1)){
                    matchflag = true;
                }
                
                //if match update to the match score from top left
                if (matchflag){
                    scores[i][j] = scores[i-1][j-1] + match;
                    direction[i][j] = 0;
                } else {
                    //otherwise set to mismatch
                    scores[i][j] = scores[i-1][j-1] + mismatch;
                    direction[i][j] = 0;
                }
                
                //if an indel is better, replace the value
                if(scores[i-1][j] + indel > scores[i][j]){
                    scores[i][j] = scores[i-1][j] + mismatch;
                    direction[i][j] = 1;
                }
                if(scores[i][j-1] + indel > scores[i][j]){
                    scores[i][j] = scores[i][j-1] + mismatch;
                    direction[i][j] = -1;
                }
            }
        }
        
        //prepare to rebuild the alignments
        
        //start at the bottom right and move backward
        int row = input1.length();
        int col = input2.length();
        while(row != 0 || col != 0){
            if (direction[row][col] == -1){
                output1 = "-".concat(output1);
                output2 = input2.substring(col-1,col).concat(output2);
                col--;
            } else if (direction[row][col] == 1){
                output2 = "-".concat(output2);
                output1 = input1.substring(row-1,row).concat(output1);
                row--;
            }
            else{
                output1 = input1.substring(row-1,row).concat(output1);
                output2 = input2.substring(col-1,col).concat(output2);
                row--;
                col--;
            }
        }
        
        return scores[scores.length-1][scores[0].length-1];
    }
    
    //make random DNA strings
    public static String generateString(int length){
        Random randy = new Random();
        String randDNA = "";
        for (int i = 0; i < length; i++){
            int select = randy.nextInt(4);
            if (select == 0){
                randDNA = randDNA.concat("G");
            } else if (select == 1){
                randDNA =randDNA.concat("C");
            } else if (select == 2){
                randDNA =randDNA.concat("T");
            } else {
                randDNA =randDNA.concat("A");
            }
        }
        
        return randDNA;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String doodle = "GGCCTT";
        String whirlimajig = "GGTT";
        GlobalAlignment steve = new GlobalAlignment(doodle, whirlimajig,1,-1,-1);
        System.out.println(steve.input1);
        System.out.println(steve.input2);
        System.out.println("Do the thing!");
        int score = steve.alignThis();
        System.out.println(steve.output1);
        System.out.println(steve.output2);
        System.out.print("Score: ");
        System.out.println(score);
        
        //big test
        doodle = generateString(1000);
        whirlimajig = generateString(1000);
        steve = new GlobalAlignment(doodle, whirlimajig,1,-1,-1);
        System.out.println(steve.input1);
        System.out.println(steve.input2);
        System.out.println("Do the thing!");
        score = steve.alignThis();
        System.out.println(steve.output1);
        System.out.println(steve.output2);
        System.out.print("Score: ");
        System.out.println(score);
    }
    
}
