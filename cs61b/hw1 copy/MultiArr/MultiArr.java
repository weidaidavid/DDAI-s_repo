import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Multidimensional array
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        int[] Columns = new int[arr.length];
        int lengtha = 0;
        for (int i = 0; i< arr.length;i++ ) {
             Columns[i] = arr[i].length;
             if (Columns[i]>lengtha){
                 lengtha = Columns[i];
             }
        }


        System.out.println("Rows :" + arr.length);
        System.out.println("Columns :" + lengtha);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int curr_max = 0;
        for (int i = 0;i< arr.length; i++){
            for (int j = 0; j< arr[i].length;j++){
                if (arr[i][j]> curr_max) {
                    curr_max = arr[i][j];
                }
            }
        }
        return curr_max;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int [] row_sums = new int[arr.length];
        int temp_sum = 0;
        for (int i = 0; i<arr.length; i++){
            temp_sum = 0;
            for (int j = 0; j<arr[i].length;j++){
                temp_sum +=  arr[i][j];

            }
            row_sums[i] = temp_sum;
        }
        return row_sums;
    }
}