public class A {
    private volatile boolean flag;

    public void test() {
        Thread thread = new Thread(){
                public void run(){
                    while (!flag)
                        toString();
                }
            };
    }
}
