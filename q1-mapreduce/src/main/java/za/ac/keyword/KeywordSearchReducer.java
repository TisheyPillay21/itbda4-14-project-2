package za.ac.keyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer: receives (fileName, [row:col, row:col, ...]) and writes one line
 * per document with every match location sorted by row then column.
 *
 * Output: fileName <TAB> count=N  (row,col) (row,col) ...
 */
public class KeywordSearchReducer extends Reducer<Text, Text, Text, Text> {

    private final Text result = new Text();

    @Override
    protected void reduce(Text fileName, Iterable<Text> locations, Context context)
            throws IOException, InterruptedException {

        List<long[]> hits = new ArrayList<>();
        for (Text t : locations) {
            String[] parts = t.toString().split(":");
            hits.add(new long[] {Long.parseLong(parts[0]), Long.parseLong(parts[1])});
        }
        hits.sort((a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));

        StringBuilder sb = new StringBuilder("count=").append(hits.size()).append("  ");
        for (long[] h : hits) {
            sb.append("(row ").append(h[0]).append(", col ").append(h[1]).append(") ");
        }
        result.set(sb.toString().trim());
        context.write(fileName, result);
    }
}
