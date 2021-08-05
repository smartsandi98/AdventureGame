package com.timbuchalka;

import java.io.*;
import java.util.*;

public class Locations implements Map<Integer, Location> {
    private static final Map<Integer, Location> LOCATIONS = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException, FileNotFoundException {

//writing data using buffered reader.Data will be written to files only when buffer is full
//otherwise memory buffer will be used to write data.Useful for small data to be written

        try (
                BufferedWriter locWriter = new BufferedWriter
                        (new FileWriter(FilesName.BUFFERED_WRITTEN_LOCATIONS));

                BufferedWriter dirWriter = new BufferedWriter(
                        new FileWriter(FilesName.BUFFERED_WRITTEN_DIRECTIONS)
                )) {

            for (Location location : Locations.LOCATIONS.values()) {
                locWriter.write(location.getLocationID() +
                        ", " +
                        location.getDescription() +
                        "\n");

                for (String direction : location.getExits().keySet()) {

                    if (!direction.equalsIgnoreCase("Q")) {
                        dirWriter.write(location.getLocationID() +
                                ", " +
                                direction +
                                ", " +
                                location.getExits().get(direction) +
                                "\n");


                    }
                }
            }

        }




/*===================================================================================================
writing data using file writer using try with resource


        try (FileWriter locFile = new FileWriter(FilesName.LOCATIONS);
             FileWriter dirFile = new FileWriter(FilesName.DIRECTIONS)) {

            for (Location location : Locations.LOCATIONS.values()) {
                locFile.write(location.getLocationID() +
                        ", " +
                        location.getDescription() +
                        "\n");

                for (String direction : location.getExits().keySet()) {
                    dirFile.write(location.getLocationID() +
                            ", " +
                            direction +
                            ", " +
                            location.getExits().get(direction) +
                            "\n");
                }
            }
        }
=========================================================================================================*/

/*  writing data using file writer using try catch or try finally


    try {
            locFile = new FileWriter(FilesName.FileWriterOutName);
            for (Location location : Locations.LOCATIONS.values()) {
                locFile.write(location.getLocationID() +
                        ", " +
                        location.getDescription() +
                        "\n");
            }

        } finally {
                if (locFile != null){
                    System.out.println("Attempting To Close LocFile");
                    locFile.close();
                }

        }
*/
    }

    static {
//using try with resource and getting rid of the scanner to read data from bufferedReader by directly using buffered
//reader read line method to read single line and splitting it.

        try (
                BufferedReader locReader = new BufferedReader(new FileReader(FilesName.LOCATIONS));
                BufferedReader directionsReader = new BufferedReader(new FileReader(FilesName.DIRECTIONS))) {

            String locData;
            String directionInput;

            while ((locData = locReader.readLine()) != null) {
                String[] locArray = locData.split(FilesName.DELIMITERS);


                int locationId = Integer.parseInt(locArray[0]);
                String description = locArray[1];

                //test
                System.out.println("Imported LocationId : " +
                        locationId +
                        " : " +
                        description);

                Map<String, Integer> tempExit = new HashMap<>();
                LOCATIONS.put(locationId,
                        new Location(
                                locationId,
                                description,
                                tempExit
                        ));
            }


            while ((directionInput = directionsReader.readLine()) != null) {

                //using string split and taking the whole line and then splitting
                String[] directionData = directionInput.split(FilesName.DELIMITERS);

                int locationId = Integer.parseInt(directionData[0]);
                String direction = directionData[1];
                int destination = Integer.parseInt(directionData[2]);

                Location location = LOCATIONS.get(locationId);
                location.addExits(direction, destination);

                //test
                System.out.println(locationId + "," +
                        direction + "," +
                        destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int size() {
        return LOCATIONS.size();
    }

    @Override
    public boolean isEmpty() {
        return LOCATIONS.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return LOCATIONS.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return LOCATIONS.containsValue(value);
    }

    @Override
    public Location get(Object key) {
        return LOCATIONS.get(key);
    }

    @Override
    public Location put(Integer key, Location value) {
        return LOCATIONS.put(key, value);
    }

    @Override
    public Location remove(Object key) {
        return LOCATIONS.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Location> m) {
        // LOCATIONS.putAll(Map<? extends Integer,m);
    }

    @Override
    public void clear() {
        LOCATIONS.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return LOCATIONS.keySet();
    }

    @Override
    public Collection<Location> values() {
        return LOCATIONS.values();
    }

    @Override
    public Set<Entry<Integer, Location>> entrySet() {
        return LOCATIONS.entrySet();
    }
}
