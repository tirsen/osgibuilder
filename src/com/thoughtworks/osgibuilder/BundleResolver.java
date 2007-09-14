package com.thoughtworks.osgibuilder;

public interface BundleResolver {
    Bundle byName(String name);
    Bundle byPackage(String name);
}
