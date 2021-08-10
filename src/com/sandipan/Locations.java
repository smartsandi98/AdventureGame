package com.sandipan;

import java.io.*;
import java.util.*;

public class Locations implements Map<Integer, Location> {

    private static final Map<Integer, Location> LOCATIONS = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {


        //storing the whole objects using serialization.The process of translating the object to format that can be
        //stored is called serialization. serialVersionUID is a sort of class version serialization id. It checked in
        //runtime,if the stored serialized id is not matched with the compiled class serialVersionUID then
        //InvalidClassException is thrown because of compatibility issue.
        try (ObjectOutputStream locFile = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(FilesName.BUFFERED_WRITTEN_LOCATION_OBJECT)
                ))) {
            for (Location location :
                    LOCATIONS.values()) {
                locFile.writeObject(location);
            }
        }



/*======================================================================================================================

//writing using byte stream.FileOutputStream class provide out put stream and BufferedOutputStream buffered the bytes
//in memory until the buffer is full.These 2 class provide raw byte stream output.using byte stream class we do not have
//to parse the bytes into various data types because byte stream itself can be used to read or write any primitive types.
//DataOutputStream provide some methods to write and read bytes.

        try (DataOutputStream locFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("locations.dat")))) {
            for (Location location : LOCATIONS.values()) {
                locFile.writeInt(location.getLocationID());
                locFile.writeUTF(location.getDescription());
                System.out.println("Writing location " + location.getLocationID() + " : " + location.getDescription());
                System.out.println("Writing " + (location.getExits().size() - 1) + " exits.");
                locFile.writeInt(location.getExits().size() - 1);
                for (String direction : location.getExits().keySet()) {
                    if (!direction.equalsIgnoreCase("Q")) {
                        System.out.println("\t\t" + direction + "," + location.getExits().get(direction));
                        locFile.writeUTF(direction);
                        locFile.writeInt(location.getExits().get(direction));
                    }
                }
            }
        }


*/
//======================================================================================================================
/*
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

*/


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
//reading using object input stream
        try (ObjectInputStream locFile = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(FilesName.BUFFERED_WRITTEN_LOCATION_OBJECT))
        )) {
            boolean eof = false;
            while (!eof) {
                try {
                    Location location = (Location) locFile.readObject();
                    LOCATIONS.put(location.getLocationID(), location);
                    System.out.println("\t\t" + location.getLocationID() + ", " + location.getDescription() +
                            ", Found Exists: " + location.getExits().size());
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


//======================================================================================================================

        //reading data using byte streams.EOFException is thrown when there is no input for the stream to read.
        //we are catching it separately because we want to distinguish between EOF(subclass of IO) and IO exception
        //we are using eof to get out of the loop.

//        try (DataInputStream locFile = new DataInputStream(
//                new BufferedInputStream(
//                        new FileInputStream("locations.dat")))) {
//
//            boolean eof = false;
//            while (!eof) {
//                try {
//                    Map<String, Integer> exits = new LinkedHashMap<>();
//                    int locID = locFile.readInt();
//                    String description = locFile.readUTF();
//                    int numExits = locFile.readInt();
//                    System.out.println("Read location " + locID + " : " + description);
//                    System.out.println("Found " + numExits + " exits");
//                    for (int i = 0; i < numExits; i++) {
//                        String direction = locFile.readUTF();
//                        int destination = locFile.readInt();
//                        exits.put(direction, destination);
//                        System.out.println("\t\t" + direction + "," + destination);
//                    }
//                    LOCATIONS.put(locID, new Location(locID, description, exits));
//
//                } catch (EOFException e) {
//                    eof = true;
//                }
//
//            }
//        } catch (IOException io) {
//            System.out.println("IO Exception");
//        }
//========================================================================================================================

//using try with resource and getting rid of the scanner to read data from bufferedReader by directly using buffered
//reader read line method to read single line and splitting it.

//        try (
//                BufferedReader locReader = new BufferedReader(new FileReader(FilesName.LOCATIONS));
//                BufferedReader directionsReader = new BufferedReader(new FileReader(FilesName.DIRECTIONS))) {
//
//            String locData;
//            String directionInput;
//
//            while ((locData = locReader.readLine()) != null) {
//                String[] locArray = locData.split(FilesName.DELIMITERS);
//
//
//                int locationId = Integer.parseInt(locArray[0]);
//                String description = locArray[1];
//
//                //test
//                System.out.println("Imported LocationId : " +
//                        locationId +
//                        " : " +
//                        description);
//
//                Map<String, Integer> tempExit = new LinkedHashMap<>();
//                LOCATIONS.put(locationId,
//                        new Location(
//                                locationId,
//                                description,
//                                tempExit
//                        ));
//            }
//
//
//            while ((directionInput = directionsReader.readLine()) != null) {
//
//                //using string split and taking the whole line and then splitting
//                String[] directionData = directionInput.split(FilesName.DELIMITERS);
//
//                int locationId = Integer.parseInt(directionData[0]);
//                String direction = directionData[1];
//                int destination = Integer.parseInt(directionData[2]);
//
//                Location location = LOCATIONS.get(locationId);
//                location.addExits(direction, destination);
//
//                //test
//                System.out.println(locationId + "," +
//                        direction + "," +
//                        destination);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//======================================================================================================================
//using try catch finally static initialization block with external file
//
//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(new FileReader(FilesName.LOCATIONS));
//            scanner.useDelimiter(FilesName.DELIMITERS);
//
//            while (scanner.hasNext()) {
//
//                int locationId = scanner.nextInt();
//                scanner.skip(scanner.delimiter());
//                String description = scanner.nextLine();
//
//                //test
//                System.out.println("Imported LocationId : " +
//                        locationId +
//                        " : " +
//                        description);
//
//                Map<String, Integer> tempExit = new HashMap<>();
//                LOCATIONS.put(locationId,
//                        new Location(
//                                locationId,
//                                description,
//                                tempExit
//                        ));
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (scanner != null) {
//                scanner.close();
//            }
//        }
//
//        try {
//            scanner = new Scanner
//                    (new BufferedReader(
//                            new FileReader(
//                                    FilesName.DIRECTIONS
//                            )));
//
//            scanner.useDelimiter(", ");
//
//            while (scanner.hasNext()) {
///*
//                 reading single word from scanner (skipping delimiter)
//                -----------------------------------------
//                int locationId = scanner.nextInt();
//                scanner.skip(scanner.delimiter());
//                String direction = scanner.next();
//                scanner.skip(scanner.delimiter());
//                String dest = scanner.nextLine();
//                int destination = Integer.parseInt(dest);
//                ------------------------------------------
//*/
//                //using string split and taking the whole line and then splitting
//                String input = scanner.nextLine();
//                String[] data = input.split(FilesName.DELIMITERS);
//
//                int locationId = Integer.parseInt(data[0]);
//                String direction = data[1];
//                int destination = Integer.parseInt(data[2]);
//
//                Location location = LOCATIONS.get(locationId);
//                location.addExits(direction, destination);
//
//                //test
//                System.out.println(locationId + "," +
//                        direction + "," +
//                        destination);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (scanner != null) {
//                scanner.close();
//            }
//        }

/*======================================================================================================================
 Static Initialization without external file
        Map<String, Integer> tempExit;
        LOCATIONS.put(0, new Location(0,
                "You are sitting in front of " +
                        "a computer learning Java",
                null));

        tempExit = new HashMap<>();
        tempExit.put("W", 2);
        tempExit.put("E", 3);
        tempExit.put("S", 4);
        tempExit.put("N", 5);
        LOCATIONS.put(1, new Location(1,
                "You are standing at the end of a " +
                        "road before a small brick building",
                tempExit));

        tempExit = new HashMap<>();
        tempExit.put("N", 5);
        LOCATIONS.put(2, new Location(2,
                "You are at the top of a hill",
                tempExit));

        tempExit = new HashMap<>();
        tempExit.put("W", 1);
        LOCATIONS.put(3, new Location(3,
                "You are inside a building," +
                        " a well house for a small spring",
                tempExit));

        tempExit = new HashMap<>();
        tempExit.put("N", 1);
        tempExit.put("W", 2);
        LOCATIONS.put(4, new Location(4,
                "You are in a valley beside a stream",
                tempExit));

        tempExit = new HashMap<>();
        tempExit.put("S", 1);
        tempExit.put("W", 2);
        LOCATIONS.put(5, new Location(5,
                "You are in the forest",
                tempExit));
===================================================================================================
*/
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
