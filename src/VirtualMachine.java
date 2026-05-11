import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class VirtualMachine {
    private int programCounter;
    private Stack<Integer> programStack;
    private ArrayList<String> programStorage;
    private Map<String, Integer> labels;
    private boolean isRunning;

    public VirtualMachine() {
        this.programCounter = 0;
        this.programStack = new Stack<>();
        this.programStorage = new ArrayList<>();
        this.labels = new HashMap<>();
        this.isRunning = true;
    }

    public void loadProgramIntoStorage(ArrayList<String> lines) {
        programStorage.clear();
        programStack.clear();
        labels.clear();
        programCounter = 0;
        isRunning = true;

        programStorage.addAll(lines);
        for (int i = 0; i < programStorage.size(); i++) {
            String line = programStorage.get(i).trim();
            if (line.startsWith(":")) {
                labels.put(line.substring(1).toUpperCase(), i);
            }
        }
    }

    public void executeProgram() {
        while (isRunning) {
            String instruction = fetchInstruction();
            if (instruction == null) {
                isRunning = false;
                break;
            }

            Instruction instructionObj = decode(instruction);
            boolean error = executeOneStep(instructionObj);
            if (error) {
                break;
            }

            programCounter++;
        }
    }

    private String fetchInstruction() {
        String instruction = "";
        if (!programStorage.isEmpty() && programCounter < programStorage.size()) {
            instruction = programStorage.get(programCounter);
            return instruction;
        }
        return null;
    }

    private Instruction decode(String instruction) {
        instruction = instruction.trim();
        String[] splitInstruction = instruction.split(" +");
        Integer operand;

        if (splitInstruction.length < 2) {
            operand = null;
        }else {
            try {
                if (splitInstruction.length == 2) {
                    operand = Integer.parseInt(splitInstruction[1].trim().toUpperCase());
                } else {
                    operand = null;
                }
            } catch (NumberFormatException e) {
                if (labels.containsKey(splitInstruction[1].trim().toUpperCase())) {
                    operand = labels.get(splitInstruction[1].trim().toUpperCase());
                }else{
                    System.out.println("Error: Invalid operand. Enter a number.");
                    operand = null;
                }
            }
        }

        String command = splitInstruction[0].trim().toUpperCase();
        return new Instruction(command, operand);
    }

    private boolean executeOneStep(Instruction instruction) {
        if (instruction.instruction().startsWith(":")) {
            return false;
        }
        switch (instruction.instruction()) {
            case "PUSH" -> {
                return handlePush(instruction);
            }
            case "ADD" -> {
                return handleAdd();
            }
            case "GT" -> {
                return handleComparison("GT");
            }
            case "LT" -> {
                return handleComparison("LT");
            }
            case "EQ" -> {
                return handleComparison("EQ");
            }
            case "JUMP" -> {
                return handleJump(instruction, "JUMP");
            }
            case "JIT" -> {
                return handleJump(instruction, "JIT");
            }
            case "JIF" -> {
                return handleJump(instruction, "JIF");
            }
            case "HALT" -> {
                return handleHalt();
            }
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
