# <img align="left" src="xyz.svg" width="70" height="70"/> XYZ Hub

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

XYZ Hub is a RESTful web service for access and management of geospatial data. 

# Overview
Some of the features of XYZ Hub are:
* Organize geo datasets in _spaces_
* Store and manipulate individual geo features
* Retrieve geo features as tiles, with or without clipped geometries
* Search for geo features spatially using a bounding box or any custom geometry
* Explore geo features by filtering property values
* Retrieve statistics for your _spaces_
* Analytical representation of geo data as hexbins with statistical information
* Connect with different data sources
* Build a real-time geodata pipeline with processors
* Attach listeners to react on events

You can find more information in the [XYZ Docs](https://www.here.xyz/api) and in the [OpenAPI specification](https://xyz.api.here.com/hub/static/redoc/index.html).

XYZ Hub uses [GeoJSON](https://tools.ietf.org/html/rfc79460) as the main geospataial data exchange format. Tiled data can also be provided as [MVT](https://github.com/mapbox/vector-tile-spec/blob/master/2.1/README.md). 

This project is _experimental and work in progress_.

# Prerequisites

 * Java 8+
 * Maven 3.6+
 * Postgres 9.6+ with PostGIS 2.5+
 * Redis 5+ (optional)
 * Docker 18+ (optional)
 * Docker Compose 1.24+  (optional)

Hint: Postgres with PostGIS will be automatically started if you use 'docker-compose up -d' to start the service.*

# Getting started
Clone and install the project using:

```bash
git clone https://github.com/heremaps/xyz-hub.git
mvn clean install
```

### With docker

The service and all dependencies could be started locally using Docker compose.
```bash
docker-compose up -d
```

Alternatively, you can start freshly from the sources by using this command after cloning the project:
```bash
mvn clean install -Pdocker
```

### Without docker

The service could also be started directly as a fat jar. In this case Postgres and the other optional dependencies need to be started separately.

```bash
java -jar xyz-hub-service/target/xyz-hub-service.jar [OPTIONS]
```

Example:

```bash
java -jar xyz-hub-service/target/xyz-hub-service.jar -DHTTP_PORT=9090
```

### Configuration options
The service start parameters could be specified by editing the [default config file](./src/main/resources/config.json), using environment variables or system properties. See the default list of  [configuration parameters](https://github.com/heremaps/xyz-hub/wiki/Configuration-parameters) and their default values.

# Usage

When the service is running, you can start creating Spaces and Features, like:

```bash
curl -H "content-type:application/json" -d '{"title": "my first space", "description": "my first geodata repo"}' http://localhost:8080/hub/spaces
```

The service will respond with the space id and some other parameters:

```json
{
    "id": "pvhQepar",
    "title": "my first space",
    "description": "my first geodata repo",
    "storage": {
        "id": "psql",
        "params": null
    },
    "owner": "ANONYMOUS",
    "createdAt": 1576601166681,
    "updatedAt": 1576601166681,
    "contentUpdatedAt": 1576601166681,
    "autoCacheProfile": {
        "browserTTL": 0,
        "cdnTTL": 0,
        "serviceTTL": 0
    }
}
```

You can use now the space id to add Features to your brand new space:
```bash
curl -H "content-type:application/geo+json" -d '{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[-2.960847,53.430828]},"properties":{"name":"Anfield","@ns:com:here:xyz":{"tags":["football","stadium"]},"amenity":"Football Stadium","capacity":54074,"description":"Home of Liverpool Football Club"}}]}' http://localhost:8080/hub/spaces/pvhQepar/features
```

The service will respond now with the inserted geo features:
```json
{
    "type": "FeatureCollection",
    "etag": "b67016e5dcabbd5f76b0719d75c84424",
    "features": [
        {
            "type": "Feature",
            "id": "nf36KMsQAUYoM5kM",
            "geometry": {
                "type": "Point",
                "coordinates": [ -2.960847, 53.430828 ]
            },
            "properties": {
                "@ns:com:here:xyz": {
                    "space": "pvhQepar",
                    "createdAt": 1576602412218,
                    "updatedAt": 1576602412218,
                    "tags": [ "football", "stadium" ]
                },
                "amenity": "Football Stadium",
                "name": "Anfield",
                "description": "Home of Liverpool Football Club",
                "capacity": 54074
            }
        }
    ],
    "inserted": [
        "nf36KMsQAUYoM5kM"
    ]
}
```

# Acknowledgements

XYZ Hub uses:

* [Vertx](http://vertx.io/)
* [Geotools](https://github.com/geotools/geotools)
* [JTS](https://github.com/locationtech/jts)
* [Jackson](https://github.com/FasterXML/jackson)

and [others](./pom.xml#L192-L494).

# Contributing

Your contributions are always welcome! Please have a look at the [contribution guidelines](CONTRIBUTING.md) first.

# License


Copyright (C) 2017-2019 HERE Europe B.V.

See the [LICENSE](./LICENSE) file in the root of this project for license details.
