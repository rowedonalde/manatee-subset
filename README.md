
This is a compiler for a subset of the Manatee Programming Language.

When complete, students will be able to fork this repository to build compilers
and interpreters for the full language.

Manatee is described at http://cs.lmu.edu/~ray/notes/manatee.

Here are some of the full language features not implemented in this subset:

* Modules --in grammar
* Codepoint escapes in character and string literals
* Object types and literals --in grammar (+expanded Type.java for object types)
* Streams for reading and writing --Gone
* Increment and decrement statements --in grammar
* Parallel assignment --in grammar
* Exceptions --in grammar
* Timers --in grammar
* `unless` and `until` modifiers --in grammar
* Bitwise operators --in grammar
* Modulo operator --in grammar
* Divides operator --in grammar
* Bitwise complement operator --in grammar

Entities that need to be added:

* IdentifierExpression
* Import
* FailStatement
* UntilLoop
* ModifiedStatement.ModifierType.UNLESS
* ModifiedStatement.ModifierType.UNTIL
    * (The previous two are constants in ModifiedStatement)

Methods that need to be added:
* 
