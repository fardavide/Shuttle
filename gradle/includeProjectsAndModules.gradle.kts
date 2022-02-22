val (projects, modules) = rootDir.projectsAndModules()
val namedProjects = projects.map { it to it.replace("/", "-") }
val namedModules = modules.map { it to it.drop(1).replace(":", "-") }

println("""
    PROJECT:  ${rootProject.name}
    Projects: ${namedProjects.sortedBy { it.first }.joinToString { "${it.first} as ${it.second}" }}
    Modules:  ${namedModules.sortedBy { it.first }.joinToString { "${it.first} as ${it.second}" }}
    
""".trimIndent())

for ((p, n) in namedProjects) includeBuild(p) { name = n }
for (m in modules) include(m)

for (m in namedModules) project(m.first).name = m.second

fun File.projectsAndModules(): Pair<Set<String>, Set<String>> {
    val blacklist = setOf(
        ".git",
        ".gradle",
        ".idea",
        "buildSrc",
        "config",
        "build",
        "src"
    )

    fun File.childrenDirectories() = listFiles { _, name -> name !in blacklist }!!
        .filter { it.isDirectory }

    fun File.isProject() =
        File(this, "settings.gradle.kts").exists() || File(this, "settings.gradle").exists()

    fun File.isModule() = !isProject() &&
        File(this, "build.gradle.kts").exists() || File(this, "build.gradle").exists()


    val modules = mutableSetOf<String>()
    val projects = mutableSetOf<String>()

    fun File.find(name: String? = null, includeModules: Boolean = true): List<File> = childrenDirectories().flatMap {
        val newName = (name ?: "") + it.name
        when {
            it.isProject() -> {
                projects += newName
                it.find("$newName:", includeModules = false)
            }
            it.isModule() && includeModules -> {
                modules += ":$newName"
                it.find("$newName:")
            }
            else -> it.find("$newName:", includeModules = includeModules)
        }
    }

    find()

    // we need to replace here since some Projects have a Module as a parent folder
    val formattedProjects = projects.map { it.replace(":", "/") }.toSet()
    return formattedProjects to modules
}
