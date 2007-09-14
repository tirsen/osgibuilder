package com.thoughtworks.osgibuilder;

import java.util.List;
import java.util.LinkedList;

public class CompositeBundleResolver implements BundleResolver {
    private List<BundleResolver> resolvers = new LinkedList<BundleResolver>();

    public void addBundleLocator(BundleResolver resolver) {
        resolvers.add(resolver);
    }

    public Bundle byName(String name) {
        for (BundleResolver resolver : resolvers) {
            Bundle bundle = resolver.byName(name);
            if (bundle != null) { return bundle; }
        }
        return null;
    }

    public Bundle byPackage(String name) {
        for (BundleResolver resolver : resolvers) {
            Bundle bundle = resolver.byPackage(name);
            if (bundle != null) { return bundle; }
        }
        return null;
    }
}
