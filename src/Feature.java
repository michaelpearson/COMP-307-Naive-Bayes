import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Feature {
    private List<Boolean> features;
    private boolean classLabel;
    private boolean hasClassLabel;

    public static List<Feature> readDatasetFromFile(File file, boolean hasClassLabel) throws FileNotFoundException {
        ArrayList<Feature> dataset = new ArrayList<>();
        Scanner s = new Scanner(file);
        while(s.hasNextLine()) {
            dataset.add(new Feature(s.nextLine(), hasClassLabel));
        }
        return dataset;
    }
    private Feature(String line, boolean hasClassLabel) throws FileNotFoundException {
        this.hasClassLabel = hasClassLabel;
        features = new ArrayList<>();
        Scanner s = new Scanner(line);
        while(s.hasNextInt()) {
            boolean f = s.nextInt() == 1;
            if(!s.hasNextInt() && hasClassLabel) {
                this.classLabel = f;
            } else {
                features.add(f);
            }
        }
    }

    public List<Boolean> getFeatures() {
        return features;
    }

    public boolean getClassLabel() {
        return classLabel;
    }

    public boolean isHasClassLabel() {
        return hasClassLabel;
    }
}
