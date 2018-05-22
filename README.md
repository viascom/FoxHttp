<h1 align="center">
  FoxHttp
</h1>

<h4 align="center">FoxHttp provides a fast and easy http client for Java and Android.</h4>

<p align="center">
  <a href="https://mvnrepository.com/artifact/ch.viascom.groundwork/foxhttp"><img src="https://img.shields.io/maven-central/v/ch.viascom.groundwork/foxhttp.svg"
         alt="Maven central"></a>
  <img src="https://img.shields.io/badge/size-204_KB-brightgreen.svg"
         alt="204 KB">
  <a href=""><img src="https://img.shields.io/badge/project-GroundWork-blue.svg"
              alt="project GroundWork"></a>
  <a href="http://www.apache.org/licenses/"><img src="https://img.shields.io/badge/license-Apache_2.0-blue.svg"
         alt="license Apache 2.0"></a>
  <img src="https://img.shields.io/badge/test--coverage-80%25-brightgreen.svg"
         alt="coverage">
</p>
<br>

<div align="center">
<img src="./FoxHttp.png"
         alt="FoxHttp Logo">
</div>


## Build-Status & Metrics

[![master](https://img.shields.io/badge/master-v1.3.3-brightgreen.svg)](https://github.com/Viascom/FoxHttp/tree/master) [![Build Status](https://travis-ci.org/Viascom/FoxHttp.svg?branch=master)](https://travis-ci.org/Viascom/FoxHttp)<br/>
[![develop](https://img.shields.io/badge/develop-v1.3.3-brightgreen.svg)](https://github.com/Viascom/FoxHttp/tree/develop) [![Build Status](https://travis-ci.org/Viascom/FoxHttp.svg?branch=develop)](https://travis-ci.org/Viascom/FoxHttp)
<br/><br/>
Request against [httpbin](https://httpbin.org/) which was installed on localhost:<br/>
[![get](https://img.shields.io/badge/GET--Request-35.4_ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#get-request)
[![post](https://img.shields.io/badge/POST--Request-47.3_ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#post-request-with-string-body)
[![Basic-Auth](https://img.shields.io/badge/BasicAuth--Request-43.4_ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#get-request-with-basicauth)
[![Post-Parsing](https://img.shields.io/badge/POST--Parsing--Request-53.3_ms-green.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#post-request-with-object-body-and-object-response)

## Functions
* HTTP / HTTPS method support
* GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE request support
* Real logging of request and response
* Builders for fast and easy request execution
* Automatic request and response parsing
* Integrated Object, URL-Encoded & Multipart-Body support
* Custom and predefined interceptors
* Powerful authorization strategy
* Lambda support
* Fully customizable cookie store
* Host and SSL trust strategy
* Easy proxy strategy
* Android support
* GroundWork ServiceResult support
* Faster than other HttpClient-Frameworks (such as httpComponents,okhttp)
* Gson and XStream support
* OAuth2 support
* Annotation application structure
* URL placeholder support
* _Advanced cache strategy (coming soon)_
* _GroundWork Server-Security support (coming soon)_
* _HAL support (coming soon)_

## Quick Start:

### Dependency

#### maven
```xml
<dependency>
    <groupId>ch.viascom.groundwork</groupId>
    <artifactId>foxhttp</artifactId>
    <version>1.3.3</version>
</dependency>
```

#### gradle
```
compile 'ch.viascom.groundwork:foxhttp:1.3'
```

### Send a request with JSON response deserialization
To run this example you need to add Gson to your dependency management!
```java
// Define Http-Client and set parser for serialization/deserialization
FoxHttpClient foxHttpClient = new FoxHttpClientBuilder(new GsonParser()).build();

// Define a System-Out logger on DEBUG level
foxHttpClient.setFoxHttpLogger(new SystemOutFoxHttpLogger(true, "FoxHttp-Logger", FoxHttpLoggerLevel.DEBUG));

// Create and Execute GET Request
FoxHttpResponse response = new FoxHttpRequestBuilder("http://httpbin.org/get?search=Viascom", RequestType.GET, foxHttpClient).buildAndExecute();

// Deserialization response
GetResponse object = repsponse.getParsedBody(GetResponse.class);

// Print result
System.out.println("Parsed-Output: " + object);
```

To deserialize the response you need the following model:
```java
public class GetResponse implements Serializable {

    public HashMap<String, String> args;
    public HashMap<String, String> headers;
    public String origin;
    public String url;

    @Override
    public String toString() {
        return "GetResponse{" +
                "args=" + args +
                ", headers=" + headers +
                ", origin='" + origin + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
```

## Licence

[Apache License, Version 2.0, January 2004](http://www.apache.org/licenses/)
