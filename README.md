# Swagger UI Launcher

[![GraalVM build](https://github.com/alexcheng1982/swagger-ui-launcher/actions/workflows/ci.yml/badge.svg)](https://github.com/alexcheng1982/swagger-ui-launcher/actions/workflows/ci.yml)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/alexcheng1982)
 
A native command-line tool to launch Swagger UI, written with Kotlin / GraalVM.

> The goal of this project is to demonstrate how to use GraalVM to build native apps. For more information about building native Java apps with GraalVM, check out [this website](https://build-native-java-apps.cc/).

# Usage

Download a binary file from the [releases page](https://github.com/alexcheng1982/swagger-ui-launcher/releases) based on the platform (Windows, macOS or Ubuntu).

See the usage of this tool.

```
Usage: swagger-ui-launcher [-hV] [--[no-]open-browser] ([-f=<fileSources>]...    
                           [-u=<urlSources>]...)
Launch Swagger UI
  -h, --help                 Show this help message and exit.
      --[no-]open-browser    Open browser
  -V, --version              Print version information and exit.
Source of OpenAPI spec  -f, --file=<fileSources>   Local file
  -u, --url=<urlSources>     URL
```

OpenAPI specs can be provided as local files or remote URLs. Multiple specs can be provided.

* Local files are specified using `-f`.
* Remote URLs are specified using `-u`.

The following command uses the Petstore swagger file as an example.

```
$ swagger-ui-launcher -u https://petstore.swagger.io/v2/swagger.json
```

The following command shows both Petstore swagger file and OpenAPI v3 file.

```
$ swagger-ui-launcher -u https://petstore.swagger.io/v2/swagger.json -u https://petstore3.swagger.io/api/v3/openapi.json
```

After starting, the service is started at a random port. Your browser is opened automatically to show Swagger UI.

```
14:52:55.604 [DefaultDispatcher-worker-1] INFO ktor.application - Autoreload is disabled because the development mode is off.
14:52:55.605 [DefaultDispatcher-worker-1] INFO ktor.application - Application started in 0.002 seconds.
14:52:55.610 [DefaultDispatcher-worker-2] INFO ktor.application - Responding at http://localhost:50354
14:52:55.610 [main] INFO io.vividcode.swaggeruilauncher.Application - Server started at port 50354
```