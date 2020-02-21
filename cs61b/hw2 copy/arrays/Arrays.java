package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        if ( A== null  && B== null){
            return null;
        }
        if (A == null){
            return B;
        }
        if (B == null) {
            return A;
        }
        int []  result  = new int[A.length +  B.length];
        int iterator;
        int tracker = 0;
        for (iterator = 0; iterator<result.length; iterator +=1){
            if (iterator <A.length){
                result[iterator] = A[iterator];
            }
            else if (tracker< B.length){
                result[iterator] = B[tracker];
                tracker ++;
            }
        }
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] res = new int[ A.length - len];
        for (int a = 0; a< start-1; a++){
            res[a] = A[a];
        }
        int track = start-1;
        for (int b = start+len-1; b<A.length;b++){
            if (track<res.length){
                res[track] = A[b];
                track++;

            }
        }
        return res;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int [][] B;
        if(A==null){
            B = null;
            return B;

        }

        int track = 1;

        {   int tracker = 0;
            while (tracker<A.length-1){
            if (A[tracker]>A[tracker+1]){
                track +=1;

            }
            tracker++;
        }}
        B = new int[track][];
        int j = 0;
        int start_ = 0;
        int boo =0;
        while(boo<A.length) {
            if (boo >= A.length - 1 || A[boo] < A[boo +1]){
                if (boo == A.length -1){
                    int check;
                    check = boo  +1 - start_;
                    B[j] = new int[check];
                    System.arraycopy(A,start_,B[j],0,check);

                }
            }
            else {
                int check;
                check = boo +1 -start_;
                B[j] = new int[check];
                System.arraycopy(A,start_,B[j],0,check);
                start_= boo  +  1;
                j ++;
            }
            boo++;
        }
        return  B;
    }
}
