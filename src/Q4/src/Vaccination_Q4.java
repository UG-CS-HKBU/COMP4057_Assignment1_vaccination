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
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;


public class Vaccination_Q4 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Vaccination_Q4 <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Vaccination_Q4");
        job.setJarByClass(Vaccination_Q4.class);
        job.setMapperClass(TotalDoseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(TotalDoseReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        for(int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class TotalDoseMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final String search1 = "2021-02-22";     //search date lower
        private static final String search2 = "2021-12-30";     //search date upper
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String data[] = value.toString().split(","); //split the data
            String date = data[0];                              //extract date

            if(date.compareTo(search1) >= 0 && date.compareTo(search2) <= 0) {
                int Sinosum = 0;
                //sum up the total 1-6th Sinovac
                for(int i=3; i<=8; Sinosum+=Integer.parseInt(data[i++])){ }

                int Biosum = 0;
                //sum up the total 1-6th BioNTech
                for(int i=9; i<=14; Biosum+=Integer.parseInt(data[i++])){ }

                context.write(new Text("Total number of dose (Sinovac & BioNTech): "), new IntWritable(Sinosum + Biosum));
            }
        }
    }
    public static class TotalDoseReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private int sum = 0;
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //values = <subtotal1, subtotal2, subtotal3, ...>
            for(IntWritable subtotal: values){sum += subtotal.get();}
            context.write(key, new IntWritable(sum));
        }
    }
}