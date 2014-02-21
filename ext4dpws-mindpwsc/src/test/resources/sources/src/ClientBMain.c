#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include "testsConst.h"
#include "ClientB.c"

void METH(serviceB_suite) ()
{

	CALL(test01)();
	CALL(test02)();
	CALL(test03)();
	CALL(test04)();
	CALL(test05)();
	CALL(test06)();
	CALL(test07)();
	CALL(test08)();

}

int METH(main,main) (int argc, char**argv){

	printf("\t\t\t---Starting clientB---\n");
	int number_failed = 0;

	int c = 0;

	do
	{
		printf("\t\t\t---Type 'q' or 'Q' for quit and 'l' for launch the test suit---\n");
		c = getchar();
		if(c=='l')
		{
			CALL(serviceB_suite)();

		}
	}while (c != 'q' && c != 'Q');

}
