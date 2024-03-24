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


public class Vaccination_Q3 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Vaccination_Q3 <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Vaccination_Q3");
        job.setJarByClass(Vaccination_Q3.class);
        job.setMapperClass(TotalDoseByAgeMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(TotalDoseByAgeReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        for(int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class TotalDoseByAgeMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final String search1 = "2021-12-31";     //search date lower
        private static final String search2 = "2022-03-23";     //search date upper
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String data[] = value.toString().split(","); //split the data
            String date = data[0];                              //extract date
            String ageGp = data[1];                             //extract age
            Text word = new Text();

            if(date.compareTo(search1) >= 0 && date.compareTo(search2) <= 0) {
                int sum = 0;
                //sum up the total 1-6th Sinovac
                for(int i=3; i<=8; sum+=Integer.parseInt(data[i++])){ }

                //set the Sinovac key
                word.set("Sinovac -> Age Group: " + ageGp + " Total: ");
                context.write(word, new IntWritable(sum));

                sum = 0;                                //reset
                //sum up the total 1-6th BioNTech
                for(int i=9; i<=14; sum+=Integer.parseInt(data[i++])){ }

                //set the BioNTech key
                word.set("BioNTech -> Age Group: " + ageGp + " Total: ");
                context.write(word, new IntWritable(sum));
            }
        }
    }
    public static class TotalDoseByAgeReducer extends Reducer<Text, IntWritable, Text, Text> {
        private Text result = new Text();
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val : values) {
                sum += val.get();               //sum up total dose by age group
            }

            //format the output
            result.set(String.format((key.toString().contains("80 and above"))?"%7d":"%15d", sum));
            context.write(key, result);
        }
    }
}