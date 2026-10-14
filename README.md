# DeliverySystem — Food Delivery Management Application

A Java desktop application with a JavaFX interface, developed as part of a university Object-Oriented Programming course.

The project models customers, restaurants, delivery riders, and orders. It includes customer and administrator screens, order processing, reports, and saving and loading data through local text files.

## Features

- **Customer management:** Add, search, and update customer records.
- **Customer dashboard:** View order history, update contact details, and manage a simulated account balance.
- **Order placement:** Calculate prices according to restaurant type, check available balance, and validate minimum order requirements for premium restaurants.
- **Order lookup:** Search orders by order code, customer, or restaurant.
- **Reports:** Sort and filter customers, orders, restaurants, and riders, and calculate total order values.
- **Data storage:** Save and load application records using local text files.

## Technologies

- Java
- JavaFX and FXML
- Scene Builder
- Eclipse IDE

## Programming Concepts

- Object-oriented design, inheritance, and polymorphism
- Java collections, including ArrayList, HashMap, and Hashtable
- Generics and wildcards
- Comparable and custom Comparators
- Java Streams, lambda expressions, and predicates
- Custom exceptions and input validation
- File input/output

## Academic Context

Developed in a two-person team across four consecutive course assignments, with responsibility divided between application screens and classes.

## Current Status

This repository contains an academic prototype. Customer screens, administrator customer management, order lookup, and reports are implemented.

Restaurant, rider, and restaurant-administrator management screens are not connected in this version. Rider login currently displays a placeholder message.

Balances and payments are simulated; the application does not process real payments.

## Project Structure

- `src/application/` — Java source files, controllers, and FXML screens
- `src/module-info.java` — Java module configuration
- `build.fxbuild` — JavaFX build configuration

## Runtime Requirements

The application requires a Java Development Kit (JDK) and a compatible JavaFX SDK. Its entry point is `application.Main`.

JavaFX must be configured in your development environment before running the application.
