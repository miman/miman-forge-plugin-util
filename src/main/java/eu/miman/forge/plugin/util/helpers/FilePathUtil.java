package eu.miman.forge.plugin.util.helpers;

import org.jboss.forge.project.Project;

public interface FilePathUtil {

	/**
	 * This function will replace the relative path token "~" with the base package of the given project.
	 * If there isn't any path given (path == null), it will be replaced with the project base package.
	 * @param path	The path we want to modify (if needed)
	 * @param project	The Forge project we are working in (MUST have JavaSourceFacet)
	 * @return	The (potentially) modified path
	 */
	public String replaceRelativePathToken(String path, Project project);
}