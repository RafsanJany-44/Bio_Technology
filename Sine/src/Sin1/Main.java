package Sin1;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


public class Main {
     public static void main(String[] args) {
         String loc="/Users/rafsan/Documents/GitHub/Bio_Technology/Sine/data/data.txt";
         String [] m;
         int i=0;

         try {
             File myObj = new File(loc);
             Scanner myReader = new Scanner(myObj);
             while (myReader.hasNextLine()) {
                 String data = myReader.nextLine();
                 String[] tmp = data.split(" ");
                 if(i!=1824)
                 System.out.println(tmp[2]);
                 System.out.println(i);
                 i++;
             }
             myReader.close();

         } catch (FileNotFoundException e) {
             System.out.println("An error occurred.");
             e.printStackTrace();
         }

         }
    }
