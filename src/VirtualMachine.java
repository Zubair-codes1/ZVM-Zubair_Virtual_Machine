import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class VirtualMachine {
    private int programCounter;
    private Stack<Integer> programStack;
    private ArrayList<String> programStorage;
    private ArrayList<Instruction> executableInstructions;
    private Map<String, Integer> labels;
    private Map<String, Integer> globalVariables;
    private ArrayList<String> constantPool;
    private final Map<String, OpCode> opCodeMapper;
    private boolean isRunning;

    public VirtualMachine() {
        this.programCounter = 0;
        this.programStack = new Stack<>();
        this.programStorage = new ArrayList<>();
        this.executableInstructions = new ArrayList<>();
        this.labels = new HashMap<>();
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
        while (isRunning) {
            Instruction instruction = fetchInstruction();
            if (instruction == null) {
                isRunning = false;
                break;
            }

            boolean error = executeOneStep(instruction);
            if (error) {
                break;
            }

            programCounter++;
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
            throw new IllegalArgumentException("Unknown command: " + command);
        }

        // SAFETY CHECK: Only try to get operand if the array has more than 1 part
        String operandStr = (instructionParts.length > 1) ? instructionParts[1].trim() : null;

        // Handle Label prefixing
        if (operandStr != null && operandStr.startsWith(":")) {
            operandStr = operandStr.substring(1);
        }

        // PUSH Logic
        if (opcode.equals(OpCode.PUSH)) {
            if (operandStr == null) throw new IllegalArgumentException("PUSH requires a number");
            return new Instruction(opcode, Integer.parseInt(operandStr));
        }

        // LOAD/STORE Logic
        if (opcode.equals(OpCode.LOAD) || opcode.equals(OpCode.STORE)) {
            if (operandStr == null) throw new IllegalArgumentException(command + " requires a variable name");
            if (!constantPool.contains(operandStr)) {
                constantPool.add(operandStr);
            }
            return new Instruction(opcode, constantPool.indexOf(operandStr));
        }

        // JUMP Logic (Fixing the Uppercase Label bug)
        if (opcode.equals(OpCode.JUMP) || opcode.equals(OpCode.JIT) || opcode.equals(OpCode.JIF)) {
            if (operandStr == null) throw new IllegalArgumentException(command + " requires a label");

            // Use toUpperCase() to match how Pass 1 stores them
            String searchLabel = operandStr.toUpperCase();
            if (labels.containsKey(searchLabel)) {
                return new Instruction(opcode, labels.get(searchLabel));
            }
            throw new IllegalArgumentException("Error: label '" + searchLabel + "' does not exist");
        }

        // Default for HALT, ADD, etc. (No operand needed)
        return new Instruction(opcode, 0);
    }

    private boolean executeOneStep(Instruction instruction) {
        switch (instruction.opcode()) {
            case PUSH -> { return handlePush(instruction); }
            case ADD -> { return handleAdd(); }
            case SUB -> { return handleSub(); }
            case MULT -> { return handleMult(); }
            case DIV -> { return handleDiv(); }
            case MOD -> { return handleMod(); }
            case GT -> { return handleComparison("GT"); }
            case LT -> { return handleComparison("LT"); }
            case EQ -> { return handleComparison("EQ"); }
            case JUMP -> { return handleJump(instruction, "JUMP"); }
            case JIT -> { return handleJump(instruction, "JIT"); }
            case JIF -> { return handleJump(instruction, "JIF"); }
            case STORE -> { return handleStore(instruction); }
            case LOAD -> { return handleLoad(instruction); }
            case PRINT -> {return handlePrint(); }
            case INPUT -> { return handleInput(); }
            case POP -> { return handlePop(); }
            case DUP -> { return handleDup(); }
            case AND -> { return handleAnd(); }
            case OR -> { return handleOr(); }
            case XOR -> { return handleXor(); }
            case NOT -> { return handleNot(); }
            case HALT -> { return handleHalt(); }
            default -> {
                System.out.println("Unknown instruction!");
                return true;
            }
        }
    }

    private boolean handlePush(Instruction instruction) {
        if (instruction.operand() == null) {
            System.out.println("Error: PUSH command requires an operand!");
            isRunning = false;
            return true;
        }
        programStack.push(instruction.operand());
        return false;
    }

    private boolean handleAdd() {
        if (programStack.size() < 2) {
            System.out.println("Error: ADD command requires at least two operands!");
            isRunning = false;
            return true;
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value1 + value2;
        programStack.push(sum);
        return false;
    }

    private boolean handleSub() {
        if (programStack.size() < 2) {
            System.out.println("Error: SUB command requires at least two operands!");
            isRunning = false;
            return true;
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value2 - value1;
        programStack.push(sum);
        return false;
    }

    private boolean handleMult() {
        if (programStack.size() < 2) {
            System.out.println("Error: MULT command requires at least two operands!");
            isRunning = false;
            return true;
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();
        int sum = value1 * value2;
        programStack.push(sum);
        return false;
    }

    private boolean handleDiv() {
        if (programStack.size() < 2) {
            System.out.println("Error: DIV command requires at least two operands!");
            isRunning = false;
            return true;
        }
        int value1 = programStack.pop();
        int value2 = programStack.pop();

        if (value1 == 0) {
            System.out.println("Error: Division by zero!");
            isRunning = false;
            return true;
        }

        int sum = value2 / value1;
        programStack.push(sum);
        return false;
    }

    private boolean handleMod() {
        if (programStack.size() < 2) {
            System.out.println("Error: MOD command requires at least two operands!");
            isRunning = false;
            return true;
        }

        int right = programStack.pop();
        int left = programStack.pop();
        if (right == 0) {
            System.out.println("Error: Division by zero!");
            isRunning = false;
            return true;
        }
        int sum = left % right;
        programStack.push(sum);
        return false;
    }

    private boolean handleComparison(String compOperator) {
        if (programStack.size() < 2) {
            System.out.println("Error: Comparison command requires at least two operands!");
            isRunning = false;
            return true;
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
        return false;
    }

    private boolean handleJump(Instruction instruction, String jumpType) {
        if (instruction.operand() >= programStorage.size()) {
            System.out.println("Error: Jumping into the void");
            isRunning = false;
            return true;
        }

        switch (jumpType) {
            case "JUMP" -> {
                programCounter = instruction.operand() - 1;
            }
            case "JIT" -> {
                if (programStack.isEmpty()) {
                    System.out.println("Error: Jumping into nothing");
                    isRunning = false;
                    return true;
                }

                int trueOrFalse = programStack.pop();
                if (trueOrFalse != 0 && trueOrFalse != 1) {
                    System.out.println("Error: Invalid boolean");
                    isRunning = false;
                    return true;
                }
                if (trueOrFalse == 1) {
                    programCounter = instruction.operand() - 1;
                }
            }
            case "JIF" -> {
                if (programStack.isEmpty()) {
                    System.out.println("Error: Jumping into nothing");
                    isRunning = false;
                    return true;
                }

                int trueOrFalse = programStack.pop();
                if (trueOrFalse != 0 && trueOrFalse != 1) {
                    System.out.println("Error: Invalid boolean");
                    isRunning = false;
                    return true;
                }
                if (trueOrFalse == 0) {
                    programCounter = instruction.operand() - 1;
                }
            }
        }
        return false;
    }

    private boolean handleStore(Instruction instruction) {
        if (instruction.operand() >= constantPool.size()) {
            System.out.println("Error: Store command requires variables!");
            isRunning = false;
            return true;
        }

        String variableName = constantPool.get(instruction.operand());
        if (programStack.isEmpty()) {
            System.out.println("Error: Store command requires data!");
            isRunning = false;
            return true;
        }
        int value = programStack.pop();
        globalVariables.put(variableName, value);
        return false;
    }

    private boolean handleLoad(Instruction instruction) {
        if (instruction.operand() >= constantPool.size()) {
            System.out.println("Error: Load command overflow!");
            isRunning = false;
            return true;
        }

        String variableName = constantPool.get(instruction.operand());
        if (!globalVariables.containsKey(variableName)) {
            System.out.println("Error: Variable not found!");
            isRunning = false;
            return true;
        }
        int value = globalVariables.get(variableName);
        programStack.push(value);
        return false;
    }

    private boolean handlePrint() {
        if (programStack.isEmpty()) {
            System.out.println("Error: No data to print!");
            isRunning = false;
            return true;
        }

        int value = programStack.pop();
        System.out.println("VM OUTPUT: " + value);
        return false;
    }

    private boolean handleInput() {
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
        return false;
    }

    private boolean handlePop() {
        if (programStack.isEmpty()) {
            System.out.println("Error: No data to pop!");
            isRunning = false;
            return true;
        }

        programStack.pop();
        return false;
    }

    private boolean handleDup() {
        if (programStack.isEmpty()) {
            System.out.println("Error: No data to duplicate!");
            isRunning = false;
            return true;
        }
        int value = programStack.peek();
        programStack.push(value);
        return false;
    }

    private boolean handleAnd() {
        if (programStack.size() < 2) {
            System.out.println("Error: No data to compare!");
            isRunning = false;
            return true;
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = (right != 0 && left !=0) ? 1 : 0;
        programStack.push(finalValue);
        return false;
    }

    private boolean handleOr() {
        if (programStack.size() < 2) {
            System.out.println("Error: No data to compare!");
            isRunning = false;
            return true;
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = (right != 0 || left !=0) ? 1 : 0;
        programStack.push(finalValue);
        return false;
    }

    private boolean handleXor() {
        if (programStack.size() < 2) {
            System.out.println("Error: No data to compare!");
            isRunning = false;
            return true;
        }

        int right = programStack.pop();
        int left = programStack.pop();
        int finalValue = ((right != 0 && left == 0) || (right == 0 && left != 0)) ? 1 : 0;
        programStack.push(finalValue);
        return false;
    }

    private boolean handleNot() {
        if (programStack.isEmpty()) {
            System.out.println("Error: No data to compare!");
            isRunning = false;
            return true;
        }

        int right = programStack.pop();
        int finalValue = (right == 0) ? 1 : 0;
        programStack.push(finalValue);
        return false;
    }

    private boolean handleHalt() {
        isRunning = false;
        programCounter = 0;
        return true;
    }

    public void returnStackData() {
        while (!programStack.isEmpty()) {
            Integer operand = programStack.pop();
            System.out.println(operand);
        }
    }
}
