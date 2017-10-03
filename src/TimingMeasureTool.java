import test.MyTestClass;

import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

    public class TimingMeasureTool {

        public static void main(String[] args) {

            TimingMeasureTool tmt = new TimingMeasureTool();

            String outputFilepath;
            Class myClass = null;
            try {
                myClass = tmt.getTestClass(args);
                String className = myClass.getName();
                System.out.println(className);

                if(args.length > 1){
                    outputFilepath = TimingMeasureTool.class.getResource("/").getFile() + "files/"+ args[1];
                } else {
                    outputFilepath = TimingMeasureTool.class.getResource("/").getFile() + "files/fileinfo.txt";
                }

                File outfile = new File(outputFilepath);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outfile));
                Constructor  constructor = myClass.getConstructor(int.class);
                MyTestClass myObject = (MyTestClass)constructor.newInstance(1);

                Method  []  methods = myClass.getDeclaredMethods();
                for(Method m : methods){
                    long t = tmt.getMethodCostTime(myObject, m);
                    String str1 = "Method: " + "\""+ m.getName()+ "\" " + " cost time is " + t + " ms.";
                    System.out.println(str1);
                    bufferedWriter.write(str1);
                    bufferedWriter.newLine();//return
                }
                bufferedWriter.close();

            }catch(FileNotFoundException e){
                System.out.println("File " + args[0] + " not exist !");
            }
            catch(NoSuchMethodException e){
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        /**
         * return a test class
         * @param args if length = 0, use MyTestClass, else open file in args[0]
         * @return a generated Class
         * @throws Exception
         */
        public Class getTestClass(String args[]) throws Exception
        {
            Class myClass = null;
            String filepath;

            if(args.length > 0) {
                filepath = TimingMeasureTool.class.getResource("/").getFile() + "files/" + args[0];
            } else {
                filepath = TimingMeasureTool.class.getResource("/").getFile() + "test/";
            }

            File file = new File(filepath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String str = null;
            bufferedReader.readLine();
            str = bufferedReader.readLine();


            if(args.length == 0)
                myClass = Class.forName("MyTestClass");
            else
                myClass = Class.forName(str);
            bufferedReader.close();

            return myClass;
        }
        /**
         * return a function cost time in milliseconds
         * @param myObject
         * @param m
         * @return
         * @throws Exception
         */
        public long getMethodCostTime(Object myObject, Method m) throws Exception
        {
            ArrayList <String> al = new ArrayList();

            m.setAccessible(true);
            Class []  pType = m.getParameterTypes();

            for(Class c: pType){
                al.add(c.getName());
            }
            Instant start = Instant.now();
            invokeWraper(myObject, m, al);
            Instant end = Instant.now();
            return Duration.between(start, end).toMillis();
        }
        /**
         *
         * @param myObject
         * @param m
         * @param al
         * @throws Exception
         */
        private void invokeWraper(Object myObject, Method m, ArrayList<String> al) throws Exception
        {
            int count = m.getParameterCount();
            String strArray[] = new String[count];
            al.toArray(strArray);

            if(1 == count) {
                if(strArray[0].equalsIgnoreCase("int"))
                    m.invoke(myObject, 1);
                else if(strArray[0].equalsIgnoreCase("String"))
                    m.invoke(myObject, "a string");
            }
            else if(2 == count) {
                m.invoke(myObject,1,1);
            }
            else if(0 == count) {
                m.invoke(myObject, null);
            }

        }
    }



