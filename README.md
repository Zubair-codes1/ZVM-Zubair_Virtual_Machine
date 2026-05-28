# **ZVM - ZUBAIR VIRTUAL MACHINE**

## **What is a Virtual Machine?**

A virtual machine is a complete software resource that acts as its own physical computer
whilst being seperate from the actual device it is running on. In simpler terms, it is a 
software based computer within a real computer. It has its own virtual CPU, memory and storage.
Virtual Machines provide a great way of mimicking other architectures and running them on your device.
They are also very useful in providing a protective layer over the actual hardware, as any malware or 
cyberattacks are carried out on the virtual machine rather than the actual hardware (although in recent
years there has been an increase in malware designed to bypass them).

## **What is the ZVM?**

The ZVM is a custom-built stack-based virtual machine that is turing complete and comes with a
complete toolchain. This includes a custom ISA (Instruction Set Architecture), an assembler (along with a custom bytecode), 
a virtual machine to run the programs and a global heap to store dynamic memory such as strings. Programs can either be run through binary or through
the .asm files straight away. The ZVM has its own Fetch-Decode-Execute (FDE) cycle along with a program stack
to manage its memory. It also uses a program counter (typical of CPUs) to keep track of lines allowing for both sequential
and non-sequential (functions/recursion/loops) programs to be run.

## **Program Structure**

```
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
```

## **ARCHITECTURE**

```
[ Your Source Code ] (.asm)
         │
         ▼
 ┌───────────────┐
 │   Zembler     │ ──► Pass 1: Scan Labels & Build Constant Pool
 │  (Assembler)  │ ──► Pass 2: Map Opcodes & Emit 12-byte Instructions
 └───────────────┘
         │
         ▼
[ Binary Bytecode ] (.bin)
         │
         ▼
 ┌───────────────┐
 │  ZVM Engine   │ ──► Dual Memory: [ Call Stack Frames ] (Local Variables)
 │   (Runtime)   │                  [ Global Byte Heap ] (Dynamic Strings)
 └───────────────┘
```

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

### **ZVM Engine - Strings**

Prints "ZVM Engine is Online" twenty times.

```asm
; --- Initialize Loop Control Variables ---
PUSH 0
STORE loop_counter

; --- Load String References ---
PUSH_STR "ZVM Engine is Online" ; Allocated inside Constant Pool via Assembler
STORE message_ptr

:print_loop
LOAD loop_counter
PUSH 20
LT                  ; Evaluates: loop_counter < 20
                    ; Pushes strictly 1 if true, 0 if false.

JIF :exit_program   ; If loop condition is strictly 0 (False), break out

LOAD message_ptr    ; Fetch the heap base address pointer
PRINT_STR           ; Custom string printer processes target item

; Increment Loop Pointer
LOAD loop_counter
PUSH 1
ADD
STORE loop_counter
JUMP :print_loop

:exit_program
HALT
```

**Assemble And Run**

```
java -jar assembler.jar programs/dynamic_strings.asm -o output/dynamic_strings.bin
java -jar vm.jar output/dynamic_strings.bin
```


**Expected Output**

```
VM OUTPUT: ZVM Engine is Online (printed 20 times)
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

Strings can be used using a global heap which is accessed using a heap pointer. Strings are 
put into the heap using a length prefixed format in which the first 4 bytes represent the length of the string.

## **Design Decisions**

Building a VM comes with a lot of decision-making and considerations of tradeoffs. Below I list a few
features that I implemented in my VM and the possible tradeoffs of it compared to other methods.

1. Stack-based vs Register-based: I chose a stack-based architecture for its simpler implementation
and smaller ISA, making it more approachable for anyone writing programs for the ZVM.
The tradeoff is performance: register-based VMs are generally faster and more optimisable,
but that wasn't a priority here.
2. Why a two-pass assembler? A single-pass assembler can't resolve forward references 
— labels defined later in the program than the jump that targets them. 
The first pass builds a symbol table of all labels and constants upfront, 
so the second pass can encode every instruction correctly regardless of label order.
3. Why frames? Each function call gets its own frame on the call stack, 
holding its local variables and return address. This makes recursion work naturally
(each call is fully isolated) and lets me design LOCAL instructions that always target
the active frame without any ambiguity.
4. Why JAR files? JAR files makes running programs on the VM much easier without understanding
the actual functionality of the VM and the assembler. This makes the project more accessible.
