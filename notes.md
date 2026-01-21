# My notes

## Java Fundamentals

### Command Line Instructions
- java files are broken down to .class files 
- `javac` command compiles code into class files
- Files created are used by JVM, (Java Virtual Machine) to recreate the program
- `main` method must be present in only one file, but it must be present in the program
- `javac -d bin/ X.java Y.java Z.java`
- `java -cp bin/ X arg1 arg2 ...`
  - cp indicates class path, where the class is so it can be used
- `static` keyword says there is only one copy of the object

## Classes and Objects

- When comparing enum values, you can use `==` instead of `o.equals(x)`
- `getClass()` comparison option for equals method restricts comparing things to only allow objects of the same class to be equal 
  - a subclass object cannot be considered equal to an object of the parent class

## Records
- Data class - transfer data across system 
  - POJO - plain old Java object
  - Once you create Record, it cannot be modified
    - You must create a new object with modified data
  - automatic getters, setters, constructor, hashCode, equals, toString
  - Can add custom 
```
public record Person(String name, int age) {}
```

## Inheritance
- Create code that can be reused elsewhere
- Person <- Student <- GradStudent <- PhdStudent
- Each subclass has access to super class's functionality, variables
- subclasses may add additional data, methods that are unique to it
- Can override functions from super class
- Polymorphism - Parent class can represent any of its subclasses
  - Can create array of Vehicle and put the subclass objects Car, Truck, Boat, Airplane all in the array
  - Can declare method on a super class cast
