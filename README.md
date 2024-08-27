# JDBI Demo

This repository demonstrates the usage of [JDBI library](https://jdbi.org/). The library fills in the gap between 
the full-fledged ORM frameworks and the low level JDBC library. A good candidate if you are looking for database
access with more control on the SQL queries with auto resource management.

## Basic CRUD operations

* Refer to `TheatreShowDao`

## Atomic transaction

* For transaction managed by JDBI library - refer to `TheatreShowDao.createReservationAndReserveSeats()`
* For transaction managed by the Spring framework @Transactional - refer to `TheatreShowService.reserveSeats()` and `AppConfig`

## Testing

* Integration test using H2 - refer to `TheatreShowDaoH2Test`
* Integration test using MySQL container - refer to `TheatreShowDaoMySQLTest`
* Test on @Transactional - refer to `TheatreShowDaoService`

## Spring Integration

Since it is a thin layer library, the integration just to define the main class `Jdbi` as a bean in `AppConfig` for
the main class to be injected into DAOs and services