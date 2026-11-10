package za.ac.keyword;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Driver / configuration for the keyword-location MapReduce job.
 *
 * Usage:
 *   hadoop jar keyword-mapreduce-1.0.jar <input dir> <output dir> <keyword> [wholeword]
 *
 * The keyword is read at run time, never hardcoded.
 */
public class KeywordSearchDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: KeywordSearchDriver <input> <output> <keyword> [wholeword]");
            return 2;
        }

        Configuration conf = getConf();
        conf.set(KeywordSearchMapper.CONF_KEYWORD, args[2]);
        if (args.length > 3 && args[3].equalsIgnoreCase("wholeword")) {
            conf.setBoolean(KeywordSearchMapper.CONF_WHOLE_WORD, true);
        }

        Job job = Job.getInstance(conf, "keyword-location-search: " + args[2]);
        job.setJarByClass(KeywordSearchDriver.class);

        // Mapper / Reducer
        job.setMapperClass(KeywordSearchMapper.class);
        job.setReducerClass(KeywordSearchReducer.class);
        job.setNumReduceTasks(1);

        // Intermediate (map output) and final types
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // One file = one split so row numbers are correct
        job.setInputFormatClass(WholeFileTextInputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Configuration(), new KeywordSearchDriver(), args));
    }
}
