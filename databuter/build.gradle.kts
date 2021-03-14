/*
 * MIT License
 *
 * Copyright (c) 2019 - 2021 databute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import databute.Dependency

plugins {
    java
    application
}

dependencies {
    implementation(Dependency.netty)

    implementation(Dependency.commonsLang)
    implementation(Dependency.curatorFramework)
    implementation(Dependency.curatorRecipes)
    implementation(Dependency.gson)
    implementation(Dependency.guava)
    implementation(Dependency.jacksonDatabind)
    implementation(Dependency.jacksonDataformatYaml)
    implementation(Dependency.slf4j)

    runtimeOnly(Dependency.logback)

    testImplementation(Dependency.junit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass.set("databute.databuter.Databuter")
}
