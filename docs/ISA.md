# **ZVM (ZUBAIR VIRTUAL MACHINE) ISA**

**The assembly language I designed is a turing complete
stack based language used as part of my virtual machine.
It contains the key functionalities required in any
assembly language including STACK, MATH, MEMORY, I/O,
BRANCHING, LOGIC/COMPARISON and CONTROL operations.**

## **Syntax Rules**

Here are some syntax rules for my language:
1. Any lines beginning with "#" or ";" are instantly not considered
    by the virtual machine and will be ignored.
2. Any lines beginning with ":" will be considered as lines that declare
    labels that will be used throughout the program. No
    actual execution will take place in these lines as they are
    only used to set up the actual execution process.
3. Commands/Instructions are placed at the beginning of each line and
    are always placed before any operands.
4. Not all instructions have operands and none have more than one.

## **Branching**

Labels are declared using ':'. Any lines beginning with ':' will be ignored by
the Virtual Machine when the program is being run. However, before the program is run,
these labels are found and stored along with a unique integer value for them that the 
Virtual Machine will use to jump to those labels. However, for the LOAD/STORE instructions,
the labels are declared instantly when the program is being run.

## **Opcode Table**

### **Reading The Opcode Table**

This table consists of the opcode along with its corresponding hex value, operand, 
any stack changes and any possible errors. When reading the stack changes section bear in mind that
the right most element is the top of the stack. However, when multiple values are popped and
operations such as ADD are performed on them, then they are placed left to right in the equation.
For the JIT (Jump if true) and JIF (jump if false) instructions, the top most element of the stack is checked
for 0 or 1. 

