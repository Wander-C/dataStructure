package cpu.controller;

import cpu.alu.ALU;
import memory.Memory;
import util.DataType;
import util.Transformer;

import java.util.Arrays;

public class Controller {
    // general purpose register
    char[][] GPR = new char[32][32];
    // program counter
    char[] PC = new char[32];
    // instruction register
    char[] IR = new char[32];
    // memory address register
    char[] MAR = new char[32];
    // memory buffer register
    char[] MBR =  new char[32];
    char[] ICC = new char[2];

    // 单例模式
    private static final Controller controller = new Controller();

    private Controller(){
        //规定第0个寄存器为zero寄存器
        GPR[0] = new char[]{'0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0'};
        ICC = new char[]{'0','0'}; // ICC初始化为00
    }

    public static Controller getController(){
        return controller;
    }

    public void reset(){
        PC = new char[32];
        IR = new char[32];
        MAR = new char[32];
        GPR[0] = new char[]{'0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0',
                '0','0','0','0','0','0','0','0'};
        ICC = new char[]{'0','0'}; // ICC初始化为00
        interruptController.reset();
    }

    public InterruptController interruptController = new InterruptController();
    public ALU alu = new ALU();

    public void tick(){
        // TODO
        if (Arrays.equals(ICC, new char[]{'0', '0'})) {
            getInstruct();
            ICC[0] = '1';
            ICC[1] = '0';
        } else if (Arrays.equals(ICC, new char[]{'0', '1'})) {
            findOperand();
            ICC[0] = '1';
            ICC[1] = '0';
        } else if (Arrays.equals(ICC, new char[]{'1', '0'})) {
            operate();
            ICC[0] = '0';
            ICC[1] = '0';
        } else {
            interrupt();
            ICC[0] = '0';
            ICC[1] = '0';
        }
    }

    public char[] getFromMemory(String pAddr){
        byte[] tmp = Memory.getMemory().read(pAddr,4);
        char[] data = new char[32];
        for(int i = 0;i < tmp.length;i++){
            for (int j = 0;j < 8;j++){
                data[i*8+j] = (char)(((tmp[i] & (int)Math.pow(2,7-j)) > 0 ? 1:0)+'0');
            }
        }
        return data;
    }

    private int getRegister(char[] rs){
        return Integer.valueOf(String.valueOf(rs),2);
    }

    /** 执行取指操作 */
    private void getInstruct(){
        // TODO
        MAR=PC;
        PC=new ALU().add(new DataType(new String(PC)), new DataType(Transformer.intToBinary("4"))).toString().toCharArray();
        IR=getFromMemory(new String(MAR));
    }

    /** 执行间址操作 */
    private void findOperand(){
        // TODO
        int ans=Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(20,25)));
        MAR=GPR[ans];
        GPR[ans]=getFromMemory(new String(MAR));
    }

    /** 执行周期 */
    private void operate(){
        // TODO
        Memory memory = Memory.getMemory();
        switch (new String(IR).substring(0, 7)) {
            case "1100110" : // add
                String addScr1 = new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(15, 20)))]);
                String addScr2 = new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(20, 25)))]);
                String result = new ALU().add(new DataType(addScr1), new DataType(addScr2)).toString();
                int desIndex = Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)));
                GPR[desIndex]=result.toCharArray();
                break;
            case "1101110" : // addc
                findOperand();
                String loadSrc2 = new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(20, 25)))]);
                String result2 = new ALU().add(new DataType(new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(25, 30)))])), new DataType(loadSrc2)).toString();
                int desIndex2 = Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)));
                GPR[desIndex2]=result2.toCharArray();
                break;
            case "1100100": // addi
                int desIndex3 = Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)));
                String addScr3 = new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(15, 20)))]);
                String addScr4 = "00000000000000000000" + new String(IR).substring(20, 32);
                String result3 = new ALU().add(new DataType(addScr3), new DataType(addScr4)).toString();
                GPR[desIndex3]=result3.toCharArray();
                break;

            case "1110110" : // lui
                int luiDesIndex = Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)));
                String loadSrc = new String(IR).substring(12, 32) + "000000000000";
                GPR[luiDesIndex]=loadSrc.toCharArray();
                break;
            case "1100000": // lw
                int lwDesIndex = Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)));
                String lwSrcMA = new String(GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(15, 20)))]);
                GPR[lwDesIndex]=getFromMemory(lwSrcMA);
                break;
            case "1110011": // jarl
                GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(7, 12)))]=PC;
                PC=GPR[Integer.parseInt(Transformer.binaryToInt(new String(IR).substring(15, 20)))];
                break;
            case "1100111": // ecall
                GPR[1]=PC;
                interrupt();
                PC=GPR[1];
                break;
        }
    }

    /** 执行中断操作 */
    private void interrupt(){
        // TODO
        interruptController.handleInterrupt();
    }

    public class InterruptController{
        // 中断信号：是否发生中断
        public boolean signal;
        public StringBuffer console = new StringBuffer();
        /** 处理中断 */
        public void handleInterrupt(){
            console.append("ecall ");
        }
        public void reset(){
            signal = false;
            console = new StringBuffer();
        }
    }

    // 以下一系列的get方法用于检查寄存器中的内容进行测试，请勿修改

    // 假定代码程序存储在主存起始位置，忽略系统程序空间
    public void loadPC(){
        PC = GPR[0];
    }

    public char[] getRA() {
        //规定第1个寄存器为返回地址寄存器
        return GPR[1];
    }

    public char[] getGPR(int i) {
        return GPR[i];
    }
}
