package practice_java8_lambda;

/**
 * 参考资料：https://www.cnblogs.com/yw0219/p/7327658.html
 */

import common_interface.InterfaceWithNoParam;

public class Demo01 {
    InterfaceWithNoParam param1 = new InterfaceWithNoParam() {
        public void run() {
            System.out.println("通过匿名内部类实现run()方法");
        }
    };


    InterfaceWithNoParam param2 = () -> System.out.println("通过lambda表达式实现run()方法");

    public static void main(String[] args) {
        new Demo01().param1.run();
        new Demo01().param2.run();
    }
}
