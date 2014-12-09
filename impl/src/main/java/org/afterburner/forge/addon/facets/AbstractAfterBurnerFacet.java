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

import java.util.Collections;
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
			.setArtifactId("afterburner.fx");

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

			if (!satisfied) {
				for (Dependency dependency : getRequiredManagedDependenciesFor(group
						.getKey())) {
					installer.installManaged(origin, dependency);
				}
				installer.install(origin, group.getKey());
			}
		}
	}

	protected Iterable<Dependency> getRequiredManagedDependenciesFor(
			Dependency dependency) {
		return Collections.singleton(AFTERBURNER_DEPENDENCY);
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
