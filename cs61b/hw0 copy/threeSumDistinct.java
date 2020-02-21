public class threeSumDistinct {
    public static void main(String[] args){

        int[] a = new int[] {-6,2,5};
        int[] b = new int[] {-6,3,10,200};
        System.out.println(threeSumDistinct(b));
    }
    public static boolean threeSumDistinct(int[] a){
        int length = a.length;
        for (int i = 0; i< length; i+=1  ){
            for (int j = i+1; j<length; j+=1){
                for (int  k = j+1; j<length; k+=1){
                    if  (a[i]+a[j]+a[k] == 0){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
