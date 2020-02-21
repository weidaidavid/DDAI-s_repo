public class Max {
    public static int main(int[] a){
        int cur_max = 0;
        for (int i: a){
            if (i>cur_max) {
                cur_max = i;
            }
        }
        return cur_max;
    }
}
