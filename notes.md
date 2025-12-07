# My notes

## Phase 0

- Start with Piece move tests
- For the test, we can use the generate functionality of IntelliJ (`cmd + N`) to populate the hashCode, equals, and override functions.
- To generate variables for the class constructor, right click on constructor and select Show Context Actions, then Bind 

### Class Notes - 9/10/25
- static function can be called without needing to create object of the class type

#### Overload methods, constructors
- Default Constructor (does nothing)
- parameterized constructor (same setup, just takes in parameter and uses it)
- Copy constructor (create copy of object)

#### Enum
- all values possible for this type, prevents typos
- Enum.valueOf() calls the 
- Checking the Peak enum (class) - `Peak.class`

#### Encapsulation
- Prevent external code from being concerned with internal workings of an object
- clone/copy the scores to prevent changing the object directly
- `extends` is keyword in Java for inheritance

#### Comparing
- `==` does well at comparing primitive types, but not objects
- Use equals() method when comparing objects, makes sure to override to compare what you want to

#### Records
- Immutable objects
- simple, safe to share and publish freely without need to make defensive copies

#### Data structure for board
- two-dimensional array

#### Rules of moves
- Math is your friend
- simplify map (how do the numbers change as you move across the board?)


### Class Notes - 9/16/25

#### Interface (implements)
- Definition, implement the functionality in an actual class
- Functionality
- Need to set up each function when function is implemented
- Abstraction so that the functionality is available to any classes that implement the interface
- Once you set the type to the type of the interface, it doesn't need to know the implementation
- If you reference object as the type of the interface, you can't use functions specific to the class

#### Inheritance (extends)
- Gain qualities from another class that say certain things about it
- color printer extends printer
- super is key word, used to get functionality from class that was extended from or access variables in the superclass
- Extend is always extending a class, implement is used for interface

#### Abstract classes
- combination of inheritance and interface
- define as abstract 
- Can't define an object of abstract class, but can be extended by other class which will need to implement abstract function(s)
- 

### Class Note - 9/23/25

#### Domain Driven Design 
- Decompose the structure of something and how that will look in context of actors, tasks, objects, and interactions
- Properties - what it is
- Methods - what it does
- Sequence diagram - functionality across time (functions on top happen first, then progress down the list)

| Relationship | Description                                                       | Example                   |
|--------------|-------------------------------------------------------------------|---------------------------|
| Is-A         | Inheritance (often represented by extending)                      | Programmer is Person      |
| Has-A        | Encapsulation (often represented by field)                        | Programmer has Computer   |
| Uses-A       | Transient association (often represented with a method parameter) | Person uses Car to travel |

#### Design Principles
- Decomposition: break more complicated code into smaller pieces
  - Prevents external code from being concerned with the internal workings of an object
- Simplicity: Keep it simple smarty
  - Everything should be made as simple as possible, but not simpler
- YAGNI: You're not going to need it
  - Always implement things when you actually need them, never when you just foresee that you need them.
  - Don't try to pre-optimize your code for the future.
- DRY: Don't repeat yourself
  - When you copy and paste, you're programming by coincidence
  - Break down into smaller functions if different areas are doing the same thing
  - Make sure that, as functionality changes, you don't overcomplicate the solution if you just need to create separate functionality
- High Cohesion low coupling
  - Cohesion is the glue that holds a module together. Coupling is the degree to which one module relies on another.
  - Things that work together for similar functionality
- Single Responsibility
  - A module should be responsible to one, and only one, actor.
  - Actor has only one reason to use you.
    - Chess Program - Participant (Observer, Player) both use same ChessGame object
- Open closed 
  - You should be able to extend the behavior of a system without having to modify that system.
  - open for extension, closed for modification
    - Write class, don't let people change it. They can extend and create their own functionality
- Liskov substitution
  - Actually implement interface
  - If a subclass violates the expectations of an interface (e.g., changes meaning, throws unexpected errors, refuses to do part of the job), it breaks LSP.
- Interface Segregation
  - Keep interfaces cohesive
  - Clients should not be forced to depend on methods they do not use.
- Dependency Inversion
  - Make dependencies parameters
  - High-level modules should not depend on low-level modules. Both should depend on abstractions.
  - Persist data - When move is made, you don't need to know very specifically where the data will be stored, put that in the main functionality
- POLA: Principle of least astonishment
  - If your API makes people scream ‘Wait, WHAT?’, you’ve broken POLA.
- Make a Move
  - GetPiece, addPiece
  - add a movePiece function to set previous spot to null and set new position to contain this piece

## Phase 1

### Input Output (I/O)
- input -> taking in data of some kind and storing it
- output -> taking data and representing in the way they know best

### IO Streams
- input stream 
  - read bytes from input stream
- output stream
  - write to stream and store bytes there (bytes represented by numbers)
- final - the pointer points to whatever object and this object can have its values edited, but the pointer cannot move

### Interfaces
- Reader
- Writer
- Generic for objects, specify if it is file, keyboard, etc.

### Generics
- Pass in whatever type you want

## Phase 2 

### Chess Design
- Stalemate - any moves, check
- separate out user/ game functionalities (user - register, login, logout; game - list games, create game, join game)

## Phase 3 

Code from ResponseException that may be of use later

```declarative
//    public static ResponseException fromJson(String json) {
//        var map = new Gson().fromJson(json, HashMap.class);
//        var status = Code.valueOf(map.get("status").toString());
//        String message = map.get("message").toString();
//        return new ResponseException(status, message);
//    }

//    public Code code() {
//        return code;
//    }
//
//    public static Code fromHttpStatusCode(int httpStatusCode) {
//        return switch (httpStatusCode) {
//            case 500 -> Code.ServerError;
//            case 400 -> Code.BadRequestError;
//            case 401 -> Code.UnauthorizedError;
//            case 403 -> Code.AlreadyTakenError;
//            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
//        };
//    }
```