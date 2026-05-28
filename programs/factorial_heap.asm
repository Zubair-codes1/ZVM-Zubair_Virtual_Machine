; =========================================================
; fibonacci_heap_logger.asm (Fixed Stack Alignment)
; =========================================================

; --- Step 1: Allocate Strings ONCE Outside the Loop ---
PUSH_STR "--- ZVM FIBONACCI HEAP LOGGER INITIALIZED ---"
PRINT_STR

PUSH_STR "Logged Value: "
STORE log_msg_ptr

; --- Step 2: Initialize Global Tracking Variables ---
PUSH 0
STORE sequence_counter

; --- Step 3: Allocate Global Buffer Array for Results ---
PUSH 32
ALLOC 32
STORE buffer_address      ; This pointer will slide forward by 4 bytes each loop

; --- Step 4: Core Execution Loop Engine ---
:sequence_loop
LOAD sequence_counter
PUSH 8
LT
JIF :exit_suite

; Calculate Fibonacci value dynamically via scoped function frames
LOAD sequence_counter
CALL :calc_fib            ; Stack has: [fib_value]

LOAD buffer_address       ; Stack has: [fib_value, base_address]
SWAP                      ; Stack has: [base_address, fib_value]
STORE_HEAP 0              ; Pops fib_value, pops base_address, writes at base+0!

; --- REUSED STRING PRINTING ---
LOAD log_msg_ptr
PRINT_STR

LOAD buffer_address
LOAD_HEAP 0               ; Reads directly from current sliding base address + 0
PRINT                     ; This will now display the correct Fibonacci number!

; --- Step 5: Advance Iteration Pointers ---
LOAD sequence_counter
PUSH 1
ADD
STORE sequence_counter

; Slide our buffer address pointer forward by 4 bytes for the next calculation
LOAD buffer_address
PUSH 4
ADD
STORE buffer_address

JUMP :sequence_loop

; --- Step 6: Recursive Fibonacci Function Blueprint ---
:calc_fib
STORE_LOCAL 0

; Check Base Case 0: n == 0
LOAD_LOCAL 0
PUSH 0
EQ
JIF :check_base_1
PUSH 0
RET

:check_base_1
; Check Base Case 1: n == 1
LOAD_LOCAL 0
PUSH 1
EQ
JIF :recursive_branch
PUSH 1
RET

:recursive_branch
; Formula: fib(n-1) + fib(n-2)
LOAD_LOCAL 0
PUSH 1
SUB
CALL :calc_fib

LOAD_LOCAL 0
PUSH 2
SUB
CALL :calc_fib

ADD
RET

:exit_suite
DUMP_STACK
HALT