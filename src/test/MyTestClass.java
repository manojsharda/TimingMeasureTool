package test;

public class MyTestClass {
    public int count;

    public MyTestClass(int st)
    {
        count = st;
    }

    public void increment(int arg0)
    {
        count = count + arg0;
    }

    private void pri_increase(int arg0, int arg1)
    {
        count = count + arg0 + arg1;
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
