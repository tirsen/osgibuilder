package com.thoughtworks.osgibuilder;

import java.util.Map;
import java.util.HashMap;

public class MapBundleResolver implements BundleResolver {
    private Map<String, Bundle> bundles = new HashMap<String, Bundle>();

    public Bundle byName(String name) {
        return bundles.get(name);
    }

    public Bundle byPackage(String name) {
        return bundles.get(name);
    }

    public void add(Bundle bundle) {
        bundles.put(bundle.getName(), bundle);
    }
}
