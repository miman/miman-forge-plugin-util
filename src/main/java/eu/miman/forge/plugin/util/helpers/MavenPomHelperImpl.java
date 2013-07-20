package eu.miman.forge.plugin.util.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.forge.project.ProjectModelException;

import eu.miman.forge.plugin.util.dto.MavenProjectId;


public class MavenPomHelperImpl implements MavenPomHelper {

	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.MavenPomHelper#openPOM(java.lang.String)
	 */
	@Override
	public Model openPOM(String filename) {
		try {
			Model result = new Model();

			// Parse the content into a Model
			MavenXpp3Reader reader = new MavenXpp3Reader();
			InputStream stream = this.getClass().getResourceAsStream(filename);
			if (stream.available() > 0) {
				result = reader.read(stream);
			}
			stream.close();
			File pomFile = new File(filename);

			result.setPomFile(pomFile);
			return result;
		} catch (IOException e) {
			throw new ProjectModelException("Could not open POM file: "
					+ filename, e);
		} catch (XmlPullParserException e) {
			throw new ProjectModelException("Could not parse POM file: "
					+ filename, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.MavenPomHelper#doesPomContainDependencies(java.util.List, org.apache.maven.model.Model)
	 */
	@Override
	public boolean doesPomContainDependencies(List<Dependency> dependencies, Model pom) {
		List<Dependency> pomDeps = pom.getDependencies();
		for (Dependency dependency : dependencies) {
			boolean dependencyFound = false;
			for (Dependency pomDep : pomDeps) {
				if (pomDep.getGroupId().equalsIgnoreCase(dependency.getGroupId()) &&
						pomDep.getArtifactId().equalsIgnoreCase(dependency.getArtifactId())) {
					if (!areDependenciesEqual(dependency, pomDep)) {
						return false;
					} else {
						dependencyFound = true;
						break;
					}
				}
			}
			if (!dependencyFound) {
				return false;	// Required dependency was not in the list
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.MavenPomHelper#addOrUpdateParentProject(java.lang.String, java.lang.String, java.lang.String, org.apache.maven.model.Model)
	 */
	@Override
	public void addOrUpdateParentProject(MavenProjectId prjId, Model pom) {
		if (pom.getParent() == null) {
			pom.setParent(new Parent());
		}
		pom.getParent().setGroupId(prjId.getGroupId());
		pom.getParent().setArtifactId(prjId.getArtifactId());
		pom.getParent().setVersion(prjId.getVersion());
		if (prjId.getRelativePath() != null) {
			pom.getParent().setRelativePath(prjId.getRelativePath() + "pom.xml");
		}
	}

	//=====================================================================
	// Helpler functions
	
	/**
	 * Validating that all sub-elements are equal in both dependencies.
	 * @param dependency
	 * @param pomDep
	 * @return
	 */
	private boolean areDependenciesEqual(Dependency dependency,
			Dependency pomDep) {
		if (dependency.getExclusions().size() == 0 && pomDep.getExclusions().size() == 0) {
			return true;	// Neither has any exclusions
		}
		List<Exclusion> exclusions = dependency.getExclusions();
		for (Exclusion exclusion : exclusions) {
			List<Exclusion> pomExclusions = pomDep.getExclusions();
			boolean found = false;
			for (Exclusion pomExclusion : pomExclusions) {
				if (pomExclusion.getGroupId().equalsIgnoreCase(exclusion.getGroupId()) &&
						pomExclusion.getArtifactId().equalsIgnoreCase(exclusion.getArtifactId())) {
					found = true;
					break;
				}				
			}
			if (!found) {
				return false;	// Required exclusion was not in the list
			}
		}
		return true;
	}
}
