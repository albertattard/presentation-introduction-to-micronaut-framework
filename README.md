# Introduction to Micronaut Framework


## Run Tests

```bash
$ ./gradlew clean ktlintFormat dependencyUpdates test
```

## Build Application

```bash
$ ./gradlew clean build
```

## Test Application

Java version

```bash
$ java -version
openjdk version "11.0.6" 2020-01-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.6+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.6+10, mixed mode)
```

Tests

1. Micronaut

    ```bash
    $ java -jar run-kotlin-demo/build/libs/run-kotlin-demo-1.0-all.jar -m | grep "Application took "
    ```

1. Spring Boot

    ```bash
    $ java -jar run-kotlin-demo/build/libs/run-kotlin-demo-1.0-all.jar -b | grep "Application took "
    ```

Comparing time to first response

|#|Micronaut|String Boot|
|-|--------:|----------:|
|1|     1713|       3944|
|2|     1723|       3452|
|3|     1797|       3501|
|4|     1739|   **3256**|
|5| **1666**|       3312|
