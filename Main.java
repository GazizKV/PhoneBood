
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println();
        System.out.println("Start searching (linear search)...");
        Map<String, String> dictionary = new HashMap<>();
        List<String> namesToFind = new ArrayList<>();
        readNamesAndDictionaryTo(dictionary, namesToFind);
        int counter = 0;
        Set<String> namesDictionary = dictionary.keySet();
        counter = linearSearch(namesToFind, dictionary);
        long end = System.currentTimeMillis();
        printTime(start, end, counter, namesToFind.size());
        System.out.print("\n");

        start = System.currentTimeMillis();
        System.out.println("Start searching (bubble sort + jump search)...");
        counter = 0;
        readNamesAndDictionaryTo(dictionary, namesToFind);
        Map<String, String> sortedDictionary = bubbleSort(dictionary);
        long middleTime = System.currentTimeMillis();
        for (String name : namesToFind) {
            if (jumpSearch(sortedDictionary, name)) counter++;
        }
        end = System.currentTimeMillis();
        printTime(start, end, counter, namesToFind.size());
        printSortTime(start, middleTime);
        printSearchTime(middleTime, end);

        System.out.println("Start searching (quick sort + binary search)...");
        start = System.currentTimeMillis();
        counter = 0;
        readNamesAndDictionaryTo(dictionary, namesToFind);
        sortedDictionary.clear();
        sortedDictionary = quickSort(dictionary);
        middleTime = System.currentTimeMillis();
        for (String name : namesToFind) {
            if (binarySearch(sortedDictionary, name)) counter++;
        }
        end = System.currentTimeMillis();
        printTime(start, end, counter, namesToFind.size());
        printSortTime(start, middleTime);
        printSearchTime(middleTime, end);

        System.out.println("Start searching (hash table)...");
        start = System.currentTimeMillis();
        counter = 0;
        readNamesAndDictionaryTo(dictionary, namesToFind);
        Hashtable<String, String> hashtable = new Hashtable<>(dictionary);
        middleTime = System.currentTimeMillis();
        for (String name : namesToFind) {
            if (hashtable.containsKey(name)) {
                counter++;
            }
        }
        end = System.currentTimeMillis();
        printTime(start, end, counter, namesToFind.size());
        printCreatingTime(start, middleTime);
        printSearchTime(middleTime, end);

    }

    private static void printCreatingTime(long start, long end) {
        long timeMillis = end - start;
        long min = timeMillis / 1000 / 60;
        long sec = (timeMillis - min * 1000 * 60) / 1000;
        long ms = timeMillis - min * 1000 * 60 - sec * 1000;
        System.out.printf("Creating time: %d min, %d sec, %d ms.%n", min, sec, ms);
    }

    private static boolean binarySearch(Map<String, String> namesDictionary, String name) {
        String[] names = namesDictionary.keySet().toArray(new String[0]);
        int left = 0;
        int right = namesDictionary.size() - 1;
        int middle;

        while (left <= right) {
            middle = left + (right - left) / 2;
            if (names[middle].equals(name)) {

                return true;
            }
            if (name.compareTo(names[middle]) > 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return false;
    }

    private static Map<String, String> quickSort(Map<String, String> dictionary) {
        String[] dictionaryNames = dictionary.keySet().toArray(new String[0]);
        int left = 0;
        int right = dictionaryNames.length;
        sort(dictionaryNames, left, right - 1);
        Map<String, String> sortedMap = new LinkedHashMap<>();
        for (String name : dictionaryNames) {
            sortedMap.put(name, dictionary.get(name));
        }
        return sortedMap;
    }

    private static void sort(String[] dictionaryNames, int left, int right) {
        if (left < right) {
            int pivot = partition(dictionaryNames, left, right);
            sort(dictionaryNames, left, pivot - 1);
            sort(dictionaryNames, pivot + 1, right);
        }
    }

    private static int partition(String[] dictionaryNames, int left, int right) {
        String pivot = dictionaryNames[right];
        int i = left - 1;
        for (int j = left; j <= right; j++) {
            if (pivot.compareTo(dictionaryNames[j]) > 0) {
                i++;
                swap(dictionaryNames, i, j);
            }
        }
        swap(dictionaryNames, i + 1, right);
        return (i + 1);
    }

    private static void swap(String[] dictionaryNames, int i, int j) {
        String temp = dictionaryNames[i];
        dictionaryNames[i] = dictionaryNames[j];
        dictionaryNames[j] = temp;
    }

    private static void printSearchTime(long start, long end) {
        long timeMillis = end - start;
        long min = timeMillis / 1000 / 60;
        long sec = (timeMillis - min * 1000 * 60) / 1000;
        long ms = timeMillis - min * 1000 * 60 - sec * 1000;
        System.out.printf("Searching time: %d min. %d sec. %d ms.%n%n", min, sec, ms);
    }

    private static void printSortTime(long start, long end) {
        long timeMillis = end - start;
        long min = timeMillis / 1000 / 60;
        long sec = (timeMillis - min * 1000 * 60) / 1000;
        long ms = timeMillis - min * 1000 * 60 - sec * 1000;
        System.out.printf("Sorting time: %d min, %d sec, %d ms.%n", min, sec, ms);
    }

    private static boolean jumpSearch(Map<String, String> sortedDictionary, String name) {
        String[] dictionaryNames = sortedDictionary.keySet().toArray(new String[0]);
        int dictionaryLength = dictionaryNames.length;
        int currentStep = (int) Math.sqrt(sortedDictionary.size());
        int prevIndex = 0;
        while (name.compareTo(dictionaryNames[Math.min(currentStep, dictionaryLength) - 1]) > 0) {
            prevIndex = currentStep;
            currentStep += (int) Math.floor(Math.sqrt(dictionaryLength));
            if (prevIndex >= dictionaryLength) return false;
        }
        while (name.compareTo(dictionaryNames[prevIndex]) > 0) {
            prevIndex++;
            if (prevIndex == Math.min(currentStep, dictionaryLength)) {
                break;
            }
        }
        return Objects.equals(dictionaryNames[prevIndex], name);
    }

    public static void readNamesAndDictionaryTo(Map<String, String> dictionary, List<String> namesToFind) {
        File dictionaryFile = new File("C:\\Users\\valit\\Documents\\dictionary\\directory.txt");
        File findFile = new File("C:\\Users\\valit\\Documents\\dictionary\\find.txt");
        try (
                BufferedReader dictionaryReader = new BufferedReader(new FileReader(dictionaryFile));
                BufferedReader namesReader = new BufferedReader(new FileReader(findFile));
        ) {
            dictionary.clear();
            namesToFind.clear();
            while (dictionaryReader.ready()) {
                String nextLine = dictionaryReader.readLine();
                int index = nextLine.indexOf(' ');
                dictionary.put(nextLine.substring(index + 1), nextLine.substring(0, index));
            }
            while (namesReader.ready()) {
                namesToFind.add(namesReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> bubbleSort(Map<String, String> dictionary) {
        return new TreeMap<>(dictionary);
    }

    private static boolean firstMoreThanSecond(String name, String name1) {
        return name.compareToIgnoreCase(name1) > 0;
    }

    public static int linearSearch(List<String> namesToFind, Map<String, String> dictionaryNames) {
        int counter = 0;
        for (String name : namesToFind) {
            for (String nextName : dictionaryNames.keySet()) {
                if (name.equals(nextName)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static void printTime(long start, long end, int counter, int size) {
        long timeMillis = end - start;
        long min = timeMillis / 1000 / 60;
        long sec = (timeMillis - min * 1000 * 60) / 1000;
        long ms = timeMillis - min * 1000 * 60 - sec * 1000;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.%n",
                counter, size, min, sec, ms);
    }
}
