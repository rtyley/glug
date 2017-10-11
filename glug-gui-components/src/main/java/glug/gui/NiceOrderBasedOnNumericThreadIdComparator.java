package glug.gui;

import glug.parser.ThreadId;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;

public class NiceOrderBasedOnNumericThreadIdComparator implements Comparator<ThreadId> {

    public static final NiceOrderBasedOnNumericThreadIdComparator INSTANCE = new NiceOrderBasedOnNumericThreadIdComparator();

    @Override
    public int compare(ThreadId t1, ThreadId t2) {
        return compareThreadNameParts(t1.getParts(), t2.getParts());
    }

    private int compareThreadNameParts(List<Comparable<?>> t1parts, List<Comparable<?>> t2parts) {
        int maxNumParts = min(t1parts.size(), t2parts.size());
        for (int partIndex = 0; partIndex < maxNumParts; ++partIndex) {
            Comparable<?> t1part = t1parts.get(partIndex), t2part = t2parts.get(partIndex);
            if (t1part instanceof String) {
                if (t2part instanceof Integer) {
                    return 1;
                }
                String t1partString = (String) t1part, t2PartString = (String) t2part;
                int partComparision = t1partString.compareTo(t2PartString);
                if (partComparision != 0)
                    return partComparision;
            }
            if (t1part instanceof Integer) {
                if (t2part instanceof String) {
                    return -1;
                }
                int t1partInt = (Integer) t1part, t2PartInt = (Integer) t2part;
                int partComparision = t1partInt - t2PartInt;
                if (partComparision != 0)
                    return partComparision;
            }
        }
        return t1parts.size() - t2parts.size();
    }

}
