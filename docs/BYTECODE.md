# **ZVM (ZUBAIR VIRTUAL MACHINE) BYTECODE STRUCTURE**

---
## **What is a .bin file?**

A .bin file is a binary file that can be run by the ZVM.
The .bin file is created by the assembler (or Zembler) when it parses the .asm file, and it
is read by the ZVM during execution of the program.
---
## **Byte Order**

All multibyte integers are represented in big-endian format for easier readability.
For reference, big-endian format refers to the method of storing the bytes in memory.
Big endian stores them chronologically from left to right similiar to how we read.
---
## **File Structure**

```
┌─────────────────┐   <- byte 0
│     HEADER      │   15 bytes of metadata
├─────────────────┤   <- byte 15
│  CONSTANT POOL  │   variable length, one entry per variable name
├─────────────────┤   <- byte 15+ - size of the constant pool
│  INSTRUCTIONS   │   N x 12 bytes, one record per instruction
└─────────────────┘
```
---
### **Header**

The header contains 15 bytes of metadata used by the ZVM to understand
how to process the instructions and constants that are going to appear later in the
.bin file. Here are the key things found in the header:


| Field Name          | Size (Bytes) | Description                                                                                                | Example                        |
|---------------------|--------------|------------------------------------------------------------------------------------------------------------|--------------------------------|
| Magic Number        | 2            | Identifies the file as ZVM binary. Required for the program to run.                                        | 1A 2B                          |
| Version             | 1            | Identifies the version of the binary format. This allows for the VM to be compatible with multiple version | 01                             |
| Entry Point         | 4            | Instruction index - where the execution of the program actually begins. Will be set to 0 for now.          | 00 00 00 00                    |
| Instruction Count   | 4            | Number of 12 byte instructions in the actual part of the program.                                          | 00 00 00 32  (50 instructions) |
| Constant Pool Count | 4            | How many strings are present within the constant pool section                                              | 00 00 00 15  (21 constants)    |

### **Constant Pool**

To store the constant pool, I use a length prefixed format. In this format, the ASCII values are converted to hexadecimal
representations (no limit on the number of bytes), but they are preceded by one byte of information that tells
the virtual machine the length of that specific constant. 

For example the word "hello" would be represented in hexadcimal as: 05 68 65 6C 6C 6F.
In this scenario, the 05 represents the length of the word so that the virtual machine knows how many bytes to read, and 
the rest of the values represent the actual ASCII representations of the word.

### **Instruction Record**

| Byte Range | Size    | Contents                                       |
|------------|---------|------------------------------------------------|
| Byte 0     | 1 byte  | Represents the opcode of the instruction       |
| Byte 1-3   | 3 bytes | Padding to make it eight bytes per instruction |
| Byte 4-7   | 4 bytes | Operand as a 32 bit integer                    |
| Byte 8-11  | 4 bytes | Line number of instruction                     |

For example, the instruction "PUSH 100" would be represented as: 10 00 00 00 00 00 00 64 00 00 00 00.
Another example with "HALT" which takes no operands: 00 00 00 00 00 00 00 00 00 00 00 00

Instructions with no operands will default to 00 00 00 00 as their operand between bytes 4-7

---

## **Example Program**

Here is an example program in the assembly code and then its equivalent in bytecode format.

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

```
; header
1A 2B                   ; Two bytes for file type check
01                      ; version
00 00 00 00             ; entry point of program - instruction 0
00 00 00 0C             ; 12 instructions
00 00 00 01             ; 1 constant ("secret")

; Constant Pool
06 73 65 63 72 65 74    ; Represents "secret" - 06 represents the letters and the rest are the ASCII hex values for 
                        ; each letter

; Instructions
10 00 00 00 00 00 00 64 00 00 00 01 ; PUSH 100
51 00 00 00 00 00 00 00 00 00 00 02 ; STORE secret - since secret is the first and only constant it is represented by 0
43 00 00 00 00 00 00 06 00 00 00 03 ; CALL :testlogic - since testlogic is declared after 6 instructions, its operand is 6
50 00 00 00 00 00 00 00 00 00 00 04 ; LOAD secret
61 00 00 00 00 00 00 00 00 00 00 05 ; PRINT_CHAR - takes no operand so operand set to 0
00 00 00 00 00 00 00 00 00 00 00 06 ; HALT - takes no operand as well
10 00 00 00 00 00 00 0A 00 00 00 07 ; PUSH 10
53 00 00 00 00 00 00 00 00 00 00 08 ; STORE_LOCAL 0 - since operand of STORE_LOCAL is 0 it is also represented as 0 in binary
27 00 00 00 00 00 00 00 00 00 00 09 ; INC_LOCAL 0
52 00 00 00 00 00 00 00 00 00 00 0A ; LOAD_LOCAL 0
60 00 00 00 00 00 00 00 00 00 00 0B ; PRINT - takes no operand
44 00 00 00 00 00 00 00 00 00 00 0C ; RET
```