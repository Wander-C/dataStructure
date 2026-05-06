import java.util.HashSet;
import java.util.Set;

public class TraceBack {
    public int ans=0;
    public HashSet<HashSet<Integer>> numbers=new HashSet<>(new HashSet<>());
    public HashSet<Integer> num=new HashSet<>();
    public HashSet<HashSet<Integer>> traceBack(int n, int k){
        backtrack(num,n,k,1);
        return numbers;
    }
    private void backtrack( Set<Integer> current, int n, int k, int start) {
        if (current.size() == k) {
            numbers.add(new HashSet<>(current));
            return;
        }
        for (int i = start; i <= n; i++) {
            current.add(i);
            backtrack( current, n, k, i + 1);
            current.remove(i);
            backtrack( current, n, k, i + 1);
        }
    }

}
