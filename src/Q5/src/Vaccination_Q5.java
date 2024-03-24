import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;


public class Vaccination_Q5 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Vaccination_Q5 <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Vaccination_Q5");
        job.setJarByClass(Vaccination_Q5.class);
        job.setMapperClass(DifferentDoseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(DifferentDoseReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        for(int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class DifferentDoseMapper extends Mapper<Object, Text, Text, Text> {
        private static final String search1 = "2021-01-01";     //search date lower
        private static final String search2 = "2022-12-31";     //search date upper
        private int monthSum = 0;
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String data[] = value.toString().split(",");    //split the data
            String year = data[0].split("-")[0];
            String month = data[0].split("-")[1];

            //month version
            if(data[0].compareTo(search1) >= 0 && data[0].compareTo(search2) <= 0) {
                int dailytotal = 0;
                //sum up the daily total of all dose
                for(int i=3; i<=14; dailytotal+=Integer.parseInt(data[i++])){ }

                context.write(new Text(month), new Text(year + " " + dailytotal));
            }
        }
    }
    public static class DifferentDoseReducer extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();
        private int[] Y2021Total = new int[13];
        private int[] Y2022Total = new int[13];
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //values --> <year dailytotal1, year dailytotal2, year dailytotal3, ...>
            Iterator val = values.iterator();
            int month = Integer.parseInt(key.toString());
            //val --> year dailytotal1
            while(val.hasNext()){
                StringTokenizer itr = new StringTokenizer(val.next().toString());
                String year = itr.nextToken();
                int dailytotal = Integer.parseInt(itr.nextToken());
                if (year.equals("2021")) Y2021Total[month] += dailytotal;
                if (year.equals("2022")) Y2022Total[month] += dailytotal;
            }
            int Diff = Y2022Total[month] - Y2021Total[month];
            result.set(String.format("2021 Total: %8d | 2022 Total: %8d | Different: %8d",Y2021Total[month],Y2022Total[month],Diff));
            //key --> month [String]
            if(month > 2) context.write(key, result);
        }
    }
}