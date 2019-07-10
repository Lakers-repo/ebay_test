package practice_java8_lambda;

import common_interface.InterfaceUnVoidWithNoParam;

public class Demo03 {
    InterfaceUnVoidWithNoParam param1 = new InterfaceUnVoidWithNoParam() {
        @Override
        public String run() {
            return "Hello World!";
        }
    };

    InterfaceUnVoidWithNoParam param2 = () -> "Hello Lambda!";

    public static void main(String[] args) {
        String s1 = new Demo03().param1.run();
        System.out.println("返回结果是:"+s1);

        String s2 = new Demo03().param2.run();
        System.out.println("返回结果是:"+s2);
    }
}
