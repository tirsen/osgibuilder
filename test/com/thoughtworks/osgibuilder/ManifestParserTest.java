package com.thoughtworks.osgibuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Test;

public class ManifestParserTest {
    @Test
    public void canParseManifest() {
        String manifest = "Manifest-Version: 1.0\n" +
                "Bundle-ManifestVersion: 2\n" +
                "Bundle-Name: CruiseControl Launcher\n" +
                "Bundle-SymbolicName: net.sourceforge.cruisecontrol.launcher\n" +
                "Bundle-Version: 1.0.0\n" +
                "Bundle-Activator: net.sourceforge.cruisecontrol.launcher.Activator\n" +
                "Import-Package: org.osgi.framework;version=\"1.3.0\"\n" +
                "Bundle-ClassPath: bin/,\n" +
                " .\n" +
                "Require-Bundle: net.sourceforge.cruisecontrol.core\n";
        Bundle bundle = new ManifestParser().parse(new StringInputStream(manifest));
        assertThat(bundle.getName(), equalTo("net.sourceforge.cruisecontrol.launcher"));
        assertThat(bundle.getDependencies(), equalTo("net.sourceforge.cruisecontrol.core"));
        assertThat(bundle.getImportPackages(), equalTo("org.osgi.framework;version=\"1.3.0\""));
        assertThat(bundle.getClasspath(), equalTo("bin/,."));
    }
}
