@file:Suppress("PropertyName", "SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val taboolib_version: String by project

dependencies {
    // 打包子项目
    implementation(project(":project:core"))
    implementation(project(":project:util-sql"))
    implementation(project(":project:util-player-data"))
    implementation(project(":project:util-item-lib"))
    implementation(project(":project:runtime-bukkit"))
    implementation(project(":module:module-warp"))
    implementation(project(":module:module-yesod"))
    implementation(project(":module:module-kits"))
    implementation(project(":module:module-home"))
    implementation(project(":module:module-spawner"))
    implementation(project(":module:module-tpa"))
    implementation(project(":module:module-economy"))
    implementation(project(":module:module-protect"))
    implementation(project(":module:module-scoreboard"))
}


// =============================
//       下面的东西不用动
// =============================
tasks {
    withType<ShadowJar> {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        // 删除一些不必要的文件
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
    }
    sourcesJar {
        archiveFileName.set("${rootProject.name}-${archiveFileName.get().substringAfter('-')}")
        // 打包子项目源代码
        rootProject.subprojects.forEach { from(it.sourceSets["main"].allSource) }
    }
    build {
        dependsOn(shadowJar)
    }
}

gradle.buildFinished {
    File(buildDir, "libs/${project.name}-${rootProject.version}.jar").delete()
}
