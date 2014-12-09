/**
 * Copyright 2014 AfterBurner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.afterburner.forge.addon.ui.setup;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.afterburner.forge.addon.facets.AfterBurnerFacet;
import org.afterburner.forge.addon.ui.AbstractAfterBurnerCommand;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */

public class SetupWizard extends AbstractAfterBurnerCommand {

	private static final Logger log = Logger.getLogger(SetupWizard.class
			.getName());

	private static final Coordinate AFTERBURNER_MAVEN_PLUGIN_COORDINATE = CoordinateBuilder
			.create().setGroupId("br.com.ingenieux")
			.setArtifactId("jbake-maven-plugin");

	Project project = null;

	@Inject
	@WithAttributes(required = true, label = "AfterBurner Version", defaultValue = "1.0", shortName = 'v')
	private UISelectOne<AfterBurnerFacet> abVersion;

	@Inject
	private FacetFactory facetFactory;

	@Inject
	private ResourceFactory resourceFactory;

	@Override
	public void initializeUI(final UIBuilder builder) throws Exception {
		builder.add(abVersion);

	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {

		project = getSelectedProject(context);

		final MetadataFacet facet = project.getFacet(MetadataFacet.class);
		facet.getProjectProvider();

		AfterBurnerFacet myFacet = abVersion.getValue();
		if (facetFactory.install(project, myFacet)) {
			return Results.success("AfterBurner " + myFacet.getSpecVersion()
					+ " has been installed  in project "
					+ project.getRoot().getName() + ".");
		}
		return Results.fail("Could not install AfterBurner.");
	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata
				.from(super.getMetadata(context), getClass())
				.name("AfterBurner: Setup")
				.description("Setup a AfterBurner FX project")
				.category(
						Categories.create(super.getMetadata(context)
								.getCategory(), "AfterBurner"));
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

}
