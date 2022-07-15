# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#using-boot-devtools)

### Package Information

1. **request**: In these package you can find the request classes related with calls to CloudB's API.
   1. **Request**: The base class for all requests.
   2. **CloudBRequest**: The implementation of all requests to CloudB's API.

2. **controller**: In these package you can find the controller classes related with calls to our API.
   1. **CloudBController**: The base class for all controllers.
   2. **ReservationCloudBedsController**: The implementation of CloudB's API calls for reservations.
   3. **ReservationNotFoundAdvice**: Defaults to return a 404 if the reservation is not found.
   4. **ReservationNotFoundException**: Custom exception for a 404 error or if reservation is not found.

3. **cloudbeds**: In these package you can find the classes related with the logic business of reservations, guests, rooms and payments .