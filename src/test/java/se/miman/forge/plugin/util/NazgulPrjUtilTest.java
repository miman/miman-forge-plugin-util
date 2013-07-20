package se.miman.forge.plugin.util;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;

import eu.miman.forge.plugin.util.NazgulPrjUtil;

public class NazgulPrjUtilTest {

	/**
	 * Tests the path when the target is up and the down
	 */
	@Test
	public void testCalculatePathToDirUpDown() {
		NazgulPrjUtil class2test = new NazgulPrjUtil();
		
		String sourceDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "plugins" + File.separator + "miman-forge-plugin-impl" + File.separator + "src";
		String targetDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String expectedPath = ".." + File.separator + ".." + File.separator + ".." + File.separator + "poms" + File.separator;
		
		String path = class2test.calculatePathToDir(sourceDir, targetDir);
		
		Assert.assertEquals("They resulting path is invalid", expectedPath, path);
	}

	/**
	 * Tests the path when the target is below the source
	 */
	@Test
	public void testCalculatePathToDirDown() {
		NazgulPrjUtil class2test = new NazgulPrjUtil();
		
		String sourceDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String targetDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms" + File.separator + "miman-forge-plugin-impl" + File.separator + "src";
		String expectedPath = "miman-forge-plugin-impl" + File.separator + "src" + File.separator;
		
		String path = class2test.calculatePathToDir(sourceDir, targetDir);
		
		Assert.assertEquals("They resulting path is invalid", expectedPath, path);
	}

	/**
	 * Tests the path when the target is above the source
	 */
	@Test
	public void testCalculatePathToDirUp() {
		NazgulPrjUtil class2test = new NazgulPrjUtil();
		
		String sourceDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms" + File.separator + "miman-forge-plugin-impl" + File.separator + "src";
		String targetDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String expectedPath = ".." + File.separator + ".." + File.separator;
		
		String path = class2test.calculatePathToDir(sourceDir, targetDir);
		
		Assert.assertEquals("They resulting path is invalid", expectedPath, path);
	}
	
	/**
	 * Tests the path when the target is the same as source
	 */
	@Test
	public void testCalculatePathSame() {
		NazgulPrjUtil class2test = new NazgulPrjUtil();
		
		String sourceDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String targetDir = "C:" + File.separator + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String expectedPath = "." + File.separator;
		
		String path = class2test.calculatePathToDir(sourceDir, targetDir);
		
		Assert.assertEquals("They resulting path is invalid", expectedPath, path);
	}
	
	/**
	 * Tests the path when the target is the same as source
	 */
	@Test
	public void testCalculatePathSameDifferentSeparatorChars() {
		NazgulPrjUtil class2test = new NazgulPrjUtil();
		
		String sourceDir = "C:" + "\\" + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String targetDir = "C:" + "/" + "code" + File.separator + "miman-forge-plugins" + File.separator + "trunk" + File.separator + "poms";
		String expectedPath = "." + File.separator;
		
		String path = class2test.calculatePathToDir(sourceDir, targetDir);
		
		Assert.assertEquals("They resulting path is invalid", expectedPath, path);
	}
}
