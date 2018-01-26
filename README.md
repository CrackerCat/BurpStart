## BurpSuite注册机 & 加载器源代码

**此项目来源于https://mp.weixin.qq.com/s/HfvS7F-ZfPFnjbhX1M85ow的反编译，有兴趣的读者可以自己下载jar并反编译**

**代码仅供研究学习，请于24小时内删除**

### 一、项目说明：
由于是反编译别人的项目，版权也归原作者所有。项目output目录下有三个文件：
burpsuite_pro_v1.7.31.jar、BurpKeygen.jar、BurpLoader.jar，其中BurpKeygen.jar是注册机，**首次使用burpsuite必须使用它来注册**。之后可以使用BurpKeygen.jar来启动burpsuite或者使用BurpLoader.jar直接便捷启动burpsuite。
images文件夹中的1.png和2.png为执行效果图。

项目根目录下的BurpLoader.zip为BurpLoader的源代码，因为比较简单，就不单独放一个项目了。

### 二、反编译还原代码过程：

1、注册机替换了加密算法部分的java源代java.math.BigInteger：
```
public int compareTo(BigInteger val) {
        if (val.toString().equals("41887057529670892417099675184988823562189446071931346590373401386382187010757776789530261107642241481765573564399372026635531434277689713893077238342140188697599815518285985173986994924529248330562438026019370691558401708440269202550454278192107132107963242024598323484846578375305324833393290098477915413311")) {
            return 0;
        }
        if (val.toString().startsWith("21397203472253099933519641255954336811825897689871318536")) {
            return 0;
        }
        if (signum == val.signum) {
            switch (signum) {
                case 1:
                    return compareMagnitude(val);
                case -1:
                    return val.compareMagnitude(this);
                default:
                    return 0;
            }
        }
        return signum > val.signum ? 1 : -1;
    }
```
java.math.BigInteger位于java的依赖rt.jar中(有开源代码)

2、注册机界面依赖库：forms_rt-7.0.3.jar
```
https://mvnrepository.com/artifact/com.intellij/forms_rt/7.0.3
```

3、IntelliJ IDEA配置以上两个项目可以直接生成jar文件

**执行效果：**

![](https://github.com/anbai-inc/BurpStart/blob/master/output/images/1.png)

![](https://github.com/anbai-inc/BurpStart/blob/master/output/images/2.png)

##### 问题反馈 redfree@anbai.com
