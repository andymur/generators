# Generators

Generators is library which main goal is to provide random (but not only) data generators of varied types.

# Why do I need this?

One can use the library just as random stream of data.

It could be primitive values (e.g. integer or string) or a complex objects with random data inside.
Data values are not necessarily random, they could be taken from different data sources or restricted by different options of generator (let's say taken from some range).

Common library usage is creation of huge data files with random or semi-random data or create data streams from these data.

# Main concepts

## Generator

## Destination

# Example of usage


## Annotations

@Generated(pattern = "", length = 10, from = resource)
private String generatedString;

@Options(from = resource, number = 10)
private Generator<T> bbb;


# Supported types

## Scalars

### Primitives

Integer
Long
Double
String
Character
Date
Time
Datetime

### Objects

## Sequences

# Supported sources

# Supported destinations