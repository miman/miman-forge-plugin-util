package se.miman.forge.plugin.util.dto;

import org.jboss.forge.project.Project;

/**
 * This class represents a Forge project and the path from to the parent project from where the call was made.
 * 
 * @author Mikael Thorman
 */
public class ProjectWithPath {
	Project project;
	String pathToParent;

	
	public ProjectWithPath(Project project, String pathToParent) {
		super();
		this.project = project;
		this.pathToParent = pathToParent;
	}
	
	public Project getProject() {
		return project;
	}
	public String getPathToParent() {
		return pathToParent;
	}
	
}
