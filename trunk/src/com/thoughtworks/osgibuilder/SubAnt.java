package com.thoughtworks.osgibuilder;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileList;

public class SubAnt extends TaskHelper {
    private String target;

    public void execute() throws BuildException {
        final org.apache.tools.ant.taskdefs.SubAnt subant = new org.apache.tools.ant.taskdefs.SubAnt();
        subant.setProject(getProject());
        subant.setTarget(target);
        final FileList list = new FileList();
        getBundleGraph().invite(new BundleVisitor() {
            public void visit(Bundle bundle) {
                if (bundle.hasDir()) {
                    FileList.FileName name = new FileList.FileName();
                    name.setName(bundle.getDir());
                    list.addConfiguredFile(name);
                }
            }
        });
        subant.addFilelist(list);
        subant.execute();
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
