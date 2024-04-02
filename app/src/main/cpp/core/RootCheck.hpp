//
// Created by td on 2024/4/2.
//
#include <string>
#include <unistd.h>

#ifndef HOOKDEMO_ROOTCHECK_HPP
#define HOOKDEMO_ROOTCHECK_HPP
#define DECL_ROOT_CHECK_ITEMS \
    char *arr_elem0 = "/su"; \
    char *arr_elem1 = "/su/bin/su"; \
    char *arr_elem2 = "/sbin/su"; \
    char *arr_elem3 = "/data/local/xbin/su"; \
    char *arr_elem4 = "/data/local/bin/su"; \
    char *arr_elem5 = "/data/local/su"; \
    char *arr_elem6 = "/system/xbin/su"; \
    char *arr_elem7 = "/system/bin/su"; \
    char *arr_elem8 = "/system/sd/xbin/su"; \
    char *arr_elem9 = "/system/bin/failsafe/su"; \
    char *arr_elem10 = "/system/su"; \
    char *arr_elem11 = "/su/su.d"; \
    char *arr_elem12 = "/su/xbin"; \
    char *arr_elem13 = "/su/suhide"; \
    char *arr_elem14 = "/sbin_orig/su"; \
    char *arr_elem15 = "/system/bin/.ext/.su"; \
    char *arr_elem16 = "/system/usr/we-need-root/su-backup"; \
    char *arr_elem17 = "/vendor/bin/su";

class RootCheck {
public:
    static bool getroot();
};


#endif //HOOKDEMO_ROOTCHECK_HPP
