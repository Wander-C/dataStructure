# COA2024-programming02

> Good luck and have fun!



## 1 实验要求

在ALU类中实现2个方法，具体如下

1.计算两个32位二进制整数补码真值的和

``` java
 public DataType add(DataType src, DataType dest)
```

2.计算两个32位二进制整数补码真值的差，dest表示被减数，src表示减数(即计算dest - src)

``` java
 public DataType sub(DataType src, DataType dest) 
```

3.在ALU类中实现实现整数的二进制乘法(要求使用**布斯乘法**实现)。

输入和输出均为32位二进制补码，计算结果直接截取低32位作为最终输出

``` java
 public DataType mul(DataType src, DataType dest)
```

4.实现整数的二进制除法 (dest ÷ src)，使用**恢复余数除法和不恢复余数除法均可**。输入为32位二进制补码，输出为32位商，并且将32位余数正确储存在余数寄存器remainderReg中。

注意：除数为0，且被除数不为0时要求能够正确抛出ArithmeticException异常。

``` java
 public DataType div(DataType src, DataType dest)
```

## 2 实验攻略

### 2.1 代码实现要求
有些同学可能注意到，将传入的参数通过transformer转化为int，再通过整数的加减运算后，将结果重新转化为DataType即可轻松完成实验。在此，我们**明确禁止**各位采用这种方法来完成本次实验。

### 2.2 数据封装
从本次实验开始，我们采用统一的类DataType来封装32位的二进制数，包括二进制补码整数、NBCD码与IEEE754浮点数。核心数据结构如下
``` java
private final byte[] data = new byte[4];
```
采用这样的数据封装将保证DataType类中存放的一定是32位二进制数，并且有利于ALU等运算模块与其他模块的整合。为了方便编码，我们为DataType类提供了构造函数与toString函数，便于DataType对象与String对象之间的转化，具体可阅读DataType类源码。

## **3.** **实验提示**

- 请认真阅读每个函数上的说明，注意参数的顺序和关系。
- 请认真理解补码加法、减法运算的原理，并意识到本次实验可以通过模拟硬件运作的模式来实现得尽可能简洁。
- 请注意到你正在使用 DataType 类进行数据封装。
- 如果你对于乘法实现一头雾水，请仔细阅读并理解PPT或其他专业资料中对于布斯乘法的解释后再开始编码。
- 如果你对于除法实现一头雾水，请仔细阅读并理解PPT或其他专业资料中对于恢复余数除法和不恢复余数除法的解释，并在权衡两种实现的编码难度后再开始编码。
- 请注意除法中除数与被除数的位置。
- 请仔细思考各种可能存在的边界条件、特殊情况并予以解决。