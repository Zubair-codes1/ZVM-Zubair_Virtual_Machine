PUSH 100
STORE hello      ; Global variable "secret" = 100
CALL :testlogic
LOAD hello      ; Should still be 100
PRINT_CHAR              ; Expected Output: 100
HALT

:testlogic
    PUSH 10
    STORE_LOCAL 0   ; Local index 0 = 10
    INC_LOCAL 0    ; Local index 0 = 11
    LOAD_LOCAL 0    ; Push 11 to stack
    PRINT           ; Expected Output: 11
    RET