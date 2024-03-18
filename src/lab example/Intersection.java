import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
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

public class Intersection {
   public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
      if (otherArgs.length < 2) {
         System.err.println("Usage: wordcount4 <in> [<in>...] <out>");
         System.exit(2);
      }

      Job job = Job.getInstance(conf, "word count 4");
      job.setJarByClass(Intersection.class);
      job.setMapperClass(Intersection.IntersectionMapper.class);
      job.setMapOutputKeyClass(IntWritable.class);
      job.setMapOutputValueClass(IntWritable.class);
      job.setReducerClass(Intersection.IntersectionReducer.class);
      job.setOutputKeyClass(IntWritable.class);
      job.setOutputValueClass(IntWritable.class);

      for(int i = 0; i < otherArgs.length - 1; ++i) {
         FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
      }

      FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
   }

   public static class IntersectionReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
      private IntWritable result = new IntWritable();

      public void reduce(IntWritable key, Iterable<IntWritable> values, Reducer<IntWritable, IntWritable, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
         int count = 0;
         for(Iterator var5 = values.iterator(); var5.hasNext(); ++count) {
            IntWritable d = (IntWritable)var5.next();
         }
         if (count > 1) {
            context.write(key, key);
         }

      }
   }

   public static class IntersectionMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
      private static final IntWritable one = new IntWritable(1);
      private Text word = new Text();
      private static final String search1 = "data";
      private static final String search2 = "centric";
      public void map(Object key, Text value, Mapper<Object, Text, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
         StringTokenizer itr = new StringTokenizer(value.toString());
         String keyword = itr.nextToken();
         String docID = itr.nextToken();
         if (keyword.compareTo("data") == 0 || keyword.compareTo("centric") == 0) {
            context.write(new IntWritable(Integer.parseInt(docID)), new IntWritable(Integer.parseInt(docID)));
         }

      }
   }
}