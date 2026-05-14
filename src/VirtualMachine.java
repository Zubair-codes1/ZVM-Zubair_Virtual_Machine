import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class VirtualMachine {
    private int programCounter;
    private Stack<Integer> programStack;
    private Stack<Integer> functionReturnStack;
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
        this.functionReturnStack = new Stack<>();
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
        opCodeMapper.put("PUSH", OpCode.PUSH);
        opCodeMapper.put("ADD", OpCode.ADD);
        opCodeMapper.put("SUB", OpCode.SUB);
        opCodeMapper.put("MULT", OpCode.MULT);
        opCodeMapper.put("DIV", OpCode.DIV);
        opCodeMapper.put("MOD", OpCode.MOD);
        opCodeMapper.put("GT", OpCode.GT);
        opCodeMapper.put("LT", OpCode.LT);
        opCodeMapper.put("EQ", OpCode.EQ);
        opCodeMapper.put("JUMP", OpCode.JUMP);
        opCodeMapper.put("JIT", OpCode.JIT);
        opCodeMapper.put("JIF", OpCode.JIF);
        opCodeMapper.put("STORE", OpCode.STORE);
        opCodeMapper.put("LOAD", OpCode.LOAD);
        opCodeMapper.put("PRINT", OpCode.PRINT);
        opCodeMapper.put("INPUT", OpCode.INPUT);
        opCodeMapper.put("POP", OpCode.POP);
        opCodeMapper.put("DUP", OpCode.DUP);
        opCodeMapper.put("AND", OpCode.AND);
        opCodeMapper.put("OR", OpCode.OR);
        opCodeMapper.put("XOR", OpCode.XOR);
        opCodeMapper.put("NOT", OpCode.NOT);
        opCodeMapper.put("CALL", OpCode.CALL);
        opCodeMapper.put("RET", OpCode.RET);
        opCodeMapper.put("HALT",  OpCode.HALT);
    }

    public void loadFromFile(String filePath) {
        try {
            // This reads the entire file into a List<String> automatically
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            ArrayList<String> sanitizedLines = new ArrayList<>();
            for (String line : lines) {
                line = line.trim();
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                    continue;
                }
                sanitizedLines.add(line);
            }

            // Pass that list directly into your existing method
            loadProgramIntoStorage(new ArrayList<>(sanitizedLines));

            System.out.println("Program loaded successfully from: " + filePath);
        } catch (IOException e) {
            System.err.println("Error: Could not read file at " + filePath);
            e.printStackTrace();
        }
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
        for (int i = 0; i < programStorage.size(); i++) {
            String line = programStorage.get(i).trim();
            if (line.startsWith(":")) {
                labels.put(line.substring(1).toUpperCase(), instructionCounter);
                addressToLabel.put(instructionCounter, line.substring(1).toUpperCase());
            }else{
                instructionCounter++;
            }
        }

        for (String line : programStorage) {
            if (line.startsWith(":") || line.isEmpty()) {
                continue;
            }
            Instruction instruction = decode(line);
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

                executeOneStep(instruction);
                programCounter++;
            }
        } catch (VirtualMachineException e) {
            System.err.println("\n[VM EXECUTION ERROR]: " + e.getMessage());
            System.err.println("Occurred at PC: " + programCounter);

            printFunctionStackTrace();
        }catch (Exception e) {
            System.err.println("[INTERNAL EXECUTION ERROR]: " + e.getMessage());
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

    private Instruction decode(String instruction) {
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
            return new Instruction(opcode, Integer.parseInt(operandStr));
        }

        // LOAD/STORE Logic
        if (opcode.equals(OpCode.LOAD) || opcode.equals(OpCode.STORE)) {
            if (operandStr == null) throw new VirtualMachineException(command + " requires a variable name");
            if (!constantPool.contains(operandStr)) {
                constantPool.add(operandStr);
            }
            return new Instruction(opcode, constantPool.indexOf(operandStr));
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
                return new Instruction(opcode, labels.get(searchLabel));
            }
            throw new VirtualMachineException("Error: label '" + searchLabel + "' does not exist");
        }

        // Default for HALT, ADD, etc. (No operand needed)
        return new Instruction(opcode, 0);
    }

    private void executeOneStep(Instruction instruction) {
        switch (instruction.opcode()) {
            case PUSH -> handlePush(instruction);
            case ADD -> handleAdd();
            case SUB -> handleSub();
            case MULT -> handleMult();
            case DIV -> handleDiv();
            case MOD -> handleMod();
            case GT -> handleComparison("GT");
            case LT -> handleComparison("LT");
            case EQ -> handleComparison("EQ");
            case JUMP -> handleJump(instruction, "JUMP");
            case JIT -> handleJump(instruction, "JIT");
            case JIF -> handleJump(instruction, "JIF");
            case CALL -> handleCall(instruction);
            case RET -> handleRet();
            case STORE -> handleStore(instruction);
            case LOAD -> handleLoad(instruction);
            case PRINT -> handlePrint();
            case INPUT -> handleInput();
            case POP -> handlePop();
            case DUP -> handleDup();
            case AND -> handleAnd();
            case OR -> handleOr();
            case XOR -> handleXor();
            case NOT -> handleNot();
            case HALT -> handleHalt();
            default -> System.out.println("Unknown instruction!");
        }
    }

    private void handlePush(Instruction instruction) {
        if (instruction.operand() == null) {
            throw new VirtualMachineException("Error: PUSH command requires an operand!");
        }
        programStack.push(instruction.operand());
    }

    private void handleAdd() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: ADD command requires at least two operands!");
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value1 + value2;
        programStack.push(sum);
    }

    private void handleSub() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: SUB command requires at least two operands!");
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value2 - value1;
        programStack.push(sum);
    }

    private void handleMult() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: MULT command requires at least two operands!");

        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value1 * value2;
        programStack.push(sum);
    }

    private void handleDiv() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: DIV command requires at least two operands!");

        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();

        if (value1 == 0) {
            throw new VirtualMachineException("Error: Division by zero!");

        }

        int sum = value2 / value1;
        programStack.push(sum);
    }

    private void handleMod() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: MOD command requires at least two operands!");
        }

        int right = programStack.pop();
        int left = programStack.pop();
        if (right == 0) {
            throw new VirtualMachineException("Error: Division by zero!");
        }
        int sum = left % right;
        programStack.push(sum);
    }

    private void handleComparison(String compOperator) {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: Comparison command requires at least two operands!");
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        boolean result;

        switch (compOperator) {
            case "GT" -> result = value1 < value2;
            case "LT" -> result = value1 > value2;
            case "EQ" -> result = value1 == value2;
            default -> result = false;
        }

        int resultInt = result ? 1 : 0;
        programStack.push(resultInt);
    }

    private void handleJump(Instruction instruction, String jumpType) {
        if (instruction.operand() >= programStorage.size()) {
            throw new VirtualMachineException("Error: Jumping into the void");
        }

        switch (jumpType) {
            case "JUMP" -> {
                programCounter = instruction.operand() - 1;
            }
            case "JIT" -> {
                if (programStack.isEmpty()) {
                    throw new VirtualMachineException("Error: Jumping into nothing");
                }

                int trueOrFalse = programStack.pop();
                if (trueOrFalse != 0 && trueOrFalse != 1) {
                    throw new VirtualMachineException("Error: Invalid boolean");
                }
                if (trueOrFalse == 1) {
                    programCounter = instruction.operand() - 1;
                }
            }
            case "JIF" -> {
                if (programStack.isEmpty()) {
                    throw new VirtualMachineException("Error: Jumping into nothing");
                }

                int trueOrFalse = programStack.pop();
                if (trueOrFalse != 0 && trueOrFalse != 1) {
                    throw new VirtualMachineException("Error: Invalid boolean");
                }
                if (trueOrFalse == 0) {
                    programCounter = instruction.operand() - 1;
                }
            }
        }
    }

    private void handleCall(Instruction instruction) {
        if (instruction.operand() >= programStorage.size()) {
            throw new VirtualMachineException("Error: Calling into nothing");
        }

        functionReturnStack.push(programCounter + 1);
        programCounter = instruction.operand() - 1;
    }

    private void handleRet() {
        if (functionReturnStack.isEmpty()) {
            throw new VirtualMachineException("Error: Returning nothing");
        }
        programCounter = functionReturnStack.pop() - 1;
    }

    private void handleStore(Instruction instruction) {
        if (instruction.operand() >= constantPool.size()) {
            throw new VirtualMachineException("Error: Store command requires variables!");
        }

        String variableName = constantPool.get(instruction.operand());
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: Store command requires data!");
        }
        int value = programStack.pop();
        globalVariables.put(variableName, value);
    }

    private void handleLoad(Instruction instruction) {
        if (instruction.operand() >= constantPool.size()) {
            throw new VirtualMachineException("Error: Load command overflow!");
        }

        String variableName = constantPool.get(instruction.operand());
        if (!globalVariables.containsKey(variableName)) {
            throw new VirtualMachineException("Error: Variable not found!");
        }
        int value = globalVariables.get(variableName);
        programStack.push(value);
    }

    private void handlePrint() {
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: No data to print!");
        }

        int value = programStack.pop();
        System.out.println("VM OUTPUT: " + value);
    }

    private void handleInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("> INPUT REQUIRED (Integer): ");

        // Check if the next token is actually a number
        while (!sc.hasNextInt()) {
            System.out.println("Error: That's not a valid integer. Try again.");
            System.out.print("> ");
            sc.next(); // Clear the "bad" input from the buffer
        }
        int value = sc.nextInt();
        programStack.push(value);
    }

    private void handlePop() {
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: No data to pop!");
        }

        programStack.pop();
    }

    private void handleDup() {
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: No data to duplicate!");

        }
        int value = programStack.peek();
        programStack.push(value);
    }

    private void handleAnd() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: No data to compare!");
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = (right != 0 && left !=0) ? 1 : 0;
        programStack.push(finalValue);
    }

    private void handleOr() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: No data to compare!");
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = (right != 0 || left !=0) ? 1 : 0;
        programStack.push(finalValue);
    }

    private void handleXor() {
        if (programStack.size() < 2) {
            throw new VirtualMachineException("Error: No data to compare!");
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = ((right != 0 && left == 0) || (right == 0 && left != 0)) ? 1 : 0;
        programStack.push(finalValue);
    }

    private void handleNot() {
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: No data to compare!");
        }

        int right = programStack.pop();
        int finalValue = (right == 0) ? 1 : 0;
        programStack.push(finalValue);
    }

    private void handleHalt() {
        isRunning = false;
        programCounter = 0;
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
        System.err.println("Stack trace (Most recent call first):");
        Stack<Integer> stack = (Stack<Integer>) functionReturnStack.clone();
        while (!stack.isEmpty()) {
            Integer operand = stack.pop();
            System.err.println("    at address " + operand + " (inside " + getFunctionName(programCounter) + ")");
        }
    }
}
