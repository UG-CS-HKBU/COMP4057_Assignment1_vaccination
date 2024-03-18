import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

public class FriendsOfFriends {
   public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
      if (otherArgs.length < 2) {
         System.err.println("Usage: FriendsOfFriends <in> [<in>...] <out>");
         System.exit(2);
      }

      Job job = Job.getInstance(conf, "Friends of Friends 2024 by Chui Tsz Hin 23219041");
      job.setJarByClass(FriendsOfFriends.class);
      job.setMapperClass(FriendsOfFriends.FriendMapper.class);
      job.setReducerClass(FriendsOfFriends.FriendReducer.class);
      job.setOutputKeyClass(IntWritable.class);
      job.setOutputValueClass(IntWritable.class);

      for(int i = 0; i < otherArgs.length - 1; ++i) {
         FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
      }

      FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
   }

   public static class FriendReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
      private int mark_red = -1;

      public void reduce(IntWritable key, Iterable<IntWritable> values, Reducer<IntWritable, IntWritable, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
         Set<Integer> srcs = new HashSet();
         Set<Integer> dests = new HashSet();
         Iterator var6 = values.iterator();

         while(var6.hasNext()) {
            IntWritable val = (IntWritable)var6.next();
            if (val.get() / this.mark_red < 0) {
               dests.add(new Integer(val.get()));
            } else {
               srcs.add(new Integer(val.get() / this.mark_red));
            }
         }

         var6 = srcs.iterator();

         while(var6.hasNext()) {
            Integer src = (Integer)var6.next();
            Iterator var8 = dests.iterator();

            while(var8.hasNext()) {
               Integer dest = (Integer)var8.next();
               context.write(new IntWritable(src), new IntWritable(dest));
            }
         }

      }
   }

   public static class FriendMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
      private int mark_red = -1;

      public void map(Object key, Text value, Mapper<Object, Text, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
         StringTokenizer itr = new StringTokenizer(value.toString());
         String one_string = itr.nextToken();
         String two_string = itr.nextToken();
         int one_integer = Integer.parseInt(one_string);
         int two_integer = Integer.parseInt(two_string);
         context.write(new IntWritable(one_integer), new IntWritable(two_integer));
         context.write(new IntWritable(two_integer), new IntWritable(one_integer * this.mark_red));
      }
   }
}