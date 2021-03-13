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

package databute

object Dependency {
    const val commonsLang = "org.apache.commons:commons-lang3:${Version.commonsLang}"
    const val curatorFramework = "org.apache.curator:curator-framework:${Version.curator}"
    const val curatorRecipes = "org.apache.curator:curator-recipes:${Version.curator}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val guava = "com.google.guava:guava:${Version.guava}"
    const val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:${Version.jackson}"
    const val jacksonDataformatYaml =
        "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Version.jackson}"
    const val logback = "ch.qos.logback:logback-classic:${Version.logback}"
    const val netty = "io.netty:netty-all:${Version.netty}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Version.rxJava}"
    const val slf4j = "org.slf4j:slf4j-api:${Version.slf4j}"

    const val junit = "junit:junit:${Version.junit}"
}
