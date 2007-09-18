package com.thoughtworks.osgibuilder;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import java.io.File;

public class PatternBundleResolver implements BundleResolver {
    private ManifestParser manifestParser = new ManifestParser();
    private Project project;
    private String dir;
    private String manifest;
    private String jarFile;

    public void setProject(Project project) {
        this.project = project;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public Bundle byName(String name) {
        if (dir == null) {
            throw new BuildException("'dir' must be set on dependencyresolver");
        }
        if (manifest == null) {
            throw new BuildException("'manifest' must be set on dependencyresolver");
        }
        File dir = project.resolveFile(evaluate(this.dir, name));
        File manifest = new File(dir, evaluate(this.manifest, name));
        Bundle bundle = manifestParser.parse(manifest);
        bundle.setDir(dir.getPath());
        if (jarFile != null) {
            bundle.setJarFile(new File(dir, evaluate(jarFile, name)).getPath());
        }
        return bundle;
    }

    public Bundle byPackage(String name) {
        return null;
    }

    public static String evaluate(String pattern, String name) {
        return pattern.replaceAll("\\{" + ManifestParser.BUNDLE_SYMBOLIC_NAME + "\\}", name);
    }
}
