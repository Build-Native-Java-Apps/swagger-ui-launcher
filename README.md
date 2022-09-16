# Swagger UI Launcher

[![GraalVM build](https://github.com/alexcheng1982/swagger-ui-launcher/actions/workflows/ci.yml/badge.svg)](https://github.com/alexcheng1982/swagger-ui-launcher/actions/workflows/ci.yml)
 
A command-line tool to launch Swagger UI

# Usage

Download a binary file from the releases page based on the platform (Windows, macOS or Ubuntu).

See the usage of this tool.

```
Usage: swagger-ui-launcher [-hV] ([-f=<fileSources>]... [-u=<urlSources>]...)
Launch Swagger UI
  -h, --help                 Show this help message and exit.
  -V, --version              Print version information and exit.
Source of OpenAPI spec  -f, --file=<fileSources>   Local file
  -u, --url=<urlSources>     URL
```

OpenAPI specs can be provided as local files or remote URLs.

* Local files are specified using `-f`.
* Remote URLs are specified using `-u`.

```
$ swagger-ui-launcher -u https://petstore.swagger.io/v2/swagger.json
```

After starting, the service is started at a random port. You can see the port number from output. Open your browser to the output location and view Swagger UI.

```
00:56:15.941 [DefaultDispatcher-worker-1] INFO ktor.application - Autoreload is disabled because the development mode is off.
00:56:15.942 [DefaultDispatcher-worker-1] INFO ktor.application - Application started in 0.002 seconds.
00:56:15.956 [DefaultDispatcher-worker-5] INFO ktor.application - Responding at http://localhost:51145
```