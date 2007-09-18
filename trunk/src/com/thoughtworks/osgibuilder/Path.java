package com.thoughtworks.osgibuilder;

import org.apache.tools.ant.BuildException;

public class Path extends TaskHelper {
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void execute() throws BuildException {
        final org.apache.tools.ant.types.Path path = new org.apache.tools.ant.types.Path(getProject());
        getProject().addReference(id, path);
        path.setPath(getBundleGraph().getClasspathAsString());
    }
}
