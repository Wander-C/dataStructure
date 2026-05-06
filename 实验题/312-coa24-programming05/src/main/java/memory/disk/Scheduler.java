package memory.disk;

import java.util.ArrayList;
import java.util.Arrays;

public class Scheduler {

    /**
     * 先来先服务算法
     *
     * @param start   磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double FCFS(int start, int[] request) {
        //TODO
        double res = 0.0;
        for(int i = 0; i < request.length; ++i) {
            res += Math.abs(request[i] - start);
            start = request[i];
        }
        return res / request.length;
    }

    /**
     * 最短寻道时间优先算法
     *
     * @param start   磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double SSTF(int start, int[] request) {
        //TODO
        double res = 0.0;
        int[] des = new int[request.length];
        for(int i = 0; i < request.length; ++i) {
            for(int j = 0; j < request.length; ++j) {
                if(request[j] == -1)
                    des[j] = -1;
                else des[j] = Math.abs(start - request[j]);
            }
            int index = nearIndex(des);
            res += des[index];
            start = request[index];
            request[index] = -1;
        }
        return res / request.length;
    }
    private int nearIndex(int[] des) {
        int index = 0;
        for(int i = 0; i < des.length; ++i) {
            if(des[i] >= 0) {//找到第一个为正数的
                index = i;
                break;
            }
        }
        for(int i = index+1; i < des.length; ++i) {
            if(des[i] != -1 && des[i] < des[index]) index = i;
        }
        return index;
    }
    /**
     * 扫描算法
     *
     * @param start     磁头初始位置
     * @param request   请求访问的磁道号
     * @param direction 磁头初始移动方向，true表示磁道号增大的方向，false表示磁道号减小的方向
     * @return 平均寻道长度
     */
    public double SCAN(int start, int[] request, boolean direction) {
        //TODO
        double res = 0.0;
        int minReq = request[0];
        int maxReq = request[0];
        for(int i = 1; i < request.length; ++i) {
            if(request[i] > maxReq)
                maxReq = request[i];
            else if(request[i] < minReq)
                minReq = request[i];
        }
        if(direction) {
            if(start <= minReq) res += (maxReq-start);
            else res += (Disk.TRACK_NUM-1 - start) + (Disk.TRACK_NUM-1 -minReq);
        } else {
            if(start >= maxReq)
                res += (start - minReq);
            else res += (start + maxReq);
        }
        return res / request.length;
    }

    /**
     * C-SCAN算法：默认磁头向磁道号增大方向移动
     *
     * @param start     磁头初始位置
     * @param request   请求访问的磁道号
     * @return 平均寻道长度
     */
    public double CSCAN(int start,int[] request){
        //TODO
        double res = 0.0;
        int low=start;
        int max=start;
        for(int i = 0; i < request.length; i++) {
            if(request[i] <start){
                if(request[i] <low) {
                    low = request[i];
                }
            }
            if(request[i] > max){
                max = request[i];
            }
        }
        if(low!=start){
            res+=2*Disk.TRACK_NUM-2-start+low;
        }else{
            res+=max-start;
        }
        return res / request.length;
    }

    /**
     * LOOK算法
     *
     * @param start     磁头初始位置
     * @param request   请求访问的磁道号
     * @param direction 磁头初始移动方向，true表示磁道号增大的方向，false表示磁道号减小的方向
     * @return 平均寻道长度
     */
    public double LOOK(int start,int[] request,boolean direction){
        //TODO
        return 0.0;
    }

    /**
     * C-LOOK算法：默认磁头向磁道号增大方向移动
     *
     * @param start     磁头初始位置
     * @param request   请求访问的磁道号
     * @return 平均寻道长度
     */
    public double CLOOK(int start,int[] request){
        //TODO
        return 0.0;
    }

}
