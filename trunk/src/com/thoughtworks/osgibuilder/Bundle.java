package com.thoughtworks.osgibuilder;

import java.util.Set;

public class Bundle {
    private String[] classpath = new String[0];
    private String dir = "";
    private String jarFile;
    private final String name;
    private String dependencies;
    private String importPackages;

    public Bundle(String name) {
        this.name = name;
    }

    public void setClasspath(String value) {
        this.classpath = value == null ? null : value.split(",");
    }

    public void setDir(String dir) {
        this.dir = dir;
        if (!dir.endsWith("/") && dir.length() > 0) {
            this.dir += "/";
        }
    }

    public String getClasspath() {
        if (classpath == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < classpath.length; i++) {
            String s = classpath[i];
            result.append(dir).append(s);
            if (i != classpath.length - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public void invite(BundleVisitor visitor, BundleResolver resolver, Set<String> visited) {
        if (dependencies != null) {
            for (String dependency : dependencies.split(",")) {
                // don't try to resolve empty strings, "" gets split into [""] :-(
                if (dependency.length() > 0 && !visited.contains(dependency)) {
                    resolver.byName(dependency).invite(visitor, resolver, visited);
                }
            }
        }
        if (importPackages != null) {
            for (String importPackage : importPackages.split(",")) {
                // don't try to resolve empty strings, "" gets split into [""] :-(
                if (importPackage.length() > 0 && !visited.contains(importPackage)) {
                    Bundle bundle = resolver.byPackage(importPackage);
                    if (bundle == null) {
                        throw new RuntimeException("Could not resolve bundle for: " + importPackage);
                    }
                    bundle.invite(visitor, resolver, visited);
                }
            }
        }
        visited.add(this.getName());
        visitor.visit(this);
    }

    public String toString() {
        return this.getClass() + "[" + name + "]";
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setImportPackages(String importPackages) {
        this.importPackages = importPackages;
    }

    public String getImportPackages() {
        return importPackages;
    }

    public String getDir() {
        return dir;
    }

    public boolean hasDir() {
        // TODO Auto-generated method stub
        return dir != null && dir.length() > 0;
    }
}
