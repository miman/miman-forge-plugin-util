/**
 * 
 */
package se.miman.forge.plugin.util;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.merge.ModelMerger;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.shell.plugins.RequiresFacet;

import se.miman.forge.plugin.util.helpers.MavenPomHelper;
import se.miman.forge.plugin.util.helpers.MavenPomHelperImpl;

/**
 * This is a base Facet class.
 * This Facet makes sure the project has the correct Dependencies, Properties & Repositories.
 * If you only override the abstract functions for Dependencies, Properties & Repositories, these will be added automatically when using this facet.
 * The presence of the Dependencies will also be verified in the isInstalled() function (except the version numbers for them). 
 * 
 * OBS! Make sure you call super() in the install & isInstalled functions to invoke the functionality in this base class.
 * 
 * @author Mikael Thorman
 */
@RequiresFacet({ MavenCoreFacet.class, DependencyFacet.class })
public abstract class MimanBaseFacet extends BaseFacet {

   protected MavenPomHelper mavenPomHelper;

   /**
    * NEVER access this directly, always use the get functions !
    */
   protected Model pomModel;

	public MimanBaseFacet() {
		super();
		mavenPomHelper = new MavenPomHelperImpl();
	}

	/**
	 * Return the list of repositories you want this project to have.
	 * These repositories will be added to the Maven pom file automatically.
	 * @return	The repositories that should be added to this project.
	 */
	abstract protected String getTargetPomFilePath();

	/* (non-Javadoc)
	 * @see org.jboss.forge.project.Facet#install()
	 */
	@Override
	public boolean install() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.forge.project.Facet#isInstalled()
	 */
	@Override
	public boolean isInstalled() {
		final MavenCoreFacet mvnFacet = project.getFacet(MavenCoreFacet.class);
		Model pom = mvnFacet.getPOM();

		// Makes sure all dependencies that this facet requires are installed in the pom file (not checking the versions)
		List<Dependency> wantedDeps = getWantedDependencies();
		if (wantedDeps != null && !mavenPomHelper.doesPomContainDependencies(wantedDeps, pom)) {
			return false;
		}
		return true;
	}

	/**
	 * Merges the pom template for this project with the needed data
	 * @param pom	The source pom
	 */
	protected void mergePomFileWithTemplate(Model pom) {
		// Merge the source pom with the target pom
		ModelMerger modelMerger = new ModelMerger();
		
		modelMerger.merge(pom, getPomModel(), true, null);
	}

	/**
	 * Return the list of dependencies you want this project to have.
	 * These dependencies will be added to the Maven pom file automatically.
	 * The isInstalled() function will verify that these dependencies are in the pom file (it will not check the version number).
	 * @return	The Dependencies that should be added to this project.
	 */
	private List<Dependency> getWantedDependencies() {
		return getPomModel().getDependencies();
	}

	public Model getPomModel() {
		if (pomModel == null) {
			pomModel = mavenPomHelper.openPOM(getTargetPomFilePath());	
		}
		return pomModel;
	}

	public MavenPomHelper getMavenPomHelper() {
		return mavenPomHelper;
	}
}
