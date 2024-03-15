package com.mygdx.game;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TUtility {
    public TUtility() {

    }

    public static int getInitialBlockHealth(String blockName) {
        try {
            File myFile = new File("src\\students.txt");
            Scanner fileScanner = new Scanner(myFile);
            while (fileScanner.hasNext()) {
                String data = fileScanner.nextLine();
                String[] splitData = data.split(",");
                String currentName = splitData[0];
                if (currentName.equals(blockName)) {
                    return Integer.parseInt(splitData[1]);
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return 69420;
    }
}