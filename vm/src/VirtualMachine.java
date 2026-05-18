import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VirtualMachine {
    private int programCounter;
    private Stack<Integer> programStack;
    private Stack<Frame> callStack;
    private ArrayList<String> programStorage;
    private ArrayList<Instruction> executableInstructions;
    private Map<String, Integer> labels;
    private TreeMap<Integer, String> addressToLabel;
    private Map<String, Integer> globalVariables;
    private ArrayList<String> constantPool;
    private final Map<String, OpCode> opCodeMapper;
    private boolean isRunning;

    public VirtualMachine() {
        this.programCounter = 0;
        this.programStack = new Stack<>();
        this.callStack = new Stack<>();
        this.programStorage = new ArrayList<>();
        this.executableInstructions = new ArrayList<>();
        this.labels = new HashMap<>();
        this.addressToLabel = new TreeMap<>();
        this.globalVariables = new HashMap<>();
        this.constantPool = new ArrayList<>();
        this.opCodeMapper = new HashMap<>();

        setOpCodeMapper();

        this.isRunning = true;
    }

    private void setOpCodeMapper() {
        // System and Control
        opCodeMapper.put("HALT", OpCode.HALT);
        opCodeMapper.put("NOOP", OpCode.NOOP);
        opCodeMapper.put("DUMP_STACK", OpCode.DUMP_STACK);

        // stack manipulation
        opCodeMapper.put("PUSH", OpCode.PUSH);
        opCodeMapper.put("POP", OpCode.POP);
        opCodeMapper.put("DUP", OpCode.DUP);
        opCodeMapper.put("SWAP", OpCode.SWAP);
        opCodeMapper.put("OVER", OpCode.OVER);

        // Arithmetic operations
        opCodeMapper.put("ADD", OpCode.ADD);
        opCodeMapper.put("SUB", OpCode.SUB);
        opCodeMapper.put("MULT", OpCode.MULT);
        opCodeMapper.put("DIV", OpCode.DIV);
        opCodeMapper.put("MOD", OpCode.MOD);
        opCodeMapper.put("LSHFT", OpCode.LSHIFT);
        opCodeMapper.put("RSHFT", OpCode.RSHIFT);
        opCodeMapper.put("INC_LOCAL", OpCode.INC_LOCAL);
        opCodeMapper.put("DEC_LOCAL", OpCode.DEC_LOCAL);

        // Logic and comparison
        opCodeMapper.put("EQ", OpCode.EQ);
        opCodeMapper.put("NEQ", OpCode.NEQ);
        opCodeMapper.put("GT", OpCode.GT);
        opCodeMapper.put("LT", OpCode.LT);
        opCodeMapper.put("GTE", OpCode.GTE);
        opCodeMapper.put("LTE", OpCode.LTE);
        opCodeMapper.put("AND", OpCode.AND);
        opCodeMapper.put("OR", OpCode.OR);
        opCodeMapper.put("XOR", OpCode.XOR);
        opCodeMapper.put("NOT", OpCode.NOT);

        // Branching and Subroutines
        opCodeMapper.put("JUMP", OpCode.JUMP);
        opCodeMapper.put("JIT", OpCode.JIT);
        opCodeMapper.put("JIF", OpCode.JIF);
        opCodeMapper.put("CALL", OpCode.CALL);
        opCodeMapper.put("RET", OpCode.RET);

        // Memory
        opCodeMapper.put("LOAD", OpCode.LOAD);
        opCodeMapper.put("STORE", OpCode.STORE);
        opCodeMapper.put("STORE_LOCAL", OpCode.STORE_LOCAL);
        opCodeMapper.put("LOAD_LOCAL", OpCode.LOAD_LOCAL);

        // I/O
        opCodeMapper.put("PRINT", OpCode.PRINT);
        opCodeMapper.put("PRINT_CHAR", OpCode.PRINT_CHAR);
        opCodeMapper.put("INPUT", OpCode.INPUT);
    }

    public void loadFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Pass the RAW lines, including blanks and comments
            loadProgramIntoStorage(new ArrayList<>(lines));
            System.out.println("Program loaded successfully from: " + filePath);
        } catch (IOException e) {
            System.err.println("Error: Could not read file at " + filePath);
        }
    }

    public void loadFromBinary(String filePath) {
        BinaryLoader loader = new BinaryLoader();
        LoadedProgram program = loader.loadProgram(filePath);
        this.executableInstructions = program.instructions();
        this.constantPool = new ArrayList<>(program.constantPool());
    }

    public void loadProgramIntoStorage(ArrayList<String> lines) {
        programStorage.clear();
        programStack.clear();
        labels.clear();
        globalVariables.clear();
        constantPool.clear();
        programCounter = 0;
        isRunning = true;

        programStorage.addAll(lines);

        int instructionCounter = 0;

        // Pass 1: Map Labels
        for (int i = 0; i < programStorage.size(); i++) {
            String line = programStorage.get(i).trim();

            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                continue;
            }

            if (line.startsWith(":")) {
                labels.put(line.substring(1).toUpperCase(), instructionCounter);
                addressToLabel.put(instructionCounter, line.substring(1).toUpperCase());
            } else {
                instructionCounter++;
            }
        }

        // Pass 2: Create Executable Instructions
        for (int i = 0; i < programStorage.size(); i++) {
            String line = programStorage.get(i).trim();

            // Skip labels, blanks, and comments
            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";") || line.startsWith(":")) {
                continue;
            }

            Instruction instruction = decode(line, i + 1);
            executableInstructions.add(instruction);
        }

    }

    public void executeProgram() {

        try {
            while (programCounter < executableInstructions.size() && isRunning) {
                Instruction instruction = fetchInstruction();
                // The Null Safety Check
                if (instruction == null) {
                    throw new VirtualMachineException("Null Instruction at address " + programCounter + ". Is there a gap in your code?");
                }

                if (instruction.opcode().getScope().equals(ScopeCategory.LOCAL) && callStack.isEmpty()) {
                    throw new VirtualMachineException("No Frame to be accessed!");
                }

                executeOneStep(instruction);
                programCounter++;
            }
        } catch (VirtualMachineException e) {
            System.err.println("\n[VM EXECUTION ERROR]: " + e.getMessage());
            printFunctionStackTrace();
        }catch (Exception e) {
            System.err.println("[INTERNAL EXECUTION ERROR]: " + e.getMessage() + " at address " + programCounter + ".");
        } finally {
            isRunning = false;
        }

    }

    private Instruction fetchInstruction() {
        if (!executableInstructions.isEmpty() && programCounter < executableInstructions.size()) {
            return executableInstructions.get(programCounter);
        }
        return null;
    }

    private Instruction decode(String instruction, Integer lineNumber) {
        instruction = instruction.trim();
        // Using \\s+ handles multiple spaces between words in a text file
        String[] instructionParts = instruction.split("\\s+");
        String command = instructionParts[0].toUpperCase().trim();

        OpCode opcode = opCodeMapper.get(command);
        if (opcode == null) {
            throw new VirtualMachineException("Unknown command: " + command);
        }

        // SAFETY CHECK: Only try to get operand if the array has more than 1 part
        String operandStr = (instructionParts.length > 1) ? instructionParts[1].trim() : null;

        // Handle Label prefixing
        if (operandStr != null && operandStr.startsWith(":")) {
            operandStr = operandStr.substring(1);
        }

        // PUSH Logic
        if (opcode.equals(OpCode.PUSH)) {
            if (operandStr == null) throw new VirtualMachineException("PUSH requires a number");
            return new Instruction(opcode, Integer.parseInt(operandStr), lineNumber);
        }

        // LOAD/STORE Logic
        if (
                opcode.equals(OpCode.LOAD) || opcode.equals(OpCode.STORE)
        ) {
            if (operandStr == null) throw new VirtualMachineException(command + " requires a variable name");
            if (!constantPool.contains(operandStr)) {
                constantPool.add(operandStr);
            }
            return new Instruction(opcode, constantPool.indexOf(operandStr), lineNumber);
        }

        // LOAD/STORE LOCAL logic
        if (opcode.equals(OpCode.LOAD_LOCAL) || opcode.equals(OpCode.STORE_LOCAL)) {
            if  (operandStr == null) throw new VirtualMachineException(command + " requires a variable name");
            return new Instruction(opcode, Integer.parseInt(operandStr), lineNumber);
        }

        // INC/DEC LOCAL logic
        if (opcode.equals(OpCode.INC_LOCAL) || opcode.equals(OpCode.DEC_LOCAL)) {
            if (operandStr == null) throw new VirtualMachineException(command + " requires a slot index");
            return new Instruction(opcode, Integer.parseInt(operandStr), lineNumber);
        }

        // JUMP Logic
        if (
                opcode.equals(OpCode.JUMP) || opcode.equals(OpCode.JIT) ||
                opcode.equals(OpCode.JIF) || opcode.equals(OpCode.CALL)
        ) {
            if (operandStr == null) throw new VirtualMachineException(command + " requires a label");

            // Use toUpperCase() to match how Pass 1 stores them
            String searchLabel = operandStr.toUpperCase();
            if (labels.containsKey(searchLabel)) {
                return new Instruction(opcode, labels.get(searchLabel), lineNumber);
            }
            throw new VirtualMachineException("Error: label '" + searchLabel + "' does not exist");
        }

        // Default for HALT, ADD, etc. (No operand needed)
        return new Instruction(opcode, 0, lineNumber);
    }

    private void executeOneStep(Instruction instruction) {
        OpCode opCode = instruction.opcode();
        switch (opCode.getCategory()) {
            case SYSTEM -> new ControlHandler().execute(instruction, opCode, this);
            case STACK ->  new StackHandler().execute(instruction, opCode, this);
            case MATH  -> new MathHandler().execute(instruction, opCode, this);
            case LOGIC ->  new LogicHandler().execute(instruction, opCode, this);
            case BRANCH -> new BranchingHandler().execute(instruction, opCode, this);
            case MEMORY ->  new MemoryHandler().execute(instruction, opCode, this);
            case IO ->  new IOHandler().execute(instruction, opCode, this);
            default -> throw new VirtualMachineException("Error: Unknown OpCode category!");
        }
    }

    public void returnStackData() {
        while (!programStack.isEmpty()) {
            Integer operand = programStack.pop();
            System.out.println(operand);
        }
    }

    public String getFunctionName(int programCounter) {
        Map.Entry<Integer, String> entry = addressToLabel.floorEntry(programCounter);
        return (entry != null) ? entry.getValue() : "MAIN";
    }

    public void printFunctionStackTrace() {
        System.err.println("\nStack Trace (Most recent call first):");

        // 1. Get the instruction that actually crashed
        Instruction crashInstr = executableInstructions.get(programCounter);
        System.err.println("  at line " + crashInstr.lineNumber() +
                " (inside :" + getFunctionName(programCounter) + ")");

        // 2. Walk back through the return stack
        for (int i = callStack.size() - 1; i >= 0; i--) {
            Frame frame = callStack.get(i);
            int returnAddress = frame.getReturnAddress();
            int callAddr = returnAddress - 1; // The address of the CALL instruction

            Instruction callInstr = executableInstructions.get(callAddr);
            System.err.println("  at line " + callInstr.lineNumber() +
                    " (inside :" + getFunctionName(callAddr) + ")");
        }
    }

    public Stack<Integer> getStack() {
        return programStack;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public ArrayList<String> getProgramStorage() {
        return programStorage;
    }


    public ArrayList<String> getConstantPool() {
        return constantPool;
    }

    public Map<String, Integer> getGlobalVariables() {
        return globalVariables;
    }

    public Frame getActiveFrame() {
        if (callStack.isEmpty()) throw new VirtualMachineException("Error: callStack is empty");
        return callStack.peek();
    }

    public Stack<Frame> getCallStack() {
        return callStack;
    }

    public void printGlobalVariables() {
        for (Map.Entry<String, Integer> entry : globalVariables.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public List<Instruction> getExecutableInstructions() {
        return executableInstructions;
    }
}
