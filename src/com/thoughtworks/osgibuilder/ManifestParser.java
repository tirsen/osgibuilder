package com.thoughtworks.osgibuilder;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestParser {
    public static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
    public static final String REQUIRE_BUNDLE = "Require-Bundle";
    public static final String BUNDLE_CLASS_PATH = "Bundle-ClassPath";
    public static final String IMPORT_PACKAGE = "Import-Package";

    public Bundle parse(InputStream manifestStream) {
        try {
            Manifest manifest = new Manifest(manifestStream);
            Attributes attributes = manifest.getMainAttributes();
            Bundle bundle = new Bundle(attributes.getValue(BUNDLE_SYMBOLIC_NAME));
            bundle.setDependencies(attributes.getValue(REQUIRE_BUNDLE));
            bundle.setClasspath(attributes.getValue(BUNDLE_CLASS_PATH));
            bundle.setImportPackages(attributes.getValue(IMPORT_PACKAGE));
            return bundle;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle parse(File manifest) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(manifest);
            return parse(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
