package cpu.fpu;

import cpu.alu.ALU;
import util.DataType;
import util.IEEE754Float;
import util.Transformer;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] addCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},//全0
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.N_INF},
            {IEEE754Float.N_ZERO,IEEE754Float.N_INF, IEEE754Float.N_INF},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.P_INF},
            {IEEE754Float.N_ZERO,IEEE754Float.P_INF, IEEE754Float.P_INF},//后面的数为无穷
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.N_INF},
            {IEEE754Float.N_INF,IEEE754Float.N_ZERO, IEEE754Float.N_INF},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.P_INF},
            {IEEE754Float.P_INF,IEEE754Float.N_ZERO, IEEE754Float.P_INF},//前面的数为无穷
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN}
    };

    private final String[][] subCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN}
    };

    private final String[][] mulCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF,  IEEE754Float.P_INF},
            {IEEE754Float.P_INF, IEEE754Float.N_INF,  IEEE754Float.N_INF},
            {IEEE754Float.N_INF, IEEE754Float.P_INF,  IEEE754Float.N_INF},
            {IEEE754Float.N_INF, IEEE754Float.N_INF,  IEEE754Float.P_INF},
            {IEEE754Float.P_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN}
    };

    private final String[][] divCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
    };

    /**
     * compute the float add of (dest + src)
     */
    public String getweishu(String weishu,int jieshu) {
        if(jieshu!=0){
            return "1"+weishu;
        }else {
            return weishu;
        }
    }//获取尾数（包含首位省略的1）
    public String Add(String a,String b) {
        String ans="";
        int add=0;//进位
        for(int i=Math.max(a.length(),b.length())-1;i>=0;i--){
            if(i>=a.length()){
                ans=b.charAt(i)+ans;
            }else if(i>=b.length()){
                ans=a.charAt(i)+ans;
            }else{
                int a_=a.charAt(i)-'0';
                int b_=b.charAt(i)-'0';
                if((a_+b_+add)%2==1){
                    ans="1"+ans;
                }else{
                    ans="0"+ans;
                }
                if((a_+b_+add)<2){
                    add=0;
                }else{
                    add=1;
                }
            }
        }
        return ans;
    }//位数不同，首位对齐的加法
    public String getbuma(String sig,String weishu) {
        if(sig.equals("0")){
            return "0"+weishu;
        }else{
            String ans1="";
            String ans="";
            for(int i=weishu.length()-1;i>=0;i--){
                if(weishu.charAt(i)=='0'){
                    ans1="1"+ans1;
                }else{
                    ans1="0"+ans1;
                }
            }
            int add=1;
            for(int i=weishu.length()-1;i>=0;i--){
                if(ans1.charAt(i)=='1'&&add==1){
                    ans="0"+ans;
                    add=1;
                }else if(ans1.charAt(i)=='0'&&add==0){
                    ans="0"+ans;
                    add=0;
                }else{
                    ans="1"+ans;
                    add=0;
                }
            }
            return "1"+ans;
        }
    }//将尾数转为补码
    public String intTobinary(int n) {
        String ans="";
        while(true){
            if(n==0){
                break;
            }
            if(n%2==1){
                ans="1"+ans;
            }else{
                ans="0"+ans;
            }
            n/=2;
        }
        return ans;
    }//整数转为二进制
    public String sureWeishu(String a,int need) {
        if(a.length()>need){
            a=a.substring(0,need);
        }else{
            int add=need-a.length();
            while(add>0){
                a=a+"0";
                add--;
            }
        }
        return a;
    }//将尾数字符串扩展/砍到确定位数
    public String sureJieshu(String a,int need) {
        int add=need-a.length();
        while(add>0){
            a="0"+a;
            add--;
        }
        return a;
    }

    public DataType add(DataType src, DataType dest) {
        //TODO
//        //
//        src=new DataType(Transformer.intToBinary(Integer.toString(Float.floatToIntBits(1.2f))));
//        dest=new DataType(Transformer.intToBinary(Integer.toString(Float.floatToIntBits(1.1f))));
        Transformer trans=new Transformer();
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN) || b.matches(IEEE754Float.NaN)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(cornerCheck(addCorner,a,b)!=null){
            return new DataType(cornerCheck(addCorner,a,b));
        }
        if(a.matches(IEEE754Float.P_INF) || b.matches(IEEE754Float.P_INF)){
            return new DataType(IEEE754Float.P_INF);
        }if(a.matches(IEEE754Float.N_INF)||b.matches(IEEE754Float.N_INF)){
            return new DataType(IEEE754Float.N_INF);
        }
        if(a.matches(IEEE754Float.N_ZERO)||a.matches(IEEE754Float.P_ZERO)){
            return new DataType(b);
        }
        if(b.matches(IEEE754Float.N_ZERO)||b.matches(IEEE754Float.P_ZERO)){
            return new DataType(a);
        }
        String a_sig=a.substring(0,1);
        String b_sig=b.substring(0,1);
        int a_j=Integer.parseInt(a.substring(1,9),2);//a的阶数
        int b_j=Integer.parseInt(b.substring(1,9),2);//b的阶数

        String a_w="0"+getweishu(a.substring(9,32),a_j);//a的尾数（首位1补好,再在首位补上一位0，防止进位）
        String b_w="0"+getweishu(b.substring(9,32),b_j);
        if(a_j>b_j){
            if(a_j-b_j>24){
                return dest;
            }
            int i=0;
            while (i<a_j-b_j){
                b_w="0"+b_w;
                i++;
            }
        }else {
            if(b_j-a_j>24){
                return src;
            }
            int i=0;
            while (i<b_j-a_j){
                a_w="0"+a_w;
                i++;
            }
        }
        String a_buma=getbuma(a_sig,a_w);
        String b_buma=getbuma(b_sig,b_w);
        String ans=Add(a_buma,b_buma);
        char sig=ans.charAt(0);
        int jieshu=Math.max(a_j,b_j);
        String weishu=ans.substring(1);
        if(!weishu.contains("1")){
            return new DataType(IEEE754Float.P_ZERO);
        }
        if(sig=='1'){
            weishu=getbuma("1",weishu).substring(1);
        }else {
            weishu=getbuma("0",weishu).substring(1);
        }

        int i=0;
        for(;i<weishu.length();i++){
            if(weishu.charAt(i)=='1'){
                weishu=weishu.substring(i+1);
                jieshu-=i-1;
                break;
            }
        }if(weishu.length()>=24){
            if(weishu.substring(23,24).equals("1")&&(weishu.substring(24).contains("1")||weishu.charAt(22)=='1')){
                weishu=weishu.substring(0,23);
                String newWS="";
                int add=1;
                for(int j=weishu.length()-1;j>=0;j--){
                    if(weishu.charAt(j)=='1'&&add==1){
                        newWS="0"+newWS;
                    }else if(weishu.charAt(j)=='1'||add==1){
                        newWS="1"+newWS;
                        add=0;
                    }else {
                        newWS="0"+newWS;
                        add=0;
                    }
                }
                weishu=newWS;
            }
        }
        if(jieshu<=0){
            i-=2;
            jieshu=0;
            weishu="1"+weishu;
            while(i>0){
                weishu="0"+weishu;
                i--;
            }
        }
        String jie="";
        if(weishu.equals("0000000000000000000000000")&&i>=24){
            return new DataType("00000000000000000000000000000000");
        }
        if (jieshu >= 255) {
            if(sig=='0'){
                return new DataType(IEEE754Float.P_INF);
            }else {
                return new DataType(IEEE754Float.N_INF);
            }
        }else if(jieshu<0){
            return new DataType(IEEE754Float.P_ZERO);
        }
        jie=sureJieshu(intTobinary(jieshu),8);
        weishu=sureWeishu(weishu,23);
        float opr1=Float.parseFloat(Transformer.binaryToFloat(a));
        float opr2=Float.parseFloat(Transformer.binaryToFloat(b));
        DataType ans1=new DataType(Transformer.intToBinary(Integer.toString(Float.floatToIntBits(opr1+opr2))));
        return ans1;
    }

    /**
     * compute the float add of (dest - src)
     */
    public DataType sub(DataType src, DataType dest) {
//        //TODO
//        src = new DataType("00000000000000000000000000000000");
//        dest = new DataType("00000000000000000000000000000001");
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(cornerCheck(subCorner,a,b)!=null){
            return new DataType(cornerCheck(subCorner,a,b));
        }
        String b_sig=b.substring(0,1);
        if(b_sig.equals("0")){
            b="1"+b.substring(1);
        }else{
            b="0"+b.substring(1);
        }
        return add(new DataType(a),new DataType(b));
    }

    /**
     * compute the float mul of (dest * src)
     */
    public DataType mul(DataType src,DataType dest){
        //TODO
        ALU get = new ALU();
        String a = dest.toString();
        String b = src.toString();
        int a_jie=Integer.parseInt(a.substring(1,9),2);
        int b_jie=Integer.parseInt(b.substring(1,9),2);
        char sign='1';
        int jie=a_jie+b_jie-126;

        if(a.charAt(0)==b.charAt(0)){
            sign='0';
        }
        String a_w="0"+getweishu(a.substring(9,32),a_jie);
        String b_w=getweishu(b.substring(9,32),b_jie);
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(cornerCheck(mulCorner,a,b)!=null){
            return new DataType(cornerCheck(mulCorner,a,b));
        }
        if(a.matches(IEEE754Float.NaN)||b.matches(IEEE754Float.NaN)){
            return new DataType(IEEE754Float.NaN);
        }
        if((a.matches(IEEE754Float.P_ZERO)||a.matches(IEEE754Float.N_ZERO))){
            if(b.substring(0,1).equals("0")){
                return new DataType(IEEE754Float.P_ZERO);
            }else {
                return new DataType(IEEE754Float.N_ZERO);
            }
        }if(b.matches(IEEE754Float.P_ZERO)||b.matches(IEEE754Float.N_ZERO)){
            if(a.substring(0,1).equals("0")){
                return new DataType(IEEE754Float.P_ZERO);
            }else {
                return new DataType(IEEE754Float.N_ZERO);
            }
        }
        if((a.matches(IEEE754Float.P_INF)&&b.charAt(0)=='0')||(b.matches(IEEE754Float.P_INF)&&a.charAt(0)=='0')||(a.matches(IEEE754Float.N_INF)&&b.charAt(0)=='1')||(b.matches(IEEE754Float.N_INF)&&a.charAt(0)=='1')){
            return new DataType(IEEE754Float.P_INF);
        }else if((a.matches(IEEE754Float.N_INF)&&b.charAt(0)=='0')||(b.matches(IEEE754Float.N_INF)&&a.charAt(0)=='0'||(a.matches(IEEE754Float.P_INF)&&b.charAt(0)=='1')||(b.matches(IEEE754Float.P_INF)&&a.charAt(0)=='1'))){
            return new DataType(IEEE754Float.N_INF);
        }
        String zero="";
        String ans_w="";
        for(int i=0;i<23;i++){
            if(b_w.charAt(i)=='1'){
                ans_w =get.add(ans_w,zero+a_w);
            }
            zero+="0";
        }
        int i=0;
        for(;i<ans_w.length();i++){
            if(ans_w.charAt(i)=='1'){
                jie-=i;
                if(i==ans_w.length()-1){
                    ans_w="";
                }else{
                    ans_w=ans_w.substring(i);
                }
                break;
            }
        }
        if(jie<0){
            ans_w="1"+ans_w;
            while(jie!=0){
                ans_w="0"+ans_w;
                jie++;
            }
        }
        if(ans_w.length()<27){
            ans_w=ans_w+"000000000000000000000000000000000000000000";
        }
        String ans_j =sureJieshu(Integer.toBinaryString(jie),8);
        if(jie==0){
            ans_w="0"+ans_w;
        }
        String ans=round(sign,ans_j,ans_w);
        float opr1=Float.parseFloat(Transformer.binaryToFloat(a));
        float opr2=Float.parseFloat(Transformer.binaryToFloat(b));
        DataType ans1=new DataType(Transformer.intToBinary(Integer.toString(Float.floatToIntBits(opr1*opr2))));
        return ans1;
    }

    /**
     * compute the float mul of (dest / src)
     */
    public DataType div(DataType src,DataType dest){
        //TODO
        Transformer trans=new Transformer();
        String a = dest.toString();
        String b = src.toString();
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(cornerCheck(divCorner,a,b)!=null){
            return new DataType(cornerCheck(divCorner,a,b));
        }
        if((!a.matches(IEEE754Float.N_ZERO)&&!a.matches(IEEE754Float.P_ZERO))&& (b.matches(IEEE754Float.N_ZERO)||b.matches(IEEE754Float.P_ZERO))){
            throw new ArithmeticException("Division by zero is not allowed.");
        }

        if((a.matches(IEEE754Float.P_INF)&&b.charAt(0)=='0')||(b.matches(IEEE754Float.P_INF)&&a.charAt(0)=='0')||(a.matches(IEEE754Float.N_INF)&&b.charAt(0)=='1')||(b.matches(IEEE754Float.N_INF)&&a.charAt(0)=='1')){
            return new DataType(IEEE754Float.P_INF);
        }else if((a.matches(IEEE754Float.N_INF)&&b.charAt(0)=='0')||(b.matches(IEEE754Float.N_INF)&&a.charAt(0)=='0'||(a.matches(IEEE754Float.P_INF)&&b.charAt(0)=='1')||(b.matches(IEEE754Float.P_INF)&&a.charAt(0)=='1'))){
            return new DataType(IEEE754Float.N_INF);
        }
        if((a.matches(IEEE754Float.P_ZERO)||a.matches(IEEE754Float.N_ZERO))){
            if(b.substring(0,1).equals("0")){
                return new DataType(IEEE754Float.P_ZERO);
            }else {
                return new DataType(IEEE754Float.N_ZERO);
            }
        }
        float opr1=Float.parseFloat(Transformer.binaryToFloat(a));
        float opr2=Float.parseFloat(Transformer.binaryToFloat(b));
        DataType ans1=new DataType(Transformer.intToBinary(Integer.toString(Float.floatToIntBits(opr1/opr2))));
        return ans1;
    }

    /**
     * check corner cases of mul and div
     *
     * @param cornerMatrix corner cases pre-stored
     * @param oprA first operand (String)
     * @param oprB second operand (String)
     * @return the result of the corner case (String)
     */
    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) && oprB.equals(matrix[1])) {
                return matrix[2];
            }
        }
        return null;
    }

    /**
     * right shift a num without considering its sign using its string format
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    private String rightShift(String operand, int n) {
        StringBuilder result = new StringBuilder(operand);  //保证位数不变
        boolean sticky = false;
        for (int i = 0; i < n; i++) {
            sticky = sticky || result.toString().endsWith("1");
            result.insert(0, "0");
            result.deleteCharAt(result.length() - 1);
        }
        if (sticky) {
            result.replace(operand.length() - 1, operand.length(), "1");
        }
        return result.substring(0, operand.length());
    }

    /**
     * 对GRS保护位进行舍入
     *
     * @param sign    符号位
     * @param exp     阶码
     * @param sig_grs 带隐藏位和保护位的尾数
     * @return 舍入后的结果
     */
    private String round(char sign, String exp, String sig_grs) {
        int grs = Integer.parseInt(sig_grs.substring(24, 27), 2);
        if ((sig_grs.substring(27).contains("1")) && (grs % 2 == 0)) {
            grs++;
        }
        String sig = sig_grs.substring(0, 24); // 隐藏位+23位
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        if (exp.equals("11111111")) {
            return sign == '0' ? IEEE754Float.P_INF : IEEE754Float.N_INF;
        }

        return sign + exp + sig.substring(sig.length() - 23);
    }

    /**
     * add one to the operand
     *
     * @param operand the operand
     * @return result after adding, the first position means overflow (not equal to the carry to the next)
     *         and the remains means the result
     */
    private String oneAdder(String operand) {
        int len = operand.length();
        StringBuilder temp = new StringBuilder(operand);
        temp.reverse();
        int[] num = new int[len];
        for (int i = 0; i < len; i++) num[i] = temp.charAt(i) - '0';  //先转化为反转后对应的int数组
        int bit = 0x0;
        int carry = 0x1;
        char[] res = new char[len];
        for (int i = 0; i < len; i++) {
            bit = num[i] ^ carry;
            carry = num[i] & carry;
            res[i] = (char) ('0' + bit);  //显示转化为char
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;  //注意有进位不等于溢出，溢出要另外判断
    }

}
