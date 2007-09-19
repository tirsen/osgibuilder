package com.thoughtworks.osgibuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class BundleGraphTest {
    private JUnit4Mockery context = new JUnit4Mockery();
    private BundleGraph bundleGraph;

    @Before
    public void setUp() {
        bundleGraph = new BundleGraph();
    }

    @Test
    public void noDependenciesShouldGiveEmptyClasspath() {
        bundleGraph.setMainBundle(new Bundle("main"));
        assertThat(bundleGraph.getClasspathAsString(), equalTo(""));
    }

    @Test
    public void noBundleDependenciesWithJarsShouldAddJarsToClasspath() {
        Bundle mainBundle = new Bundle("main");
        mainBundle.setClasspath("lib/jar1.jar,lib/jar2.jar");
        bundleGraph.setMainBundle(mainBundle);
        assertThat(bundleGraph.getClasspathAsString(), equalTo("lib/jar1.jar:lib/jar2.jar"));
    }

    @Test
    public void withBundleDependenciesShouldAddAllDependenciesToClasspath() {
        Bundle mainBundle = new Bundle("main");
        mainBundle.setDependencies("dep");
        mainBundle.setClasspath("lib/jar1.jar,lib/jar2.jar");
        Bundle depBundle = new Bundle("dep");
        depBundle.setJarFile("../dep/bin/dep.jar");
        depBundle.setDir("../dep");
        depBundle.setClasspath("lib/depjar1.jar,lib/depjar2.jar");
        bundleGraph.setMainBundle(mainBundle);
        bundleGraph.addBundle(depBundle);
        assertThat(bundleGraph.getClasspathAsString(),
                equalTo("../dep/lib/depjar1.jar:../dep/lib/depjar2.jar:" +
                        "../dep/bin/dep.jar:" +
                        "lib/jar1.jar:lib/jar2.jar"));
    }

    @Test
    public void addsAllDependantBundlesWhenSettingMainBundle() {
        Bundle mainBundle = new Bundle("main");
        mainBundle.setDependencies("dep1,dep2,dep3");

        final BundleResolver resolver = context.mock(BundleResolver.class);
        context.checking(new Expectations() {{
            one(resolver).byName("dep1"); will(returnValue(new Bundle("dep1")));
            one(resolver).byName("dep2"); will(returnValue(new Bundle("dep2")));
            one(resolver).byName("dep3"); will(returnValue(new Bundle("dep3")));
        }});
        bundleGraph.addBundleLocator(resolver);
        bundleGraph.setMainBundle(mainBundle);
        assertThat(bundleGraph.getBundleNames(), equalTo("dep1,dep2,dep3,main"));
    }

    @Test
    public void addsAllDependantBundlesWhenSettingMainBundleAndAddingAdditionalBundles() {
        Bundle mainBundle = new Bundle("main");
        mainBundle.setDependencies("dep1");
        final Bundle dep1 = new Bundle("dep1");
        dep1.setDependencies("dep2");
        final Bundle dep2 = new Bundle("dep2");
        dep2.setDependencies("dep3");

        final BundleResolver resolver = context.mock(BundleResolver.class);
        context.checking(new Expectations() {{
            one(resolver).byName("dep1"); will(returnValue(dep1));
            one(resolver).byName("dep2"); will(returnValue(dep2));
            one(resolver).byName("dep3"); will(returnValue(new Bundle("dep3")));
        }});
        bundleGraph.addBundleLocator(resolver);
        bundleGraph.setMainBundle(mainBundle);
        assertThat(bundleGraph.getBundleNames(), equalTo("dep3,dep2,dep1,main"));
    }

    @Test
    public void circlesDoesNotVisitSameModuleTwice() {
        Bundle top = new Bundle("top");
        top.setDependencies("left,right");
        final Bundle left = new Bundle("left");
        left.setDependencies("bottom");
        final Bundle right = new Bundle("right");
        right.setDependencies("bottom");
        final Bundle bottom = new Bundle("bottom");

        final BundleResolver resolver = context.mock(BundleResolver.class);
        context.checking(new Expectations() {{
            one(resolver).byName("left"); will(returnValue(left));
            one(resolver).byName("right"); will(returnValue(right));
            one(resolver).byName("bottom"); will(returnValue(bottom));
        }});
        bundleGraph.addBundleLocator(resolver);
        bundleGraph.setMainBundle(top);
        assertThat(bundleGraph.getBundleNames(), equalTo("bottom,left,right,top"));
    }

    @Test
    public void visitsAllBundlesDepthFirstWithoutDuplicates() {
        final Bundle top = new Bundle("top");
        top.setDependencies("left,right");
        bundleGraph.addBundle(top);

        final Bundle left = new Bundle("left");
        left.setDependencies("bottom");
        bundleGraph.addBundle(left);

        final Bundle right = new Bundle("right");
        right.setDependencies("bottom");
        bundleGraph.addBundle(right);

        final Bundle bottom = new Bundle("bottom");
        bundleGraph.addBundle(bottom);
        
        final BundleVisitor visitor = context.mock(BundleVisitor.class);
        context.checking(new Expectations() {{
            one(visitor).visit(bottom);
            one(visitor).visit(left);
            one(visitor).visit(right);
            one(visitor).visit(top);
        }});
        bundleGraph.invite(visitor);
    }

    @Test
    public void resolvesPackages() {
        Bundle mainBundle = new Bundle("main");
        mainBundle.setImportPackages("org.osgi.framework;version=\"1.3.0\"");
        Bundle depBundle = new Bundle("org.osgi.framework;version=\"1.3.0\"");
        depBundle.setJarFile("osgi.core.jar");
        bundleGraph.addBundle(depBundle);
        bundleGraph.setMainBundle(mainBundle);
        assertThat(bundleGraph.getClasspathAsString(), equalTo("osgi.core.jar"));
    }
    
    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }
}
