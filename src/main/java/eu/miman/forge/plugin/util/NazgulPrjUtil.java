package eu.miman.forge.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.maven.model.Model;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.services.ProjectFactory;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;

import eu.miman.forge.plugin.util.dto.MavenProjectId;
import eu.miman.forge.plugin.util.dto.ProjectWithPath;


/**
 * This is a helper class for project organized according to the Nazgul project
 * philosophy. Info about this philosophy can be found here:
 * 
 * https://bytebucket.org/lennartj/nazgul_tools/wiki/index.html
 * 
 * @author Mikael Thorman
 */
public class NazgulPrjUtil {
	
	ForgeProjectUtil forgeProjectUtil;

	public NazgulPrjUtil() {
		forgeProjectUtil = new ForgeProjectUtil();
	}
	
	/**
	 * Retrieves the groupId/artifactId & version for the parent project
	 * (according to the Nazgul philosophy).
	 * 
	 * s * That is the parent prj under the poms directory.
	 * 
	 * @param pom
	 *            The Forge POM object
	 * @param project
	 *            The project we are originating the request from
	 * @return The parent project id's
	 */
	public MavenProjectId getParentProjectId(Project project,
			ProjectFactory prjFactory) {
		MavenProjectId reply = new MavenProjectId();

		ProjectWithPath parentPrj = findParentProject(project, prjFactory);
		if (parentPrj == null) {
			return null;
		}

		MavenCoreFacet maprentPrjMvnFacet = parentPrj.getProject()
				.getFacet(MavenCoreFacet.class);
		Model parentPom = maprentPrjMvnFacet.getPOM();

		reply.setGroupId(parentPom.getGroupId());
		reply.setArtifactId(parentPom.getArtifactId());
		reply.setVersion(parentPom.getVersion());
		reply.setRelativePath(parentPrj.getPathToParent());
//		System.out.println("Parent maven id found: " + reply);

		return reply;
	}

