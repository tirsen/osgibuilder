package com.thoughtworks.osgibuilder;

import java.util.Set;

public class NamedBundleResolver implements BundleResolver {
    private String name;
    private String jarfile;
    private boolean ignore = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setJarfile(String jarfile) {
        this.jarfile = jarfile;
    }

    public Bundle byName(String name) {
        return byPackage(name);
    }

    public Bundle byPackage(String name) {
        if (!name.equals(this.name)) {
            return null;
        }
        if (ignore) {
            return new Bundle(name) {
                @Override
                public void invite(BundleVisitor visitor, BundleResolver resolver, Set<String> visited) {
                }
            };
        }
        if (jarfile == null) {
            throw new RuntimeException("'jarfile' must be specified on a package resolver");
        }
        Bundle bundle = new Bundle(name);
        bundle.setJarFile(jarfile);
        return bundle;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
}
