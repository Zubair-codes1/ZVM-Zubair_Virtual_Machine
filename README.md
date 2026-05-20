# **ZVM - ZUBAIR VIRTUAL MACHINE**

The ZVM is a custom-built virtual machine that is turing complete and comes with a
complete toolchain. This includes a custom ISA, an assembler (along with a custom bytecode)
and a virtual machine to run the programs. Programs can either be run through binary or through
the .asm files straight away.

## **Program Structure**

````
VirtualMachine/
├── assembler/
│   ├── src/
│   │   ├── Assembler.java
│   │   ├── AssemblerException.java
│   │   ├── BinaryWriter.java
│   │   ├── CodeGenerator.java
│   │   ├── EncodedInstruction.java
│   │   ├── Lexer.java
│   │   ├── Main.java
│   │   ├── ParsedLine.java
│   │   ├── Parser.java
│   │   ├── SymbolTable.java
│   │   ├── Token.java
│   │   └── TokenType.java
│   └── tests/
│       ├── LexerTest.java
│       ├── ParserTest.java
│       ├── SymbolTableTest.java
│       ├── CodeGeneratorTest.java
│       └── FullAssemblerTest.java
├── vm/
│   ├── src/
│   │   ├── BinaryLoader.java
│   │   ├── BranchingHandler.java
│   │   ├── ControlHandler.java
│   │   ├── Frame.java
│   │   ├── Instruction.java
│   │   ├── InstructionHandler.java
│   │   ├── IOHandler.java
│   │   ├── LoadedProgram.java
│   │   ├── LogicHandler.java
│   │   ├── Main.java
│   │   ├── MathHandler.java
│   │   ├── MemoryHandler.java
│   │   ├── OpCode.java
│   │   ├── OpCodeCategory.java
│   │   ├── ScopeCategory.java
│   │   ├── StackHandler.java
│   │   ├── VirtualMachine.java
│   │   └── VirtualMachineException.java
│   └── tests/
│       ├── BinaryLoaderTest.java
│       └── VirtualMachineTest.java
├── programs/
│   ├── hello_world.asm
│   ├── factorial.asm
│   └── fibonacci.asm
├── output/
│   └── .gitkeep
├── docs/
│   ├── ISA.md
│   └── BYTECODE.md
├── assembler.jar
├── vm.jar
├── .gitignore
└── README.md
````

## **ISA**

The instruction set architecture is based on the functionality of a stack based 
virtual machine. Hence, all of its operations only have one operand or none. The ISA
is turing complete with full capability for functions, recursion, loops and branching.
The program also produces a stack trace in case of any invalid operations that are passed
into the program. The ISA also has an accompanying Bytecode format that is used by the assembler
to encode the instructions in binary using hex values. You can find the link to both of these below:

[ISA Documentation](docs/ISA.md)  
[Bytecode Format](docs/BYTECODE.md)

## **Assembling Programs**

```
java -jar assembler.jar <input.asm> -o <output.bin>
```

## **Running Binary Programs**

```
java -jar vm.jar <program.bin>
```

## **Example Programs**

### Factorial

Calculating 5!

```asm
CALL :factorial
HALT

:factorial
PUSH 5
STORE_LOCAL 0
PUSH 1
STORE_LOCAL 1

:loop
LOAD_LOCAL 1
LOAD_LOCAL 0
MULT
STORE_LOCAL 1
DEC_LOCAL 0
LOAD_LOCAL 0
PUSH 0
GT
JIT :loop

LOAD_LOCAL 1
PRINT
RET
```

**Assemble And Run**

```
java -jar assembler.jar programs/factorial.asm -o output/factorial.bin
java -jar vm.jar output/factorial.bin
```

**Expected Output**

```
VM OUTPUT: 120
```

---

### **Hello World**

Prints "Hello World".

```asm
PUSH 72
PRINT_CHAR
PUSH 101
PRINT_CHAR
PUSH 108
PRINT_CHAR
PUSH 108
PRINT_CHAR
PUSH 111
PRINT_CHAR
PUSH 32
PRINT_CHAR
PUSH 87
PRINT_CHAR
PUSH 111
PRINT_CHAR
PUSH 114
PRINT_CHAR
PUSH 108
PRINT_CHAR
PUSH 100
PRINT_CHAR
HALT
```

**Assemble And Run**

```
java -jar assembler.jar programs/hello_world.asm -o output/hello_world.bin
java -jar vm.jar output/hello_world.bin
```


**Expected Output**

```
VM OUTPUT: H
VM OUTPUT: e
VM OUTPUT: l
VM OUTPUT: l
VM OUTPUT: o
VM OUTPUT:  
VM OUTPUT: W
VM OUTPUT: o
VM OUTPUT: r
VM OUTPUT: l
VM OUTPUT: d
```

---

### **Fibonacci**

Prints the 9th fibonacci number (34)

```asm
CALL :fibonacci
HALT

:fibonacci
PUSH 0
STORE_LOCAL 0
PUSH 1
STORE_LOCAL 1
PUSH 8
STORE_LOCAL 2

:loop
LOAD_LOCAL 0
LOAD_LOCAL 1
ADD
STORE_LOCAL 3

LOAD_LOCAL 1
STORE_LOCAL 0

LOAD_LOCAL 3
STORE_LOCAL 1

DEC_LOCAL 2
LOAD_LOCAL 2
PUSH 0
GT
JIT :loop

LOAD_LOCAL 1
PRINT
RET
```

**Assemble And Run**

```
java -jar assembler.jar programs/fibonacci.asm -o output/fibonacci.bin
java -jar vm.jar output/fibonacci.bin
```

**Expected Output**

```
VM OUTPUT: 34
```

---

## **Technical Details**

The Assembler runs on a two pass system. It first looks at the entire
.asm file and strips it of comments, labels and empty lines. In the first pass
it also builds a symbol table which includes all the labels and a constant pool.
In the second pass, the assembler generates the actual binary instructions in their 
bytecode format.

The approach to functions in my assembly language is based on frames.
Each function has an associated frame. This frame stores its local variables.
Every time a function is called, a new frame is created and put in the call stack.
The frame at the top of the stack is the current active frame, and it is where all the
LOCAL instructions act upon. Once the RET instruction is executed, the active frame is removed.
