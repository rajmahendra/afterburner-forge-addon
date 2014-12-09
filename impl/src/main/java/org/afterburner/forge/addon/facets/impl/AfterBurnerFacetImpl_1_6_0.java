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
package org.afterburner.forge.addon.facets.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.afterburner.forge.addon.facets.AbstractAfterBurnerFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.furnace.versions.SingleVersion;
import org.jboss.forge.furnace.versions.Version;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public class AfterBurnerFacetImpl_1_6_0 extends AbstractAfterBurnerFacet {

	@Inject
	public AfterBurnerFacetImpl_1_6_0(DependencyInstaller installer) {
		super(installer);
	}

	@Override
	protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions() {

		Map<Dependency, List<Dependency>> result = new LinkedHashMap<>();
		Dependency AFTERBURNER_PROVIDED = DependencyBuilder.create(
				AFTERBURNER_DEPENDENCY).setVersion("1.6.0");
		result.put(AFTERBURNER_PROVIDED, Arrays.asList(AFTERBURNER_PROVIDED));
		return result;
	}

	@Override
	public Version getSpecVersion() {
		return new SingleVersion("1.6.0");
	}

}
