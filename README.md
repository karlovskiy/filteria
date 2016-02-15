Filteria
========
[![Maven Central](https://img.shields.io/maven-central/v/info.karlovskiy/filteria.svg)](https://img.shields.io/maven-central/v/info.karlovskiy/filteria.svg) [![GitHub license](https://img.shields.io/github/license/karlovskiy/filteria.svg)](https://img.shields.io/github/license/karlovskiy/filteria.svg)

API for generation filtering and sorting restrictions from special syntax parameters and appending them to the criteria.
You need only two special formatted parameters (for example form parameters from you javascript frontend)
for multiple properties filtering and sorting.

Usage
-----
To use it you have to create filteria object and append it to the criteria object.
For example:

```java
Session session = sessionFactory.getCurrentSession();
Criteria criteria = session.createCriteria(Parameter.class);
criteria.createAlias("type", "type", INNER_JOIN);
Filteria.create(sessionFactory, Parameter.class)
                .parameter("type", "type.code")
                .parameter("id", "id")
                .parameter("name", "name")
                .parameter("description", "desc")
                .parameter("mandatory", "mandate")
                .parameter("value", "value")
                .filter(filter)
                .sorter(sort)
                .appendTo(criteria);
List<Parameter> filteredAndSortedParameters = criteria.list();
```
where ```filter``` and ```sort``` are two string with special syntax.
To use filtering or sorting you need to map parameters with entity properties by ```parameter``` method first.
Nested properties for aliased joined entities are supported.

#### Filtering
Filtering string has format:
```
parameter1::<value1>|parameter2::<value2>|...|parameterN::<valueN>
```
for example:
```
type::!MAIN|id::>=100|name::*vely param|mandatory::true|value::*igo gravitational wave astro*
```
Currently filteria supports operations:

Operation | Supported types | Description
--- | --- | ---
**EQUALS** | string, number, boolean | ```MAIN``` - string, number ( ```true``` , ```false``` - boolean )
**NOT EQUALS** | string, number | ```!MAIN```
**STARTS WITH** | string | ```mai*```
**ENDS WITH** | string | ```*ain```
**CONTAINS** | string | ```*ai*```
**LESS** | number | ```<10```
**LESS OR EQUALS** | number | ```<=10```
**GREATER** | number | ```>0```
**GREATER OR EQUALS** | number | ```>=-30```
**IS NULL** | string, number | ```^null```
**IS NOT NULL** | string, number | ```!^null```

#### Sorting
Sorting string has format:
```
parameter1|-parameter2|...|parameterN
```
for example:
```
id|-type|mandatory
```
parameter name only for ASC sorting, parameter with ```-``` sign for DESC sorting.

Maven dependency
----------------

```xml
<dependency>
    <groupId>info.karlovskiy</groupId>
    <artifactId>filteria</artifactId>
    <version>0.6</version>
</dependency>
```
