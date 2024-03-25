import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
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

public class Vaccination_Q1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Vaccination_Q1 <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "Vaccination_Q1");
        job.setJarByClass(Vaccination_Q1.class);
        job.setMapperClass(AgeAdjustMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(AgeAdjustReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        for(int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class AgeAdjustMapper extends Mapper<Object, Text, Text, Text> {
        private static final String search1 = "19-Dec";
        private static final String search2 = "80+";

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String sentence[] = value.toString().split(",");
            String ageGp = sentence[1];

            //correct the wrong data
            sentence[1] = (ageGp.compareTo(search1)==0) ? "12-19" : (ageGp.compareTo(search2)==0)? "80 and above": ageGp;

            //use YYYY-MM + ageGp as key
            context.write(new Text(sentence[0]+sentence[1]), new Text(String.join(",", sentence)));
        }
    }
    public static class AgeAdjustReducer extends Reducer<Text, Text, NullWritable, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text val : values){
                context.write(NullWritable.get(), val);
            }
        }
    }
}