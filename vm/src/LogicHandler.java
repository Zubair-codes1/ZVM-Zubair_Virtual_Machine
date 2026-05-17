import java.util.Stack;

public class LogicHandler implements InstructionHandler{
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        Stack<Integer> programStack = virtualMachine.getStack();

        switch (opcode) {
            case EQ -> handleComparison(programStack, "EQ");
            case NEQ -> handleComparison(programStack, "NEQ");
            case GT -> handleComparison(programStack, "GT");
            case LT -> handleComparison(programStack, "LT");
            case GTE -> handleComparison(programStack, "GTE");
            case LTE -> handleComparison(programStack, "LTE");
            case AND -> handleComparison(programStack, "AND");
            case OR -> handleComparison(programStack, "OR");
            case XOR -> handleComparison(programStack, "XOR");
            case NOT -> handleNot(programStack);
            default -> throw new VirtualMachineException("Error: Unknown opcode");
        }

    }

    private void handleComparison(Stack<Integer> programStack, String type) {
        if  (programStack.size() < 2) { throw new VirtualMachineException("Error: Comparison Requires at least 2 values!"); }
        int right = programStack.pop();
        int left =  programStack.pop();

        int result;

        switch (type) {
            case "EQ" -> result = (right == left) ? 1 : 0;
            case "NEQ" -> result = (right != left) ? 1 : 0;
            case "GT" -> result = (left > right) ? 1 : 0;
            case "LT" -> result = (left < right) ? 1 : 0;
            case "GTE" -> result = (left >= right) ? 1 : 0;
            case "LTE" -> result = (left <= right) ? 1 : 0;
            case "AND" -> result = (right == 1 && left == 1) ? 1 : 0;
            case "OR" -> result = (right == 1 || left == 1) ? 1 : 0;
            case "XOR" -> result = ((left == 1 && right == 0) || (right == 1 && left == 0)) ? 1 : 0;
            default -> throw new VirtualMachineException("Error: Invalid Comparison Type!");
        }

        programStack.push(result);

    }

    private void handleNot(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.isEmpty()) {  throw new VirtualMachineException("Error: Logics Requires at least 2 values!"); }
        int value = programStack.pop() == 1 ? 0 : 1;
        programStack.push(value);
    }
}
