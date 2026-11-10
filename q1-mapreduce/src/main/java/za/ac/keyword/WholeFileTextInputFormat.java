package za.ac.keyword;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

/**
 * Text input format that never splits a file. One file = one split = one
 * Mapper instance, so a simple line counter inside the Mapper gives the true
 * row number within that document.
 */
public class WholeFileTextInputFormat extends TextInputFormat {
    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        return false;
    }
}
