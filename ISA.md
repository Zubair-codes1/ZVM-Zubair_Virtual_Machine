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


## **Opcode Table**

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
| INC_LOCAL   | 0x27      | None                       | [a] -> [a+1]   (local memory)                   | Underflow                                 |
| DEC_LOCAL   | 0x28      | None                       | [a] -> [a-1]   (local memory)                   | Underflow                                 |
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
