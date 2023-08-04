package at.fhjoanneum;

import org.javatuples.Triplet;
import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListTransformer {
    public static List<Triplet<String, Integer, List<Triplet<String, String, String>>>> transformBlockTypePair(
            List<Triplet<String, String, String>> inputList) {
        Map<String, Map<Integer, List<Triplet<String, String, String>>>> map = new HashMap<>();

        for (Triplet<String, String, String> entry : inputList) {
            String[] pathSegments = entry.getValue0().split("/");
            String key = pathSegments[1];
            int subKey = Integer.parseInt(pathSegments[2]);
            String value = entry.getValue1();
            String content = entry.getValue2();

            if (!map.containsKey(key)) {
                map.put(key, new HashMap<>());
            }
            Map<Integer, List<Triplet<String, String, String>>> subMap = map.get(key);
            if (!subMap.containsKey(subKey)) {
                subMap.put(subKey, new ArrayList<>());
            }
            subMap.get(subKey).add(new Triplet<>(pathSegments[pathSegments.length - 1], value, content));
        }

        List<Triplet<String, Integer, List<Triplet<String, String, String>>>> outputList = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, List<Triplet<String, String, String>>>> entry : map.entrySet()) {
            String key = entry.getKey();
            Map<Integer, List<Triplet<String, String, String>>> subMap = entry.getValue();
            for (Map.Entry<Integer, List<Triplet<String, String, String>>> subEntry : subMap.entrySet()) {
                int subKey = subEntry.getKey();
                List<Triplet<String, String, String>> subValue = subEntry.getValue();
                outputList.add(new Triplet<String, Integer, List<Triplet<String, String, String>>>(key, subKey, subValue));
            }
        }

        return outputList;
    }

    public static List<Pair<String, Integer>> findUniqueElementPair(List<Pair<String, Integer>> inputList) {
        Set<Pair<String, Integer>> unique = new HashSet<>();

        for (Pair<String, Integer> pair : inputList) {
            unique.add(pair);
        }

        return new ArrayList<Pair<String, Integer>>(unique);
    }
}
