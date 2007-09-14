package com.thoughtworks.osgibuilder;

import java.util.HashSet;

public class BundleGraph {
    private Bundle mainBundle;
    private CompositeBundleResolver locator = new CompositeBundleResolver();

    public void addBundleLocator(BundleResolver resolver) {
        this.locator.addBundleLocator(resolver);
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
        mainBundle.invite(visitor, locator, new HashSet<String>());
    }
}
