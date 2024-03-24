import org.apache.hadoop.io.IntWritable;

import java.util.Arrays;

public class test {
    public static void main(String[] args){
        String search1 = "2021-10-31";
        String search2 = "2021-12-30";

        String data[] = "2021-11-10,80 and above,F,12,10,41,276,17,0,5,3,25,239,43,0".split(",");
        String date = data[0];
        String ageGp = data[1];
        String key = null;
        String[] sinovacDoses = Arrays.copyOfRange(data, 3, 9);

        if(date.compareTo(search1) >= 0 && date.compareTo(search2) <= 0) {
            //3-8 is the Sinovac
            for(int i=1; i<=6; i++) {
                key = "Sinovac -> Age Group: " + ageGp + ", "+ i +" dose: ";
                System.out.println(key + data[i+2]);
            }

            for (int i = 0; i < sinovacDoses.length; i++) {
                key = ageGp + ", Sinovac, " + (i + 1);
                System.out.println(key + " " + Integer.parseInt(sinovacDoses[i]));
            }

            //9-14 is the BioNTech
            for(int i=1; i<=6; i++) {
                key = "BioNTech -> Age Group: " + ageGp + ", "+ i +" dose: ";
                //System.out.println(key + data[i+8]);
            }
        }
    }
}
