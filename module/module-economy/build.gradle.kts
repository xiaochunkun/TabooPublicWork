dependencies {
    // 如果你不需要跨平台，你可以在这里引入 Bukkit 核心
    compileOnly("ink.ptms.core:v12002:12002:universal")
    compileOnly("ink.ptms.core:v12002:12002:mapped")

    compileOnly(project(":project:core"))
    compileOnly(project(":project:util-sql"))

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
}