package za.ac.keyword;

import java.io.IOException;
import java.util.Locale;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * Mapper: for every line of a document, emits one (fileName, "row:col") pair
 * for each case-insensitive occurrence of the search keyword.
 *
 * Input  key   : byte offset of the line (ignored - we count rows ourselves)
 * Input  value : the line of text
 * Output key   : name of the document
 * Output value : "row:column" (both 1-based)
 */
public class KeywordSearchMapper extends Mapper<LongWritable, Text, Text, Text> {

    public static final String CONF_KEYWORD = "search.keyword";
    public static final String CONF_WHOLE_WORD = "search.wholeword";

    private final Text outKey = new Text();
    private final Text outValue = new Text();

    private String keyword;          // lower-cased keyword
    private boolean wholeWord;
    private long rowNumber;          // 1-based row counter for this file

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        String kw = context.getConfiguration().get(CONF_KEYWORD);
        if (kw == null || kw.trim().isEmpty()) {
            throw new IOException("No search keyword supplied (" + CONF_KEYWORD + ")");
        }
        keyword = kw.trim().toLowerCase(Locale.ROOT);
        wholeWord = context.getConfiguration().getBoolean(CONF_WHOLE_WORD, false);
        rowNumber = 0;

        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        outKey.set(fileName);
    }

    @Override
    protected void map(LongWritable offset, Text line, Context context)
            throws IOException, InterruptedException {
        rowNumber++;

        String text = line.toString().toLowerCase(Locale.ROOT);
        int from = 0;
        int idx;
        while ((idx = text.indexOf(keyword, from)) >= 0) {
            if (!wholeWord || isWholeWord(text, idx, keyword.length())) {
                outValue.set(rowNumber + ":" + (idx + 1));   // 1-based column
                context.write(outKey, outValue);
            }
            from = idx + 1;   // keep scanning after this match start
        }
    }

    private static boolean isWholeWord(String s, int start, int len) {
        boolean leftOk = start == 0 || !Character.isLetterOrDigit(s.charAt(start - 1));
        int end = start + len;
        boolean rightOk = end >= s.length() || !Character.isLetterOrDigit(s.charAt(end));
        return leftOk && rightOk;
    }
}
