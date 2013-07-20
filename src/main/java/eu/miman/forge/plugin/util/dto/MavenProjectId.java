package eu.miman.forge.plugin.util.dto;

/**
 * A DTO class for a maven pom file, with its id's and relative path.
 * @author Mikael
 *
 */
public class MavenProjectId {
	/**
	 * The group id of the maven project.
	 */
	private String groupId;
	/**
	 * The artifact id of the maven project.
	 */
	private String artifactId;
	/**
	 * The version of the maven project.
	 */
	private String version;
	/**
	 * The path to the maven project related from where this object was generated.
	 */
	private String relativePath;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "MavenProjectId [groupId=" + groupId + ", artifactId="
				+ artifactId + ", version=" + version + ", relativePath="
				+ relativePath + "]";
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	
}
