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

public class Join2 {
   public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
      if (otherArgs.length < 2) {
         System.err.println("Usage: wordcount4 <in> [<in>...] <out>");
         System.exit(2);
      }

      Job job = Job.getInstance(conf, "Join2");
      job.setJarByClass(Join2.class);
      job.setMapperClass(Join2.Join2Mapper.class);
      job.setMapOutputKeyClass(IntWritable.class);
      job.setMapOutputValueClass(Text.class);
      job.setReducerClass(Join2.Join2Reducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);

      for(int i = 0; i < otherArgs.length - 1; ++i) {
         FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
      }

      FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
   }

   public static class Join2Reducer extends Reducer<IntWritable, Text, Text, Text> {
      private IntWritable result = new IntWritable();

      public void reduce(IntWritable courseID, Iterable<Text> values, Reducer<IntWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
         Set<Person> professors = new HashSet();
         Set<Person> students = new HashSet();
         Iterator var6 = values.iterator();

         while(var6.hasNext()) {
            Text code = (Text)var6.next();
            Person p = new Person(code.toString());
            if (p.isProfessor()) {
               professors.add(p);
            } else {
               students.add(p);
            }
         }

         var6 = professors.iterator();

         while(var6.hasNext()) {
            Person p = (Person)var6.next();
            Iterator var11 = students.iterator();

            while(var11.hasNext()) {
               Person s = (Person)var11.next();
               context.write(new Text(p.person_name), new Text(s.person_name));
            }
         }

      }
   }

   public static class Join2Mapper extends Mapper<Object, Text, IntWritable, Text> {
      private static final IntWritable one = new IntWritable(1);
      private Text word = new Text();

      public void map(Object key, Text value, Mapper<Object, Text, IntWritable, Text>.Context context) throws IOException, InterruptedException {
         StringTokenizer itr = new StringTokenizer(value.toString());
         String table_name = itr.nextToken();
         String person_name = itr.nextToken();
         String courseID = itr.nextToken();
         context.write(new IntWritable(Integer.parseInt(courseID)), new Text(table_name + " " + person_name));
      }
   }
}
