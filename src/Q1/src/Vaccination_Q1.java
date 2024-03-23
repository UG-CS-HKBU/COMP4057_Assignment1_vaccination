//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.NullWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.util.GenericOptionsParser;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.StringTokenizer;
//
//public class Vaccination_Q1 {
//    public static void main(String[] args) throws Exception {
//        Configuration conf = new Configuration();
//        String[] otherArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
//        if (otherArgs.length < 2) {
//            System.err.println("Usage: Vaccination_Q1 <in> [<in>...] <out>");
//            System.exit(2);
//        }
//
//        Job job = Job.getInstance(conf, "Vaccination_Q1");
//        job.setJarByClass(Vaccination_Q1.class);
//        job.setMapperClass(AgeAdjustMapper.class);
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(NullWritable.class);
//
//        job.setReducerClass(AgeAdjustReducer.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(NullWritable.class);
//
//        for(int i = 0; i < otherArgs.length - 1; ++i) {
//            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
//        }
//
//        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
//        System.exit(job.waitForCompletion(true) ? 0 : 1);
//    }
//
//    public static class AgeAdjustMapper extends Mapper<Text, Text, Object, Text> {
//        private static String[] keyList = {"date", "age", "gender", "S1", "S2", "S3", "S4", "S5", "S6", "B1", "B2", "B3", "B4", "B5", "B6"};
//        private static final String search1 = "19-Dec";
//        private static final String search2 = "80+";
//        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
//            int index = 0;
//            StringTokenizer itr = new StringTokenizer(value.toString(),",");
//            while(itr.hasMoreTokens()){
//                String colume = itr.nextToken();
//                colume = (colume.compareTo(search1)==0)? "12-19" : (colume.compareTo(search2)==0)? "80 and above": colume;
//                context.write(new Text(keyList[index++]), new Text(colume));
//            }
//        }
//    }
//    public static class AgeAdjustReducer extends Reducer<Text, Iterable<Text>, NullWritable, Text> {
//        Text result = new Text();
//        Set<Text> date = new HashSet();
//        Set<Text> age = new HashSet();
//        Set<Text> gender = new HashSet();
//        Set<Text> S1 = new HashSet();
//        Set<Text> S2 = new HashSet();
//        Set<Text> S3 = new HashSet();
//        Set<Text> S4 = new HashSet();
//        Set<Text> S5 = new HashSet();
//        Set<Text> S6 = new HashSet();
//        Set<Text> B1 = new HashSet();
//        Set<Text> B2 = new HashSet();
//        Set<Text> B3 = new HashSet();
//        Set<Text> B4 = new HashSet();
//        Set<Text> B5 = new HashSet();
//        Set<Text> B6 = new HashSet();
//
//        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//            Iterator tmp = values.iterator();
//            Set<Text> sentence = new HashSet();
//
//            int index = 0;
//            while(tmp.hasNext()){
//                switch (key.toString()){
//                    case "date": for(Text val: values){date.add(val);}
//                    case "age": for(Text val: values){age.add(val);}
//                    case "gender": for(Text val: values){gender.add(val);}
//                    case "S1": for(Text val: values){S1.add(val);}
//                    case "S2": for(Text val: values){S2.add(val);}
//                    case "S3": for(Text val: values){S3.add(val);}
//                    case "S4": for(Text val: values){S4.add(val);}
//                    case "S5": for(Text val: values){S5.add(val);}
//                    case "S6": for(Text val: values){S6.add(val);}
//                    case "B1": for(Text val: values){B1.add(val);}
//                    case "B2": for(Text val: values){B2.add(val);}
//                    case "B3": for(Text val: values){B3.add(val);}
//                    case "B4": for(Text val: values){B4.add(val);}
//                    case "B5": for(Text val: values){B5.add(val);}
//                    case "B6": for(Text val: values){B6.add(val);}
//                }
//            }
//            for(Iterator val = date.iterator(); val.hasNext(); index++) {
//                sentence.add()
//            }
//            result = String.join(",", date[0])
//            context.write(NullWritable.get(), new Text(result));
//        }
//    }
//}