/**
 * Enum to store all instructions, their hex value,
 * their type and their scope.
 *
 * @author Zubair Abdul Matin
 */
public enum OpCode {
    // System and Control
    HALT(0x00, OpCodeCategory.SYSTEM, ScopeCategory.GLOBAL),
    NOOP(0x01, OpCodeCategory.SYSTEM, ScopeCategory.GLOBAL),
    DUMP_STACK(0x02, OpCodeCategory.SYSTEM, ScopeCategory.GLOBAL),

    // Stack manipulation
    PUSH(0x10, OpCodeCategory.STACK, ScopeCategory.GLOBAL),
    POP(0x11, OpCodeCategory.STACK, ScopeCategory.GLOBAL),
    DUP(0x12, OpCodeCategory.STACK, ScopeCategory.GLOBAL),
    SWAP(0x13, OpCodeCategory.STACK, ScopeCategory.GLOBAL),
    OVER(0x14, OpCodeCategory.STACK, ScopeCategory.GLOBAL),
    PUSH_STR(0x15, OpCodeCategory.STACK, ScopeCategory.GLOBAL),

    // Arithmetic Operations
    ADD(0x20, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    SUB(0x21, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    MULT(0x22, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    DIV(0x23, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    MOD(0x24, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    LSHIFT(0x25, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    RSHIFT(0x26, OpCodeCategory.MATH, ScopeCategory.GLOBAL),
    INC_LOCAL(0x27, OpCodeCategory.MATH, ScopeCategory.LOCAL),
    DEC_LOCAL(0x28, OpCodeCategory.MATH,  ScopeCategory.LOCAL),

    // Logic and Comparison
    EQ(0x30, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    NEQ(0x31, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    GT(0x32, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    LT(0x33, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    GTE(0x34, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    LTE(0x35, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    AND(0x36, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    OR(0x37, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    XOR(0x38, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),
    NOT(0x39, OpCodeCategory.LOGIC, ScopeCategory.GLOBAL),


    // Branching and Subroutines
    JUMP(0x40, OpCodeCategory.BRANCH, ScopeCategory.GLOBAL),
    JIT(0x41, OpCodeCategory.BRANCH, ScopeCategory.GLOBAL),
    JIF(0x42, OpCodeCategory.BRANCH, ScopeCategory.GLOBAL),
    CALL(0x43, OpCodeCategory.BRANCH, ScopeCategory.GLOBAL),
    RET(0x44, OpCodeCategory.BRANCH, ScopeCategory.GLOBAL),

    // Memory
    LOAD(0x50, OpCodeCategory.MEMORY, ScopeCategory.GLOBAL),
    STORE(0x51,  OpCodeCategory.MEMORY, ScopeCategory.GLOBAL),
    LOAD_LOCAL(0x52, OpCodeCategory.MEMORY, ScopeCategory.LOCAL),
    STORE_LOCAL(0x53, OpCodeCategory.MEMORY, ScopeCategory.LOCAL),
    ALLOC(0x54, OpCodeCategory.MEMORY, ScopeCategory.GLOBAL),
    STORE_HEAP(0x55, OpCodeCategory.MEMORY, ScopeCategory.GLOBAL),
    LOAD_HEAP(0x56, OpCodeCategory.MEMORY, ScopeCategory.LOCAL),

    // I/O
    PRINT(0x60, OpCodeCategory.IO, ScopeCategory.GLOBAL),
    PRINT_CHAR(0x61, OpCodeCategory.IO, ScopeCategory.GLOBAL),
    INPUT(0x62, OpCodeCategory.IO, ScopeCategory.GLOBAL),
    PRINT_STR(0x63, OpCodeCategory.IO, ScopeCategory.GLOBAL);

    // hex value
    private int value;
    // category
    private OpCodeCategory category;
    // scope
    private ScopeCategory scope;

    /**
     * Opcode constructor
     * @param value hex value
     * @param category type
     * @param scope scope
     */
    OpCode(int value, OpCodeCategory category, ScopeCategory scope) {
        this.value = value;
        this.category = category;
        this.scope = scope;
    }

    /**
     * get Hex value
     * @return hex value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get category
     * @return category
     */
    public OpCodeCategory getCategory() {
        return category;
    }

    /**
     * Get Scope
     * @return scope
     */
    public ScopeCategory getScope() {
        return scope;
    }
}
