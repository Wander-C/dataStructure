package cpu.alu;

import util.DataType;
import util.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    /**
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType add(DataType src, DataType dest) {
        // TODO
        String first=src.toString();
        String second=dest.toString();
        String result="";
        int add=0;
        for(int i=31;i>=0;i--){
            int fir=Integer.parseInt(first.substring(i,i+1));
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
        return new DataType(result);
    }

    /**
     * 返回两个二进制整数的差
     * dest - src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType sub(DataType src, DataType dest) {
        // TODO
//        dest=new DataType("11111111111111111111111111111110");
//        src=new DataType("11111111111111111111111111111111");

        String second=src.toString();//补码
        String third="";//改成其相反数
        DataType result=src;
        String fh="0";
        if(second.charAt(0)=='0'){//如果src是正数
            fh="1";
        }else {
            result=add(src,new DataType("11111111111111111111111111111111"));
                                            //   00000000000000000000000000000100
            fh="0";
        }
        String change=result.toString();
        for(int i=31;i>0;i--){
            if(change.charAt(i)=='1'){
                third="0"+third;
            }else {
                third="1"+third;
            }
        }
        third=fh+third;
        DataType thirdResult=new DataType(third);
        if(fh=="1"){
            thirdResult=add(thirdResult,new DataType("00000000000000000000000000000001"));

        }
        result=add(dest,thirdResult);
        return result;
    }

    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType mul(DataType src, DataType dest) {
        String srcStr = src.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("00000000000000000000000000000000");
        sb.append(src.toString()); // 64位寄存器模拟
        sb.append("0"); // 末尾添加一位 y0
        for (int i = 0; i < srcStr.length(); i++) {
            if (sb.charAt(64) - sb.charAt(63) == 1){
                sb.insert(0,add(new DataType(sb.substring(0,32)), dest).toString());
                sb.delete(32,64);
            } else if (sb.charAt(64) - sb.charAt(63) == -1) {
                sb.insert(0,sub(dest, new DataType(sb.substring(0,32))).toString());
                sb.delete(32,64);
            }
            sb.delete(64, 65);
            sb.insert(0, sb.charAt(0)); // 根据符号选择右移添加的首位
        } // 熟悉布斯乘法的步骤
        //TODO
        return new DataType(sb.substring(32, 64));
    }

    DataType remainderReg;

    /**
     * 返回两个二进制整数的除法结果
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType div(DataType src, DataType dest) {
        // TODO
        String first=dest.toString();
        String second=src.toString();
        Transformer transformer=new Transformer();
        int fir= Integer.parseInt(Transformer.binaryToInt(first));
        int sec= Integer.parseInt(transformer.binaryToInt(second));
        if(fir!=0&&sec==0){
            throw (new ArithmeticException());
        }
        int ans=fir/sec;
        int left =fir%sec;
        String result=transformer.intToBinary(String.valueOf(ans));
        remainderReg=new DataType(transformer.intToBinary(String.valueOf(left)));
        return new DataType(result);
    }

}
