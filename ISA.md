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

| OPCODE      | HEX VALUE | OPERAND              | STACK CHANGES                 | ERRORS |
|-------------|-----------|----------------------|-------------------------------|--------|
| HALT        | 0x00      | None                 | [a, b ,c] -> / (ends program) | None   |
| NOOP        | 0x01      | None                 | [a, b, c] -> [a, b, c]        | None   |
| DUMP_STACK  | 0x02      | None                 | [a, b, c] -> [a, b, c]        | None   |
| PUSH        | 0x10      | Pushed item          | [a] -> [a, b]                 |        |
| POP         | 0x11      | None                 | [a, b] -> [a]                 |        |
| DUP         | 0x12      | None                 | [a, b] -> [a, b, b]           |        |
| SWAP        | 0x13      | None                 | [a, b] -> [b, a]              |        |
| OVER        | 0x14      | None                 | [a, b] -> [a, b, a]           |        |
| ADD         | 0x20      | None                 | [a, b] -> [a+b]               |        |
| SUB         | 0x21      | None                 | [a, b] -> [a-b]               |        |
| MULT        | 0x22      | None                 | [a, b] -> [a*b]               |        |
| DIV         | 0x23      | None                 | [a, b] -> [a/b]               |        |
| MOD         | 0x24      | None                 | [a, b] -> [a%b]               |        |
| LSHIFT      | 0x25      | None                 | [a, b] -> [a << b]            |        |
| RSHIFT      | 0x26      | None                 | [a, b] -> [a >> b]            |        |
| INC_LOCAL   | 0x27      | None                 | [a] -> [a+1]   (local memory) |        |
| DEC_LOCAL   | 0x28      | None                 | [a] -> [a-1]   (local memory) |        |
| EQ          | 0x30      | None                 | [a, b] -> [a == b]            |        |
| NEQ         | 0x31      | None                 | [a, b] -> [a != b]            |        |
| GT          | 0x32      | None                 | [a, b] -> [a > b]             |        |
| LT          | 0x33      | None                 | [a, b] -> [a < b]             |        |
| GTE         | 0x34      | None                 |                               |        |
| LTE         | 0x35      | None                 |                               |        |
| AND         | 0x36      | None                 |                               |        |
| OR          | 0x37      | None                 |                               |        |
| XOR         | 0x38      | None                 |                               |        |
| NOT         | 0x39      | None                 |                               |        |
| JUMP        | 0x40      | Jump Label           |                               |        |
| JIT         | 0x41      | Jump Label           |                               |        |
| JIF         | 0x42      | Jump Label           |                               |        |
| CALL        | 0x43      | Function Label       |                               |        |
| RET         | 0x44      | None                 |                               |        |
| LOAD        | 0x50      | Memory Address Name  |                               |        |
| STORE       | 0x51      | Memory Address Name  |                               |        |
| LOAD_LOCAL  | 0x52      | Memory Address Index |                               |        |
| STORE_LOCAL | 0x53      | Memory Address Index |                               |        |
| PRINT       | 0x60      | None                 |                               |        |
| PRINT_CHAR  | 0x61      | None                 |                               |        |
| INPUT       | 0x62      | None                 |                               |        |
