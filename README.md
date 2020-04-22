# Introduction to Micronaut Framework

## Java version

```bash
$ java -version
```

```bash
openjdk version "11.0.6" 2020-01-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.6+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.6+10, mixed mode)
```

## Run Tests

```bash
$ ./gradlew clean ktlintFormat dependencyUpdates test
```

## Build Application

```bash
$ ./gradlew clean build
```

## Time to First Response

Comparing time to first response

| # | Micronaut | String Boot |
|:-:|----------:|------------:|
| 1 |      1713 |        3944 |
| 2 |      1723 |        3452 |
| 3 |      1797 |        3501 |
| 4 |      1739 |    **3256** |
| 5 |  **1666** |        3312 |

# Memory Footprint

Starting application with no contacts

| # | Micronaut | String Boot |
|:-:|----------:|------------:|
| 1 |     116.5 |       352.5 |
| 2 |     118.5 |   **337.7** |
| 3 |     117.4 |       339.2 |
| 4 |     116.7 |       339.1 |
| 5 | **115.2** |       343.6 |
