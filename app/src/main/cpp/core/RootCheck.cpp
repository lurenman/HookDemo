//
// Created by td on 2024/4/2.
//

#include "RootCheck.hpp"


bool RootCheck::getroot() {
    DECL_ROOT_CHECK_ITEMS
    const char *stringarray[] = {arr_elem0, arr_elem1, arr_elem2, arr_elem3, arr_elem4, arr_elem5,
                                 arr_elem6, arr_elem7, arr_elem8, arr_elem9, arr_elem10,
                                 arr_elem11, arr_elem12, arr_elem13, arr_elem14, arr_elem15,
                                 arr_elem16, arr_elem17};
    int result = false;
    size_t files_num = sizeof(stringarray) / sizeof(stringarray[0]);
    for (size_t i = 0; i < files_num; i++) {
#if defined(__arm__)
        int ret = syscall(__NR_access, stringarray[i],F_OK);
#else
        int ret = access(stringarray[i], F_OK);
#endif
        if (ret == 0) {
            result = true;
            break;
        }
    }
    return result;
}
