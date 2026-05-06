
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class DirectedGraph {
    private int Vertexnum;
    private int Edgenum;
    private int[][] Edge;

    public DirectedGraph() {
    }
    /**
     * @param Vertexnum  表示顶点的数目，顶点编号为0，Vertexnum-1
     * @param Edge 二维数组，表示边，第一维是边的个数，第二维长度为2，表示一条有向边，分别为出度和入度。
     * @param Edgenum  表示有几条边
     */
    DirectedGraph(int Vertexnum, int[][] Edge, int Edgenum) {
        this.Vertexnum = Vertexnum;
        this.Edge = Edge;
        this.Edgenum = Edgenum;
    }
    /**
     * 如果包括环 返回一个包含环的字符串格式如下“”
     *
     * @param graph  输入的图对象
     * @return  若有环，按升序返回环所在的边的字符串，eg"(1,2)(2,3)(3,1)"，如果不包括则返回空字符串
     */
    public static StringBuilder FindCycle(DirectedGraph graph) {
        // please enter your code here...
        //查看所有边
        /*
        for (int i = 0; i < graph.Edgenum; i++) {
            System.out.println(Arrays.toString(graph.Edge[i]));
        }*/
        boolean[] visited = new boolean[graph.Vertexnum];
        for (int i=0; i<graph.Edgenum; i++) {
            int from=i;
            int[] way=new int[graph.Vertexnum];
            int length=1;
            way[0]=0;
            while (true) {
                for (int j=0;j<graph.Edgenum;j++){
                    if(graph.Edge[i][0]==way&&)
                }
            }
        }
        return null;
    }


}