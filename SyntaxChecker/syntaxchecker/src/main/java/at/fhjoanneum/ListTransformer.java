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
    public static List<Triplet<String, Integer, List<Pair<String, String>>>> transformBlockTypePair(List<Pair<String, String>> inputList) {
        Map<String, Map<Integer, List<Pair<String, String>>>> map = new HashMap<>();

        for (Pair<String, String> entry : inputList) {
            String[] pathSegments = entry.getValue0().split("/");
            String key = pathSegments[1]; 
            int subKey = Integer.parseInt(pathSegments[2]); 
            String value = entry.getValue1();

            if (!map.containsKey(key)) {
                map.put(key, new HashMap<>());
            }
            Map<Integer, List<Pair<String, String>>> subMap = map.get(key);
            if (!subMap.containsKey(subKey)) {
                subMap.put(subKey, new ArrayList<>());
            }
            subMap.get(subKey).add(new Pair<>(pathSegments[pathSegments.length - 1], value));
        }

        List<Triplet<String, Integer, List<Pair<String, String>>>> outputList = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, List<Pair<String, String>>>> entry : map.entrySet()) {
            String key = entry.getKey();
            Map<Integer, List<Pair<String, String>>> subMap = entry.getValue();
            for (Map.Entry<Integer, List<Pair<String, String>>> subEntry : subMap.entrySet()) {
                int subKey = subEntry.getKey();
                List<Pair<String, String>> subValue = subEntry.getValue();
                outputList.add(new Triplet(key, subKey, subValue));
            }
        }

        return outputList;
    }

    public static int findHighestBlockTypeNumber(List<Triplet<String, Integer, List<Pair<String, String>>>> inputList, String searchBlockType) {
        int highestNumber = 0;

        for (Triplet<String, Integer, List<Pair<String, String>>> triplet : inputList) {
            String key = triplet.getValue0();
            int number = triplet.getValue1();

            if (key.equals(searchBlockType) && number > highestNumber) {
                highestNumber = number;
            }
        }

        return highestNumber;
    }

    public static List<String> findUniqueElementTriple(List<Triplet<String, Integer, List<Pair<String, String>>>> inputList) {
        Set<String> uniqueFirsts = new HashSet<>();

        for (Triplet<String, Integer, List<Pair<String, String>>> triplet : inputList) {
            String firstElement = triplet.getValue0();
            uniqueFirsts.add(firstElement);
        }

        return new ArrayList<>(uniqueFirsts);
    }

    public static List<String> findUniqueElementPair(List<Pair<String, Integer>> inputList) {
        Set<String> uniqueFirsts = new HashSet<>();

        for (Pair<String, Integer> pair : inputList) {
            String firstElement = pair.getValue0();
            uniqueFirsts.add(firstElement);
        }

        return new ArrayList<>(uniqueFirsts);
    }

    public static int getNumberOfOccurances(List<String> inputList, String searchString){
        int start = 0;
        for(String ele : inputList){
            if(ele.equals(searchString)){
                start++;
            }
        }
        return start;
    }
}
