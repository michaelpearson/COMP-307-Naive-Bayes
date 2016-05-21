import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] argv) throws FileNotFoundException {
        List<Feature> trainingSet = Feature.readDatasetFromFile(new File(argv[0]), true);
        List<Feature> testSet = Feature.readDatasetFromFile(new File(argv[1]), false);
        int featureCount = trainingSet.get(0).getFeatures().size();


        int[] occurrencesSpamTrue = new int[featureCount];
        int[] occurrencesSpamFalse = new int[featureCount];
        int[] occurrencesNotSpamTrue = new int[featureCount];
        int[] occurrencesNotSpamFalse = new int[featureCount];

        int spam = 1, notspam = 1;
        for(int a = 0;a < featureCount;a++) {
            occurrencesNotSpamFalse[a] = 1;
            occurrencesNotSpamTrue[a] = 1;
            occurrencesSpamFalse[a] = 1;
            occurrencesSpamTrue[a] = 1;
        }

        for(Feature f : trainingSet) {
            if(f.getClassLabel()) {
                spam++;
            } else {
                notspam++;
            }
            for(int a = 0;a < featureCount;a++) {
                if (f.getClassLabel()) {
                    if(f.getFeatures().get(a)) {
                        occurrencesSpamTrue[a]++;
                    } else {
                        occurrencesSpamFalse[a]++;
                    }
                } else {
                    if(f.getFeatures().get(a)) {
                        occurrencesNotSpamTrue[a]++;
                    } else {
                        occurrencesNotSpamFalse[a]++;
                    }
                }
            }
        }

        System.out.println("----------------------------------------------");
        System.out.printf("|                    | Spam      | Not Spam  |\n");
        System.out.printf("| Class              | %4d      | %4d      |\n", spam, notspam);
        for(int a = 0;a < featureCount;a++) {
            System.out.printf("| Feature %2d = True  | %4d/%4d | %4d/%4d |\n", a + 1, occurrencesSpamTrue[a], occurrencesSpamTrue[a] + occurrencesSpamFalse[a], occurrencesNotSpamTrue[a], occurrencesNotSpamTrue[a] + occurrencesNotSpamFalse[a]);
            System.out.printf("| Feature %2d = False | %4d/%4d | %4d/%4d |\n", a + 1, occurrencesSpamFalse[a], occurrencesSpamFalse[a] + occurrencesSpamTrue[a], occurrencesNotSpamFalse[a], occurrencesNotSpamFalse[a] + occurrencesNotSpamTrue[a]);
        }
        System.out.println("----------------------------------------------");


        int i = 0;
        for(Feature f : testSet) {
            double probabilitySpam = (double)spam / (double)(spam + notspam);
            double probabilityNotSpam = (double)notspam / (double)(spam + notspam);
            for(int a = 0;a < featureCount;a++) {
                if(f.getFeatures().get(a)) {
                    probabilitySpam *= (double)occurrencesSpamTrue[a] / (double)(occurrencesSpamTrue[a] + occurrencesSpamFalse[a]);
                    probabilityNotSpam *= (double)occurrencesNotSpamTrue[a] / (double)(occurrencesNotSpamTrue[a] + occurrencesNotSpamFalse[a]);
                } else {
                    probabilitySpam *= (double)occurrencesSpamFalse[a] / (double)(occurrencesSpamTrue[a] + occurrencesSpamFalse[a]);
                    probabilityNotSpam *= (double)occurrencesNotSpamFalse[a] / (double)(occurrencesNotSpamTrue[a] + occurrencesNotSpamFalse[a]);
                }
            }
            double norm = probabilityNotSpam + probabilitySpam;
            System.out.printf("%d: Probability Spam: %6.4f, Probability not spam: %6.4f. This message is probably %s\n",++i ,probabilitySpam / norm, probabilityNotSpam / norm, probabilitySpam > probabilityNotSpam ? "Spam" : "Not Spam");
        }

    }
}
