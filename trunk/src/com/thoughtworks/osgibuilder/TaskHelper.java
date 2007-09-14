package com.thoughtworks.osgibuilder;

import org.apache.tools.ant.Task;

import java.io.File;

public class TaskHelper extends Task {
    protected String id;
    protected BundleGraph bundleGraph = new BundleGraph();

    public void setManifest(File manifest) {
        bundleGraph.setMainBundle(new ManifestParser().parse(manifest));
    }

    public void addDependencyResolver(PatternBundleResolver resolver) {
        resolver.setProject(getProject());
        bundleGraph.addBundleLocator(resolver);
    }

    public void addPackageResolver(PackageBundleResolver resolver) {
        bundleGraph.addBundleLocator(resolver);
    }
}
