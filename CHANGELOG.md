## [XYZ Hub - 2019.48.01] 2019-11-29

### Added
- Tile requests now supports Accept header's value "application/vnd.mapbox-vector-tile" for MVT
    responses.
- MVT request now supports ".mvtf" extension for MVT flattened properties.
- Add new endpoints: GET and POST /spatial.
- Advanced spatial requests by any geometry with or without proximity-radius

### Changed
- Deprecation of CountFeaturesEvent & CountResponse.
- The property "searchable" is now always visible in the statistics response.
- The "datatype" in the statistics response can only appear in the properties-section rather than
    the tags-section.
- Remove internal error messages when sending responses.

### Fixed
- Fix rounding issues on geometry calculation.
- Filter out false empty geometries on Hexbin / H3 clipping.


## [XYZ Hub - 2019.47.01] 2019-11-20

### Added
- Add option to return the centroid of the hexagons as a feature geometry, for the hexbin 
    clustering mode.       

### Changed
- Update the format of the MVT response: nested properties are serialized as JSON.


## [XYZ Hub - 2019.44.03] 2019-10-31

### Added
- Add property contentUpdateAt indicating the last time the content of the space was updated.
- Add property readOnly to indicate, if the space is accessible only for read operations.
    The service will respond will 405 Method Not Allowed, if the write features in the space,
    when the flag is set.
- Add option to receive deleted feature Ids by setting accept header to application/geo+json.
- Add broadcast messaging for service nodes.

### Changed
- Update the ETag to a 128-bit hash value
- The property "features" is always present in FeatureCollection responses.
- Move creation of on-demand indexing for searchable properties completely into the database
- Move the global searchable field to the properties section of the statistics response.

### Fixed
- Fix incorrect bounding box for some spaces in the statistics response.
- Fix inconsistent status codes for delete requests
- Fix that all connector config updates triggers connector reload.


## [XYZ Hub - 2019.41.01] 2019-10-10 

### Added
- Automated caching in backend
    - skipCache query-parameter for overriding
- Activate browser caching
- Finalize feature clustering (Hexbins)
- Finalize searchableProperties feature for spaces
- Introduce new permission-type "useCapabilities" to allow using special XYZ features
    - Include new capability "searchablePropertiesConfiguration" to control searchableProperties

### Changed
- Update OAS to contain clustering query-parameters & docs

### Fixed
- Storage's connectorParams are not sent to listeners anymore


## [XYZ Hub - 2019.37.01] 2019-09-09 

### Added
- A new optional parameter "includeStorageId" for the GET /spaces query could be used to include the ids of the storage connectors in the list of space definitions.
- The space definitions, include two new properties, accessible only by internal clients
    - contentUpdatedAt: indicates the last time the content of a space was modified
    - volatility: a value between 0 and 1 to describe how volatile is the content of a space 
- The connector protocol is extended to support feature clustering

### Changed
- The values createdAt and updatedAt of the space definition are now public and visible in single space responses and space list responses.
- A new error response code (513 Response Payload Too Large) is sent to the clients for responses, which are too large for AWS API Gateway.
- The URI length limit for requests to the API is increased from 4K to 10K.

### Fixed
- Resolve an issue that the space is not stored when a preprocessor returns a space definition without a modification.
- Storing a space with an empty list of listeners no longer results in an error.