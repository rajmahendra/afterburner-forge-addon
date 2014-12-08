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
package org.afterburner.forge.addon.facets;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public abstract class AbstractAfterBurnerFacet extends AbstractFacet<Project>
		implements ProjectFacet, AfterBurnerFacet {

	protected static final Dependency AFTERBURNER_DEPENDENCY = DependencyBuilder
			.create().setGroupId("com.airhacks")
			.setArtifactId("afterburner.fx").setVersion("1.6.0");

	private final DependencyInstaller installer;

	@Inject
	public AbstractAfterBurnerFacet(final DependencyInstaller installer) {
		this.installer = installer;
	}

	abstract protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions();

	@Override
	public boolean install() {
		addRequiredDependency();
		return true;
	}

	@Override
	public boolean isInstalled() {
		return isDependencyRequirementsMet();
	}

	private void addRequiredDependency() {
		boolean isInstalled = false;
		DependencyFacet dependencyFacet = origin
				.getFacet(DependencyFacet.class);
		for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions()
				.entrySet()) {
			for (Dependency dependency : group.getValue()) {
				if (dependencyFacet.hasEffectiveDependency(dependency)) {
					isInstalled = true;
					break;
				}
			}
			if (!isInstalled) {
				installer.installManaged(origin, AFTERBURNER_DEPENDENCY);
				installer.install(origin, group.getKey());
			}
		}
	}

	protected boolean isDependencyRequirementsMet() {
		DependencyFacet deps = origin.getFacet(DependencyFacet.class);
		for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions()
				.entrySet()) {
			boolean satisfied = false;
			for (Dependency dependency : group.getValue()) {
				if (deps.hasEffectiveDependency(dependency)) {
					satisfied = true;
					break;
				}
			}

			if (!satisfied)
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getSpecVersion().toString();
	}
}
