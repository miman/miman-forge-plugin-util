package eu.miman.forge.plugin.util.helpers;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import eu.miman.forge.plugin.util.dto.MavenProjectId;


/**
 * A helper class for working with Maven pom files.
 * 
 * @author Mikael
 *
 */
public interface MavenPomHelper {

	/**
	 * Loads the pom file from the forge project resource and converts this into a Model object.
	 * @param filename	The file to read relative to the jar file content 
	 * 					(ex: /template-files/web/pom.xml for a file located in 
	 * 						src/main/resources/template-files/web/pom.xml)
	 * @return	The Model for the read maven pom file
	 */
	public abstract Model openPOM(String filename);

	/**
	 * Checks if all the given dependencies are in the pom.
	 * It checks all group & artifact ids and subelements, but not the version number.
	 * @param dependencies	The dependencies that should be present in the pom
	 * @param pom	The pom
	 * @return
	 */
	public abstract boolean doesPomContainDependencies(
			List<Dependency> dependencies, Model pom);

	/**
	 * Adds or updates the pom parent project.
	 * @param grpId	The group id of the parent project
	 * @param artifactId	The artifact id of the parent project
	 * @param version	The version of the parent project
	 * @param pom	The pom we are working with
	 */
	public abstract void addOrUpdateParentProject(MavenProjectId prjId, Model pom);
}