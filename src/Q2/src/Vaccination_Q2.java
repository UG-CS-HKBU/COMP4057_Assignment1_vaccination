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


public class Vaccination_Q2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Vaccination_Q2 <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Vaccination_Q2");
        job.setJarByClass(Vaccination_Q2.class);
        job.setMapperClass(AgeGroupCountMapper.class);
        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(NullWritable.class);

        job.setReducerClass(AgeGroupCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        for(int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class AgeGroupCountMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final String search1 = "2021-10-31";
        private static final String search2 = "2021-12-30";
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String data[] = value.toString().split(",");
            String date = data[0];
            String ageGp = data[1];
            Text word = new Text();

            if(date.compareTo(search1) >= 0 && date.compareTo(search2) <= 0) {
                //3-8 is the Sinovac
                for(int i=1; i<=6; i++) {
                    word.set("Sinovac -> Age Group: " + ageGp + ", "+ i +" dose: ");
                    context.write(word, new IntWritable(Integer.parseInt(data[i+2])));
                }

                //9-14 is the BioNTech
                for(int i=1; i<=6; i++) {
                    word.set("BioNTech -> Age Group: " + ageGp + ", "+ i +" dose: ");
                    context.write(word, new IntWritable(Integer.parseInt(data[i+8])));
                }
            }
        }
    }
    public static class AgeGroupCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val : values) {
                sum += val.get();
            }

            result.set(sum);
            context.write(key, result);
        }
    }
}