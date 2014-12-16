package com.lela.dtrules;

import com.dtrules.compiler.excel.util.Excel2XML;
import com.dtrules.session.RuleSet;
import com.dtrules.session.RulesDirectory;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.project.MavenProject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * User: Chris Tallent
 * Date: 2/15/12
 * Time: 5:25 PM
 *
 * @goal generate
 * @phase generate-sources
 */
public class GenerateMojo extends AbstractMojo {

    /**
     * The base ruleset path
     *
     * @parameter expression="${dtrules.rulesetPath} default-value="/"
     */
    private String rulesetPath;

    /**
     * The base ruleset file name
     *
     * @parameter expression="${dtrules.rulesetFile} default-value="DTRules.xml"
     */
    private String rulesetFile;

    /**
     * The base ruleset name
     *
     * @parameter expression="${dtrules.rulesetName}
     */
    private String rulesetName;

    /**
     * The target output directory for build output
     *
     * @parameter expression="${dtrules.outputDirectory} default-value="${project.build.outputDirectory}/dtrules"
     */
    private File outputDirectory;

    /**
     * @parameter expression="${project}" readonly=true required=true
     */
    protected MavenProject project;
    
    private String workDirectory = "dtrules-work";
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Validate arguments
        if (rulesetPath == null || rulesetPath.length() == 0) {
            throw new MojoExecutionException("Ruleset Path must be defined");
        }

        if (rulesetFile == null || rulesetFile.length() == 0) {
            throw new MojoExecutionException("Ruleset File must be defined");
        }

        if (rulesetName == null || rulesetName.length() == 0) {
            throw new MojoExecutionException("Ruleset Name must be defined");
        }

        if (outputDirectory == null) {
            throw new MojoExecutionException("DTRules output directory must be defined");
        }

        getLog().info("Generate all rule sets from Excel");
        getLog().info("Ruleset Path: " + rulesetPath);
        getLog().info("Ruleset File: " + rulesetFile);
        getLog().info("Ruleset Name: " + rulesetName);
        getLog().info("Output Directory: " + outputDirectory.getAbsolutePath());

        // Since the compile happens in place, copy all the dtrules files including Excel
        // to a work directory that can be cleaned and won't affect the SVN repo
        File rulesetWorkDirectory = new File(project.getBuild().getDirectory(), workDirectory);
        try {
            FileUtils.copyDirectory(new File(rulesetPath), rulesetWorkDirectory, true);
        } catch (IOException e) {
            throw new MojoFailureException("Could not copy dtrules files to work dir: " + rulesetWorkDirectory);
        }

        // Create the rules directory configuration in order
        // to determine the artifacts for the ruleset
        RulesDirectory rd = new RulesDirectory(rulesetWorkDirectory.getAbsolutePath(), rulesetFile);
        RuleSet rs = rd.getRuleSet(rulesetName);

        try {
            String[] maps = { "main" };

            Excel2XML excel2XML = new Excel2XML(rulesetWorkDirectory.getAbsolutePath(), rulesetFile, rulesetName);
            excel2XML.compileRuleSet(rulesetWorkDirectory.getAbsolutePath(), rulesetFile, rulesetName, null, maps, 10);

            // Check to see if there were errors in this build
            if (excel2XML.getDTCompiler().getErrors().size() > 0) {
                throw new MojoFailureException("Errors found during compilation of: " + rulesetName);
            }

            // Copy and Move the generated artifacts to the output directory
            
            // Extract the relative dtrules filepath for the ruleset
            String normRulePath = FilenameUtils.normalize(rulesetWorkDirectory.getAbsolutePath());
            String targetFilePath = FilenameUtils.normalize(rs.getFilepath());

            if (normRulePath.length() > 0 && normRulePath.length() < targetFilePath.length()) {
                targetFilePath = targetFilePath.substring(normRulePath.length(), targetFilePath.length());
            }

            // COPY the dtrules.xml file
            File origin = new File(rulesetWorkDirectory.getAbsolutePath(), rulesetFile);
            File dest = new File(outputDirectory.getAbsolutePath());
            FileUtils.copyFileToDirectory(origin, dest, true);

            // COPY the map file
            if (rs.getMapPath() != null && !rs.getMapPath().isEmpty()) {
                origin = new File(rs.getFilepath(), rs.getMapPath().get(0));

                // If the origin exists, use it... otherwise copy the automatically generated mapping file
                if (!origin.exists()) {
                    File generated = new File(rulesetWorkDirectory.getAbsolutePath(), "/temp/mapping_main.xml");
                    if (generated.exists()) {
                        getLog().info("Auto-generated mapping file found at: " + generated.getAbsolutePath());
                        FileUtils.copyFile(generated, origin);
                    }
                }

                dest = new File(outputDirectory.getAbsolutePath(), targetFilePath);

                FileUtils.copyFileToDirectory(origin, dest, true);
            }

            // Move the compiled EDD xml file
            origin = new File(rs.getFilepath(), rs.getEDD_XMLName());
            dest = new File(outputDirectory.getAbsolutePath(), targetFilePath);
            FileUtils.copyFileToDirectory(origin, dest, true);

            // Move the compiled DT xml file
            origin = new File(rs.getFilepath(), rs.getDT_XMLName());
            dest = new File(outputDirectory.getAbsolutePath(), targetFilePath);
            FileUtils.copyFileToDirectory(origin, dest, true);
        } catch (Exception e) {
            // Error while compiling ruleset
            throw new MojoFailureException("Error compiling ruleset: " + rulesetName + " with error: " + e.getMessage());
        }
    }
}
