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
