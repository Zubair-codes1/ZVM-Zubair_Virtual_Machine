; =========================================================
; heap_math.asm (Now truly running globally!)
; =========================================================

PUSH 8
ALLOC 8           ; Allocates 8 bytes globally

DUP
PUSH 123456
STORE_HEAP 0      ; Modifies global heap index 0

DUP
PUSH -789
STORE_HEAP 4      ; Modifies global heap index 4

; --- Verification ---
DUP
LOAD_HEAP 0
PRINT             ; Output: 123456

LOAD_HEAP 4
PRINT             ; Output: -789

HALT