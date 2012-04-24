
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

* IdentifierExpression --Added constructor and getters
* Import --Added constructor and getters
* FailStatement --Added constructor and getters
* UntilLoop --Added constructor and getters
* ModifiedStatement.ModifierType.UNLESS
* ModifiedStatement.ModifierType.UNTIL
    * (The previous two are members of enum ModifierType in ModifiedStatement)

Resubmit for Homework2:
* IncrementStatement.java Entity
* CallStatement Entity (for wait time addition)
* TryStatement.java
* manatee.jj
    * Parallel Assignment
    * Increment/Decrement
    * TYPEDEC (<ID> where TYPE() used to be)

Homework3:
analyze methods that need to be filled out/updated:

* Type (for object types)
* IncrementStatement --done
* CallStatement (for wait time)
* ParallelAssignmentStatement --done
* TryStatement --done
* FailStatement
* UntilLoop --done
* Import


