package com.thoughtworks.osgibuilder;

public class PackageBundleResolver implements BundleResolver {
    private String name;
    private String jarfile;

    public void setName(String name) {
        this.name = name;
    }

    public void setJarfile(String jarfile) {
        this.jarfile = jarfile;
    }

    public Bundle byName(String name) {
        return null;
    }

    public Bundle byPackage(String name) {
        if (!name.equals(this.name)) {
            return null;
        }
        Bundle bundle = new Bundle(name);
        bundle.setJarFile(jarfile);
        return bundle;
    }
}