	/**
	 * Traverses up from the given project until it finds the root directory
	 * (the one with a poms sub-folder)
	 * 
	 * @param project
	 * @return
	 */
	public Project findRootProject(Project project,
			ProjectFactory prjFactory) {
		
		DirectoryResource rootDir = findRootDirectory(project, prjFactory);
		if (rootDir == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Project prj = prjFactory.createProject(rootDir, MavenCoreFacet.class);
		return prj;
	}

	/**
	 * Goes though the root projects children & their children for the parent
	 * 
	 * @param project
	 *            The project we are starting our search from
	 * @param projectLocator
	 *            The project locator
	 * @return The parent project (or null if not found)
	 */
	@SuppressWarnings("unchecked")
	public ProjectWithPath findParentProject(Project project,
			ProjectFactory prjFactory) {
		DirectoryResource rootDir = findRootDirectory(project, prjFactory);
		DirectoryResource dirForThisPrj = project.getProjectRoot();
		
		if (rootDir == null) {
			return null;	// Root not found
		}
		// Now find the parent project under the poms subdirectory
		for (Resource<?> child : rootDir.listResources()) {
			if ("poms".equals(child.getName())) {
//				System.out.println("poms subdir found for prj: " + child.getFullyQualifiedName());
				if (DirectoryResource.class.isInstance(child)) {
					Resource<?> parentDir = ((DirectoryResource) child)
							.getChild(child.getParent().getName() + "-parent");
					if (DirectoryResource.class.isInstance(parentDir)) { 
						String pathToParent = calculatePathToDir(dirForThisPrj.getFullyQualifiedName(), parentDir.getFullyQualifiedName());
						ProjectWithPath reply = new ProjectWithPath(prjFactory
								.createProject((DirectoryResource) parentDir, MavenCoreFacet.class), pathToParent);
						return reply;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Calculated the path from the source dir to the target dir.
	 * OBS ! They must be on the same disk ! 
	 * @return
	 */
	public String calculatePathToDir(String source, String target) {
		StringBuffer reply = new StringBuffer();
		List<String> replyPath = new ArrayList<String>();
		
		// First find the common root
		// Make sure we only have one type of separator char
		source = source.replace("\\", "/");
		target = target.replace("\\", "/");
		String[] sourceParts = source.split(Pattern.quote("/"));
		String[] targetParts = target.split(Pattern.quote("/"));

		int noOfSameDirs = 0;
		if (target.equalsIgnoreCase(source)) {
			// the target is the same as the source address
			return "." + File.separator;
		} else if (target.startsWith(source)) {
			// the target is below the source address
			noOfSameDirs = sourceParts.length;
		} else if (source.startsWith(target)) {
			// the source is below the target address
			// Find how many dirs up we must go to come to the common root
			int i=0;
			while(targetParts.length>i && sourceParts.length>=i && targetParts[i].equals(sourceParts[i]) ) {
				// This directory part is the same for both directories
				i++; 
			}
			int nrOfDirsUpToSame = sourceParts.length - i;
			for (int j = 0; j < nrOfDirsUpToSame; j++) {
				replyPath.add("..");
			}
			noOfSameDirs = i;
		} else {
			// the target is NOT below the source address
			
			// Find how many dirs up we must go to come to the common root
			int i=0;
			while(sourceParts.length>=i && targetParts.length>=i && sourceParts[i].equals(targetParts[i]) ) {
				// This directory part is the same for both directories
				i++; 
			}
			int nrOfDirsUpToSame = sourceParts.length - i;
			for (int j = 0; j < nrOfDirsUpToSame; j++) {
				replyPath.add("..");
			}
			noOfSameDirs = i;
		}
		
		// Add the rest of the target path to get to the target.
		for (int i = noOfSameDirs; i < targetParts.length; i++) {
			replyPath.add(targetParts[i]);
		}
		
		// Compose path
		for (String dir : replyPath) {
			if (dir.length() > 0) {
				reply.append(dir + File.separator);
			}	// Else ignore empty dirs (probably double / chars)
		}
		
		return reply.toString();
	}
	
	/**
	 * Goes though the root projects children & their children for a project
	 * with the given artifactId
	 * 
	 * @param project
	 *            The project we are starting our search from
	 * @param artifactId
	 * @param projectLocator
	 *            The project locator
	 * @return The project with the given artifact id (or null if not found)
	 */
	public Project findProjectWithArtifactId(Project project,
			String artifactId, ProjectFactory prjFactory) {
		DirectoryResource rootDir = findRootDirectory(project, prjFactory);
		Project prj = findSubProjectWithArtifactId(artifactId, prjFactory,
				rootDir);

		return prj;
	}

	/**
	 * Goes though all children & their children for a project with the given
	 * artifactId
	 * 
	 * @param artifactId
	 * @param projectLocator
	 * @param rootDir
	 * @return
	 */
	private static Project findSubProjectWithArtifactId(String artifactId,
			ProjectFactory prjFactory, DirectoryResource rootDir) {
		for (Resource<?> child : rootDir.listResources()) {
			if (DirectoryResource.class.isInstance(child)) {
				if (prjFactory.containsProject((DirectoryResource) child)) {
					@SuppressWarnings("unchecked")
					Project prj = prjFactory
							.createProject((DirectoryResource) child, MavenCoreFacet.class);
					MavenCoreFacet mvnFacet = prj
							.getFacet(MavenCoreFacet.class);
					Model pom = mvnFacet.getPOM();
					if (artifactId.compareToIgnoreCase(pom.getArtifactId()) == 0) {
						// This is the project with the given artifactId
						return prj;
					} else {
						// Search all sub-folders
						Project p = findSubProjectWithArtifactId(artifactId,
								prjFactory, (DirectoryResource) child);
						if (p != null) {
							return p;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Traverses up from the given project until it finds the root directory
	 * (the one with a poms sub-folder)
	 * 
	 * @param project
	 * @return
	 */
	private DirectoryResource findRootDirectory(Project project,
			ProjectFactory prjFactory) {
		DirectoryResource currentDir = project.getProjectRoot();

		while (currentDir != null) {
			DirectoryResource pomsDir = forgeProjectUtil.findSubFolderWithName(
					"poms", prjFactory, currentDir);
			if (pomsDir != null) {
				return currentDir;
			} else {
				// See if the current folders parent folder is the root
				currentDir = (DirectoryResource) currentDir.getParent();
			}
		}
		return null; // Root was not found
	}
}
