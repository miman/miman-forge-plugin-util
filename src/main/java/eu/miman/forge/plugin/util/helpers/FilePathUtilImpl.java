package eu.miman.forge.plugin.util.helpers;

import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;

public class FilePathUtilImpl implements FilePathUtil {

	/**
	 * This function will replace the relative path token "~" with the base package of the given project.
	 * If there isn't any path given (path == null), it will be replaced with the project base package.
	 * @param path	The path we want to modify (if needed)
	 * @param project	The Forge project we are working in (MUST have JavaSourceFacet)
	 * @return	The (potentially) modified path
	 */
	@Override
	public String replaceRelativePathToken(String path, Project project) {
		JavaSourceFacet javaFacet = project.getFacet(JavaSourceFacet.class);
		String usedPath = path;
		if (usedPath == null) {
			usedPath = javaFacet.getBasePackage();
		} else {
			// Replace ~ with base package if it starts the given path
			if (usedPath.startsWith("~")) {
				usedPath = javaFacet.getBasePackage() + usedPath.replaceAll("~", "");
			}
		}
		return usedPath;
	}
}
