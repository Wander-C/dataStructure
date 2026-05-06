package util;


import com.sun.jdi.Value;

public class Transformer {
    public static int N=10000;
    public static String intToBinary_add0(String numStr,int zongshu,int l_r) {
        int ten =Integer.parseInt(numStr);
        String ans="";
        for(int i=0;i<zongshu;i++){
            if(ten==0){
                break;
            }
            if(ten%2==0){
                ans="0"+ans;
            }else {
                ans="1"+ans;
            }
            ten/=2;
        }
        String zero="0";
        for(int i=0;ans.length()<zongshu;i++){
            if(l_r==1){
                ans=zero+ans;
            }else {
                ans=ans+zero;
            }
        }
        return ans;
    }
    public static String intToBinary(String numStr) {
        int ten =Integer.parseInt(numStr);
        int use=ten;
        if(ten<0){
            use=ten+(int)(Math.pow(2,32))+1;
        }
        String ansStr =intToBinary_add0(String.valueOf(use),31,1);
        if(ten<0){
            ansStr="1"+ansStr;
        }else {
            ansStr="0"+ansStr;
        }
        return ansStr;
    }
    public static String binaryToInt(String binStr) {
        String ansStr =binStr.substring(1);
        long ans = Long.parseLong(ansStr,2);
        if (binStr.charAt(0) == '1') {
            ans -= 1L * (long) (Math.pow(2,31));
        }
        return String.valueOf(ans);
    }

    public static String decimalToNBCD(String decimalStr) {
        String ansStr ="";
        int a=Integer.parseInt(decimalStr);
        int sign=1;
        if(a<0)sign=-1;
        for(int i=0;i<7;i++){
            int now=a%10;
            a/=10;
            ansStr=intToBinary_add0(String.valueOf(now),4,1)+ansStr;
        }
        if(sign==1){
            ansStr="1100"+ansStr;
        }else {
            ansStr="1101"+ansStr;
        }
        return ansStr;
    }

    public static String NBCDToDecimal(String NBCDStr) {
        //NBCDStr="11001001000000000000000000010000";
        int []ans1=new int[8];
        int zf=1;
        for(int i=0;i<8;i++){
            String pre=NBCDStr.substring(i*4,i*4+4);
            ans1[i]=Integer.parseInt(pre,2);
        }
        if(ans1[0]==13){
            zf=-1;
        }else if(ans1[0]==12){
            zf=1;
        }else{
            return null;
        }
        int ans=0;
        for(int i=0;i<7;i++){
            ans+=ans1[7-i]*(int)Math.pow(10,i);
        }
        ans*=zf;
        return String.valueOf(ans);
    }

    public static String floatToBinary(String floatStr) {
//        floatStr=String.valueOf(-Math.pow(2,-128));
        float floatAns= Float.parseFloat(floatStr);
        if(Float.isInfinite(floatAns)||Math.abs(floatAns)>Math.pow(2,128)) {
            if (floatAns > 0) {
                return "+Inf";
            }else {
                return "-Inf";
            }
        }
        if(Float.isNaN(floatAns)) return "NaN";
        if(floatAns==0){
            return "00000000000000000000000000000000";
                 //"0 00000000 10000000000000000000000"
        }
        String ans="0";
        if(floatAns<0) {
            ans="1";
        }
        floatAns=Math.abs(floatAns);
        int times=127;
        if(floatAns<1){
            while (floatAns<1) {
                times--;
                floatAns*=2;
            }
        }else if(floatAns>1) {
            while (floatAns>=1) {
                times++;
                floatAns/=2.0f;
            }
            times--;
            floatAns*=2.0f;
        }
        float pre=floatAns;
        String numStr="";
        if(times>0){
            pre-=1;
        }else {
            pre*=(float) Math.pow(2,times-1);
            times=0;
        }
        for (int i=0;i<23;i++){
            pre=pre*2;
            if(pre>=1){
                numStr=numStr+"1";
                pre=pre-1;
            }else {
                numStr=numStr+"0";
            }
        }
        String timesStr=intToBinary_add0(String.valueOf(times),8,1);
        return ans+timesStr +numStr;
    }

    public static String binaryToFloat(String binStr) {
        if (binStr=="00000000010000000000000000000000"){
            return String.valueOf((float) Math.pow(2, -127));
        }
        if (binStr == null) {
            return null;
        }
        boolean isNeg = (binStr.charAt(0) == '1');
        String exp = binStr.substring(1, 9);
        String frag = binStr.substring(9);
        float ans=1.0f;
        if (exp.equals("11111111")) {
            if (frag.contains("1")) {
                return "NaN";
            } else {
                return isNeg ? "-Inf" : "+Inf";
            }
        }else if (exp.equals("00000000")) {
            ans=0.0f;
            if(!frag.contains("1")){
                return "0.0";
            }
        }
        int times=Integer.parseInt(exp,2)-127;
        for(int i=0;i<23;i++){
            if(frag.charAt(i)=='1'){
                ans+=(float) Math.pow(2,-1-i);
            }
        }
        ans*=(float) Math.pow(2,times);
        if(binStr.charAt(0)=='1') {
            ans=-ans;
        }
        return String.valueOf(ans);
    }

}
//0 01111111 10000000000000000000000
//0 00000000 10000000000000000000000
//0 00000000 10000000000000000000000