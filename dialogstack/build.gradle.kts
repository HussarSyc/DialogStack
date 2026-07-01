plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "io.github.dialogstack"
    compileSdk {
    version = release(36) {
        minorApiLevel = 1
    }
}

    defaultConfig {
        minSdk = 24
        version = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github"
                artifactId = "dialogstack"
                version = "1.0.0"

                pom {
                    name = "DialogStack"
                    description = "A priority-based dialog management library for Android"
                    url = "https://github.com/dialogstack/dialogstack"

                    licenses {
                        license {
                            name = "The MIT License"
                            url = "https://opensource.org/licenses/MIT"
                        }
                    }

                    developers {
                        developer {
                            id = "dialogstack"
                            name = "DialogStack Team"
                            email = "support@dialogstack.io"
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com/dialogstack/dialogstack.git"
                        developerConnection = "scm:git:ssh://github.com/dialogstack/dialogstack.git"
                        url = "https://github.com/dialogstack/dialogstack"
                    }
                }
            }
        }
    }
}