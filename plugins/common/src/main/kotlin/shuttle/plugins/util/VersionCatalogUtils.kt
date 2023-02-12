package shuttle.plugins.util

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

/**
 * Returns the [VersionCatalog] named "libs" from the root project.
 */
val Project.libsCatalog: VersionCatalog
    get() = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
