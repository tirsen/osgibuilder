package com.thoughtworks.osgibuilder;

import java.util.HashSet;
import java.util.Set;

public class BundleGraph {
    private Set<Bundle> bundles = new HashSet<Bundle>();
    private Bundle mainBundle;
    private CompositeBundleResolver resolver = new CompositeBundleResolver();
    private MapBundleResolver mapBundleResolver = new MapBundleResolver();

    public BundleGraph() {
        resolver.addBundleLocator(mapBundleResolver);
    }
    
    public void addBundleLocator(BundleResolver resolver) {
        this.resolver.addBundleLocator(resolver);
    }

    public String getBundleNames() {
        final StringBuffer result = new StringBuffer();
        invite(new BundleVisitor() {
            public void visit(Bundle bundle) {
                result.append(bundle.getName()).append(",");
            }
        });
        // trim last comma
        return result.toString().replaceAll(",$", "");
    }

    public void setMainBundle(Bundle mainBundle) {
        this.mainBundle = mainBundle;
        addBundle(mainBundle);
    }

    public void addBundle(Bundle bundle) {
        this.bundles.add(bundle);
        mapBundleResolver.add(bundle);
    }
    
    public String getClasspathAsString() {
        final StringBuffer result = new StringBuffer();
        invite(new BundleVisitor() {
            public void visit(Bundle bundle) {
                String classpath = bundle.getClasspath();
                if (classpath.length() > 0) {
                    result.append(classpath).append(",");
                }
                if (bundle != mainBundle) {
                    result.append(bundle.getJarFile()).append(",");
                }
            }
        });
        // trim last comma
        return result.toString().replaceAll(",$", "").replaceAll(",", ":");
    }

    public void invite(BundleVisitor visitor) {
        Set<String> visited = new HashSet<String>();
        for(Bundle bundle : bundles) {
            if (!visited.contains(bundle.getName())) {
                visited.add(bundle.getName());
                bundle.invite(visitor, resolver, visited);
            }
        }
    }
}
