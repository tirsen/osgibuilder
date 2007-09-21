package com.thoughtworks.osgibuilder;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.resources.FileResource;

public class TaskHelper extends Task {
    private BundleGraph bundleGraph = new BundleGraph();
    private List<ManifestFileSet> manifests = new LinkedList<ManifestFileSet>();
    private List<Resolvers> resolvers = new LinkedList<Resolvers>();

    
    public BundleGraph getBundleGraph() {
        for (Resolvers resolvers : this.resolvers) {
            if (resolvers.isReference()) {
                resolvers = (Resolvers) resolvers.getRefid().getReferencedObject();
            }
            for (BundleResolver resolver : resolvers.getPackageResolvers()) {
                addNamedResolver((NamedBundleResolver) resolver);
            }
        }
        for (ManifestFileSet fileSet : manifests) {
            for (Iterator iterator = fileSet.iterator(); iterator.hasNext();) {
                FileResource manifest = (FileResource) iterator.next();
                Bundle bundle = parseManifest(manifest.getFile());
                String bundleDir = fileSet.getBundleDir();
                if (bundleDir == null) {
                    throw new RuntimeException("Need to specify 'bundledir' option");
                }
                bundleDir = PatternBundleResolver.evaluate(bundleDir, bundle.getName());
                if (fileSet.getDir() != null) {
                    bundleDir = new File(fileSet.getDir(), bundleDir).getPath();
                }
                bundle.setDir(bundleDir);
                bundleGraph.addBundle(bundle);
            }
        }
        return bundleGraph;
    }
    
    public void setManifest(File manifest) {
        Bundle mainBundle = parseManifest(manifest);
        mainBundle.setDir(getProject().getBaseDir().getPath());
        bundleGraph.setMainBundle(mainBundle);
    }

    private Bundle parseManifest(File manifest) {
        return new ManifestParser().parse(manifest);
    }
    
    public void addResolvers(Resolvers resolvers) {
        this.resolvers.add(resolvers);
    }

    public void addDependencyResolver(PatternBundleResolver resolver) {
        resolver.setProject(getProject());
        bundleGraph.addBundleLocator(resolver);
    }

    public void addNamedResolver(NamedBundleResolver resolver) {
        bundleGraph.addBundleLocator(resolver);
    }
    
    public void addManifests(ManifestFileSet manifests) {
        this.manifests.add(manifests);
    }
    
    public static class ManifestFileSet extends FileSet {
        private String bundleDir;
        
        public void setBundleDir(String bundleDir) {
            this.bundleDir = bundleDir;
        }
        
        public String getBundleDir() {
            return bundleDir;
        }
    }
}
