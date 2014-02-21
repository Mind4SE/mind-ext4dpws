#ifndef TESTS_COMMON_H
#define TESTS_COMMON_H

#define REMOTE_EXCEPTION  "RemoteException"
#define HELLO_WORLD "helloworld"
#define HELLOA "HelloA"
#define HELLOB "HelloB"
#define HELLOC "HelloC"
#define BYEA "ByeA"
#define BYEB "ByeB"
#define BYEC "ByeC"
#define SIZE 256
#define TIME 123456789
#define INT_A 655
#define INT_B 700
#define INT_C 755
#define DOUBLE_A 1.77
#define ARRAY_SIZE 10
#define ARRAY_SIZE_CHAR "0123456789"

static struct structA myStructA;

struct structA a1;
struct structA a2;
struct structA a3;

static struct structOfArray serverStructOfArray;

static int int_array [ARRAY_SIZE]  = {18,16,14,12,10,8,6,4,2,0};
static int reverse_int_array [ARRAY_SIZE]  = {0,2,4,6,8,10,12,14,16,18};
static int out_int_array [ARRAY_SIZE]  = {20,22,24,26,28,30,32,34,36,38};

static short short_array [ARRAY_SIZE]  = {-18,-16,-14,-12,10,8,6,4,2,0};
static float float_array [ARRAY_SIZE]  = {18,-16.23,-14.14,-12.12,10.10,8.8,6.6,4.4,2.2,0};
static long long_array [ARRAY_SIZE]  = {1800,-1623,-1414,-1212,1010,88,66,44,22,0};

static long client_long_array [ARRAY_SIZE]  = {180000,-162300,-141400,-121200,101000,8800,6600,4400,2200,0};

static const char* client_string_array [ARRAY_SIZE]  = {"Fractal","programming","component","model","through","the","dpws","stack"," : ","dpwscore2.2.0"};


#endif
