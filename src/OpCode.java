public enum OpCode {
    HALT(0),
    PUSH(1),
    ADD(2),
    STORE(3),
    LOAD(4),
    JUMP(5),
    JIT(6),
    JIF(7),
    GT(8),
    LT(9),
    EQ(10),
    SUB(11),
    MULT(12),
    DIV(13),
    MOD(14),
    PRINT(15),
    POP(16),
    DUP(17),
    INPUT(18),
    AND(19),
    OR(20),
    XOR(21),
    NOT(22);

    private int value;
    OpCode(int value) {
        this.value = value;
    }
}
