#include <stdio.h>

int METH(main,main) (int argc, char**argv){
	printf("\t\t\t---Starting serviceB---\n");
	int c;
	do
	{
		c = getchar();
	}
	while (c != 'q' && c != 'Q');

}
