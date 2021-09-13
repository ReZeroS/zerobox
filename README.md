# Introduction
# <img src="./logo.png" width="250px" align="center" alt="ZeroBox"/>
The Zero-Box uses some popular lite framework based on the java language to build a collection of tools for common developping.

# Features
* Supports the convertor for object and xml. You can try create object in your package and declare the package in config annotation(@XmlObjectPackageScan), then the framework will try to convert them for both way.

* Support the common util. I collect some util for common developing and will try to optimize them in the future.

* Support some beta framework tool like multiple threads insert tool with transaction promised in spring.

* Support config file for docker remote debug with clion, you can check out docker-clion directory, and here I write an [example for redis](https://qqtim.club/2021/07/17/gdb-debug-remote/). 

* Support rule match with bitmap, you can check out doc/rule-match.pptx, and code location is `club.qqtim.dimension.DimensionCalculator`. Hope this [introduce video](https://www.bilibili.com/video/bv1dq4y1N7EM) would work for you.

# Requirements
* JDK: 8 or greater
* Maven: install the dependency

# Developer guide
* API document 

---------

# Contributing
Welcome to contribute by creating issues or sending pull requests. See [Contributing Guide](CONTRIBUTING.md) for guidelines.



# License
Zero-Box is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file.
