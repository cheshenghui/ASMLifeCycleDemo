### 自定义gradle插件

##### 创建Android项目 -> 在项目中创建Android Library类型的module，起个名字test_gradle_plugin

* 将test_gradle_plugin中除了 build.gradle文件都删除，再将 build.gradle内容删除。

* 在test_gradle_plugin项目下创建目录src/main/groovy

  * 创建com.test.plugin.TestPlugin.groovy 文件（全类名）

  ```groovy
  package com.test.plugin
  
  import org.gradle.api.Plugin
  import org.gradle.api.Project
  
  public class TestPlugin implements Plugin<Project> {
  
      @Override
      void apply(Project project) {
          System.out.println("=============> Hello Plugin")
      }
  }
  ```

  

* 在src/main 目录下新建目录 resources/META-INF/gradle-plugins

  * 创建test.plugin.properties文件，写入上面创建的TestPlugin类

  ```properties
  implementation-class=com.test.plugin.TestPlugin
  ```

  

* 编写test_gradle_plugin中build.gradle文件，插件需要被部署到 maven 库中，我们可以选择部署到远程或者本地

  ```
  apply plugin: 'groovy'
  apply plugin: 'maven'
  
  dependencies {
      implementation fileTree(dir: "libs", include: ["*.jar"])
      implementation gradleApi()
      implementation localGroovy()
      implementation 'com.android.tools.build:gradle:4.0.0'
  }
  
  group='com.test.plugin'
  version='1.0.0'
  
  repositories {
      mavenCentral()
  }
  
  uploadArchives {
      repositories {
          mavenDeployer {
              repository(url: uri('../repo'))
          }
      }
  }
  ```

* 在 Android Studio 的右边栏找到 Gradle 中点击 uploadArchives，执行 plugin 的部署任务

* 当构建成功之后，在项目的根目录下将会出现一个 repo 目录，里面存放的就是我们的插件目标文件



##### 测试我们发布的plugin

1. 在 app module 中的 build.gradle 中引用此插件

```

apply plugin: 'test.plugin' // plugin项目中的 .properties文件名
buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url uri('../repo')
        }
    }
    dependencies {
        classpath "com.test.plugin:test_gradle_plugin:1.0.0" // group+module+version
    }
}
```

2. 然后在命令行中使用 gradlew 执行构建命令

```
gradlew clean assembleDebug
```

如果打印出我们自定义插件里的 log，则说明自定义 Gradle 插件可以使用