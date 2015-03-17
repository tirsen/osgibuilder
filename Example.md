Example showing how to use osgi:path and osg:subant.

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<project name="net.sourceforge.cruisecontrol.launcher" xmlns:osgi="antlib:com.thoughtworks.osgibuilder" basedir="." default="jar">

    <target name="clean">
        <delete dir="bin"/>
        <delete file="${ant.project.name}.jar"/>
    </target>

    <target name="compile">
        <osgi:path id="classpath" manifest="META-INF/MANIFEST.MF">
            <dependencyresolver dir="../{Bundle-SymbolicName}" 
                                jarfile="{Bundle-SymbolicName}.jar"
                                manifest="META-INF/MANIFEST.MF"/>
        	<packageresolver name='org.osgi.framework;version="1.3.0"' 
        					 jarfile="../net.sourceforge.cruisecontrol.core/lib/osgi.core.jar"/>
    	</osgi:path>
        <mkdir dir="bin"/>
        <javac classpathref="classpath" srcdir="src" destdir="bin"/>
    </target>

    <target name="jar" depends="compile">
        <jar destfile="${ant.project.name}.jar" basedir="bin"/>
    </target>

    <target name="jar-deps" depends="compile">
        <osgi:subant target="jar" manifest="META-INF/MANIFEST.MF">
            <dependencyresolver dir="../{Bundle-SymbolicName}" manifest="META-INF/MANIFEST.MF"/>
    	</osgi:subant>
    </target>

</project>
```