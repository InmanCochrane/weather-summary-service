# Assignment
Write an HTTP server that serves the forecasted weather. Your server should expose an endpoint that:
1. Accepts latitude and longitude coordinates
2. Returns the short forecast for that area for Today (“Partly Cloudy” etc)
3. Returns a characterization of whether the temperature is “hot”, “cold”, or “moderate” (use your discretion on mapping temperatures to each type)
4. Use the [National Weather Service API Web Service](https://www.weather.gov/documentation/services-web-api) as a data source.

# Run Locally
`sbt run` will initialize a local Ember server bound to `0.0.0.0::8080`.

# API
`/weather-summary?lat=$latitude&lon=$longitude`
- `GET`
    - Request Parameters: 
      - Replace `$latitude` with ...
      - Replace `$longitude` with ...
    - Response: Text summary of the weather at the location specified by the provided latitude and longitude.

# Improve
- Testing
  - Establish patterns for Endpoint presence and behaviors at edge
  - Establish patterns for requests to dependencies with dependency mocks
  - Establish patterns for responses to dependencies with dependency stubs
  - Establish patterns for object behaviors, including encode/decode, parameter options, and transformations
- Observability
  - Establish instrumentation patterns; e.g. logging, events, history
- User Friendliness
  - Handle the many potential unknowns and more complex summarizations possible in parsing NOAA data into a summary message
  - Expand summary to include things like apparent temperature, alerts, and warnings
  - Represent NOAA's time string more explicitly, perhaps with parsing as Instant
  - Parameter friendliness; e.g. full spelling of "lat/lon", difference in ordering
  - Error message clarity; e.g. lift all possible errors into map for user-facing messages
- Developer Friendliness
    - Improve clarity of decoders and encoders (dependent upon philosophies and API translation needs)
    - Figure out how to huge http4s ecosystem (i.e. circe) to make friendly ADT decoders
      - Apply to ADTs used in `NOAAWeatherSummaryValue`
    - Make NOAA's "uom" an ADT, or even a batch-updated, data-stored set of `http://codes.wmo.int/common/unit` values
- Additional API mapping to NOAA resources
