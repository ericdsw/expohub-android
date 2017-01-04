package com.example.ericdesedas.expohub.helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileReaderHelper {

    /**
     * AReads a file from assets folder and converts it's content to an array
     * @param filename the file path inside the assets folder
     * @return the file's content as a {@link String}
     * @throws IOException
     */
    public String readFile(String filename) throws IOException {

        ClassLoader loader  = this.getClass().getClassLoader();
        URL resource        = loader.getResource(filename);

        return FileUtils.readFileToString(new File(resource.getPath()));
    }
}
