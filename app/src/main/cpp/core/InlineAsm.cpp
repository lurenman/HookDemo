//
// Created by DELL on 2024-04-05.
//
#include "InlineAsm.h"

/**
 *  https://gcc.gnu.org/onlinedocs/gcc/Extended-Asm.html
    https://blog.csdn.net/tidyjiang/article/details/52138598
 * @param base
 * @return
 */
long InlineAsm::test_inline_asm_add(long base) {
    long result = 0;
#ifdef __aarch64__
    return result;
#else
    //arm
    __asm__ __volatile__("mov r0, %[base]\r\n"
                         "add r0, r0 \r\n"
                         "mov %[result], r0\r\n"

            :[result] "=r"(result)     //传出来的结果(输出)
    :[base] "r"(base)                //传进去的参数(输入)
    );
    return result;
#endif
}
