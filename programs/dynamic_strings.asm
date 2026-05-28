; =========================================================
; dynamic_strings.asm
; Tests: PUSH_STR, IDENTIFIERS, PRINT_STR, LT, and Loops via JIT
; =========================================================

; --- Initialize Loop Control Variables ---
PUSH 0
STORE loop_counter

; --- Load String References ---
PUSH_STR "ZVM Engine is Online" ; Allocated inside Constant Pool via Assembler
STORE message_ptr

:print_loop
LOAD loop_counter
PUSH 20
LT                  ; Evaluates: loop_counter < 20
                    ; Pushes strictly 1 if true, 0 if false.

JIF :exit_program   ; If loop condition is strictly 0 (False), break out

LOAD message_ptr    ; Fetch the heap base address pointer
PRINT_STR           ; Custom string printer processes target item

; Increment Loop Pointer
LOAD loop_counter
PUSH 1
ADD
STORE loop_counter
JUMP :print_loop

:exit_program
HALT