| OPCODE      | HEX VALUE | OPERAND                    | STACK CHANGES                                   | ERRORS                                    |
|-------------|-----------|----------------------------|-------------------------------------------------|-------------------------------------------|
| HALT        | 0x00      | None                       | [a, b ,c] -> / (ends program)                   | None                                      |
| NOOP        | 0x01      | None                       | [a, b, c] -> [a, b, c]                          | None                                      |
| DUMP_STACK  | 0x02      | None                       | [a, b, c] -> [a, b, c]                          | None                                      |
| PUSH        | 0x10      | Pushed item                | [a] -> [a, b]                                   | None                                      |
| POP         | 0x11      | None                       | [a, b] -> [a]                                   | Underflow                                 |
| DUP         | 0x12      | None                       | [a, b] -> [a, b, b]                             | Underflow                                 |
| SWAP        | 0x13      | None                       | [a, b] -> [b, a]                                | Underflow                                 |
| OVER        | 0x14      | None                       | [a, b] -> [a, b, a]                             | Underflow                                 |
| ADD         | 0x20      | None                       | [a, b] -> [a+b]                                 | Underflow                                 |
| SUB         | 0x21      | None                       | [a, b] -> [a-b]                                 | Underflow                                 |
| MULT        | 0x22      | None                       | [a, b] -> [a*b]                                 | Underflow                                 |
| DIV         | 0x23      | None                       | [a, b] -> [a/b]                                 | Underflow and division by zero            |
| MOD         | 0x24      | None                       | [a, b] -> [a%b]                                 | Underflow and division by zero            |
| LSHIFT      | 0x25      | None                       | [a, b] -> [a << b]                              | Underflow                                 |
| RSHIFT      | 0x26      | None                       | [a, b] -> [a >> b]                              | Underflow                                 |
| INC_LOCAL   | 0x27      | Local slot index           | [a, b] -> [a, b]   (changes local variables)    | Underflow                                 |
| DEC_LOCAL   | 0x28      | Local slot index           | [a, b] -> [a, b]   (changes local variables)    | Underflow                                 |
| EQ          | 0x30      | None                       | [a, b] -> [a == b]                              | Underflow                                 |
| NEQ         | 0x31      | None                       | [a, b] -> [a != b]                              | Underflow                                 |
| GT          | 0x32      | None                       | [a, b] -> [a > b]                               | Underflow                                 |
| LT          | 0x33      | None                       | [a, b] -> [a < b]                               | Underflow                                 |
| GTE         | 0x34      | None                       | [a, b] -> [a >= b]                              | Underflow                                 |
| LTE         | 0x35      | None                       | [a, b] -> [a <= b]                              | Underflow                                 |
| AND         | 0x36      | None                       | [a, b] -> [a AND b]                             | Underflow                                 |
| OR          | 0x37      | None                       | [a, b] -> [a OR b]                              | Underflow                                 |
| XOR         | 0x38      | None                       | [a, b] -> [a XOR b]                             | Underflow                                 |
| NOT         | 0x39      | None                       | [a] -> [NOT A]                                  | Underflow                                 |
| JUMP        | 0x40      | Jump Label                 | [a, b] -> [a, b]                                | Label not existing                        |
| JIT         | 0x41      | Jump Label                 | [a, b] -> [a]  (b used to check truthiness)     | Underflow and label not existing          |
| JIF         | 0x42      | Jump Label                 | [a, b] -> [a]  (b used to check truthiness)     | Underflow and label not existing          |
| CALL        | 0x43      | Function Label             | [a, b] -> [a, b]   (changes program counter)    | Label not existing                        |
| RET         | 0x44      | None                       | [a, b] -> [a, b]   (changes program counter)    | None                                      |
| LOAD        | 0x50      | Memory Address Name        | [a, b] -> [a, b ,c] (loads from variables)      | Underflow and memory address not existing |
| STORE       | 0x51      | Memory Address Name        | [a, b] -> [a] (stores into variables)           | Underflow and memory address not existing |
| LOAD_LOCAL  | 0x52      | Local Memory Address Index | [a, b] -> [a, b, c] (loads from local variable) | Underflow and memory address not existing |
| STORE_LOCAL | 0x53      | Local Memory Address Index | [a, b] -> [a] (stores into local variable)      | Underflow and memory address not existing |
| PRINT       | 0x60      | None                       | [a, b] -> [a]   (prints b)                      | Underflow                                 |
| PRINT_CHAR  | 0x61      | None                       | [a, b] -> [a] (prints b as ASCII)               | Underflow and invalid ASCII               |
| INPUT       | 0x62      | None                       | [a, b] -> [a, b, c]                             | None                                      |


## **Scopes**

In my program instructions can have two scopes; either global
or local. The vast majority of the instructions perform operations on
the global stack and change values that are common to all the program.

However, certain instructions are local. These local variables work on the premice of
frames. Each function call creates a frame which contants an array that holds the
local variables for that function call. These local instructions affect this array of
variables in the current active frame (the latest function that has been called).

## **Calling Convention**

Before any of the program runs, the Virtual Machine scans the entire program once to find any labels.
These labels are indicated by beginning with a colon. Once all of these labels are found, then the program
can be traced starting from the first line. As soon as a CALL instruction is noticed, it instantly gets
the operand (function label) attached to it and jumps to the line where this label was found when the scanning
took place. After the function is finished, a RET keyword takes the program counter back to the original place that
the function was called from and the program continues from there until the end.

## **Example Program**

```
PUSH 100        ; Pushes 100 on to the stack  
STORE secret    ; Global variable "secret" = 100  
CALL :testlogic ; Calls the testlogic function  
LOAD secret     ; Load 100  
PRINT_CHAR      ; Expected Output: d (since 100 = d in ASCII)  
HALT            ; ends the program       

:testlogic      ; testlogic function  
PUSH 10         ; Pushes 10 onto the stack  
STORE_LOCAL 0   ; Local index 0 = 10  
INC_LOCAL 0     ; Local index 0 = 11  
LOAD_LOCAL 0    ; Push 11 to stack  
PRINT           ; Expected Output: 11  
RET             ; Return to function call
```