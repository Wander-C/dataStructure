package cpu.alu;

import util.DataType;

public class ALU {
    public String add(String first, String second) {
        String result="";
        int add=0;
        for(int i=second.length()-1;i>=0;i--){
            int fir=0;
            if(first.length()>i){
                fir=Integer.parseInt(first.substring(i,i+1));
            }
            int sec=Integer.parseInt(second.substring(i,i+1));
            if((fir+sec+add)%2==1){
                result="1"+result;
            }else {
                result="0"+result;
            }
            if(fir+sec+add>=2){
                add=1;
            }else {
                add=0;
            }
        }
        return result;
    }
}
