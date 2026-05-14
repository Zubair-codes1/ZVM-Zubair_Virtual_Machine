public enum OpCode {
    // System and Control
    HALT(0x00, OpCodeCategory.SYSTEM), NOOP(0x01, OpCodeCategory.SYSTEM),
    DUMP_STACK(0x02, OpCodeCategory.SYSTEM),

    // Stack manipulation
    PUSH(0x10, OpCodeCategory.STACK), POP(0x11, OpCodeCategory.STACK),
    DUP(0x12, OpCodeCategory.STACK), SWAP(0x13, OpCodeCategory.STACK),
    OVER(0x14, OpCodeCategory.STACK),

    // Arithmetic Operations
    ADD(0x20, OpCodeCategory.MATH), SUB(0x21, OpCodeCategory.MATH),
    MULT(0x22, OpCodeCategory.MATH), DIV(0x23, OpCodeCategory.MATH),
    MOD(0x24, OpCodeCategory.MATH), LSHIFT(0x25, OpCodeCategory.MATH),
    RSHIFT(0x26, OpCodeCategory.MATH),

    // Logic and Comparison
    EQ(0x30, OpCodeCategory.LOGIC), NEQ(0x31, OpCodeCategory.LOGIC),
    GT(0x32, OpCodeCategory.LOGIC), LT(0x33, OpCodeCategory.LOGIC),
    GTE(0x34, OpCodeCategory.LOGIC), LTE(0x35, OpCodeCategory.LOGIC),
    AND(0x36, OpCodeCategory.LOGIC), OR(0x37, OpCodeCategory.LOGIC),
    XOR(0x38, OpCodeCategory.LOGIC), NOT(0x39, OpCodeCategory.LOGIC),


    // Branching and Subroutines
    JUMP(0x40, OpCodeCategory.BRANCH), JIT(0x41, OpCodeCategory.BRANCH),
    JIF(0x42, OpCodeCategory.BRANCH), CALL(0x43, OpCodeCategory.BRANCH),
    RET(0x44, OpCodeCategory.BRANCH),

    // Memory
    LOAD(0x50, OpCodeCategory.MEMORY), STORE(0x51,  OpCodeCategory.MEMORY),

    // I/O
    PRINT(0x60, OpCodeCategory.IO), PRINT_CHAR(0x61, OpCodeCategory.IO),
    INPUT(0x62, OpCodeCategory.IO),;

    private int value;
    private OpCodeCategory category;
    OpCode(int value, OpCodeCategory category) {
        this.value = value;
        this.category = category;
    }

    public int getValue() {
        return value;
    }
    public OpCodeCategory getCategory() {
        return category;
    }
}
