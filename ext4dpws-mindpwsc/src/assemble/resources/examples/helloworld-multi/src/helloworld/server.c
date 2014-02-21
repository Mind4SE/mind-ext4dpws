#include <stdio.h>

// -----------------------------------------------------------------------------
// Implementation of the service interface.
// -----------------------------------------------------------------------------

int METH(main,main) (int argc, char**argv){
	printf("\t\t\t---Starting helloworld service---\n");
	printf("Type q or Q to quit\n");

	int c;
	do
	{
		c = getchar();
	}
	while (c != 'q' && c != 'Q');

}


// void print(string msg)
void METH(s, print)(const char *msg)
{
  int i;

  printf("Server: begin printing...\n");
  for (i = 0; i < ATTR(count); ++i) {
    printf("%s", msg);
  }

  printf("Server: print done\n");
}

void METH(s, println)(const char *msg)
{
  int i;

  printf("Server: begin printing...\n");
  for (i = 0; i < ATTR(count); ++i) {
    printf("%s\n", msg);
  }

  printf("Server: print done\n");

}
