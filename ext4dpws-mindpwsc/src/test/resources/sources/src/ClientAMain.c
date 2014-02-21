#include <stdio.h>
#include <errno.h>
#include <unistd.h>

#include "testsConst.h"

#include "ClientA.c"
#include "ClientA2.c"


void METH(serviceA_suite) (){


	CALL(test01)();
	CALL(test02)();
	CALL(test03)();
	CALL(test04)();
	CALL(test05)();
	CALL(test06)();
	CALL(test07)();
	CALL(test08)();
	CALL(test09)();
	CALL(test10)();
	CALL(test11)();
	CALL(test12)();
	CALL(test13)();
	CALL(test15)();
	CALL(test16)();
	CALL(test17)();
	CALL(test18)();
	CALL(test20)();
	CALL(test21)();
	CALL(test22)();
	CALL(test23)();
	CALL(test24)();
	CALL(test25)();
	CALL(test26)();
	CALL(test27)();
	CALL(test28)();
	CALL(test29)();
	CALL(test30)();
	CALL(test31)();
	CALL(test32)();
	CALL(test33)();
	CALL(test34)();
	CALL(test35)();
	CALL(test36)();
	CALL(test37)();
	CALL(test38)();
	CALL(test39)();
	CALL(test40)();
	CALL(test41)();
	CALL(test42)();
	CALL(test43)();
	CALL(test44)();
	CALL(test45)();
	CALL(test46)();
	CALL(test47)();
	CALL(test48)();
}

void METH(serviceA2_suite) (){


	CALL(test_a2_01)();
	CALL(test_a2_02)();
	CALL(test_a2_03)();
	CALL(test_a2_04)();
	CALL(test_a2_05)();
	CALL(test_a2_06)();
	CALL(test_a2_07)();
	CALL(test_a2_08)();
	CALL(test_a2_09)();
	CALL(test_a2_10)();
	CALL(test_a2_11)();
	CALL(test_a2_12)();
	CALL(test_a2_13)();
	CALL(test_a2_15)();
	CALL(test_a2_16)();
	CALL(test_a2_17)();
	CALL(test_a2_18)();
	CALL(test_a2_20)();
	CALL(test_a2_21)();
	CALL(test_a2_22)();
	CALL(test_a2_23)();
	CALL(test_a2_24)();
	CALL(test_a2_25)();
	CALL(test_a2_26)();
	CALL(test_a2_27)();
	CALL(test_a2_28)();
	CALL(test_a2_29)();
	CALL(test_a2_30)();
	CALL(test_a2_31)();
	CALL(test_a2_32)();
	CALL(test_a2_33)();
	CALL(test_a2_34)();
	CALL(test_a2_35)();
	CALL(test_a2_36)();
	CALL(test_a2_37)();
	CALL(test_a2_38)();
	CALL(test_a2_39)();
	CALL(test_a2_40)();
	CALL(test_a2_41)();
	CALL(test_a2_42)();
	CALL(test_a2_43)();
	CALL(test_a2_44)();
	CALL(test_a2_45)();
	CALL(test_a2_46)();
	CALL(test_a2_47)();
	CALL(test_a2_48)();
}


int METH(main,main) (int argc, char**argv){

	printf("\t\t\t---Starting clientA---\n");

	int c;
	
	do
	{
		printf("\t\t\t---Type 'q' or 'Q' for quit and 'l' for launch the test suit---\n");
		c = getchar();
		if(c=='l') {
	
			CALL(serviceA_suite)();
			CALL(serviceA2_suite)();

		}
	}
	while (c != 'q' && c != 'Q');

}
